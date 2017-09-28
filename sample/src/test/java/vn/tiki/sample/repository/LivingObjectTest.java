package vn.tiki.sample.repository;

import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class LivingObjectTest {

  @Test
  public void testIsAlive() throws Exception {
    assertThat(new ObjectCache.LivingObject<>(
        new Object(),
        System.currentTimeMillis(),
        1000)
        .isAlive()).isTrue();
  }

  @Test
  public void testIsDead() throws Exception {
    assertThat(new ObjectCache.LivingObject<>(
        new Object(),
        System.currentTimeMillis(),
        0)
        .isAlive()).isFalse();
  }
}