package vn.tiki.collectionview;

import io.reactivex.Observable;
import java.util.Arrays;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CollectionViewPresenterTest {
  @Rule public final RxSchedulerTestRule rxSchedulerTestRule = new RxSchedulerTestRule();

  @Mock DataProvider mockedDataProvider;
  @Mock CollectionView mockedView;

  ListData<Integer> loadData = new ListData<>(
      Arrays.asList(1, 2, 3),
      Paging.builder()
          .total(6)
          .currentPage(1)
          .lastPage(2)
          .make());
  ListData<Integer> loadMoreData = new ListData<>(
      Arrays.asList(4, 5, 6),
      Paging.builder()
          .total(6)
          .currentPage(2)
          .lastPage(2)
          .make());

  ListData<Integer> refreshData = new ListData<>(
      Arrays.asList(1, 2, 3, 4),
      Paging.builder()
          .total(6)
          .currentPage(1)
          .lastPage(2)
          .make());

  private CollectionViewPresenter tested;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    when(mockedDataProvider.fetch(eq(1))).thenReturn(Observable.just(loadData));
    when(mockedDataProvider.fetch(eq(2))).thenReturn(Observable.just(loadMoreData));
    when(mockedDataProvider.fetchNewest()).thenReturn(Observable.just(refreshData));
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
  public void testLoadSuccess() throws Exception {
    verify(mockedView).showContent();
    verify(mockedView).setItems(eq(Arrays.asList(1, 2, 3, CollectionViewPresenter.LOADING_ITEM)));
  }

  @Test
  public void testLoadError() throws Exception {
    tested.detach();
    final Throwable exception = new Throwable();
    when(mockedDataProvider.fetch(eq(1))).thenReturn(Observable.error(exception));

    tested.attach(mockedView);
    verify(mockedView).showError(eq(exception));
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

  @Test
  public void testRefreshError() throws Exception {
    final Throwable exception = new Throwable();
    when(mockedDataProvider.fetchNewest()).thenReturn(Observable.error(exception));

    tested.onRefresh();
    verify(mockedView).hideRefreshing();
  }

  @Test
  public void testLoadMoreSuccess() throws Exception {
    tested.onLoadMore();
    verify(mockedView).setItems(Arrays.asList(1, 2, 3, 4, 5, 6));
  }

  @Test
  public void testLoadMoreError() throws Exception {
    final Throwable exception = new Throwable();
    when(mockedDataProvider.fetch(eq(2))).thenReturn(Observable.error(exception));

    tested.onLoadMore();
    verify(mockedView).hideLoadMore();
  }
}