package vn.tiki.sample.di

import android.arch.persistence.room.Room
import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import vn.tiki.sample.BuildConfig
import vn.tiki.sample.api.ApiService
import vn.tiki.sample.database.AppDatabase
import vn.tiki.sample.database.CartDao
import vn.tiki.sample.entity.MyAdapterFactory
import vn.tiki.sample.model.UserModel
import java.io.File
import javax.inject.Singleton

@Module
open class AppModule(private val appContext: Context) {

  @Singleton
  @Provides
  open fun provideUserModel(): UserModel {
    return UserModel()
  }

  @Singleton
  @Provides
  internal fun provideApiService(okHttpClient: OkHttpClient): ApiService {
    return Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(ApiService::class.java)
  }

  @Singleton
  @Provides
  internal fun provideAppDatabase(): AppDatabase {
    return Room.databaseBuilder(appContext, AppDatabase::class.java, "shopping").build()
  }

  @Provides
  internal fun provideCacheDir(): File {
    return appContext.cacheDir
  }

  @Provides
  internal fun provideCartDao(appDatabase: AppDatabase): CartDao {
    return appDatabase.cartDao()
  }

  @Provides
  internal fun provideGson(): Gson {
    return GsonBuilder()
        .registerTypeAdapterFactory(MyAdapterFactory.create())
        .create()
  }

  @Singleton
  @Provides
  internal fun provideOkHttpClient(): OkHttpClient {
    val logging = HttpLoggingInterceptor()
    logging.level = HttpLoggingInterceptor.Level.BODY
    return OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()
  }

}
