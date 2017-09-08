package vn.tiki.activities.compiler;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import java.util.ArrayList;
import java.util.List;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

import static com.google.auto.common.MoreElements.getPackage;
import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.PUBLIC;
import static javax.lang.model.element.Modifier.STATIC;
import static vn.tiki.activities.compiler.ActivitiesProcessor.isSubtypeOfType;

class BindingSet {
  private static final ClassName BUNDLES = ClassName.get("vn.tiki.activities.internal", "Bundles");
  private static final ClassName BUNDLE = ClassName.get("android.os", "Bundle");
  private static final String ACTIVITY_TYPE = "android.app.Activity";

  private final TypeName targetTypeName;
  private final ClassName bindingClassName;
  private final boolean isActivity;
  private final List<FieldExtraBinding> fieldExtraBindings;

  BindingSet(
      TypeName targetTypeName,
      ClassName bindingClassName,
      boolean isActivity,
      List<FieldExtraBinding> fieldExtraBindings) {
    this.targetTypeName = targetTypeName;
    this.bindingClassName = bindingClassName;
    this.isActivity = isActivity;
    this.fieldExtraBindings = fieldExtraBindings;
  }

  JavaFile brewJava() {
    return JavaFile.builder(bindingClassName.packageName(), createType())
        .addFileComment("Generated code from Activities. Do not modify!")
        .build();
  }

  private TypeSpec createType() {
    TypeSpec.Builder result = TypeSpec.classBuilder(bindingClassName.simpleName())
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

  static Builder newBuilder(TypeElement enclosingElement) {
    TypeMirror typeMirror = enclosingElement.asType();

    TypeName targetType = TypeName.get(typeMirror);
    if (targetType instanceof ParameterizedTypeName) {
      targetType = ((ParameterizedTypeName) targetType).rawType;
    }

    String packageName = getPackage(enclosingElement).getQualifiedName().toString();
    String className = enclosingElement.getQualifiedName().toString().substring(
        packageName.length() + 1).replace('.', '$');
    ClassName bindingClassName = ClassName.get(packageName, className + "_");

    boolean isActivity = isSubtypeOfType(typeMirror, ACTIVITY_TYPE);

    return new Builder(targetType, bindingClassName, isActivity);
  }

  static class Builder {
    private final TypeName targetTypeName;
    private final ClassName bindingClassName;
    private final boolean isActivity;
    private final List<FieldExtraBinding> fieldExtraBindings = new ArrayList<>();

    private Builder(
        TypeName targetTypeName,
        ClassName bindingClassName,
        boolean isActivity) {
      this.targetTypeName = targetTypeName;
      this.bindingClassName = bindingClassName;
      this.isActivity = isActivity;
    }

    void addField(FieldExtraBinding fieldExtraBinding) {
      fieldExtraBindings.add(fieldExtraBinding);
    }

    BindingSet build() {
      return new BindingSet(
          targetTypeName,
          bindingClassName,
          isActivity,
          fieldExtraBindings
      );
    }
  }
}
