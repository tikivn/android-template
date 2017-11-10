package vn.tiki.sample.productlist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import intents.Intents;
import java.util.List;
import javax.inject.Inject;
import viewholders.NoAdapterFactory;
import vn.tale.viewholdersdemo.viewholder.ProductModel;
import vn.tiki.daggers.Daggers;
import vn.tiki.noadapter2.OnlyAdapter;
import vn.tiki.sample.R;
import vn.tiki.sample.base.BaseMvpActivity;
import vn.tiki.sample.base.NetworkStatusObserver;
import vn.tiki.sample.entity.Product;
import vn.tiki.sample.repository.ProductRepository;
import vn.tiki.sample.util.InfiniteScrollListener;

public class ProductListingActivity extends BaseMvpActivity<ProductListingView, ProductListingPresenter>
    implements NetworkStatusObserver, ProductListingView {

  @BindString(R.string.product_listing_error_occurred) String textError;
  @BindString(R.string.product_listing) String textProductListing;
  @BindString(R.string.product_listing_try_again) String textTryAgain;
  @BindView(android.R.id.content) View rootView;
  @BindView(R.id.toolbar) Toolbar toolbar;
  @BindView(R.id.vError) View vError;
  @BindView(R.id.pbLoading) View pbLoading;
  @BindView(R.id.rvProducts) RecyclerView rvProducts;
  @BindView(R.id.srlRefresh) SwipeRefreshLayout srlRefresh;
  @Inject ProductRepository productRepository;
  @Inject ProductListingPresenter presenter;
  private Snackbar errorSnackBar;
  private OnlyAdapter rvProductAdapter;

  public static Intent intent(Context context) {
    return new Intent(context, ProductListingActivity.class);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Daggers.inject(this);

    setContentView(R.layout.activity_product_listing);
    ButterKnife.bind(this);

    errorSnackBar = Snackbar.make(rootView, textError, BaseTransientBottomBar.LENGTH_SHORT);

    configureToolbar();
    configureRecyclerView();

    srlRefresh.setOnRefreshListener(() -> presenter.onRefresh());

    connect(presenter, this);
    presenter.onLoad();
  }

  @Override
  public void onNetworkStatusChanged(final boolean isConnected) {

  }

  @Override
  public void showContent(final List<ProductModel> products) {
    pbLoading.setVisibility(View.GONE);
    srlRefresh.setVisibility(View.VISIBLE);
    srlRefresh.setRefreshing(false);
    rvProductAdapter.setItems(products);
  }

  @Override
  public void showLoadMoreError() {
    showErrorSnackBar();
  }

  @Override
  public void showLoadMoreLoading() {
    hideErrorSnackBar();
  }

  @Override
  public void showRefreshError() {
    srlRefresh.setRefreshing(false);
    showErrorSnackBar();
  }

  @Override
  public void showRefreshLoading() {
    srlRefresh.setRefreshing(true);
    hideErrorSnackBar();
  }

  @Override
  public void showStartError() {
    pbLoading.setVisibility(View.GONE);
    vError.setVisibility(View.VISIBLE);
  }

  @Override
  public void showStartLoading() {
    srlRefresh.setVisibility(View.GONE);
    vError.setVisibility(View.GONE);
    pbLoading.setVisibility(View.VISIBLE);
  }

  private void configureRecyclerView() {
    final LinearLayoutManager layoutManaer = new LinearLayoutManager(this);
    rvProducts.setLayoutManager(layoutManaer);
    rvProducts.addOnScrollListener(new InfiniteScrollListener(layoutManaer, 4, () -> presenter.onLoadMore()));
    rvProductAdapter = NoAdapterFactory.builder()
        .onItemClickListener((view, item, position) -> Intents.productDetailActivity(
            ProductListingActivity.this)
            .product(((ProductModel) item))
            .make())
        .build();
    rvProducts.setAdapter(rvProductAdapter);
  }

  private void configureToolbar() {
    setSupportActionBar(toolbar);
    final ActionBar supportActionBar = getSupportActionBar();
    if (supportActionBar == null) {
      return;
    }
    setTitle(textProductListing);
  }

  private void hideErrorSnackBar() {
    if (errorSnackBar.isShownOrQueued()) {
      errorSnackBar.dismiss();
    }
  }

  private void showErrorSnackBar() {
    if (!errorSnackBar.isShownOrQueued()) {
      errorSnackBar.show();
    }
  }
}
