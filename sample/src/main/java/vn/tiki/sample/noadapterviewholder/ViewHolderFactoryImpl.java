package vn.tiki.sample.noadapterviewholder;

import android.view.ViewGroup;
import vn.tiki.noadapter2.AbsViewHolder;
import vn.tiki.noadapter2.ViewHolderFactory;
import vn.tiki.noadapterviewholder.LastViewHolder;

class ViewHolderFactoryImpl implements ViewHolderFactory {

  @Override
  public AbsViewHolder viewHolderForType(final ViewGroup parent, final int type) {
    switch (type) {
      case 10:
        return LastViewHolder.create(parent, new LoadingViewHolder_ViewHolderDelegate());
      default:
        throw new IllegalArgumentException("unknown type: " + type);
    }
  }
}
