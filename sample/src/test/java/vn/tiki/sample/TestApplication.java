package vn.tiki.sample;

import android.annotation.SuppressLint;
import javax.annotation.Nonnull;
import vn.tiki.daggers.Daggers;
import vn.tiki.sample.di.AppComponent;
import vn.tiki.sample.di.AppModule;
import vn.tiki.sample.di.DaggerAppComponent;

@SuppressLint("Registered")
public class TestApplication extends SampleApp {

  private AppComponent mockedAppComponent;

  @Nonnull
  @Override
  public Object appComponent() {
    if (mockedAppComponent == null) {
      throw new NullPointerException("appComponent must not be null");
    }
    return mockedAppComponent;
  }

  public void setAppModule(AppModule appModule) {
    mockedAppComponent = DaggerAppComponent.builder()
        .appModule(appModule)
        .build();

    Daggers.configure(this);
  }
}
