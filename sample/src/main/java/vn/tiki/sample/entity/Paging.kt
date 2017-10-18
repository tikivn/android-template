package vn.tiki.sample.entity

import paperparcel.PaperParcel
import paperparcel.PaperParcelable
import vn.tiki.collectionview.Paging

@PaperParcel
data class Paging(private val currentPage: Int = 1, private val lastPage: Int, private val total: Int = 0) : PaperParcelable, Paging {
  override fun currentPage() = currentPage

  override fun lastPage() = lastPage

  override fun total() = total

  companion object {
    @JvmField
    val CREATOR = PaperParcelPaging.CREATOR
  }
}