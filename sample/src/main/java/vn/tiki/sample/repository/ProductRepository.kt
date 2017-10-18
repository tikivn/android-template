package vn.tiki.sample.repository

import com.google.gson.Gson
import com.nytimes.android.external.store3.base.impl.Store
import com.nytimes.android.external.store3.base.impl.StoreBuilder
import io.reactivex.Single
import okio.BufferedSource
import vn.tiki.sample.api.ApiService
import vn.tiki.sample.entity.ListData
import vn.tiki.sample.entity.Product
import vn.tiki.sample.util.listDataParser
import vn.tiki.sample.util.memoryPolicy
import vn.tiki.sample.util.persister
import java.io.File
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepository @Inject
internal constructor(cacheDir: File, api: ApiService, gson: Gson) {

  private val store: Store<ListData<Product>, Int>

  init {
    val builder = StoreBuilder.parsedWithKey<Int, BufferedSource, ListData<Product>>()
        .fetcher { page ->
          api.getProducts(page, 10)
              .map { it.source() }
        }
        .parser(listDataParser(gson, Product::class.java))
        .memoryPolicy(memoryPolicy(10, 10))

    try {
      builder.persister(persister(File(cacheDir, "product_listing")))
    } catch (e: IOException) {
      e.printStackTrace()
    }

    store = builder.open()
  }

  fun getProducts(page: Int, forceApi: Boolean): Single<ListData<Product>> {
    return if (forceApi) {
      store.clear()
      store.fetch(page)
    } else {
      store.get(page)
    }
  }

}

