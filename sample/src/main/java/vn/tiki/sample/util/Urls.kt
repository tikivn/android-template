package vn.tiki.sample.util

import vn.tiki.sample.BuildConfig

class Urls private constructor() {


  init {
    throw InstantiationError()
  }

  companion object {
    @JvmStatic
    fun resolveImageUrl(imageUrl: String): String {
      return BuildConfig.BASE_URL + imageUrl
    }
  }

}
