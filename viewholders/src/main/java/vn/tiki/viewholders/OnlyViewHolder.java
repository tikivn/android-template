package vn.tiki.viewholders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import vn.tiki.noadapter2.AbsViewHolder;

public final class OnlyViewHolder extends AbsViewHolder {

  private final ViewHolderDelegate viewHolderDelegate;

  public static OnlyViewHolder create(ViewGroup parent, ViewHolderDelegate viewHolderDelegate) {
    final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    final View view = inflater.inflate(viewHolderDelegate.layout(), parent, false);
    return new OnlyViewHolder(view, viewHolderDelegate);
  }

  private OnlyViewHolder(View itemView, final ViewHolderDelegate viewHolderDelegate) {
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

  @Override
  public void bind(final Object item) {
    super.bind(item);
    viewHolderDelegate.bind(item);
  }

  @Override
  public void unbind() {
    super.unbind();
    viewHolderDelegate.unbind();
  }
}