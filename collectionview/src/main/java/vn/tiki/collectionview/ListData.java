package vn.tiki.collectionview;

import java.util.List;

public final class ListData<T> {

  private final List<T> items;
  private final Paging paging;

  public ListData(List<T> items, Paging paging) {
    this.items = items;
    this.paging = paging;
  }

  public List<T> getItems() {
    return items;
  }

  public Paging getPaging() {
    return paging;
  }

  @Override public int hashCode() {
    int result = items != null ? items.hashCode() : 0;
    result = 31 * result + (paging != null ? paging.hashCode() : 0);
    return result;
  }

  @Override public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ListData<?> listData = (ListData<?>) o;

    if (items != null ? !items.equals(listData.items) : listData.items != null) {
      return false;
    }
    return paging != null ? paging.equals(listData.paging) : listData.paging == null;
  }
}
