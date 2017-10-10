package vn.tiki.sample.productlist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import intents.Intents;
import io.reactivex.Observable;
import java.util.List;
import javax.inject.Inject;
import vn.tiki.collectionview.Adapter;
import vn.tiki.collectionview.CollectionView;
import vn.tiki.collectionview.DataProvider;
import vn.tiki.collectionview.ListData;
import vn.tiki.daggers.Daggers;
import vn.tiki.noadapter2.DiffCallback;
import vn.tiki.noadapter2.OnlyAdapter;
import vn.tiki.sample.R;
import vn.tiki.sample.base.BaseActivity;
import vn.tiki.sample.entity.Product;
import vn.tiki.sample.repository.ProductRepository;

public class ProductListingActivity extends BaseActivity {

  @BindView(android.R.id.content) View rootView;
  @BindView(R.id.toolbar) Toolbar toolbar;
  @BindView(R.id.vCollectionView) CollectionView vCollectionView;

  @BindString(R.string.product_listing) String textProductListing;
  @BindString(R.string.product_listing_try_again) String textTryAgain;
  @BindString(R.string.product_listing_error_occurred) String textError;

  @Inject ProductRepository productRepository;

  public static Intent intent(Context context) {
    return new Intent(context, ProductListingActivity.class);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Daggers.inject(this);

    setContentView(R.layout.activity_product_listing);
    ButterKnife.bind(this);

    configureToolbar();
    configureCollectionView();
  }

  private void configureToolbar() {
    setSupportActionBar(toolbar);
    final ActionBar supportActionBar = getSupportActionBar();
    if (supportActionBar == null) {
      return;
    }
    setTitle(textProductListing);
  }

  private void configureCollectionView() {
    final OnlyAdapter adapter = new OnlyAdapter.Builder()
        .viewHolderFactory((parent, type) -> ProductViewHolder.create(parent))
        .diffCallback(new DiffCallback() {
          @Override public boolean areItemsTheSame(Object oldItem, Object newItem) {
            return oldItem instanceof Product
                && newItem instanceof Product
                && ((Product) oldItem).id().equals(((Product) newItem).id());
          }

          @Override public boolean areContentsTheSame(Object oldItem, Object newItem) {
            return oldItem.equals(newItem);
          }
        })
        .onItemClickListener((view, item, position) -> startActivity(
            Intents.productDetailActivity(ProductListingActivity.this)
                .product(((Product) item))
                .make()))
        .build();

    vCollectionView.setAdapter(new Adapter() {
      @Override public DataProvider onCreateDataProvider() {
        return new DataProvider() {
          @Override public Observable<ListData<Product>> fetchNewest() {
            return productRepository.getProducts(1, true);
          }

          @Override public Observable<ListData<Product>> fetch(int page) {
            return productRepository.getProducts(page, false);
          }
        };
      }

      @Override public RecyclerView.LayoutManager onCreateLayoutManager() {
        return new LinearLayoutManager(
            ProductListingActivity.this,
            LinearLayoutManager.VERTICAL,
            false);
      }

      @Override
      public RecyclerView.Adapter<? extends RecyclerView.ViewHolder> onCreateRecyclerViewAdapter() {
        return adapter;
      }

      @Override public void onBindItems(List items) {
        adapter.setItems(items);
      }

      @NonNull @Override public View onCreateErrorView(ViewGroup parent, Throwable throwable) {
        return getLayoutInflater().inflate(R.layout.view_error, parent, false);
      }
    });
  }
}
