package vn.tiki.noadapterviewholder;

import android.view.View;

public interface ViewHolderDelegate {

  void bindView(View view);

  void bind(Object item);

  int layout();

  int[] onClick();
}
