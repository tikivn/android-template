package vn.tiki.sample.mvp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * Created by Giang Nguyen on 9/4/17.
 */

public class MvpFragment<V extends Mvp.View, P extends Mvp.Presenter<V>> extends Fragment {

  @Nullable MvpBinding<V, P> mvpBinding;

  /**
   * Connect Presenter to View then Presenter will attach/detach view and destroy base on lifecycle.
   * NOTE: this must be called before {@link Fragment#onViewCreated(View, Bundle)} method.
   * @param presenter the Presenter
   * @param view the View
   */
  public void connect(P presenter, V view) {
    mvpBinding = new MvpBinding<>(presenter, view);
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    if (mvpBinding != null) {
      mvpBinding.bind();
    }
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    if (mvpBinding != null) {
      mvpBinding.unbind();
    }
  }

  @Override public void onDestroy() {
    super.onDestroy();
    if (mvpBinding != null) {
      mvpBinding.destroy();
    }
  }
}
