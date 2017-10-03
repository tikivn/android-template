package vn.tiki.intents.compiler;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import vn.tiki.intents.BindExtra;

import static javax.lang.model.element.ElementKind.CLASS;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.STATIC;

@AutoService(Processor.class)
public final class IntentsProcessor extends AbstractProcessor {
  static final String ACTIVITY_TYPE = "android.app.Activity";
  static final String FRAGMENT_TYPE = "android.app.Fragment";
  static final String SUPPORT_FRAGMENT_TYPE = "android.support.v4.app.Fragment";
  static final ClassName CONTEXT = ClassName.get("android.content", "Context");
  static final ClassName INTENT = ClassName.get("android.content", "Intent");
  static final ClassName BUNDLES = ClassName.get("vn.tiki.intents.internal", "Bundles");
  static final ClassName BUNDLE = ClassName.get("android.os", "Bundle");

  private Filer filer;

  private static boolean isTypeEqual(TypeMirror typeMirror, String otherType) {
    return otherType.equals(typeMirror.toString());
  }

  static boolean isSubtypeOfType(TypeMirror typeMirror, String otherType) {
    if (isTypeEqual(typeMirror, otherType)) {
      return true;
    }
    if (typeMirror.getKind() != TypeKind.DECLARED) {
      return false;
    }
    DeclaredType declaredType = (DeclaredType) typeMirror;
    List<? extends TypeMirror> typeArguments = declaredType.getTypeArguments();
    if (typeArguments.size() > 0) {
      StringBuilder typeString = new StringBuilder(declaredType.asElement().toString());
      typeString.append('<');
      for (int i = 0; i < typeArguments.size(); i++) {
        if (i > 0) {
          typeString.append(',');
        }
        typeString.append('?');
      }
      typeString.append('>');
      if (typeString.toString().equals(otherType)) {
        return true;
      }
    }
    Element element = declaredType.asElement();
    if (!(element instanceof TypeElement)) {
      return false;
    }
    TypeElement typeElement = (TypeElement) element;
    TypeMirror superType = typeElement.getSuperclass();
    if (isSubtypeOfType(superType, otherType)) {
      return true;
    }
    for (TypeMirror interfaceType : typeElement.getInterfaces()) {
      if (isSubtypeOfType(interfaceType, otherType)) {
        return true;
      }
    }
    return false;
  }

  @Override public synchronized void init(ProcessingEnvironment env) {
    super.init(env);

    filer = env.getFiler();
  }

  @Override public Set<String> getSupportedAnnotationTypes() {
    Set<String> types = new LinkedHashSet<>();
    types.add(BindExtra.class.getCanonicalName());
    return types;
  }

  @Override
  public boolean process(Set<? extends TypeElement> set, RoundEnvironment env) {
    Map<TypeElement, BindingSet> bindingMap = findAndParseTargets(env);

    final IntentsBuilder intentsBuilder = new IntentsBuilder();
    for (Map.Entry<TypeElement, BindingSet> entry : bindingMap.entrySet()) {
      TypeElement typeElement = entry.getKey();
      BindingSet binding = entry.getValue();

      try {
        if (binding.isActivity) {
          new IntentBuilder(binding)
              .brewJava()
              .writeTo(filer);
          new ActivityBinding(binding)
              .brewJava()
              .writeTo(filer);
        } else if (binding.isFragment) {
          new FragmentBuilder(binding)
              .brewJava()
              .writeTo(filer);

          new FragmentBinding(binding)
              .brewJava()
              .writeTo(filer);
        }
        intentsBuilder.addBinding(binding);
        intentsBuilder.addBuilderFactory(binding);
      } catch (IOException e) {
        error(typeElement, "Unable to write binding for type %s: %s", typeElement, e.getMessage());
      }

      try {
        intentsBuilder.brewJava().writeTo(filer);
      } catch (IOException e) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Unable to write Intents");
      }
    }

