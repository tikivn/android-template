package vn.tiki.noadapterviewholder.compiler;

import com.squareup.javapoet.ClassName;

public class ViewHolderInfo {

  private final int layout;
  private final int[] onClick;
  private final ClassName targetType;

  ViewHolderInfo(
      final int layout,
      final int[] onClick,
      final ClassName targetType) {
    this.layout = layout;
    this.onClick = onClick;
    this.targetType = targetType;
  }

  public int[] getOnClick() {
    return onClick;
  }

  public ClassName getTargetType() {
    return targetType;
  }

  int getLayout() {
    return layout;
  }
}
