package vn.tiki.sample.extra;

import android.os.Bundle;

public final class ExtraInjection {
  private ExtraInjection() {
    throw new InstantiationError();
  }

  @SuppressWarnings("unchecked") static <T> T get(Bundle extras, String key) {
    return (T) extras.get(key);
  }
}