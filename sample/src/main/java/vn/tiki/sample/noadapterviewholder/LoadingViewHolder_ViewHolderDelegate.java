package vn.tiki.sample.noadapterviewholder;

import android.view.View;
import vn.tiki.noadapterviewholder.ViewHolderDelegate;

public class LoadingViewHolder_ViewHolderDelegate extends LoadingViewHolder implements ViewHolderDelegate {

  @Override
  public void bind(final Object item) {
    super.bind((Loading) item);
  }

  @Override
  public void bindView(final View view) {
    super.bindView(view);
  }

  @Override
  public int layout() {
    return 10;
  }

  @Override
  public int[] onClick() {
    return new int[0];
  }
}
