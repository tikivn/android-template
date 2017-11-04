package vn.tale.viewholdersdemo.viewholder;

import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import vn.tale.viewholdersdemo.R2;
import vn.tale.viewholdersdemo.glide.GlideApp;
import vn.tale.viewholdersdemo.util.TextViewsKt;
import vn.tiki.viewholders.ViewHolder;

@ViewHolder(
    layout = R2.layout.viewholder_item_product,
    onClick = {R2.id.itemView},
    bindTo = ProductModel.class
)
class ProductViewHolder extends BaseViewHolder {

  @BindView(R2.id.ivThumb) ImageView ivThumb;
  @BindView(R2.id.tvPrice) TextView tvPrice;
  @BindView(R2.id.tvTitle) TextView tvTitle;

  void bind(ProductModel product) {
    tvTitle.setText(product.getTitle());
    TextViewsKt.setPrice(tvPrice, product.getPrice());
    GlideApp
        .with(ivThumb.getContext())
        .load(product.getImageUrl())
        .into(ivThumb);
  }
}
