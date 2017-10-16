package vn.tiki.sample.api

import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

  @GET("products")
  fun getProducts(
      @Query("page") page: Int,
      @Query("per_page") perPage: Int): Single<ResponseBody>
}
