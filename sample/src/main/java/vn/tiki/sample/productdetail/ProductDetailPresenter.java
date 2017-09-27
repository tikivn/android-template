package vn.tiki.sample.productdetail;

import javax.inject.Inject;
import vn.tiki.sample.di.ActivityScope;
import vn.tiki.sample.mvp.rx.RxBasePresenter;

@ActivityScope
public class ProductDetailPresenter extends RxBasePresenter<ProductDetailView> {

  @Inject
  public ProductDetailPresenter() {
  }

  void onSizeSelected(String size) {

  }

  void onAddToCartClick() {

  }
}
