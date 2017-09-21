package vn.tiki.sample.response;

import com.google.gson.annotations.SerializedName;

public class ProductResponse {

  @SerializedName("image")
  private String image;

  @SerializedName("price")
  private float price;

  @SerializedName("description")
  private String description;

  @SerializedName("title")
  private String title;

  public String getImage() {
    return image;
  }

  public float getPrice() {
    return price;
  }

  public String getDescription() {
    return description;
  }

  public String getTitle() {
    return title;
  }
}