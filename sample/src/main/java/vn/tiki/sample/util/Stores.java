package vn.tiki.sample.util;

import com.google.gson.Gson;
import com.nytimes.android.external.fs3.FileSystemPersisterFactory;
import com.nytimes.android.external.fs3.filesystem.FileSystemFactory;
import com.nytimes.android.external.store3.base.Parser;
import com.nytimes.android.external.store3.base.Persister;
import com.nytimes.android.external.store3.base.impl.MemoryPolicy;
import com.nytimes.android.external.store3.middleware.GsonParserFactory;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;
import okio.BufferedSource;

public final class Stores {

  public static MemoryPolicy memoryPolicy(int size, int minutes) {
    return MemoryPolicy.builder()
        .setMemorySize(size)
        .setExpireAfterWrite(minutes)
        .setExpireAfterTimeUnit(TimeUnit.MINUTES)
        .build();
  }

  public static <T> Parser<BufferedSource, T> parser(Gson gson, Type type) {
    return GsonParserFactory.createSourceParser(gson, type);
  }

  public static <T> Persister<BufferedSource, T> persister(File root) throws IOException {
    return FileSystemPersisterFactory.create(
        FileSystemFactory.<T>create(root),
        key -> "" + key);
  }

  private Stores() {
    throw new InstantiationError();
  }
}
