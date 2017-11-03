package vn.tiki.sample.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import vn.tiki.sample.R;
import vn.tiki.sample.entity.Product;
import vn.tiki.sample.glide.GlideApp;
import vn.tiki.sample.util.TextViews;
import vn.tiki.sample.util.Urls;

//@ViewHolder(
//    layout = R.layout.productlist_item_product,
//    onClick = R.id.itemView
//)
public class ProductViewHolder {

  @BindView(R.id.ivThumb) ImageView ivThumb;
  @BindView(R.id.tvPrice) TextView tvPrice;
  @BindView(R.id.tvTitle) TextView tvTitle;

  void bind(Product product) {
    tvTitle.setText(product.id() + " - " + product.title());
    TextViews.setPrice(tvPrice, product.price());
    GlideApp
        .with(ivThumb.getContext())
        .load(Urls.resolveImageUrl(product.image()))
        .into(ivThumb);
  }

  void bindView(View itemView) {
    ButterKnife.bind(this, itemView);
  }
}
