package vn.tiki.sample.viewholder;

import android.os.Parcelable;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import vn.tiki.sample.R;
import vn.tiki.sample.glide.GlideApp;
import vn.tiki.sample.util.TextViews;
import vn.tiki.sample.util.Urls;

@com.google.auto.value.AutoValue
public abstract class Product implements Parcelable, ViewHolderDelegate {

  ImageView ivThumb;
  TextView tvPrice;
  TextView tvTitle;

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

  @Override
  public void bind() {
    tvTitle.setText(id() + " - " + title());
    TextViews.setPrice(tvPrice, price());
    GlideApp
        .with(ivThumb.getContext())
        .load(Urls.resolveImageUrl(image()))
        .into(ivThumb);
  }

  @Override
  public int layout() {
    return R.layout.productlist_item_product;
  }

  @Override
  public int[] onClickIds() {
    return new int[]{R.id.ivThumb};
  }

  static TypeAdapter<Product> typeAdapter(Gson gson) {
    return new AutoValue_Product.GsonTypeAdapter(gson);
  }
}
