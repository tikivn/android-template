package vn.tiki.sample.collectionview;

import vn.tiki.collectionview.Paging;

public final class PagingImpl implements Paging {
  private final int total;
  private final int currentPage;
  private final int lastPage;

  public PagingImpl(int total, int currentPage, int lastPage) {
    this.total = total;
    this.currentPage = currentPage;
    this.lastPage = lastPage;
  }

  @Override public int total() {
    return total;
  }

  @Override public int currentPage() {
    return currentPage;
  }

  @Override public int lastPage() {
    return lastPage;
  }
}
