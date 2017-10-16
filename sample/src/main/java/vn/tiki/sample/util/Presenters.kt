package vn.tiki.sample.util

import io.reactivex.Observable
import vn.tiki.sample.mvp.rx.RxBasePresenter

fun <T> Observable<T>.subscribe(presenter: RxBasePresenter<*>, consumer: (T) -> Unit) {
  presenter.disposeOnDestroy(subscribe(consumer))
}