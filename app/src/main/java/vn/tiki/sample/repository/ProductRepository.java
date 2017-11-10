package vn.tiki.sample.repository;

import com.google.gson.Gson;
import com.nytimes.android.external.store3.base.impl.RealStoreBuilder;
import com.nytimes.android.external.store3.base.impl.Store;
import com.nytimes.android.external.store3.base.impl.StoreBuilder;
import io.reactivex.Single;
import java.io.File;
import java.io.IOException;
import javax.inject.Inject;
import javax.inject.Singleton;
import okhttp3.ResponseBody;
import okio.BufferedSource;
import vn.tiki.sample.api.ApiService;
import vn.tiki.sample.entity.ListData;
import vn.tiki.sample.entity.Product;
import vn.tiki.sample.util.Stores;

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


  public Single<ListData<Product>> fetchProducts(int page) {
    return store.fetch(page);
  }

  public Single<ListData<Product>> getProducts(int page) {
    return store.get(page);
  }

}

