package vn.tiki.daggers;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;
import static org.mockito.Mockito.*;

import org.junit.*;

public class DaggersTest {

  class ActivityModule {

  }

  class Target {

  }

  private class SubTarget extends Target {

  }

  interface ActivityComponent {

    void inject(Target target);
  }

  interface AppComponent {

    ActivityComponent plus(ActivityModule module);

    void inject(Target target);
  }

  private ActivityComponent mockedActivityComponent;

  private AppComponent mockedAppComponent;

  @Before
  public void setUp() throws Exception {
    mockedAppComponent = mock(AppComponent.class);
    mockedActivityComponent = mock(ActivityComponent.class);

    when(mockedAppComponent.plus(any(ActivityModule.class))).thenReturn(mockedActivityComponent);

    Daggers.openAppScope(mockedAppComponent);
    Daggers.openActivityScope(new ActivityModule());
  }

  @After
  public void tearDown() throws Exception {
    Daggers.clear();
  }

  @Test
  public void testInject() throws Exception {
    final Target target = new Target();
    Daggers.inject(target);

    verify(mockedActivityComponent).inject(target);
  }

  @Test
  public void testInjectAppDependencies() throws Exception {
    final Target target = new Target();
    Daggers.injectAppDependencies(target);

    verify(mockedAppComponent).inject(target);
  }

  @Test
  public void testInjectSubClass() throws Exception {
    final SubTarget subTarget = new SubTarget();
    Daggers.injectAppDependencies(subTarget);
    verify(mockedAppComponent).inject(subTarget);

    Daggers.inject(subTarget);
    verify(mockedActivityComponent).inject(subTarget);
  }

  @Test
  public void testOpenAppScope() throws Exception {
    assertEquals(mockedAppComponent, Daggers.appComponent);
  }

  @Test
  public void testOpenCloseActivityScope() throws Exception {
    assertEquals(mockedActivityComponent, Daggers.getTopScope());

    Daggers.closeActivityScope();

    try {
      Daggers.getTopScope();
      fail();
    } catch (IllegalStateException e) {
    }
  }
}