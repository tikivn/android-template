package vn.tiki.sample.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity

inline fun <reified T : Activity> Context.intentOf(): Intent {
  return Intent(this, T::class.java)
}

inline fun <reified T : Activity> Context.start() {
  startActivity(intentOf<T>())
}

inline fun <reified T : Activity> Context.start(func: Intent.() -> Unit) {
  val intent = intentOf<T>().apply(func)
  startActivity(intent)
}

fun FragmentActivity.setFragment(@IdRes id: Int, fragment: Fragment) {
  supportFragmentManager.beginTransaction()
      .replace(id, fragment)
      .commit()
}