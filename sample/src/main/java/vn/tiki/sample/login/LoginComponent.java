package vn.tiki.sample.login;

import dagger.Subcomponent;
import vn.tiki.sample.di.ActivityScope;

/**
 * Created by Giang Nguyen on 8/25/17.
 */
@ActivityScope
@Subcomponent(modules = LoginModule.class)
public interface LoginComponent {
  void inject(LoginActivity __);
}
