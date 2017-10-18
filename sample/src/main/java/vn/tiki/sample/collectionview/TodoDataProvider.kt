package vn.tiki.sample.collectionview

import io.reactivex.Single
import vn.tiki.collectionview.DataProvider
import vn.tiki.collectionview.ListData
import java.util.ArrayList

class TodoDataProvider : DataProvider<String> {

  override fun fetch(page: Int): Single<out ListData<String>> {
    return Single.fromCallable { generateItems(page) }
  }

  override fun fetchNewest(): Single<out ListData<String>> {
    return Single.fromCallable { generateItems(1) }
  }

  @Throws(Exception::class)
  private fun generateItems(page: Int): ListData<String> {
    Thread.sleep(1000)
    if (System.currentTimeMillis() % 2 == 0L) {
      throw Exception("Error")
    }
    val startIndex = (page - 1) * PER_PAGE
    val result = (0 until PER_PAGE)
        .map { it + startIndex }
        .mapTo(ArrayList(PER_PAGE)) { "Item " + it }
    return vn.tiki.sample.entity.ListData(result, page, LAST_PAGE, LAST_PAGE * PER_PAGE)
  }

  companion object {
    private val PER_PAGE = 5
    private val LAST_PAGE = 10
  }
}
