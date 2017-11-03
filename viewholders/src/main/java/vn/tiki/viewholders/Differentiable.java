package vn.tiki.viewholders;

public interface Differentiable {

  /**
   * Called by the DiffUtil to decide whether two object represent the same Item.
   * <p>
   * For example, if your items have unique ids, this method should check their id equality.
   *
   * @param other The item in the new list
   * @return True if the two items represent the same object or false if they are different.
   */
  boolean isSameItem(Object other);


  /**
   * Called by the DiffUtil when it wants to check whether two items have the same data.
   * DiffUtil uses this information to detect if the contents of an item has changed.
   * <p>
   * DiffUtil uses this method to check equality instead of {@link Object#equals(Object)}
   * so that you can change its behavior depending on your UI.
   * For example, if you are using DiffUtil with a
   * {@link android.support.v7.widget.RecyclerView.Adapter RecyclerView.Adapter}, you should
   * return whether the items' visual representations are the same.
   * <p>
   * This method is called only if {@link #isSameItem(Object)} returns
   * {@code true} for these items.
   *
   * @param other The item in the new list which replaces the
   *              oldItem
   * @return {@code True} if the contents of the items are the same or {@code false}
   * if they are different.
   */
  boolean isSameContent(Object other);
}
