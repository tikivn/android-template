package vn.tiki.architecture.mvp;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public final class ActivityResult {

  private final int requestCode;
  private final int resultCode;
  @Nullable private final Intent intent;

  private ActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
    this.requestCode = requestCode;
    this.resultCode = resultCode;
    this.intent = intent;
  }

  public static @NonNull ActivityResult create(
      final int requestCode,
      final int resultCode,
      final @Nullable Intent intent) {
    return new ActivityResult(requestCode, resultCode, intent);
  }

  public int requestCode() {
    return requestCode;
  }

  public int resultCode() {
    return resultCode;
  }

  @Nullable public Intent intent() {
    return intent;
  }

  public boolean isCanceled() {
    return resultCode == Activity.RESULT_CANCELED;
  }

  public boolean isOk() {
    return resultCode == Activity.RESULT_OK;
  }

  public boolean isRequestCode(final int v) {
    return requestCode == v;
  }
}
