package vn.tiki.sample.util

import android.os.Build
import android.text.Html
import android.text.Spanned
import java.io.UnsupportedEncodingException
import java.net.URLDecoder

//fun String.decode(encoded: String): String {
//  return try {
//    URLDecoder.decode(encoded, "utf-8")
//  } catch (e: UnsupportedEncodingException) {
//    e.printStackTrace()
//    encoded
//  }
//}

class Strings private constructor() {
  init {
    throw InstantiationError()
  }

  companion object {

    fun decode(encoded: String): String {
      return try {
        URLDecoder.decode(encoded, "utf-8")
      } catch (e: UnsupportedEncodingException) {
        e.printStackTrace()
        encoded
      }

    }

    fun toHtml(htmlString: String): Spanned {
      return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(htmlString, Html.FROM_HTML_MODE_COMPACT)
      } else {
        Html.fromHtml(htmlString)
      }
    }
  }
}
