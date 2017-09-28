package vn.tiki.sample.repository;

import io.reactivex.Observable;
import java.util.concurrent.TimeUnit;

final class CacheSource<K, V> {
  private final ObjectCache<K, V> cache;
  private final long expiry;
  private final TimeUnit expiryTimeUnit;

  CacheSource(ObjectCache<K, V> cache, long expiry, TimeUnit expiryTimeUnit) {
    this.cache = cache;
    this.expiry = expiry;
    this.expiryTimeUnit = expiryTimeUnit;
  }

  public Observable<V> get(K key) {
    final V cached = cache.get(key);
    if (cached == null) {
      return Observable.empty();
    } else {
      return Observable.just(cached);
    }
  }

  void put(K key, V value) {
    cache.put(key, value, expiry, expiryTimeUnit);
  }

  void clear() {
    cache.clear();
  }
}
