package vn.tiki.collectionview;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import android.app.Application;
import android.view.View;
import android.view.ViewGroup;
import io.reactivex.Single;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.*;
import org.junit.runner.*;
import org.mockito.*;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

@SuppressWarnings({"ConstantConditions", "unchecked", "WeakerAccess"})
@RunWith(RobolectricTestRunner.class)
public class CollectionViewTest {

  @Rule public final RxSchedulerTestRule rxSchedulerTestRule = new RxSchedulerTestRule();

  @Mock DataProvider mockedDataProvider;
  @Mock Adapter mockedAdapter;
  private Application application;
  private Throwable emptyError = new NoSuchElementException();
  private View emptyView;
  private View errorView;
  private Throwable networkError = new Throwable();
  private CollectionView tested;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    application = RuntimeEnvironment.application;
    emptyView = new View(application);
    errorView = new View(application);
    when(mockedDataProvider.fetch(anyInt())).thenReturn(Single.error(new Throwable()));
    when(mockedAdapter.onCreateDataProvider()).thenReturn(mockedDataProvider);
    when(mockedAdapter.onCreateErrorView(any(ViewGroup.class), eq(networkError)))
        .thenReturn(errorView);
    when(mockedAdapter.onCreateErrorView(any(ViewGroup.class), eq(emptyError)))
        .thenReturn(emptyView);
    tested = new CollectionView(application, null);
    tested.setAdapter(mockedAdapter);
    tested.onAttachedToWindow();
  }

  @Test
  public void testAttachToPresenterOnAttachedToWindow() throws Exception {
    tested.removeAllViews();
    tested = new CollectionView(application, null);
    tested.setAdapter(mockedAdapter);
    tested.onAttachedToWindow();
    assertThat(tested.presenter.collectionView).isEqualTo(tested);
  }

  @Test
  public void testAttachToPresenterWhenSetAdapter() throws Exception {
    tested.removeAllViews();
    tested = new CollectionView(application, null);
    tested.onAttachedToWindow();
    tested.setAdapter(mockedAdapter);
    assertThat(tested.presenter.collectionView).isEqualTo(tested);
  }

  @Test
  public void testHideRefreshing() throws Exception {
    tested.hideRefreshing();
    assertThat(tested.refreshLayout.isRefreshing()).isFalse();
  }

  @Test
  public void testSetItems() throws Exception {
    final List<Integer> items = Arrays.asList(1, 2, 3);
    tested.setItems(items);
    Mockito.verify(mockedAdapter).onBindItems(eq(items));
  }

  @Test
  public void testShowContent() throws Exception {
    tested.showLoading();
    tested.showContent();
    assertThat(tested.refreshLayout.isRefreshing()).isFalse();
    assertThat(tested.getChildCount()).isEqualTo(1);
  }

  @Test
  public void testShowError() throws Exception {
    tested.showLoading();
    tested.showError(emptyError);
    assertThat(tested.refreshLayout.isRefreshing()).isFalse();
    assertThat(tested.getChildAt(1)).isEqualTo(emptyView);

    tested.showLoading();
    tested.showError(networkError);
    assertThat(tested.refreshLayout.isRefreshing()).isFalse();
    assertThat(tested.getChildAt(1)).isEqualTo(errorView);
  }

  @Test
  public void testShowLoading() throws Exception {
    tested.showLoading();
    assertThat(tested.refreshLayout.isRefreshing()).isTrue();
    assertThat(tested.getChildCount()).isEqualTo(1);
  }
}