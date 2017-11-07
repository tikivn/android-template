package vn.tiki.sample.di;

import android.content.Context;
import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {

  private final Context context;

  public ActivityModule(Context context) {
    this.context = context;
  }

  @ActivityScope @Provides Context provideContext() {
    return context;
  }
}
