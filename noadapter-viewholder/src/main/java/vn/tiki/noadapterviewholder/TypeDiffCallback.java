package vn.tiki.noadapterviewholder;

import vn.tiki.noadapter2.DiffCallback;

public class TypeDiffCallback implements DiffCallback {

  @Override
  public boolean areContentsTheSame(final Object oldItem, final Object newItem) {
    return oldItem.equals(newItem);
  }

  @Override
  public boolean areItemsTheSame(final Object oldItem, final Object newItem) {
    return oldItem.getClass().equals(newItem.getClass());
  }
}
