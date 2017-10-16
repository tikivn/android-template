package vn.tiki.sample.util

import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity

fun FragmentActivity.setFragment(@IdRes id: Int, fragment: Fragment) {
  supportFragmentManager.beginTransaction()
      .replace(id, fragment)
      .commit()
}