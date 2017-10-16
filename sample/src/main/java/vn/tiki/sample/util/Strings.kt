package vn.tiki.sample.util

import android.os.Build
import android.text.Html
import android.text.Spanned
import vn.tiki.sample.BuildConfig
import java.io.UnsupportedEncodingException
import java.net.URLDecoder

@Suppress("DEPRECATION")
fun String.toHtml(): Spanned = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
  Html.fromHtml(this, Html.FROM_HTML_MODE_COMPACT)
} else {
  Html.fromHtml(this)
}

fun String.decode(): String = try {
  URLDecoder.decode(this, "utf-8")
} catch (e: UnsupportedEncodingException) {
  e.printStackTrace()
  this
}

fun String.toUrl(): String = BuildConfig.BASE_URL + this