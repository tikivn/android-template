package vn.tiki.sample.collectionview;

import java.util.List;
import vn.tiki.collectionview.ListData;
import vn.tiki.collectionview.Paging;

public class ListDataImpl<T> implements ListData<T> {
  private final List<T> items;
  private final Paging paging;

  public ListDataImpl(List<T> items, Paging paging) {
    this.items = items;
    this.paging = paging;
  }

  @Override public List<T> items() {
    return items;
  }

  @Override public Paging paging() {
    return paging;
  }
}
