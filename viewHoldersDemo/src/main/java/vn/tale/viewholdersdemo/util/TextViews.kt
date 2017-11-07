package vn.tale.viewholdersdemo.util

import android.widget.TextView
import java.text.DecimalFormat

private val decimalFormat = DecimalFormat("#,###,###,###")

fun Float.toDecimal(): String {
  val result = decimalFormat.format(toDouble())
  return String.format("$%s", result)
}

fun TextView.setPrice(price: Float) {
  text = price.toDecimal()
}
