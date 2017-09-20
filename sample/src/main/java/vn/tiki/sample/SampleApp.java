package vn.tiki.sample;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import io.fabric.sdk.android.Fabric;
import timber.log.Timber;
import vn.tiki.daggers.ActivityInjector;
import vn.tiki.daggers.AppInjector;
import vn.tiki.daggers.Daggers;
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

    configureDagger();
    configureFabric();
    configureTimber();
  }

  private void configureTimber() {
    if (BuildConfig.DEBUG) {
      Timber.plant(new Timber.DebugTree());
    } else {
      Timber.plant(new Timber.Tree() {
        @Override protected void log(int priority, String tag, String message, Throwable t) {
          if (priority == Log.VERBOSE || priority == Log.DEBUG) {
            return;
          }

          Crashlytics.log(priority, tag, message);

          if (t != null) {
            Crashlytics.logException(t);
          }
        }
      });
    }
  }

  protected void configureDagger() {
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

  private void configureFabric() {
    final Crashlytics crashlyticsKit = new Crashlytics.Builder()
        .core(new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build())
        .build();
    Fabric.with(this, crashlyticsKit);
  }

  @Override public Object appComponent() {
    return appComponent;
  }
}
