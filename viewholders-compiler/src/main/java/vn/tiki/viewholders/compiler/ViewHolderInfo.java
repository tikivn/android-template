package vn.tiki.viewholders.compiler;

import com.squareup.javapoet.ClassName;

public class ViewHolderInfo {

  private final boolean hasView;
  private final ClassName itemType;
  private final int layout;
  private final int[] onClick;
  private final ClassName targetType;

  ViewHolderInfo(
      final int layout,
      final int[] onClick,
      final ClassName itemType,
      final boolean hasView, final ClassName targetType) {
    this.layout = layout;
    this.onClick = onClick;
    this.itemType = itemType;
    this.hasView = hasView;
    this.targetType = targetType;
  }

  boolean hasView() {
    return hasView;
  }

  ClassName getItemType() {
    return itemType;
  }

  int getLayout() {
    return layout;
  }

  int[] getOnClick() {
    return onClick;
  }

  ClassName getTargetType() {
    return targetType;
  }
}
