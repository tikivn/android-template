package vn.tiki.sample.productlist;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.util.List;
import javax.inject.Inject;
import timber.log.Timber;
import vn.tiki.sample.di.ActivityScope;
import vn.tiki.sample.entity.ListData;
import vn.tiki.sample.entity.Product;
import vn.tiki.sample.model.ProductModel;
import vn.tiki.sample.mvp.rx.RxBasePresenter;
import vn.tiki.sample.util.Lists;

@ActivityScope
public class ProductListingPresenter extends RxBasePresenter<ProductListingView> {

  private final ProductModel productModel;
  private ListData<Product> productListData;

  @Inject ProductListingPresenter(ProductModel productModel) {
    this.productModel = productModel;
  }

  @Override public void attach(ProductListingView view) {
    super.attach(view);

    loadProducts();
  }

  private void loadProducts() {
    getViewOrThrow().showLoading();
    disposeOnDestroy(getProducts(1)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
            items -> sendToView(view -> view.showContent(items)),
            throwable -> sendToView(ProductListingView::showLoadError)));
  }

  private Observable<List<Product>> getProducts(int page) {
    return productModel.getProducts(page)
        .subscribeOn(Schedulers.io())
        .doOnNext(productListData -> this.productListData = productListData)
        .map(ListData::items);
  }

  void onRetry() {
    loadProducts();
  }

  void onRefresh() {
    getViewOrThrow().showRefreshing();
    disposeOnDestroy(getProducts(1)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
            items -> sendToView(view -> view.showContent(items)),
            throwable -> sendToView(ProductListingView::showRefreshError)));
  }

  void onLoadMore() {
    if (productListData.currentPage() == productListData.lastPage()) {
      return;
    }
    final List<Product> currentProducts = productListData.items();
    disposeOnDestroy(getProducts(productListData.currentPage() + 1)
        .map(items -> Lists.merged(currentProducts, items))
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
            items -> sendToView(view -> view.showContent(items)),
            throwable -> Timber.e("load more products", throwable)));
  }
}
