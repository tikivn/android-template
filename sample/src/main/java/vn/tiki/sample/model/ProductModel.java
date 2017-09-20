package vn.tiki.sample.model;

import io.reactivex.Observable;
import java.util.Arrays;
import vn.tiki.sample.entity.ListData;
import vn.tiki.sample.entity.Product;

public class ProductModel {

  public Observable<ListData<Product>> getProducts(int page) {
    final ListData<Product> data = ListData.<Product>builder()
        .currentPage(1)
        .lastPage(2)
        .total(19)
        .items(
            Arrays.asList(
                Product.builder()
                    .title("Ladies Chrome T-Shirt")
                    .description("")
                    .imageUrl("https://data/images/10-23169B.jpg")
                    .price(13.3f)
                    .make(),
                Product.builder()
                    .title("Ladies Chrome T-Shirt")
                    .description("")
                    .imageUrl("https://data/images/10-23169B.jpg")
                    .price(13.3f)
                    .make()
            ))
        .make();
    return Observable.just(data);
  }
}
