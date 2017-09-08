package vn.tiki.sample.mvp;

import android.support.annotation.NonNull;

/**
 * Created by Giang Nguyen on 7/7/17.
 */
final class Binder<V extends Mvp.View, P extends Mvp.Presenter<V>> {
  @NonNull private final P presenter;
  @NonNull private final V view;

  public Binder(@NonNull P presenter, @NonNull V view) {
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
}
