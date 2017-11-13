package vn.tiki.daggers;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

import org.junit.*;
import vn.tiki.daggers.di.Activity;
import vn.tiki.daggers.di.ActivityModule;
import vn.tiki.daggers.di.App;
import vn.tiki.daggers.di.AppComponent;
import vn.tiki.daggers.di.AppModule;
import vn.tiki.daggers.di.DaggerAppComponent;

public class DaggersIntegrationTest {

  private AppComponent appComponent;

  @Before
  public void setUp() throws Exception {
    appComponent = DaggerAppComponent.builder()
        .appModule(new AppModule())
        .build();
    Daggers.openAppScope(appComponent);

  }

  @After
  public void tearDown() throws Exception {
    Daggers.clear();
  }

  @Test
  public void testSingletonInActivityScope() throws Exception {
    Daggers.openActivityScope(new ActivityModule());
    final Activity activity1 = new Activity();
    final Activity activity2 = new Activity();

    Daggers.inject(activity1);
    Daggers.inject(activity2);

    assertTrue(activity1.activityResource == activity2.activityResource);

    Daggers.openActivityScope(new ActivityModule());
    final Activity activity3 = new Activity();
    final Activity activity4 = new Activity();

    assertTrue(activity3.activityResource == activity4.activityResource);
    assertFalse(activity1.activityResource == activity3.activityResource);
  }

  @Test
  public void testSingletonInAppScope() throws Exception {
    Daggers.openActivityScope(new ActivityModule());
    final App app = new App();
    final Activity activity = new Activity();
    final Activity activity1 = new Activity();

    Daggers.injectAppDependencies(app);
    Daggers.inject(activity);
    Daggers.inject(activity1);

    assertTrue(app.appResource == activity.appResource);
    assertTrue(app.appResource == activity1.appResource);
  }
}
