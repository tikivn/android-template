package vn.tiki.viewholders.compiler;

import android.support.annotation.Nullable;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.sun.source.tree.ClassTree;
import com.sun.source.util.Trees;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeScanner;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import vn.tiki.viewholders.ViewHolder;

@AutoService(Processor.class)
public final class ViewHolderProcessor extends AbstractProcessor {

  private static class RClassScanner extends TreeScanner {

    private PackageElement currentPackage;
    // Maps the currently evaluated rPackageName to R Classes
    private final Map<PackageElement, Set<Symbol.ClassSymbol>> rClasses = new LinkedHashMap<>();
    private Set<String> referenced = new HashSet<>();

    @Override
    public void visitSelect(JCTree.JCFieldAccess jcFieldAccess) {
      Symbol symbol = jcFieldAccess.sym;
      if (symbol != null
          && symbol.getEnclosingElement() != null
          && symbol.getEnclosingElement().getEnclosingElement() != null
          && symbol.getEnclosingElement().getEnclosingElement().enclClass() != null) {
        Set<Symbol.ClassSymbol> rClassSet = rClasses.get(currentPackage);
        if (rClassSet == null) {
          rClassSet = new HashSet<>();
          rClasses.put(currentPackage, rClassSet);
        }
        referenced.add(getFqName(symbol));
        rClassSet.add(symbol.getEnclosingElement().getEnclosingElement().enclClass());
      }
    }

    Map<PackageElement, Set<Symbol.ClassSymbol>> getRClasses() {
      return rClasses;
    }

    Set<String> getReferenced() {
      return referenced;
    }

    void setCurrentPackage(PackageElement packageElement) {
      this.currentPackage = packageElement;
    }
  }

  private static class IdScanner extends TreeScanner {

    private final Map<QualifiedId, Id> ids;
    private final PackageElement rPackageName;
    private final Set<String> referenced;
    private final PackageElement respectivePackageName;

    IdScanner(
        Map<QualifiedId, Id> ids,
        PackageElement rPackageName,
        PackageElement respectivePackageName,
        Set<String> referenced) {
      this.ids = ids;
      this.rPackageName = rPackageName;
      this.respectivePackageName = respectivePackageName;
      this.referenced = referenced;
    }

    @Override
    public void visitClassDef(JCTree.JCClassDecl jcClassDecl) {
      for (JCTree tree : jcClassDecl.defs) {
        if (tree instanceof ClassTree) {
          ClassTree classTree = (ClassTree) tree;
          String className = classTree.getSimpleName().toString();
          if (SUPPORTED_TYPES.contains(className)) {
            ClassName rClassName = ClassName.get(rPackageName.getQualifiedName().toString(), "R",
                className);
            VarScanner scanner = new VarScanner(ids, rClassName, respectivePackageName, referenced);
            ((JCTree) classTree).accept(scanner);
          }
        }
      }
    }
  }

  private static class VarScanner extends TreeScanner {

    private final ClassName className;
    private final Map<QualifiedId, Id> ids;
    private final Set<String> referenced;
    private final PackageElement respectivePackageName;

    private VarScanner(
        Map<QualifiedId, Id> ids,
        ClassName className,
        PackageElement respectivePackageName,
        Set<String> referenced) {
      this.ids = ids;
      this.className = className;
      this.respectivePackageName = respectivePackageName;
      this.referenced = referenced;
    }

    @Override
    public void visitVarDef(JCTree.JCVariableDecl jcVariableDecl) {
      if ("int".equals(jcVariableDecl.getType().toString())) {
        String resourceName = jcVariableDecl.getName().toString();
        if (referenced.contains(getFqName(jcVariableDecl.sym))) {
          int id = Integer.valueOf(jcVariableDecl.getInitializer().toString());
          QualifiedId qualifiedId = new QualifiedId(respectivePackageName, id);
          ids.put(qualifiedId, new Id(id, className, resourceName));
        }
      }
    }
  }

  static final ClassName VIEW_HOLDER_DELEGATE = ClassName.get(
      "vn.tiki.viewholders",
      "ViewHolderDelegate");
  static final ClassName VIEW = ClassName.get("android.view", "View");
  static final ClassName VIEW_GROUP = ClassName.get("android.view", "ViewGroup");
  static final ClassName ONLY_ADAPTER_BUILDER = ClassName.get("vn.tiki.noadapter2", "OnlyAdapter", "Builder");
  static final ClassName DIFF_CALLBACK = ClassName.get("vn.tiki.viewholders", "OnlyDiffCallback");
  private static final List<String> SUPPORTED_TYPES = Arrays.asList(
      "layout", "id"
  );
  private Elements elementUtils;
  private Filer filer;
  private final Map<QualifiedId, Id> symbols = new LinkedHashMap<>();
  private Trees trees;
  private Types typeUtils;

