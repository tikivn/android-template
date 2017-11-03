package vn.tiki.viewholders;

import vn.tiki.noadapter2.DiffCallback;

public class OnlyDiffCallback implements DiffCallback {

  @Override
  public boolean areContentsTheSame(final Object oldItem, final Object newItem) {
    if (oldItem instanceof Differentiable) {
      return ((Differentiable) oldItem).isSameContent(newItem);
    } else if (newItem instanceof Differentiable) {
      return ((Differentiable) newItem).isSameContent(oldItem);
    } else {
      return oldItem.equals(newItem);
    }
  }

  @Override
  public boolean areItemsTheSame(final Object oldItem, final Object newItem) {
    if (oldItem instanceof Differentiable) {
      return ((Differentiable) oldItem).isSameItem(newItem);
    } else if (newItem instanceof Differentiable) {
      return ((Differentiable) newItem).isSameItem(oldItem);
    } else {
      return oldItem.equals(newItem);
    }
  }
}
