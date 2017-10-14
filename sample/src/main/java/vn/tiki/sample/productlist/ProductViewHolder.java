package vn.tiki.sample.productlist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import vn.tiki.noadapter2.AbsViewHolder;
import vn.tiki.sample.R;
import vn.tiki.sample.entity.Product;
import vn.tiki.sample.glide.GlideApp;
import vn.tiki.sample.util.TextViews;
import vn.tiki.sample.util.Urls;

public class ProductViewHolder extends AbsViewHolder {

  @BindView(R.id.ivThumb) ImageView ivThumb;
  @BindView(R.id.tvPrice) TextView tvPrice;
  @BindView(R.id.tvTitle) TextView tvTitle;

  public static ProductViewHolder create(ViewGroup parent) {
    final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    final View view = inflater.inflate(R.layout.productlist_item_product, parent, false);
    return new ProductViewHolder(view);
  }

  private ProductViewHolder(View itemView) {
    super(itemView);
    ButterKnife.bind(this, itemView);
  }

  @Override
  public void bind(Object item) {
    super.bind(item);
    if (!(item instanceof Product)) {
      return;
    }

    final Product product = (Product) item;

    tvTitle.setText(product.id() + " - " + product.title());
    TextViews.setPrice(tvPrice, product.price());
    GlideApp
        .with(itemView.getContext())
        .load(Urls.Companion.resolveImageUrl(product.image()))
        .into(ivThumb);
  }
}
