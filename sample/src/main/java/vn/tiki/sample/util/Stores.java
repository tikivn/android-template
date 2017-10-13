package vn.tiki.sample.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
import vn.tiki.sample.entity.ListData;

public final class Stores {

  public static <T> Parser<BufferedSource, ListData<T>> listDataParser(Gson gson, Class<T> tClass) {
    final Type type = TypeToken.getParameterized(ListData.class, tClass).getType();
    return GsonParserFactory.createSourceParser(gson, type);
  }

  public static MemoryPolicy memoryPolicy(int size, int minutes) {
    return MemoryPolicy.builder()
        .setMemorySize(size)
        .setExpireAfterWrite(minutes)
        .setExpireAfterTimeUnit(TimeUnit.MINUTES)
        .build();
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
