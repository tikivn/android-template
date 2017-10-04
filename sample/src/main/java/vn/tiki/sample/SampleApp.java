package vn.tiki.sample;

import android.app.Application;
import android.util.Log;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import io.fabric.sdk.android.Fabric;
import timber.log.Timber;
import vn.tiki.daggers.AppInjector;
import vn.tiki.daggers.Daggers;
import vn.tiki.sample.di.AppComponent;
import vn.tiki.sample.di.AppModule;
import vn.tiki.sample.di.DaggerAppComponent;

public class SampleApp extends Application implements AppInjector {
  private AppComponent appComponent;

  @Override public void onCreate() {
    super.onCreate();

    configureDagger();
    configureFabric();
    configureTimber();
  }

  protected void configureDagger() {
    appComponent = DaggerAppComponent.builder()
        .appModule(new AppModule(this))
        .build();
    Daggers.configure(this);
  }

  private void configureFabric() {
    final Crashlytics crashlyticsKit = new Crashlytics.Builder()
        .core(new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build())
        .build();
    Fabric.with(this, crashlyticsKit);
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

  @Override public Object appComponent() {
    return appComponent;
  }
}
