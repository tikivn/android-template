package vn.tiki.sample.productdetail

import vn.tiki.sample.di.ActivityScope
import vn.tiki.sample.entity.Product
import vn.tiki.sample.mvp.rx.RxBasePresenter
import vn.tiki.sample.repository.CartRepository
import javax.inject.Inject

@ActivityScope
class ProductDetailPresenter @Inject
constructor(private val cartRepository: CartRepository) : RxBasePresenter<ProductDetailView>() {
  private var size: String? = null
  private var product: Product? = null

  internal fun setProduct(product: Product) {
    this.product = product
  }

  internal fun onSizeSelected(size: String) {
    this.size = size
  }

  internal fun onAddToCartClick() {
    cartRepository.addToCart(product!!, size!!, 1)
  }
}
