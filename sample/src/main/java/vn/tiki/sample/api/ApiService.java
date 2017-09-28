package vn.tiki.sample.api;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import vn.tiki.sample.response.ListResponse;
import vn.tiki.sample.response.ProductResponse;

public interface ApiService {

  @SuppressWarnings("SameParameterValue") @GET("products")
  Observable<ListResponse<ProductResponse>> getProducts(
      @Query("page") int page,
      @Query("per_page") int perPage);
}