  @Override
  public Set<String> getSupportedAnnotationTypes() {
    Set<String> types = new LinkedHashSet<>();
    types.add(ViewHolder.class.getCanonicalName());
    return types;
  }

  @Override
  public SourceVersion getSupportedSourceVersion() {
    return SourceVersion.latestSupported();
  }

  @Override
  public synchronized void init(ProcessingEnvironment env) {
    super.init(env);

    elementUtils = env.getElementUtils();
    typeUtils = env.getTypeUtils();
    filer = env.getFiler();
    try {
      trees = Trees.instance(processingEnv);
    } catch (IllegalArgumentException ignored) {
    }
  }

  @Override
  public boolean process(Set<? extends TypeElement> set, RoundEnvironment env) {
    final List<ViewHolderInfo> viewHolderInfoList = findAndParseTargets(env);
    if (viewHolderInfoList.isEmpty()) {
      return false;
    }

    for (ViewHolderInfo viewHolderInfo : viewHolderInfoList) {
      try {
        new ViewHolderDelegateGenerator(viewHolderInfo)
            .brewJava()
            .writeTo(filer);

      } catch (IOException e) {
        error("Unable to write delegate for type %s: %s", viewHolderInfo.getTargetType(), e.getMessage());
      }
    }

    try {
      new TypeFactoryGenerator(viewHolderInfoList)
          .brewJava()
          .writeTo(filer);
    } catch (Exception e) {
      error("Unable to write TypeFactory %s", e.getMessage());
    }
    try {
      new ViewHolderFactoryGenerator(viewHolderInfoList)
          .brewJava()
          .writeTo(filer);
    } catch (Exception e) {
      error("Unable to write ViewHolderFactory  %s", e.getMessage());
    }

    try {
      new NoAdapterFactoryGenerator()
          .brewJava()
          .writeTo(filer);
    } catch (Exception e) {
      error("Unable to write NoAdapterFactory  %s", e.getMessage());
    }

    return false;
  }

  private QualifiedId elementToQualifiedId(Element element, int id) {
    return new QualifiedId(elementUtils.getPackageOf(element), id);
  }

  private void error(String message, Object... args) {
    printMessage(Diagnostic.Kind.ERROR, message, args);
  }

  private List<ViewHolderInfo> findAndParseTargets(RoundEnvironment env) {
    List<ViewHolderInfo> viewHolderInfoList = new LinkedList<>();

    scanForRClasses(env);

    // Process each @BindExtra element.
    for (Element element : env.getElementsAnnotatedWith(ViewHolder.class)) {
      // we don't SuperficialValidation.validateElement(element)
      // so that an unresolved View type can be generated by later processing rounds
      final TypeElement typeElement = (TypeElement) element;
      try {
        final ViewHolder annotation = element.getAnnotation(ViewHolder.class);
        QualifiedId layoutQualifiedId = elementToQualifiedId(element, annotation.layout());
        Id layoutId = getId(layoutQualifiedId);
        Id[] onClickIds;
        final int[] onClick = annotation.onClick();
        if (onClick.length == 0) {
          onClickIds = new Id[0];
        } else {
          onClickIds = new Id[onClick.length];
          for (int i = 0; i < onClick.length; i++) {
            QualifiedId qualifiedId = elementToQualifiedId(element, onClick[i]);
            onClickIds[i] = getId(qualifiedId);
          }
        }
        TypeMirror modelClass = getModelClass(annotation);
        if (modelClass == null) {
          error("can not read Model class");
          return Collections.emptyList();
        } else if (modelClass.toString().equals("java.lang.Object")) {
          error("Object is not allow to be a Model class");
          return Collections.emptyList();
        }

        final TypeElement paramType = (TypeElement) typeUtils.asElement(modelClass);
        final boolean hasBindMethod = hasMethod(typeElement, "bind", paramType.toString());
        final ClassName itemClassName = ClassName.get(paramType);
        final ViewHolderInfo viewHolderInfo = new ViewHolderInfo(
            ClassName.get(typeElement), layoutId,
            onClickIds,
            itemClassName,
            hasBindMethod, hasMethod(typeElement, "bindView", "android.view.View")
        );
        viewHolderInfoList.add(viewHolderInfo);
      } catch (Exception e) {
        logParsingError(ViewHolder.class, e);
      }
    }

    return viewHolderInfoList;
  }

  private Id getId(QualifiedId qualifiedId) {
    if (symbols.get(qualifiedId) == null) {
      symbols.put(qualifiedId, new Id(qualifiedId.id));
    }
    return symbols.get(qualifiedId);
  }

  @Nullable
  private TypeMirror getModelClass(final ViewHolder annotation) {
    try {
      annotation.bindTo();
    } catch (MirroredTypeException mte) {
      return mte.getTypeMirror();
    }
    return null;
  }

