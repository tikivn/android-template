package vn.tiki.architecture.mvp;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
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

  private static Activity findActivity(Context context) {
    if (context instanceof Activity) {
      return ((Activity) context);
    }

    if (context instanceof ContextWrapper) {
      return findActivity(((ContextWrapper) context).getBaseContext());
    } else {
      throw new IllegalArgumentException("context or baseContext must be instance of "
          + Activity.class.getName());
    }
  }

  void unbind() {
    presenter.detach();
  }

  void destroy(Activity activity) {
    if (activity.isFinishing()) {
      presenter.destroy();
    }
  }

  @Override public void onViewAttachedToWindow(View view) {
    bind();
  }

  void bind() {
    presenter.attach(view);
  }

  @Override public void onViewDetachedFromWindow(View view) {
    unbind();
    destroy(findActivity(view.getContext()));
  }
}
