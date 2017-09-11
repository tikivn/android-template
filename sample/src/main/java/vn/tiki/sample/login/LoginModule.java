package vn.tiki.sample.login;

import dagger.Provides;
import vn.tiki.sample.di.ActivityScope;
import vn.tiki.sample.model.UserModel;

/**
 * Created by Giang Nguyen on 8/25/17.
 */
public interface LoginModule {

  @ActivityScope
  @dagger.Subcomponent(modules = Module.class)
  interface Component {
    void inject(LoginActivity __);
  }

  @dagger.Module
  class Module {
    @ActivityScope
    @Provides LoginPresenter provideLoginPresenter(UserModel userModel) {
      return new LoginPresenter(userModel);
    }
  }
}
