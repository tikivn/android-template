package vn.tiki.sample.util;

import android.text.Spanned;
import android.widget.TextView;
import java.text.DecimalFormat;

public final class TextViews {
  private static final DecimalFormat formatter = new DecimalFormat("#,###,###,###");

  private TextViews() {
    throw new InstantiationError();
  }

  public static void setPrice(TextView textView, float price) {
    final CharSequence text = formatPrice(price);
    textView.setText(text);
  }

  private static CharSequence formatPrice(float price) {
    final String result = formatter.format(price);
    return String.format("$%s", result);
  }

  public static void setHtml(TextView textView, String htmlContent) {
    final Spanned html = Strings.toHtml(htmlContent);
    textView.setText(html);
  }
}
