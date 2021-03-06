package vn.tiki.viewholders;

import android.view.View;

public interface ViewHolderDelegate {

  void bindView(View view);

  void bind(Object item);

  void unbind();

  int layout();

  int[] onClick();
}
