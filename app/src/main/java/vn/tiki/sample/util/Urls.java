package vn.tiki.sample.util;

import android.support.annotation.NonNull;
import vn.tiki.sample.BuildConfig;

public final class Urls {

  @NonNull
  public static String resolveImageUrl(String imageUrl) {
    return BuildConfig.BASE_URL + imageUrl;
  }


  private Urls() {
    throw new InstantiationError();
  }

}
