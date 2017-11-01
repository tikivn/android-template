package vn.tiki.noadapterviewholder.compiler;

import com.squareup.javapoet.ClassName;

public class ViewHolderInfo {

  private final int layout;
  private final int[] onClick;
  private final ClassName itemType;
  private final ClassName targetType;

  ViewHolderInfo(
      final int layout,
      final int[] onClick,
      final ClassName itemType,
      final ClassName targetType) {
    this.layout = layout;
    this.onClick = onClick;
    this.itemType = itemType;
    this.targetType = targetType;
  }

  int[] getOnClick() {
    return onClick;
  }

  ClassName getItemType() {
    return itemType;
  }

  int getLayout() {
    return layout;
  }

  ClassName getTargetType() {
    return targetType;
  }
}
