package vn.tiki.sample.di;

import android.arch.persistence.room.Room;
import android.content.Context;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dagger.Module;
import dagger.Provides;
import java.io.File;
import javax.annotation.Nullable;
import javax.inject.Singleton;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.BufferedSource;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import vn.tiki.sample.BuildConfig;
import vn.tiki.sample.api.ApiService;
import vn.tiki.sample.database.AppDatabase;
import vn.tiki.sample.database.CartDao;
import vn.tiki.sample.entity.MyAdapterFactory;
import vn.tiki.sample.model.UserModel;
import vn.tiki.sample.util.Assets;

/**
 * Created by Giang Nguyen on 8/25/17.
 */
@Module
public class AppModule {

  private final Context appContext;

  public AppModule(Context appContext) {
    this.appContext = appContext;
  }

  @Singleton
  @Provides
  public UserModel provideUserModel() {
    return new UserModel();
  }

  @Singleton
  @Provides
  ApiService provideApiService(Assets assets, OkHttpClient okHttpClient) {
    if ("dev".equals(BuildConfig.FLAVOR)) {
      return (page, perPage) -> assets.read("products.json")
          .map(bufferedSource -> new ResponseBody() {
            @Override
            public long contentLength() {
              return 0;
            }

            @Nullable
            @Override
            public MediaType contentType() {
              return MediaType.parse("application/json");
            }

            @Override
            public BufferedSource source() {
              return bufferedSource;
            }
          });
    }
    return new Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(ApiService.class);
  }

  @Singleton
  @Provides
  AppDatabase provideAppDatabase() {
    return Room.databaseBuilder(appContext, AppDatabase.class, "shopping").build();
  }

  @Singleton
  @Provides
  Assets provideAssets() {
    return new Assets(appContext);
  }

  @Provides
  File provideCacheDir() {
    return appContext.getCacheDir();
  }

  @Provides
  CartDao provideCartDao(AppDatabase appDatabase) {
    return appDatabase.cartDao();
  }

  @Provides
  Gson provideGson() {
    return new GsonBuilder()
        .registerTypeAdapterFactory(MyAdapterFactory.create())
        .create();
  }

  @Singleton
  @Provides
  OkHttpClient provideOkHttpClient() {
    HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
    logging.setLevel(HttpLoggingInterceptor.Level.BODY);
    return new OkHttpClient.Builder()
        .addInterceptor(logging)
        .build();
  }

}
