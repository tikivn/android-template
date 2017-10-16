package vn.tiki.sample.util

import android.app.Activity
import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager

inline fun <reified T : Activity> Context.intentOf(): Intent {
  return Intent(this, T::class.java)
}

inline fun <reified T : Activity> Context.start() {
  start<T> {}
}

inline fun <reified T : Activity> Context.start(func: Intent.() -> Unit) {
  val intent = intentOf<T>().apply(func)
  val clazz = T::class.java
  when {
    Activity::class.java.isAssignableFrom(clazz) -> startActivity(intent)
    Service::class.java.isAssignableFrom(clazz) -> startService(intent)
    else -> throw IllegalArgumentException("only accept Activity or Service")
  }
}

fun Context.isConnected(): Boolean {
  val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

  val activeNetwork = cm.activeNetworkInfo
  return activeNetwork != null && activeNetwork.isConnectedOrConnecting
}