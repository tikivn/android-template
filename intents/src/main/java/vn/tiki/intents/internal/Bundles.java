package vn.tiki.intents.internal;

import android.os.Bundle;

public final class Bundles {
  private Bundles() {
    //no instance
  }

  @SuppressWarnings("unchecked") public static <T> T get(Bundle bundle, String key) {
    return (T) bundle.get(key);
  }
}
