package vn.tiki.viewholders.compiler;

import com.squareup.javapoet.ClassName;

public class ViewHolderInfo {

  private final boolean hasView;
  private final ClassName itemType;
  private final Id layout;
  private final Id[] onClick;
  private final ClassName targetType;

  ViewHolderInfo(
      final Id layout,
      final Id[] onClick,
      final ClassName itemType,
      final boolean hasView, final ClassName targetType) {
    this.layout = layout;
    this.onClick = onClick;
    this.itemType = itemType;
    this.hasView = hasView;
    this.targetType = targetType;
  }

  ClassName getItemType() {
    return itemType;
  }

  Id getLayout() {
    return layout;
  }

  Id[] getOnClick() {
    return onClick;
  }

  ClassName getTargetType() {
    return targetType;
  }

  boolean hasView() {
    return hasView;
  }
}