  private boolean hasMethod(TypeElement target, String name, String param) {
    final List<? extends Element> elements = target.getEnclosedElements();
    for (Element e : elements) {
      if (e instanceof ExecutableElement && name.equals(e.getSimpleName().toString())) {
        final List<? extends VariableElement> parameters = ((ExecutableElement) e).getParameters();
        if (parameters.size() == 1) {
          final VariableElement variableElement = parameters.get(0);
          final TypeElement parameterType = (TypeElement) typeUtils.asElement(variableElement.asType());
          if (parameterType.toString().equals(param)) {
            return true;
          }
        }
      }
    }
    // find in supper class.
    final TypeMirror superclass = target.getSuperclass();
    return superclass.getKind() != TypeKind.NONE && hasMethod(
        (TypeElement) typeUtils.asElement(superclass),
        name,
        param);
  }

  private void logParsingError(
      Class<? extends Annotation> annotation,
      Exception e) {
    StringWriter stackTrace = new StringWriter();
    e.printStackTrace(new PrintWriter(stackTrace));
    error("Unable to parse @%s binding.\n\n%s", annotation.getSimpleName(), stackTrace);
  }

  private void parseCompiledR(
      PackageElement respectivePackageName, TypeElement rClass,
      Set<String> referenced) {
    for (Element element : rClass.getEnclosedElements()) {
      String innerClassName = element.getSimpleName().toString();
      if (SUPPORTED_TYPES.contains(innerClassName)) {
        for (Element enclosedElement : element.getEnclosedElements()) {
          if (enclosedElement instanceof VariableElement) {
            String fqName = elementUtils.getPackageOf(enclosedElement).getQualifiedName().toString()
                            + ".R."
                            + innerClassName
                            + "."
                            + enclosedElement.toString();
            if (referenced.contains(fqName)) {
              VariableElement variableElement = (VariableElement) enclosedElement;
              Object value = variableElement.getConstantValue();

              if (value instanceof Integer) {
                int id = (Integer) value;
                ClassName rClassName =
                    ClassName.get(elementUtils.getPackageOf(variableElement).toString(), "R",
                        innerClassName);
                String resourceName = variableElement.getSimpleName().toString();
                QualifiedId qualifiedId = new QualifiedId(respectivePackageName, id);
                symbols.put(qualifiedId, new Id(id, rClassName, resourceName));
              }
            }
          }
        }
      }
    }
  }

  private void parseRClass(
      PackageElement respectivePackageName, Symbol.ClassSymbol rClass,
      Set<String> referenced) {
    TypeElement element;

    try {
      element = rClass;
    } catch (MirroredTypeException mte) {
      element = (TypeElement) typeUtils.asElement(mte.getTypeMirror());
    }

    JCTree tree = (JCTree) trees.getTree(element);
    if (tree != null) { // tree can be null if the references are compiled types and not source
      IdScanner idScanner =
          new IdScanner(symbols, elementUtils.getPackageOf(element), respectivePackageName,
              referenced);
      tree.accept(idScanner);
    } else {
      parseCompiledR(respectivePackageName, element, referenced);
    }
  }

  private void printMessage(Diagnostic.Kind kind, String message, Object[] args) {
    if (args.length > 0) {
      message = String.format(message, args);
    }

    message = "\n\n======== ERROR ========\nViewHolderProcessor: " + message + "\n\n";
    processingEnv.getMessager().printMessage(kind, message);
  }

  private void scanForRClasses(RoundEnvironment env) {
    if (trees == null) {
      return;
    }

    RClassScanner scanner = new RClassScanner();

    for (Element element : env.getElementsAnnotatedWith(ViewHolder.class)) {
      JCTree tree = (JCTree) trees.getTree(element, getMirror(element, ViewHolder.class));
      if (tree != null) { // tree can be null if the references are compiled types and not source
        scanner.setCurrentPackage(elementUtils.getPackageOf(element));
        tree.accept(scanner);
      }
    }

    for (Map.Entry<PackageElement, Set<Symbol.ClassSymbol>> packageNameToRClassSet
        : scanner.getRClasses().entrySet()) {
      PackageElement respectivePackageName = packageNameToRClassSet.getKey();
      for (Symbol.ClassSymbol rClass : packageNameToRClassSet.getValue()) {
        parseRClass(respectivePackageName, rClass, scanner.getReferenced());
      }
    }
  }

  private static String getFqName(Symbol rSymbol) {
    return rSymbol.packge().getQualifiedName().toString()
           + ".R."
           + rSymbol.enclClass().name.toString()
           + "."
           + rSymbol.name.toString();
  }

  @Nullable
  private static AnnotationMirror getMirror(
      Element element,
      Class<? extends Annotation> annotation) {
    for (AnnotationMirror annotationMirror : element.getAnnotationMirrors()) {
      if (annotationMirror.getAnnotationType().toString().equals(annotation.getCanonicalName())) {
        return annotationMirror;
      }
    }
    return null;
  }

}