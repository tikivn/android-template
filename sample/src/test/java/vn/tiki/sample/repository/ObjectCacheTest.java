package vn.tiki.sample.repository;

import java.util.concurrent.TimeUnit;
import org.junit.Before;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class ObjectCacheTest {

  private ObjectCache<Integer, String> tested;

  @Before
  public void setUp() throws Exception {
    tested = new ObjectCache<>(100);
    tested.put(1, "first", 100, TimeUnit.MILLISECONDS);
  }

  @Test
  public void testGet() throws Exception {
    assertThat(tested.get(1)).isEqualTo("first");
  }

  @Test
  public void testExpired() throws Exception {
    Thread.sleep(101);
    assertThat(tested.get(1)).isNull();
  }

  @Test
  public void testClear() throws Exception {
    tested.clear();
    assertThat(tested.get(1)).isNull();
  }
}