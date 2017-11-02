package vn.tiki.viewholders.compiler;

import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.PUBLIC;
import static vn.tiki.viewholders.compiler.ViewHolderProcessor.VIEW_GROUP;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.MethodSpec.Builder;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import java.util.List;

class ViewHolderFactoryGenerator {

  public static final String NAME = "ViewHolderFactoryImpl";
  private final ClassName lastViewHolderType;
  private final TypeName mapType;
  private final ClassName viewHolderFactoryType;
  private final List<ViewHolderInfo> viewHolderInfoList;
  private final ClassName viewHolderType;

  ViewHolderFactoryGenerator(final List<ViewHolderInfo> viewHolderInfoList) {
    this.viewHolderInfoList = viewHolderInfoList;
    ClassName map = ClassName.get("java.util", "LinkedHashMap");
    mapType = ParameterizedTypeName.get(map, ClassName.get(Class.class), ClassName.get(Integer.class));
    viewHolderType = ClassName.get("vn.tiki.noadapter2", "AbsViewHolder");
    viewHolderFactoryType = ClassName.get("vn.tiki.noadapter2", "ViewHolderFactory");
    lastViewHolderType = ClassName.get("vn.tiki.viewholders", "LastViewHolder");
  }

  JavaFile brewJava() {
    return JavaFile.builder("viewholders", createType())
        .addFileComment("Generated code from NoAdapter ViewHolder. Do not modify!")
        .build();
  }

  private MethodSpec createMakeViewHolderDelegateMethod() {
    final CodeBlock.Builder switchStatementBuilder = CodeBlock.builder();

    switchStatementBuilder.beginControlFlow("switch(type)");
    for (ViewHolderInfo viewHolderInfo : viewHolderInfoList) {
      final ClassName targetType = viewHolderInfo.getTargetType();
      final ClassName viewHolderDelegateType = ClassName.get(
          targetType.packageName(),
          targetType.simpleName() + "_ViewHolderDelegate");
      switchStatementBuilder.addStatement(
          "case $L: return new $T()",
          viewHolderInfo.getLayout(),
          viewHolderDelegateType);
    }
    switchStatementBuilder.addStatement(
        "default: throw new $T(\"unknown type: \" + type)",
        IllegalArgumentException.class);
    switchStatementBuilder.endControlFlow();

    final Builder result = MethodSpec.methodBuilder("makeViewHolderDelegate")
        .addModifiers(PRIVATE)
        .returns(ViewHolderProcessor.VIEW_HOLDER_DELEGATE)
        .addParameter(TypeName.INT, "type")
        .addCode(switchStatementBuilder.build());
    return result.build();
  }

  private TypeSpec createType() {
    TypeSpec.Builder result = TypeSpec.classBuilder(NAME)
        .addModifiers(FINAL)
        .addSuperinterface(viewHolderFactoryType);

    result.addMethod(createViewHolderForTypeMethod());
    result.addMethod(createMakeViewHolderDelegateMethod());

    return result.build();
  }

  private MethodSpec createViewHolderForTypeMethod() {
    return MethodSpec.methodBuilder("viewHolderForType")
        .addAnnotation(Override.class)
        .addModifiers(PUBLIC)
        .returns(viewHolderType)
        .addParameter(VIEW_GROUP, "parent")
        .addParameter(TypeName.INT, "type")
        .addStatement("final ViewHolderDelegate viewHolderDelegate = makeViewHolderDelegate(type)")
        .addStatement("return $T.create(parent, viewHolderDelegate)", lastViewHolderType)
        .build();
  }
}
