package vn.tiki.sample.di;

import dagger.Component;
import javax.inject.Singleton;

/**
 * Created by Giang Nguyen on 8/25/17.
 */
@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {

  ActivityComponent plus(ActivityModule __);
}
