package vn.tiki.sample.viewholder;

import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;

public interface ViewHolderDelegate {

  @LayoutRes
  int layout();

  @IdRes
  int[] onClickIds();

  boolean isSameItem(Object other);

  boolean isSameContent(Object other);

  void bind();
}
