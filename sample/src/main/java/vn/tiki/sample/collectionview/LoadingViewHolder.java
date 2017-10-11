package vn.tiki.sample.collectionview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import vn.tiki.noadapter2.AbsViewHolder;
import vn.tiki.sample.R;

public class LoadingViewHolder extends AbsViewHolder {

  private LoadingViewHolder(View view) {
    super(view);
  }

  public static LoadingViewHolder create(ViewGroup parent) {
    final View view = LayoutInflater.from(parent.getContext()).inflate(
        R.layout.clv_item_loading,
        parent,
        false);
    return new LoadingViewHolder(view);
  }
}
