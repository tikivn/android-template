package vn.tiki.sample.noadapterviewholder;

import android.view.ViewGroup;
import vn.tiki.noadapter2.AbsViewHolder;
import vn.tiki.noadapter2.ViewHolderFactory;
import vn.tiki.noadapterviewholder.LastViewHolder;
import vn.tiki.noadapterviewholder.ViewHolderDelegate;

final class ViewHolderFactoryImpl implements ViewHolderFactory {

  @Override
  public AbsViewHolder viewHolderForType(ViewGroup parent, int type) {
    final ViewHolderDelegate viewHolderDelegate = makeViewHolderDelegate(type);
    return LastViewHolder.create(parent, viewHolderDelegate);
  }

  private ViewHolderDelegate makeViewHolderDelegate(int type) {
    switch (type) {
      case 10: return new LoadingViewHolder_ViewHolderDelegate();
      default:
        throw new IllegalArgumentException("unknown type: " + type);
    }
  }
}
