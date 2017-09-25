package vn.tiki.sample.di;

import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import vn.tiki.sample.BuildConfig;
import vn.tiki.sample.api.ApiService;
import vn.tiki.sample.model.UserModel;

/**
 * Created by Giang Nguyen on 8/25/17.
 */
@Module
public class AppModule {

  @Singleton @Provides OkHttpClient provideOkHttpClient() {
    HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
    logging.setLevel(HttpLoggingInterceptor.Level.BODY);
    return new OkHttpClient.Builder()
        .addInterceptor(logging)
        .build();
  }

  @Singleton @Provides ApiService provideApiService(OkHttpClient okHttpClient) {
    return new Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(ApiService.class);
  }

  @Singleton @Provides public UserModel provideUserModel() {
    return new UserModel();
  }
}
