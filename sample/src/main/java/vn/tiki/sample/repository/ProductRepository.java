package vn.tiki.sample.repository;

import com.google.gson.Gson;
import com.nytimes.android.external.store3.base.impl.RealStoreBuilder;
import com.nytimes.android.external.store3.base.impl.Store;
import com.nytimes.android.external.store3.base.impl.StoreBuilder;
import io.reactivex.Single;
import ix.Ix;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import okhttp3.ResponseBody;
import okio.BufferedSource;
import vn.tale.viewholdersdemo.viewholder.ProductModel;
import vn.tiki.collectionview.Paging;
import vn.tiki.sample.api.ApiService;
import vn.tiki.sample.entity.ListData;
import vn.tiki.sample.entity.Product;
import vn.tiki.sample.util.Stores;
import vn.tiki.sample.util.Urls;

@Singleton
public class ProductRepository {

  private final Store<ListData<Product>, Integer> store;

  @Inject
  ProductRepository(File cacheDir, ApiService api, Gson gson) {
    final RealStoreBuilder<BufferedSource, ListData<Product>, Integer> builder = StoreBuilder.<Integer, BufferedSource, ListData<Product>>parsedWithKey()
        .fetcher(
            page -> api.getProducts(page, 10)
                .map(ResponseBody::source))
        .parser(Stores.listDataParser(gson, Product.class))
        .memoryPolicy(Stores.memoryPolicy(10, 10));

    try {
      builder.persister(Stores.persister(new File(cacheDir, "product_listing")));
    } catch (IOException e) {
      e.printStackTrace();
    }

    store = builder.open();
  }

  public Single<vn.tiki.collectionview.ListData<ProductModel>> getProducts(int page, boolean forceApi) {
    final Single<ListData<Product>> response;
    if (forceApi) {
      response = store.fetch(page);
    } else {
      response = store.get(page);
    }

    return response.map(productListData -> {
      final List<ProductModel> productModels = Ix.from(productListData.items())
          .map(product -> new ProductModel(product.title(), product.price(), Urls.resolveImageUrl(product.image())))
          .toList();

      final vn.tiki.sample.entity.Paging paging = vn.tiki.sample.entity.Paging.builder()
          .currentPage(productListData.currentPage())
          .lastPage(productListData.lastPage())
          .total(productListData.total())
          .make();

      return new vn.tiki.collectionview.ListData<ProductModel>() {
        @Override
        public List<ProductModel> items() {
          return productModels;
        }

        @Override
        public Paging paging() {
          return paging;
        }
      };
    });
  }

}

