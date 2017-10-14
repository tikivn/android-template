package vn.tiki.sample.util;

import android.text.Spanned;
import android.widget.TextView;
import java.text.DecimalFormat;

public final class TextViews {
  private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#,###,###,###");

  public static void setHtml(TextView textView, String htmlContent) {
    final Spanned html = Strings.Companion.toHtml(htmlContent);
    textView.setText(html);
  }

  public static void setPrice(TextView textView, float price) {
    final CharSequence text = formatPrice(price);
    textView.setText(text);
  }

  private TextViews() {
    throw new InstantiationError();
  }

  private static CharSequence formatPrice(float price) {
    final String result = DECIMAL_FORMAT.format(price);
    return String.format("$%s", result);
  }
}
