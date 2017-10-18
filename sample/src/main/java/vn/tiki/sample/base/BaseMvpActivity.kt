package vn.tiki.sample.base

import vn.tiki.architecture.mvp.Mvp
import vn.tiki.architecture.mvp.MvpActivity
import vn.tiki.daggers.ActivityInjector

abstract class BaseMvpActivity<V : Mvp.View, P : Mvp.Presenter<V>> : MvpActivity<V, P>(), ActivityInjector {

  private val activityDelegate = ActivityDelegate()

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
}
