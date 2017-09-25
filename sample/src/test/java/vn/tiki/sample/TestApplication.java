package vn.tiki.sample;

import android.annotation.SuppressLint;
import vn.tiki.daggers.Daggers;
import vn.tiki.sample.di.AppComponent;
import vn.tiki.sample.di.AppModule;
import vn.tiki.sample.di.DaggerAppComponent;

@SuppressLint("Registered")
public class TestApplication extends SampleApp {

  private AppComponent mockedAppComponent;

  public void setAppModule(AppModule appModule) {
    mockedAppComponent = DaggerAppComponent.builder()
        .appModule(appModule)
        .build();

    Daggers.configure(this);
  }

  @Override public Object appComponent() {
    return mockedAppComponent;
  }
}
