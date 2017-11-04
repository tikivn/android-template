package vn.tale.viewholdersdemo.glide;

import com.bumptech.glide.annotation.GlideExtension;
import com.bumptech.glide.annotation.GlideOption;
import com.bumptech.glide.request.RequestOptions;
import vn.tale.viewholdersdemo.R;

@GlideExtension
public class MyAppExtension {
  private static final int MINI_THUMB_SIZE = 100;

  private MyAppExtension() {
  } // utility class

  @GlideOption
  public static void miniThumb(RequestOptions options) {
    options
        .fitCenter()
        .placeholder(R.drawable.ic_placeholder);
  }
}