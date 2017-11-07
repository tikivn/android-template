package vn.tiki.sample.util;

import android.os.Build;
import android.text.Html;
import android.text.Spanned;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public final class Strings {
  private Strings() {
    throw new InstantiationError();
  }

  public static String decode(String encoded) {
    try {
      return URLDecoder.decode(encoded, "utf-8");
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
      return encoded;
    }
  }

  @SuppressWarnings("deprecation") public static Spanned toHtml(String htmlString) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      return Html.fromHtml(htmlString, Html.FROM_HTML_MODE_COMPACT);
    } else {
      return Html.fromHtml(htmlString);
    }
  }
}
