package vn.tiki.sample.entity;

import android.os.Parcelable;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

@com.google.auto.value.AutoValue
public abstract class Product implements Parcelable {

  @SerializedName("description")
  public abstract String description();

  @SerializedName("id")
  public abstract String id();

  @SerializedName("image")
  public abstract String image();

  @SerializedName("price")
  public abstract float price();

  @SerializedName("title")
  public abstract String title();

  static TypeAdapter<Product> typeAdapter(Gson gson) {
    return new AutoValue_Product.GsonTypeAdapter(gson);
  }
}
