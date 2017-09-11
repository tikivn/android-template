package vn.tiki.architecture.mvp;

import android.support.annotation.NonNull;

/**
 * Created by Giang Nguyen on 9/4/17.
 */
public interface ViewAction<V> {
  void call(@NonNull V view);
}
