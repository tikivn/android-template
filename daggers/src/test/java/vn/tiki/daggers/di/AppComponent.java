package vn.tiki.daggers.di;

import dagger.Component;
import javax.inject.Singleton;

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {

  ActivityComponent plus(ActivityModule __);

  void inject(App __);
}
