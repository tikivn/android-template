package vn.tiki.collectionview;

import static com.google.common.truth.Truth.assertThat;
import static junit.framework.Assert.assertTrue;
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
  private CollectionView tested;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    application = RuntimeEnvironment.application;
    when(mockedDataProvider.fetch(anyInt())).thenReturn(Single.error(new Throwable()));
    when(mockedAdapter.onCreateDataProvider()).thenReturn(mockedDataProvider);
    when(mockedAdapter.onCreateErrorView(any(ViewGroup.class), any(Throwable.class)))
        .thenReturn(new View(application));
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
  }

  @Test
  public void testShowError() throws Exception {
    tested.showLoading();
    tested.showError(new Throwable());
    assertThat(tested.refreshLayout.isRefreshing()).isFalse();
    assertThat(tested.errorView.getVisibility()).isEqualTo(View.VISIBLE);
  }

  @Test
  public void testShowLoading() throws Exception {
    tested.showLoading();
    assertThat(tested.refreshLayout.isRefreshing()).isTrue();
    assertTrue(tested.errorView == null || tested.errorView.getVisibility() == View.GONE);
  }
}