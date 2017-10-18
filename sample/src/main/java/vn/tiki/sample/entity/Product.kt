package vn.tiki.sample.entity

import paperparcel.PaperParcel
import paperparcel.PaperParcelable

@PaperParcel
data class Product(val id: String, val title: String, val price: Float, val image: String, val description: String) : PaperParcelable {
  companion object {
    @JvmField
    val CREATOR = PaperParcelProduct.CREATOR
  }
}