package vn.tiki.intents.compiler;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.PUBLIC;
import static javax.lang.model.element.Modifier.STATIC;
import static vn.tiki.intents.compiler.IntentsProcessor.BUNDLE;
import static vn.tiki.intents.compiler.IntentsProcessor.CONTEXT;

final class IntentsBuilder {

  public static final String NAME = "Intents";
  private final TypeSpec.Builder classBuilder;

  IntentsBuilder() {
    classBuilder = TypeSpec.classBuilder(NAME)
        .addModifiers(PUBLIC)
        .addModifiers(FINAL)
        .addMethod(createPrivateDefaultConstructor());
  }

  void addBinding(BindingSet bindingSet) {
    classBuilder.addMethod(createBindingMethod(bindingSet));
  }

  void addBuilderFactory(BindingSet bindingSet) {
    classBuilder.addMethod(createBuilderFactoryMethod(bindingSet));
  }

  private MethodSpec createBuilderFactoryMethod(BindingSet bindingSet) {
    final String name = bindingSet.targetClassName.simpleName();
    final String factoryName = createFactoryMethodName(name);
    MethodSpec.Builder builderFactoryMethodBuilder = MethodSpec.methodBuilder(factoryName)
        .addModifiers(PUBLIC)
        .addModifiers(STATIC);

    if (bindingSet.isActivity) {
      final ClassName intentBuilderClassName =
          IntentBuilder.intentBuilderClassName(bindingSet.targetClassName);
      return builderFactoryMethodBuilder
          .returns(intentBuilderClassName)
          .addParameter(CONTEXT, "context")
          .addStatement("return new $L(context)", intentBuilderClassName.simpleName())
          .build();
    } else {
      final ClassName fragmentBuilderClassName =
          FragmentBuilder.fragmentBuilderClassName(bindingSet.targetClassName);
      return builderFactoryMethodBuilder
          .returns(fragmentBuilderClassName)
          .addStatement("return new $T()", fragmentBuilderClassName)
          .build();
    }
  }

  private String createFactoryMethodName(String name) {
    final String firstChar = String.valueOf(name.charAt(0));
    return name.replace(firstChar, firstChar.toLowerCase());
  }

  private MethodSpec createBindingMethod(BindingSet bindingSet) {
    MethodSpec.Builder bindExtrasMethodBuilder = MethodSpec.methodBuilder("bind")
        .addModifiers(PUBLIC)
        .addModifiers(STATIC)
        .addParameter(bindingSet.targetTypeName, "target");

    if (bindingSet.isActivity) {
      bindExtrasMethodBuilder.addStatement("$T source = target.getIntent().getExtras()", BUNDLE);
    } else if (bindingSet.isFragment) {
      bindExtrasMethodBuilder.addStatement("$T source = target.getArguments()", BUNDLE);
    }

    ClassName extraBindingClassName = ClassName.get(
        bindingSet.targetClassName.packageName(),
        bindingSet.targetClassName.simpleName() + "_ExtraBinding");

    return bindExtrasMethodBuilder
        .addStatement("$T.bindExtras(target, source)", extraBindingClassName)
        .build();
  }

  private MethodSpec createPrivateDefaultConstructor() {
    return MethodSpec.constructorBuilder()
        .addModifiers(PRIVATE)
        .build();
  }

  JavaFile brewJava() {
    return JavaFile.builder("intents", classBuilder.build())
        .addFileComment("Generated code from Intents. Do not modify!")
        .build();
  }
}
