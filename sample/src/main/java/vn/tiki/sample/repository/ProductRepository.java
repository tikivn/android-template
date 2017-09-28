package vn.tiki.sample.repository;

import android.support.annotation.NonNull;
import io.reactivex.Observable;
import ix.Ix;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import javax.inject.Singleton;
import vn.tiki.sample.BuildConfig;
import vn.tiki.sample.api.ApiService;
import vn.tiki.sample.entity.ListData;
import vn.tiki.sample.entity.Product;
import vn.tiki.sample.util.Strings;

@Singleton
public class ProductRepository {

  private final BoundResource<Integer, ListData<Product>> resource;

  @Inject ProductRepository(ApiService api) {
    final ObjectCache<Integer, ListData<Product>> cache = new ObjectCache<>(1000);
    final CacheSource<Integer, ListData<Product>> cacheSource = new CacheSource<>(
        cache,
        10,
        TimeUnit.MINUTES);
    final ApiSource<Integer, ListData<Product>> apiSource = key -> api.getProducts(key, 10)
        .map(response -> ListData.<Product>builder()
            .total(response.getTotal())
            .currentPage(response.getCurrentPage())
            .lastPage(response.getLastPage())
            .items(
                Ix.from(response.getItems())
                    .map(productResponse -> Product.builder()
                        .title(productResponse.getTitle())
                        .price(productResponse.getPrice())
                        .imageUrl(resolveImageUrl(productResponse.getImage()))
                        .description(Strings.toHtml(productResponse.getDescription()).toString())
                        .make())
                    .toList())
            .make());
    resource = new BoundResource<>(cacheSource, apiSource);
  }

  @NonNull private String resolveImageUrl(String imageUrl) {
    return BuildConfig.BASE_URL + imageUrl;
  }

  public Observable<ListData<Product>> getProducts(int page, boolean forceApi) {
    if (forceApi) {
      this.resource.clearCache();
    }
    return resource.get(page);
  }
}

