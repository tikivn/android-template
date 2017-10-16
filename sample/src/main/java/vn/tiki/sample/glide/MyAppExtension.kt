package vn.tiki.sample.glide

import com.bumptech.glide.annotation.GlideExtension
import com.bumptech.glide.annotation.GlideOption
import com.bumptech.glide.request.RequestOptions
import vn.tiki.sample.R

@GlideExtension
object MyAppExtension {

  @GlideOption
  @JvmStatic
  fun miniThumb(options: RequestOptions) {
    options
        .fitCenter()
        .placeholder(R.drawable.ic_placeholder)
  }
}// utility class