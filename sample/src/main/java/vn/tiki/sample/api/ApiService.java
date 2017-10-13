package vn.tiki.sample.api;

import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

  @SuppressWarnings("SameParameterValue")
  @GET("products")
  Single<ResponseBody> getProducts(
      @Query("page") int page,
      @Query("per_page") int perPage);
}
