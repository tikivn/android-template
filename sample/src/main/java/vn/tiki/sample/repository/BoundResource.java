package vn.tiki.sample.repository;

import io.reactivex.Observable;

final class BoundResource<K, V> {
  private final CacheSource<K, V> cacheSource;
  private final ApiSource<K, V> apiSource;

  BoundResource(
      CacheSource<K, V> cacheSource,
      ApiSource<K, V> apiSource) {
    this.cacheSource = cacheSource;
    this.apiSource = apiSource;
  }

  void clearCache() {
    cacheSource.clear();
  }

  public Observable<V> get(K key) {
    final Observable<V> cache = cacheSource.get(key);
    final Observable<V> api = apiSource.get(key)
        .doOnNext(v -> cacheSource.put(key, v));
    return Observable.concat(cache, api)
        .firstOrError()
        .toObservable();
  }
}