    return false;
  }

  private Map<TypeElement, BindingSet> findAndParseTargets(RoundEnvironment env) {
    Map<TypeElement, BindingSet.Builder> builderMap = new LinkedHashMap<>();
    // Process each @BindExtra element.
    for (Element element : env.getElementsAnnotatedWith(BindExtra.class)) {
      // we don't SuperficialValidation.validateElement(element)
      // so that an unresolved View type can be generated by later processing rounds
      try {
        parseExtra(element, builderMap);
      } catch (Exception e) {
        logParsingError(element, BindExtra.class, e);
      }
    }

    Map<TypeElement, BindingSet> bindingMap = new LinkedHashMap<>();
    for (Map.Entry<TypeElement, BindingSet.Builder> entry : builderMap.entrySet()) {

      TypeElement type = entry.getKey();
      BindingSet.Builder builder = entry.getValue();

      bindingMap.put(type, builder.build());
    }

    return bindingMap;
  }

  private boolean isInaccessibleViaGeneratedCode(
      Class<? extends Annotation> annotationClass,
      String targetThing, Element element) {
    boolean hasError = false;
    TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();

    // Verify method modifiers.
    Set<Modifier> modifiers = element.getModifiers();
    if (modifiers.contains(PRIVATE) || modifiers.contains(STATIC)) {
      error(element, "@%s %s must not be private or static. (%s.%s)",
          annotationClass.getSimpleName(), targetThing, enclosingElement.getQualifiedName(),
          element.getSimpleName());
      hasError = true;
    }

    // Verify containing type.
    if (enclosingElement.getKind() != CLASS) {
      error(enclosingElement, "@%s %s may only be contained in classes. (%s.%s)",
          annotationClass.getSimpleName(), targetThing, enclosingElement.getQualifiedName(),
          element.getSimpleName());
      hasError = true;
    }

    // Verify containing class visibility is not private.
    if (enclosingElement.getModifiers().contains(PRIVATE)) {
      error(enclosingElement, "@%s %s may not be contained in private classes. (%s.%s)",
          annotationClass.getSimpleName(), targetThing, enclosingElement.getQualifiedName(),
          element.getSimpleName());
      hasError = true;
    }

    return hasError;
  }

  private boolean isNotSupportedType(TypeElement enclosingElement) {
    boolean hasError = false;
    final TypeMirror typeMirror = enclosingElement.asType();
    boolean isActivity = isSubtypeOfType(typeMirror, ACTIVITY_TYPE);
    boolean isFragment = isSubtypeOfType(typeMirror, FRAGMENT_TYPE)
        || isSubtypeOfType(typeMirror, SUPPORT_FRAGMENT_TYPE);
    if (!isActivity && !isFragment) {
      error(
          enclosingElement,
          "@%s only support Activity or Fragment",
          BindExtra.class.getCanonicalName());
      hasError = true;
    }
    return hasError;
  }

  private boolean isBindingInWrongPackage(
      Class<? extends Annotation> annotationClass,
      Element element) {
    TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();
    String qualifiedName = enclosingElement.getQualifiedName().toString();

    if (qualifiedName.startsWith("android.")) {
      error(element, "@%s-annotated class incorrectly in Android framework package. (%s)",
          annotationClass.getSimpleName(), qualifiedName);
      return true;
    }
    if (qualifiedName.startsWith("java.")) {
      error(element, "@%s-annotated class incorrectly in Java framework package. (%s)",
          annotationClass.getSimpleName(), qualifiedName);
      return true;
    }

    return false;
  }

  private void parseExtra(
      Element element, Map<TypeElement, BindingSet.Builder> builderMap) {
    TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();

    // Start by verifying common generated code restrictions.
    boolean hasError = isInaccessibleViaGeneratedCode(BindExtra.class, "fields", element)
        || isBindingInWrongPackage(BindExtra.class, element)
        || isNotSupportedType(enclosingElement);

    if (hasError) {
      return;
    }

    // Assemble information on the field.
    Name simpleName = element.getSimpleName();

    BindingSet.Builder builder = builderMap.get(enclosingElement);
    if (builder == null) {
      builder = getOrCreateBindingBuilder(builderMap, enclosingElement);
    }

    TypeMirror elementType = element.asType();

    String name = simpleName.toString();
    TypeName type = TypeName.get(elementType);

    builder.addField(new FieldExtraBinding(name, type));
  }

  @Override public SourceVersion getSupportedSourceVersion() {
    return SourceVersion.latestSupported();
  }

  private BindingSet.Builder getOrCreateBindingBuilder(
      Map<TypeElement, BindingSet.Builder> builderMap, TypeElement enclosingElement) {
    BindingSet.Builder builder = builderMap.get(enclosingElement);
    if (builder == null) {
      builder = BindingSet.newBuilder(enclosingElement);
      builderMap.put(enclosingElement, builder);
    }
    return builder;
  }

  private void logParsingError(
      Element element, Class<? extends Annotation> annotation,
      Exception e) {
    StringWriter stackTrace = new StringWriter();
    e.printStackTrace(new PrintWriter(stackTrace));
    error(element, "Unable to parse @%s binding.\n\n%s", annotation.getSimpleName(), stackTrace);
  }

  private void error(Element element, String message, Object... args) {
    printMessage(Diagnostic.Kind.ERROR, element, message, args);
  }

  private void printMessage(Diagnostic.Kind kind, Element element, String message, Object[] args) {
    if (args.length > 0) {
      message = String.format(message, args);
    }

    processingEnv.getMessager().printMessage(kind, message, element);
  }
}