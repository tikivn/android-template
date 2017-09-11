package vn.tiki.intents.compiler;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import java.util.List;

import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.PUBLIC;
import static vn.tiki.intents.compiler.IntentsProcessor.CONTEXT;
import static vn.tiki.intents.compiler.IntentsProcessor.INTENT;

final class IntentBuilder {

  private final ClassName targetClassName;
  private final List<FieldExtraBinding> fieldExtraBindings;
  private final String name;
  private final ClassName builderClassName;

  IntentBuilder(BindingSet bindingSet) {
    this.targetClassName = bindingSet.targetClassName;
    this.fieldExtraBindings = bindingSet.fieldExtraBindings;
    name = targetClassName.simpleName() + "_IntentBuilder";
    builderClassName = ClassName.get(targetClassName.packageName(), name);
  }

  JavaFile brewJava() {
    return JavaFile.builder(targetClassName.packageName(), createType())
        .addFileComment("Generated code from Intents. Do not modify!")
        .build();
  }

  private TypeSpec createType() {
    TypeSpec.Builder result = TypeSpec.classBuilder(name)
        .addModifiers(PUBLIC)
        .addModifiers(FINAL)
        .addField(INTENT, "intent", PRIVATE, FINAL);

    result.addMethod(createConstructor());

    for (FieldExtraBinding fieldExtraBinding : fieldExtraBindings) {
      result.addMethod(createBuilderMethod(fieldExtraBinding));
    }

    result.addMethod(createMakeMethod());

    return result.build();
  }

  private MethodSpec createBuilderMethod(FieldExtraBinding fieldExtraBinding) {
    String name = fieldExtraBinding.getName();
    TypeName type = fieldExtraBinding.getType();
    return MethodSpec.methodBuilder(name)
        .addModifiers(PUBLIC)
        .returns(builderClassName)
        .addParameter(type, name)
        .addStatement("intent.putExtra(\"$L\", $L)", name, name)
        .addStatement("return this")
        .build();
  }

  private MethodSpec createMakeMethod() {
    return MethodSpec.methodBuilder("make")
        .addModifiers(PUBLIC)
        .returns(INTENT)
        .addStatement("return intent")
        .build();
  }

  private MethodSpec createConstructor() {
    return MethodSpec.constructorBuilder()
        .addParameter(CONTEXT, "context")
        .addStatement("intent = new Intent(context, $L.class)", targetClassName.simpleName())
        .build();
  }
}
