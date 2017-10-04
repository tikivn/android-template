package vn.tiki.sample.productdetail;

import javax.inject.Inject;
import vn.tiki.sample.di.ActivityScope;
import vn.tiki.sample.entity.Product;
import vn.tiki.sample.mvp.rx.RxBasePresenter;
import vn.tiki.sample.repository.CartRepository;

@ActivityScope
public class ProductDetailPresenter extends RxBasePresenter<ProductDetailView> {

  private final CartRepository cartRepository;
  private String size;
  private Product product;

  @Inject
  public ProductDetailPresenter(CartRepository cartRepository) {
    this.cartRepository = cartRepository;
  }

  void setProduct(Product product) {
    this.product = product;
  }

  void onSizeSelected(String size) {
    this.size = size;
  }

  void onAddToCartClick() {
    cartRepository.addToCart(product, size, 1);
  }
}
