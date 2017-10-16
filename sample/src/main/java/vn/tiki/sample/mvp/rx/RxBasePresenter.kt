package vn.tiki.sample.mvp.rx

import android.support.annotation.CallSuper
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import vn.tiki.architecture.mvp.BasePresenter
import vn.tiki.architecture.mvp.Mvp

/**
 * Created by Giang Nguyen on 9/5/17.
 */

open class RxBasePresenter<V : Mvp.View> : BasePresenter<V>() {
  private val disposeOnDestroyDisposables = CompositeDisposable()

  fun disposeOnDestroy(vararg disposables: Disposable) {
    if (disposables.size == 1) {
      disposeOnDestroyDisposables.add(disposables[0])
    } else if (disposables.size > 1) {
      disposeOnDestroyDisposables.addAll(*disposables)
    }
  }

  @CallSuper
  override fun destroy() {
    disposeOnDestroyDisposables.clear()
    super.destroy()
  }
}
