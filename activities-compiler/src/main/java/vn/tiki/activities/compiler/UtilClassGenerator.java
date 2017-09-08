package vn.tiki.activities.compiler;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import java.util.List;

import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.PUBLIC;
import static javax.lang.model.element.Modifier.STATIC;

final class UtilClassGenerator {
  private static final ClassName BUNDLES = ClassName.get("vn.tiki.activities.internal", "Bundles");
  private static final ClassName BUNDLE = ClassName.get("android.os", "Bundle");

  private final TypeName targetTypeName;
  private final ClassName targetClassName;
  private final boolean isActivity;
  private final List<FieldExtraBinding> fieldExtraBindings;

  UtilClassGenerator(BindingSet bindingSet) {
    this.targetTypeName = bindingSet.targetTypeName;
    this.targetClassName = bindingSet.targetClassName;
    this.isActivity = bindingSet.isActivity;
    this.fieldExtraBindings = bindingSet.fieldExtraBindings;
  }
  
  JavaFile brewJava() {
    return JavaFile.builder(targetClassName.packageName(), createType())
        .addFileComment("Generated code from Activities. Do not modify!")
        .build();
  }

  private TypeSpec createType() {
    TypeSpec.Builder result = TypeSpec.classBuilder(targetClassName.simpleName() + "_")
        .addModifiers(PUBLIC)
        .addModifiers(FINAL);

    result.addMethod(createPrivateDefaultConstructor());

    if (isActivity) {
      result.addMethod(createBindingMethodForActivity());
    }

    result.addMethod(createBindingMethod());

    return result.build();
  }

  private MethodSpec createPrivateDefaultConstructor() {
    return MethodSpec.constructorBuilder()
        .addModifiers(PRIVATE)
        .build();
  }

  private MethodSpec createBindingMethodForActivity() {
    return MethodSpec.methodBuilder("bindExtras")
        .addModifiers(PUBLIC)
        .addModifiers(STATIC)
        .addParameter(targetTypeName, "target")
        .addStatement("bindExtras(target, target.getIntent().getExtras())")
        .build();
  }

  private MethodSpec createBindingMethod() {
    MethodSpec.Builder bindExtrasMethodBuilder = MethodSpec.methodBuilder("bindExtras")
        .addModifiers(PUBLIC)
        .addModifiers(STATIC)
        .addParameter(targetTypeName, "target")
        .addParameter(BUNDLE, "source");

    for (FieldExtraBinding fieldExtraBinding : fieldExtraBindings) {
      addFieldBinding(bindExtrasMethodBuilder, fieldExtraBinding);
    }

    return bindExtrasMethodBuilder.build();
  }

  private void addFieldBinding(
      MethodSpec.Builder bindExtrasMethodBuilder,
      FieldExtraBinding fieldExtraBinding) {

    CodeBlock.Builder builder = CodeBlock.builder()
        .add("target.$L = ", fieldExtraBinding.getName())
        .add("$T.get(source, \"$L\")", BUNDLES, fieldExtraBinding.getName());

    bindExtrasMethodBuilder.addStatement("$L", builder.build());
  }
}
