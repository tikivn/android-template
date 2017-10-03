package vn.tiki.intents.compiler;

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
import static vn.tiki.intents.compiler.IntentsProcessor.BUNDLE;
import static vn.tiki.intents.compiler.IntentsProcessor.BUNDLES;
import static vn.tiki.intents.compiler.IntentsProcessor.CONTEXT;

final class ExtraBinding {

  private final TypeName targetTypeName;
  private final ClassName targetClassName;
  private final List<FieldExtraBinding> fieldExtraBindings;

  ExtraBinding(BindingSet bindingSet) {
    this.targetTypeName = bindingSet.targetTypeName;
    this.targetClassName = bindingSet.targetClassName;
    this.fieldExtraBindings = bindingSet.fieldExtraBindings;
  }

  JavaFile brewJava() {
    return JavaFile.builder(targetClassName.packageName(), createType())
        .addFileComment("Generated code from Intents. Do not modify!")
        .build();
  }

  private TypeSpec createType() {
    return TypeSpec.classBuilder(targetClassName.simpleName() + "_ExtraBinding")
        .addModifiers(PUBLIC)
        .addModifiers(FINAL)
        .addMethod(createBindingMethod())
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

    final String name = fieldExtraBinding.getName();
    CodeBlock.Builder builder = CodeBlock.builder()
        .add("if ($T.has(source, \"$L\")) {", BUNDLES, name)
        .add("target.$L = ", name)
        .add("$T.get(source, \"$L\");", BUNDLES, name)
        .add("}\n");
    bindExtrasMethodBuilder.addCode(builder.build());
  }
}
