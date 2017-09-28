package vn.tiki.sample.model;

import android.support.annotation.NonNull;
import io.reactivex.Observable;
import ix.Ix;
import javax.inject.Inject;
import javax.inject.Singleton;
import vn.tiki.sample.BuildConfig;
import vn.tiki.sample.api.ApiService;
import vn.tiki.sample.entity.ListData;
import vn.tiki.sample.entity.Product;
import vn.tiki.sample.util.Strings;

@Singleton
public class ProductModel {

  private final ApiService apiService;

  @Inject
  public ProductModel(ApiService apiService) {
    this.apiService = apiService;
  }

  public Observable<ListData<Product>> getProducts(int page) {
    return apiService.getProducts(page, 10)
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
  }

  @NonNull private String resolveImageUrl(String imageUrl) {
    return BuildConfig.BASE_URL + imageUrl;
  }
}
