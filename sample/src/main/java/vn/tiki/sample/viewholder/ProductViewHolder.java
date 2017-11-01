package vn.tiki.sample.viewholder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import vn.tiki.noadapter2.AbsViewHolder;
import vn.tiki.sample.R;

public class ProductViewHolder extends AbsViewHolder {

  ImageView ivThumb;
  TextView tvPrice;
  TextView tvTitle;
  Product product;

  public static ProductViewHolder create(ViewGroup parent) {
    final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    final View view = inflater.inflate(R.layout.productlist_item_product, parent, false);
    return new ProductViewHolder(view);
  }

  private ProductViewHolder(View itemView) {
    super(itemView);
    registerOnClickOn(itemView.findViewById(R.id.ivThumb));
    ivThumb = itemView.findViewById(R.id.ivThumb);
    tvPrice = itemView.findViewById(R.id.tvPrice);
    tvTitle = itemView.findViewById(R.id.tvTitle);
  }

  @Override
  public void bind(Object item) {
    super.bind(item);
    if (item instanceof Product) {
      product = (Product) item;
      product.ivThumb = ivThumb;
      product.tvPrice = tvPrice;
      product.tvTitle = tvTitle;
      product.bind();
    }
  }

  @Override
  public void unbind() {
    product.ivThumb = null;
    product.tvPrice = null;
    product.tvTitle = null;
    super.unbind();
  }
}
