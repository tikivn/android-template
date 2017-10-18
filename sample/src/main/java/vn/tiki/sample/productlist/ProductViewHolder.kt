package vn.tiki.sample.productlist

import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import kotlinx.android.synthetic.main.productlist_item_product.view.ivThumb
import kotlinx.android.synthetic.main.productlist_item_product.view.tvPrice
import kotlinx.android.synthetic.main.productlist_item_product.view.tvTitle
import vn.tiki.noadapter2.AbsViewHolder
import vn.tiki.sample.R
import vn.tiki.sample.entity.Product
import vn.tiki.sample.util.inflate
import vn.tiki.sample.util.setImage
import vn.tiki.sample.util.setPrice

class ProductViewHolder private constructor(itemView: View) : AbsViewHolder(itemView) {

  init {
    ButterKnife.bind(this, itemView)
  }

  override fun bind(item: Any): Unit = with(itemView) {
    super.bind(item)
    if (item !is Product) {
      return
    }

    tvTitle.text = item.title
    tvPrice.setPrice(item.price)
    ivThumb.setImage(item.image)
  }

  companion object {
    fun create(parent: ViewGroup): ProductViewHolder {
      val view = parent.inflate(R.layout.productlist_item_product)
      return ProductViewHolder(view)
    }
  }
}
