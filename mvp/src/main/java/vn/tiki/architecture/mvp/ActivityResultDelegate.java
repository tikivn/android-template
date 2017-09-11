package vn.tiki.architecture.mvp;

import android.support.annotation.NonNull;

public interface ActivityResultDelegate {
  void onActivityResult(@NonNull ActivityResult activityResult);
}
