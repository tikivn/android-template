package vn.tiki.sample.util;

import android.content.Context;
import io.reactivex.Single;
import java.io.InputStream;
import okio.BufferedSource;
import okio.Okio;

public final class Assets {

  private final Context context;

  public Assets(Context context) {
    this.context = context;
  }

  public Single<BufferedSource> read(String file) {
    return Single.fromCallable(() -> {
      final InputStream inputStream = context.getAssets()
          .open(file);
      return Okio.buffer(Okio.source(inputStream));
    });
  }

}
