package vn.tiki.daggers;

import static com.google.common.truth.Truth.assertThat;
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

  private ActivityComponent mockedActicityComponent;
  private ActivityInjector mockedActivityInjector;
  private AppComponent mockedAppComponent;
  private AppInjector mockedAppInjector;

  @Before
  public void setUp() throws Exception {
    ActivityModule activityModule = new ActivityModule();
    mockedAppInjector = mock(AppInjector.class);
    mockedAppComponent = mock(AppComponent.class);
    mockedActicityComponent = mock(ActivityComponent.class);
    mockedActivityInjector = mock(ActivityInjector.class);

    when(mockedAppInjector.appComponent()).thenReturn(mockedAppComponent);
    when(mockedAppComponent.plus(activityModule)).thenReturn(mockedActicityComponent);
    when(mockedActivityInjector.activityModule()).thenReturn(activityModule);

    Daggers.installAppInjector(mockedAppInjector);
    Daggers.installActivityInjector(mockedActivityInjector);
  }

  @After
  public void tearDown() throws Exception {
    Daggers.uninstallActivityInjector(mockedActivityInjector);
  }

  @Test
  public void testInjectSubType_shouldSupport() throws Exception {
    final SubTarget subTarget = new SubTarget();
    Daggers.inject(subTarget, mockedAppInjector);
    verify(mockedAppComponent).inject(subTarget);

    Daggers.inject(subTarget, mockedActivityInjector);
    verify(mockedActicityComponent).inject(subTarget);
  }

  @Test
  public void testInject_shouldUseRightComponent() throws Exception {
    Target target = new Target();

    Daggers.inject(target, mockedAppInjector);
    verify(mockedAppComponent).inject(target);

    Daggers.inject(target, mockedActivityInjector);
    verify(mockedActicityComponent).inject(target);
  }

  @Test
  public void testInstallActivityInjector_shouldPlusActivityComponent() throws Exception {
    assertThat(Daggers.get(mockedActivityInjector)).isEqualTo(mockedActicityComponent);
  }

  @Test
  public void testUninstallActivityInjector_shouldClearReferenceToActivityInjector() throws Exception {
    Daggers.uninstallActivityInjector(mockedActivityInjector);
    assertThat(Daggers.get(mockedActivityInjector)).isNull();
  }
}