package vn.tiki.sample.base

import android.support.v7.app.AppCompatActivity
import vn.tiki.daggers.ActivityInjector

abstract class BaseActivity : AppCompatActivity(), ActivityInjector {

  protected val activityDelegate = ActivityDelegate()

  override fun onPause() {
    super.onPause()
    activityDelegate.onPause(this)
  }

  override fun onResume() {
    super.onResume()
    activityDelegate.onResume(this)
  }

  override fun activityModule(): Any {
    return activityDelegate.makeActivityModule(this)
  }

  protected fun setOnNetworkStatusChanged(onNetworkStatusChanged: (Boolean) -> Unit) {
    activityDelegate.onNetworkStatusChanged = onNetworkStatusChanged
  }
}
