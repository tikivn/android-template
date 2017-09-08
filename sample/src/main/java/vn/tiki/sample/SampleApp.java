package vn.tiki.sample;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import vn.tiki.daggerhelper.ActivityInjector;
import vn.tiki.daggerhelper.AppInjector;
import vn.tiki.daggerhelper.Daggers;
import vn.tiki.sample.di.AppComponent;
import vn.tiki.sample.di.AppModule;
import vn.tiki.sample.di.DaggerAppComponent;
import vn.tiki.sample.misc.SimpleActivityLifecycleCallbacks;

public class SampleApp extends Application implements AppInjector {
  private AppComponent appComponent;

  @Override public void onCreate() {
    super.onCreate();
    appComponent = DaggerAppComponent.builder()
        .appModule(new AppModule())
        .build();

    setupDagger();
  }

  protected void setupDagger() {
    Daggers.installAppInjector(this);

    registerActivityLifecycleCallbacks(new SimpleActivityLifecycleCallbacks() {
      @Override public void onActivityCreated(Activity activity, Bundle bundle) {
        if (activity instanceof ActivityInjector) {
          Daggers.installActivityInjector((ActivityInjector) activity);
        }
      }

      @Override public void onActivityDestroyed(Activity activity) {
        if (activity instanceof ActivityInjector) {
          Daggers.uninstallActivityInjector((ActivityInjector) activity);
        }
      }
    });
  }

  @Override public Object appComponent() {
    return appComponent;
  }
}
