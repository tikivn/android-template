package vn.tiki.sample.mvp;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Giang Nguyen on 9/4/17.
 */

public abstract class MvpActivity<V extends Mvp.View, P extends Mvp.Presenter<V>> extends
    AppCompatActivity {

  @Nullable MvpBinding<V, P> mvpBinding;

  /**
   * Connect Presenter to View then Presenter will attach/detach view and destroy base on lifecycle.
   * NOTE: this must be called before {@link Activity#onStart()} method.
   *
   * @param presenter the Presenter
   * @param view the View
   */
  public void connect(P presenter, V view) {
    mvpBinding = new MvpBinding<>(presenter, view);
  }

  @Override protected void onStart() {
    super.onStart();
    if (mvpBinding != null) {
      mvpBinding.bind();
    }
  }

  @Override protected void onStop() {
    super.onStop();
    if (mvpBinding != null) {
      mvpBinding.unbind();
    }
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    if (mvpBinding != null) {
      mvpBinding.destroy();
    }
  }
}
