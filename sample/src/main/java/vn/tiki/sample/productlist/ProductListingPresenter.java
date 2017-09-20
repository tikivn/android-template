package vn.tiki.sample.productlist;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import javax.inject.Inject;
import vn.tiki.sample.entity.ListData;
import vn.tiki.sample.entity.Product;
import vn.tiki.sample.model.ProductModel;
import vn.tiki.sample.mvp.rx.RxBasePresenter;

public class ProductListingPresenter extends RxBasePresenter<ProductListingView> {

  private final ProductModel productModel;
  private ListData<Product> productListData;

  @Inject
  public ProductListingPresenter(ProductModel productModel) {
    this.productModel = productModel;
  }

  @Override public void attach(ProductListingView view) {
    super.attach(view);

    loadProducts(0);
  }

  private void loadProducts(int page) {
    getViewOrThrow().showLoading();
    productModel.getProducts(page)
        .doOnNext(productListData -> this.productListData = productListData)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
            productListData -> sendToView(view -> view.showContent(productListData.items())),
            throwable -> sendToView(ProductListingView::showLoadError));
  }

  void onRetry() {
    loadProducts(0);
  }
}
