package vn.tiki.sample.entity;

import java.util.List;

@com.google.auto.value.AutoValue
public abstract class ListData<T> {

  public static <T> Builder<T> builder() {
    return new AutoValue_ListData.Builder<>();
  }

  public abstract int total();

  public abstract int currentPage();

  public abstract int lastPage();

  public abstract List<T> items();

  @com.google.auto.value.AutoValue.Builder
  public static abstract class Builder<T> {
    public abstract Builder<T> total(int total);

    public abstract Builder<T> currentPage(int currentPage);

    public abstract Builder<T> lastPage(int lastPage);

    public abstract Builder<T> items(List<T> items);

    public abstract ListData<T> make();
  }
}
