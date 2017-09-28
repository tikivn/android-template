package vn.tiki.sample.repository;

import android.support.annotation.Nullable;
import android.support.v4.util.LruCache;
import java.util.List;
import java.util.concurrent.TimeUnit;
import vn.tiki.sample.entity.ListData;

public class ObjectCache<K, V> {
  private final LruCache<K, LivingObject<V>> cache;

  ObjectCache(int count) {
    cache = new LruCache<K, LivingObject<V>>(count) {
      @Override protected int sizeOf(K key, LivingObject<V> value) {
        if (value.getObject() instanceof List) {
          return ((List) value.getObject()).size();
        } else if (value.getObject() instanceof ListData) {
          return ((ListData) value.getObject()).items().size();
        } else {
          return 1;
        }
      }
    };
  }

  @Nullable public V get(K key) {
    final LivingObject<V> livingObject = cache.get(key);
    if (livingObject == null) {
      return null;
    } else if (livingObject.isAlive()) {
      return livingObject.getObject();
    } else {
      cache.remove(key); // remove dead object
      return null;
    }
  }

  void put(K key, V value, long expiry, TimeUnit timeUnit) {
    cache.put(
        key,
        new LivingObject<>(value, System.currentTimeMillis(), timeUnit.toMillis(expiry)));
  }

  void clear() {
    cache.evictAll();
  }

  static class LivingObject<T> {
    private final T object;
    private final long createdAt;
    private final long lifetime;

    LivingObject(T object, long createdAt, long lifetime) {
      this.object = object;
      this.createdAt = createdAt;
      this.lifetime = lifetime;
    }

    public T getObject() {
      return object;
    }

    boolean isAlive() {
      return createdAt + lifetime > System.currentTimeMillis();
    }
  }
}
