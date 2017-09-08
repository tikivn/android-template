package vn.tiki.sample.login;

import android.support.annotation.NonNull;

/**
 * Created by Giang Nguyen on 9/6/17.
 */

public final class LoginResult {
  private final boolean inFlight;
  private final Throwable error;
  private final Boolean success;

  private LoginResult(boolean inFlight, Throwable error, Boolean success) {
    this.inFlight = inFlight;
    this.error = error;
    this.success = success;
  }

  public static LoginResult inFlight() {
    return new LoginResult(true, null, false);
  }

  public static LoginResult error(@NonNull Throwable error) {
    return new LoginResult(false, error, false);
  }

  public static LoginResult success() {
    return new LoginResult(false, null, true);
  }

  public boolean isInFlight() {
    return inFlight;
  }

  public Boolean isSuccess() {
    return success;
  }

  public boolean isError() {
    return error != null;
  }

  @NonNull public Throwable getError() {
    if (error == null) {
      throw new IllegalStateException("no error");
    }
    return error;
  }

  @Override public String toString() {
    return "LoginResult{" +
        "inFlight=" + inFlight +
        ", error=" + error +
        ", success=" + success +
        '}';
  }
}
