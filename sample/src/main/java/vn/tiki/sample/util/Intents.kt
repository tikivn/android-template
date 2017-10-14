package vn.tiki.sample.util

import android.app.Activity
import android.support.v4.app.Fragment

@Suppress("UNCHECKED_CAST")
fun <T> Fragment.extra(key: String): Lazy<T> = lazy { arguments.get(key) as T }

@Suppress("UNCHECKED_CAST")
fun <T> Activity.extra(key: String): Lazy<T> = lazy { intent.extras.get(key) as T }

@Suppress("UNCHECKED_CAST")
fun <T> Activity.extraOrElse(key: String, default: T): Lazy<T> = lazy {
  if (intent.hasExtra(key)) {
    intent.extras.get(key) as T
  } else default
}

@Suppress("UNCHECKED_CAST")
fun <T> Fragment.extraOrElse(key: String, default: T): Lazy<T> = lazy {
  if (arguments.containsKey(key)) {
    arguments.get(key) as T
  } else default
}