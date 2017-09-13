package vn.tiki.intents.internal;

import android.os.Bundle;
import android.support.annotation.Nullable;

public final class Bundles {
  private Bundles() {
    //no instance
  }

  public static boolean has(Bundle bundle, String key) {
    return bundle.containsKey(key);
  }

  @Nullable @SuppressWarnings("unchecked") public static <T> T get(Bundle bundle, String key) {
    return (T) bundle.get(key);
  }
}
