package vn.tiki.sample.mvp.rx;

import android.support.annotation.CallSuper;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import vn.tiki.sample.mvp.BasePresenter;
import vn.tiki.sample.mvp.Mvp;

/**
 * Created by Giang Nguyen on 9/5/17.
 */

public class RxBasePresenter<V extends Mvp.View> extends BasePresenter<V> {
  private CompositeDisposable disposeOnDestroyDisposables = new CompositeDisposable();

  protected void disposeOnDestroy(Disposable... disposables) {
    if (disposables.length == 1) {
      disposeOnDestroyDisposables.add(disposables[0]);
    } else if (disposables.length > 1) {
      disposeOnDestroyDisposables.addAll(disposables);
    }
  }

  @CallSuper
  @Override public void destroy() {
    disposeOnDestroyDisposables.clear();
    super.destroy();
  }
}
