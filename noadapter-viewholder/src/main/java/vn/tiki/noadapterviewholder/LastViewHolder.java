package vn.tiki.noadapterviewholder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import vn.tiki.noadapter2.AbsViewHolder;

public final class LastViewHolder extends AbsViewHolder {

  private final ViewHolderDelegate viewHolderDelegate;

  public static LastViewHolder create(ViewGroup parent, ViewHolderDelegate viewHolderDelegate) {
    final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    final View view = inflater.inflate(viewHolderDelegate.layout(), parent, false);
    return new LastViewHolder(view, viewHolderDelegate);
  }

  private LastViewHolder(View itemView, final ViewHolderDelegate viewHolderDelegate) {
    super(itemView);
    this.viewHolderDelegate = viewHolderDelegate;
    itemView.setOnClickListener(null);
    viewHolderDelegate.bindView(itemView);
    final int[] clickableIds = viewHolderDelegate.onClick();
    if (clickableIds == null || clickableIds.length == 0) {
      return;
    }
    for (int id : clickableIds) {
      registerOnClickOn(itemView.findViewById(id));
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public void bind(final Object item) {
    super.bind(item);
    viewHolderDelegate.bind(item);
  }
}