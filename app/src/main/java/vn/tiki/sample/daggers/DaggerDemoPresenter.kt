package vn.tiki.sample.daggers

import vn.tiki.architecture.mvp.BasePresenter
import vn.tiki.sample.di.ActivityScope
import javax.inject.Inject

@ActivityScope
class DaggerDemoPresenter @Inject constructor() : BasePresenter<DaggerDemoView>() {
  private var value: Int = 0

  override fun attach(view: DaggerDemoView) {
    super.attach(view)
    updateView()
  }

  private fun updateView() {
    viewOrThrow.showValue("$value")
  }

  fun onIncreaseClick() {
    value++
    updateView()
  }

  fun onDecreaseClick() {
    value--
    updateView()
  }
}