package vn.tiki.sample.util

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nytimes.android.external.fs3.FileSystemPersisterFactory
import com.nytimes.android.external.fs3.filesystem.FileSystemFactory
import com.nytimes.android.external.store3.base.Parser
import com.nytimes.android.external.store3.base.Persister
import com.nytimes.android.external.store3.base.impl.MemoryPolicy
import com.nytimes.android.external.store3.middleware.GsonParserFactory
import okio.BufferedSource
import vn.tiki.sample.entity.ListData
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

fun <T> listDataParser(gson: Gson, tClass: Class<T>): Parser<BufferedSource, ListData<T>> {
  val type = TypeToken.getParameterized(ListData::class.java, tClass).type
  return GsonParserFactory.createSourceParser(gson, type)
}

fun memoryPolicy(size: Int, minutes: Int): MemoryPolicy {
  return MemoryPolicy.builder()
      .setMemorySize(size.toLong())
      .setExpireAfterWrite(minutes.toLong())
      .setExpireAfterTimeUnit(TimeUnit.MINUTES)
      .build()
}

@Throws(IOException::class)
fun <T> persister(root: File): Persister<BufferedSource, T> {
  return FileSystemPersisterFactory.create(
      FileSystemFactory.create(root)
  ) { key -> "" + key }
}