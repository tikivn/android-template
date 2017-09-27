package vn.tiki.sample.entity;

import android.os.Parcelable;

@com.google.auto.value.AutoValue
public abstract class Product implements Parcelable {

  public static Builder builder() {
    return new AutoValue_Product.Builder();
  }

  public abstract String title();

  public abstract float price();

  public abstract String description();

  public abstract String imageUrl();

  @com.google.auto.value.AutoValue.Builder
  public static abstract class Builder {
    public abstract Builder title(String title);

    public abstract Builder price(float price);

    public abstract Builder description(String description);

    public abstract Builder imageUrl(String imageUrl);

    public abstract Product make();
  }
}
