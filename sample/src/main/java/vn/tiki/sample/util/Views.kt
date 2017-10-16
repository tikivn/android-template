package vn.tiki.sample.util

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import vn.tiki.sample.glide.GlideApp
import java.text.DecimalFormat

private val decimalFormat = DecimalFormat("#,###,###,###")

fun onClick(v: View, onClick: (View) -> Unit) {
  v.setOnClickListener(onClick)
}


fun TextView.setHtml(htmlContent: String) {
  this.text = htmlContent.toHtml()
}

fun TextView.setPrice(price: Float) {
  this.text = formatPrice(price)
}

fun ImageView.setImage(image: String) {
  GlideApp
      .with(this)
      .load(image.toUrl())
      .into(this)
}

private fun formatPrice(price: Float): CharSequence {
  val result = decimalFormat.format(price.toDouble())
  return "\$$result"
}