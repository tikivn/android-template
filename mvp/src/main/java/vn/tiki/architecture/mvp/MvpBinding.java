package vn.tiki.architecture.mvp;

import android.support.annotation.NonNull;
import android.view.View;

/**
 * Created by Giang Nguyen on 7/7/17.
 */
public final class MvpBinding<V extends Mvp.View, P extends Mvp.Presenter<V>>
    implements View.OnAttachStateChangeListener {

  @NonNull private final P presenter;
  @NonNull private final V view;

  public MvpBinding(@NonNull P presenter, @NonNull V view) {
    this.presenter = presenter;
    this.view = view;
  }

  void bind() {
    presenter.attach(view);
  }

  void unbind() {
    presenter.detach();
  }

  void destroy() {
    presenter.destroy();
  }

  @Override public void onViewAttachedToWindow(View view) {
    bind();
  }

  @Override public void onViewDetachedFromWindow(View view) {
    unbind();
    destroy();
  }
}
