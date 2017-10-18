package vn.tiki.sample.entity


data class ListData<T>(private val items: List<T>, private val currentPage: Int, private val lastPage: Int, private val total: Int) : vn.tiki.collectionview.ListData<T> {
  private var paging: Paging? = null
  override fun items() = items
  override fun paging(): vn.tiki.collectionview.Paging {
    paging = paging ?: Paging(currentPage, lastPage, total)
    return paging!!
  }
}