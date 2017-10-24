package vn.tiki.collectionview;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import io.reactivex.Single;
import java.util.Arrays;
import java.util.List;
import org.junit.*;
import org.mockito.*;

public class CollectionViewPresenterTest {

  private static class PagingImpl implements Paging {

    final int currentPage;
    final int lastPage;
    final int total;

    public PagingImpl(final int currentPage, final int lastPage, final int total) {
      this.currentPage = currentPage;
      this.lastPage = lastPage;
      this.total = total;
    }

    @Override
    public int currentPage() {
      return currentPage;
    }

    @Override
    public int lastPage() {
      return lastPage;
    }

    @Override
    public int total() {
      return total;
    }
  }

  private static class ListDataTest implements ListData<Integer> {

    private final List<Integer> items;
    private final Paging paging;

    ListDataTest(final List<Integer> items, final Paging paging) {
      this.items = items;
      this.paging = paging;
    }

    @Override
    public List<Integer> items() {
      return items;
    }

    @Override
    public Paging paging() {
      return paging;
    }
  }

  @Rule public final RxSchedulerTestRule rxSchedulerTestRule = new RxSchedulerTestRule();
  private ListData<Integer> loadData = new ListDataTest(
      Arrays.asList(1, 2, 3),
      new PagingImpl(1, 2, 6));
  private ListData<Integer> loadMoreData = new ListDataTest(
      Arrays.asList(4, 5, 6),
      new PagingImpl(2, 2, 6));
  @Mock private DataProvider mockedDataProvider;
  @Mock private CollectionView mockedView;
  private ListData<Integer> refreshData = new ListDataTest(
      Arrays.asList(1, 2, 3, 4),
      new PagingImpl(1, 2, 6));


  private CollectionViewPresenter tested;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    when(mockedDataProvider.fetch(eq(1))).thenReturn(Single.just(loadData));
    when(mockedDataProvider.fetch(eq(2))).thenReturn(Single.just(loadMoreData));
    when(mockedDataProvider.fetchNewest()).thenReturn(Single.just(refreshData));
    tested = new CollectionViewPresenter(mockedDataProvider);
    tested.attach(mockedView);
  }

  @Test
  public void testAttach() throws Exception {
    assertThat(tested.collectionView).isNotNull();
    verify(mockedDataProvider).fetch(eq(1));
    verify(mockedView).showLoading();
  }

  @Test
  public void testDetach() throws Exception {
    tested.detach();
    assertThat(tested.collectionView).isNull();
  }

  @Test
  public void testLoadError() throws Exception {
    tested.detach();
    final Throwable exception = new Throwable();
    when(mockedDataProvider.fetch(eq(1))).thenReturn(Single.error(exception));

    tested.attach(mockedView);
    verify(mockedView).showError(eq(exception));
  }

  @Test
  public void testLoadMoreError() throws Exception {
    final Throwable exception = new Throwable();
    when(mockedDataProvider.fetch(eq(2))).thenReturn(Single.error(exception));

    tested.onLoadMore();
    verify(mockedView).hideLoadMore();
  }

  @Test
  public void testLoadMoreSuccess() throws Exception {
    tested.onLoadMore();
    verify(mockedView).setItems(Arrays.asList(1, 2, 3, 4, 5, 6));
  }

  @Test
  public void testLoadSuccess() throws Exception {
    verify(mockedView).showContent();
    verify(mockedView).setItems(eq(Arrays.asList(1, 2, 3, CollectionViewPresenter.LOADING_ITEM)));
  }

  @Test
  public void testRefreshError() throws Exception {
    final Throwable exception = new Throwable();
    when(mockedDataProvider.fetchNewest()).thenReturn(Single.error(exception));

    tested.onRefresh();
    verify(mockedView).hideRefreshing();
  }

  @Test
  public void testRefreshSuccess() throws Exception {
    tested.onRefresh();
    verify(mockedView).hideRefreshing();
    verify(mockedView).setItems(eq(Arrays.asList(
        1,
        2,
        3,
        4,
        CollectionViewPresenter.LOADING_ITEM)));
  }
}