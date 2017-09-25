package vn.tiki.sample.util;

import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class ListsTest {

  private List<Integer> list;

  @Before
  public void setUp() throws Exception {
    list = Arrays.asList(1, 2, 3);
  }

  @Test
  public void testMerged() throws Exception {
    final List<Integer> mergedList = Lists.merged(list, Arrays.asList(4, 5, 6));
    assertThat(mergedList).isEqualTo(Arrays.asList(1, 2, 3, 4, 5, 6));
  }

  @Test
  public void testAppended() throws Exception {
    final List<Integer> appendedList = Lists.appended(list, 4);
    assertThat(appendedList).isEqualTo(Arrays.asList(1, 2, 3, 4));
  }
}