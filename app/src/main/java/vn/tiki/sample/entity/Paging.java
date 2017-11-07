package vn.tiki.sample.entity;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Paging implements vn.tiki.collectionview.Paging {

  @AutoValue.Builder
  public static abstract class Builder {

    public abstract Paging make();

    public abstract Builder currentPage(int currentPage);

    public abstract Builder lastPage(int lastPage);

    public abstract Builder total(int total);
  }

  public static Builder builder() {
    return new AutoValue_Paging.Builder();
  }
}
