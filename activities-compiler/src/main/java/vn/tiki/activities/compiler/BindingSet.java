package vn.tiki.activities.compiler;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import java.util.ArrayList;
import java.util.List;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

import static com.google.auto.common.MoreElements.getPackage;
import static vn.tiki.activities.compiler.ActivitiesProcessor.ACTIVITY_TYPE;
import static vn.tiki.activities.compiler.ActivitiesProcessor.FRAGMENT_TYPE;
import static vn.tiki.activities.compiler.ActivitiesProcessor.SUPPORT_FRAGMENT_TYPE;
import static vn.tiki.activities.compiler.ActivitiesProcessor.isSubtypeOfType;

class BindingSet {

  final TypeName targetTypeName;
  final ClassName targetClassName;
  final boolean isActivity;
  final boolean isFragment;
  final List<FieldExtraBinding> fieldExtraBindings;

  BindingSet(
      TypeName targetTypeName,
      ClassName targetClassName,
      boolean isActivity,
      boolean isFragment,
      List<FieldExtraBinding> fieldExtraBindings) {
    this.targetTypeName = targetTypeName;
    this.targetClassName = targetClassName;
    this.isActivity = isActivity;
    this.isFragment = isFragment;
    this.fieldExtraBindings = fieldExtraBindings;
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
    ClassName targetClassName = ClassName.get(packageName, className);

    boolean isActivity = isSubtypeOfType(typeMirror, ACTIVITY_TYPE);
    boolean isFragment = isSubtypeOfType(typeMirror, FRAGMENT_TYPE)
        || isSubtypeOfType(typeMirror, SUPPORT_FRAGMENT_TYPE);

    return new Builder(targetType, targetClassName, isActivity, isFragment);
  }

  static class Builder {
    private final TypeName targetTypeName;
    private final ClassName targetClassName;
    private final boolean isActivity;
    private final boolean isFragment;
    private final List<FieldExtraBinding> fieldExtraBindings = new ArrayList<>();

    private Builder(
        TypeName targetTypeName,
        ClassName targetClassName,
        boolean isActivity,
        boolean isFragment) {
      this.targetTypeName = targetTypeName;
      this.targetClassName = targetClassName;
      this.isActivity = isActivity;
      this.isFragment = isFragment;
    }

    void addField(FieldExtraBinding fieldExtraBinding) {
      fieldExtraBindings.add(fieldExtraBinding);
    }

    BindingSet build() {
      return new BindingSet(
          targetTypeName,
          targetClassName,
          isActivity,
          isFragment,
          fieldExtraBindings
      );
    }
  }
}
