package vn.tiki.sample.repository;

import io.reactivex.Observable;

public interface ApiSource<K, V> {
  Observable<V> get(K key);
}
