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
import vn.tiki.sample.mvp.rx.RxBasePresenter;
import vn.tiki.sample.repository.ProductRepository;
import vn.tiki.sample.util.Lists;

@ActivityScope
public class ProductListingPresenter extends RxBasePresenter<ProductListingView> {

  private final ProductRepository productRepository;
  private ListData<Product> productListData;

  @Inject ProductListingPresenter(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  @Override public void attach(ProductListingView view) {
    super.attach(view);

    loadProducts();
  }

  private void loadProducts() {
    getViewOrThrow().showLoading();
    disposeOnDestroy(getProducts(1, false)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
            items -> sendToView(view -> view.showContent(items)),
            throwable -> sendToView(ProductListingView::showLoadError)));
  }

  private Observable<List<Product>> getProducts(int page, boolean forceApi) {
    return productRepository.getProducts(page, forceApi)
        .subscribeOn(Schedulers.io())
        .doOnNext(productListData -> this.productListData = productListData)
        .map(ListData::items)
        .doOnError(Throwable::printStackTrace);
  }

  void onRetry() {
    loadProducts();
  }

  void onRefresh() {
    getViewOrThrow().showRefreshing();
    disposeOnDestroy(getProducts(1, true)
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
    disposeOnDestroy(getProducts(productListData.currentPage() + 1, false)
        .map(items -> Lists.merged(currentProducts, items))
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
            items -> sendToView(view -> view.showContent(items)),
            throwable -> Timber.e("getProducts more products", throwable)));
  }
}
