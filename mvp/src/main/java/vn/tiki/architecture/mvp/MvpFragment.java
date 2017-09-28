package vn.tiki.architecture.mvp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * Created by Giang Nguyen on 9/4/17.
 */

public class MvpFragment<V extends Mvp.View, P extends Mvp.Presenter<V>> extends Fragment {

  @Nullable MvpBinding<V, P> mvpBinding;
  private P presenter;

  /**
   * Connect Presenter to View then Presenter will attach/detach view and destroy base on lifecycle.
   * NOTE: this must be called before {@link Fragment#onViewCreated(View, Bundle)} method.
   *
   * @param presenter the Presenter
   * @param view the View
   */
  public void connect(P presenter, V view) {
    this.presenter = presenter;
    mvpBinding = new MvpBinding<>(presenter, view);
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (presenter instanceof ActivityResultDelegate) {
      ((ActivityResultDelegate) presenter).onActivityResult(ActivityResult.create(
          requestCode,
          resultCode,
          data));
    }
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
      mvpBinding.destroy(getActivity());
    }
  }
}
