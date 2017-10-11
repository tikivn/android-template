package vn.tiki.collectionview;

public final class Paging {

  private final int currentPage;
  private final int lastPage;
  private final int total;

  private Paging(int currentPage, int lastPage, int total) {
    this.currentPage = currentPage;
    this.lastPage = lastPage;
    this.total = total;
  }

  public static Builder builder() {
    return new Builder();
  }

  public int total() {
    return total;
  }

  public int currentPage() {
    return currentPage;
  }

  public int lastPage() {
    return lastPage;
  }

  @Override public int hashCode() {
    int result = currentPage;
    result = 31 * result + lastPage;
    result = 31 * result + total;
    return result;
  }

  @Override public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Paging paging = (Paging) o;

    if (currentPage != paging.currentPage) {
      return false;
    }
    if (lastPage != paging.lastPage) {
      return false;
    }
    return total == paging.total;
  }

  public static final class Builder {

    private int currentPage;
    private int lastPage;
    private int total;

    public Builder currentPage(int currentPage) {
      this.currentPage = currentPage;
      return this;
    }

    public Builder lastPage(int lastPage) {
      this.lastPage = lastPage;
      return this;
    }

    public Builder total(int total) {
      this.total = total;
      return this;
    }

    public Paging make() {
      return new Paging(currentPage, lastPage, total);
    }
  }
}
