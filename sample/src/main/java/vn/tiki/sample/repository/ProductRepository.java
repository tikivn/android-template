package vn.tiki.sample.repository;

import android.support.annotation.NonNull;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nytimes.android.external.store3.base.impl.RealStoreBuilder;
import com.nytimes.android.external.store3.base.impl.Store;
import com.nytimes.android.external.store3.base.impl.StoreBuilder;
import io.reactivex.Single;
import ix.Ix;
import java.io.File;
import java.io.IOException;
import javax.inject.Inject;
import javax.inject.Singleton;
import okhttp3.ResponseBody;
import okio.BufferedSource;
import vn.tiki.collectionview.ListData;
import vn.tiki.collectionview.Paging;
import vn.tiki.sample.BuildConfig;
import vn.tiki.sample.api.ApiService;
import vn.tiki.sample.entity.Product;
import vn.tiki.sample.response.ListResponse;
import vn.tiki.sample.response.ProductResponse;
import vn.tiki.sample.util.Stores;
import vn.tiki.sample.util.Strings;

@Singleton
public class ProductRepository {

  private final Store<ListResponse<ProductResponse>, Integer> store;

  @Inject
  ProductRepository(File cacheDir, ApiService api) {
    final RealStoreBuilder<BufferedSource, ListResponse<ProductResponse>, Integer> builder = StoreBuilder.<Integer, BufferedSource, ListResponse<ProductResponse>>parsedWithKey()
        .fetcher(
            page -> api.getProducts(page, 10)
                .map(ResponseBody::source))
        .parser(Stores.parser(new Gson(), new TypeToken<ListResponse<ProductResponse>>() {
        }.getType()))
        .memoryPolicy(Stores.memoryPolicy(10, 10));

    try {
      builder.persister(Stores.persister(new File(cacheDir, "product_listing")));
    } catch (IOException e) {
      e.printStackTrace();
    }

    store = builder.open();
  }

  public Single<ListData<Product>> getProducts(int page, boolean forceApi) {
    final Single<ListResponse<ProductResponse>> response;
    if (forceApi) {
      response = store.fetch(page);
    } else {
      response = store.get(page);
    }
    return response
        .map(res -> new ListData<>(
            Ix.from(res.getItems())
                .map(productResponse -> Product.builder()
                    .id(productResponse.getId())
                    .title(productResponse.getTitle())
                    .price(productResponse.getPrice())
                    .imageUrl(resolveImageUrl(productResponse.getImage()))
                    .description(Strings.toHtml(productResponse.getDescription()).toString())
                    .make())
                .toList(),
            Paging.builder()
                .currentPage(res.getCurrentPage())
                .lastPage(res.getLastPage())
                .total(res.getTotal())
                .make()));
  }

  @NonNull
  private String resolveImageUrl(String imageUrl) {
    return BuildConfig.BASE_URL + imageUrl;
  }
}

