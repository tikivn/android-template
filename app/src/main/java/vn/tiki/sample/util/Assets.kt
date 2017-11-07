package vn.tiki.sample.util

import android.content.Context
import io.reactivex.Single
import okio.BufferedSource
import okio.Okio

class Assets(private val context: Context) {

  fun read(file: String): Single<BufferedSource> {
    return Single.fromCallable {
      val inputStream = context.assets.open(file)
      Okio.buffer(Okio.source(inputStream))
    }
  }

}
