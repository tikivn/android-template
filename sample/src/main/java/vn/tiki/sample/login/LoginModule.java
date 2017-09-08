package vn.tiki.sample.login;

import dagger.Module;
import dagger.Provides;
import vn.tiki.sample.di.ActivityScope;
import vn.tiki.sample.model.UserModel;

/**
 * Created by Giang Nguyen on 8/25/17.
 */
@Module
public class LoginModule {

  @ActivityScope
  @Provides LoginPresenter provideLoginPresenter(UserModel userModel) {
    return new LoginPresenter(userModel);
  }
}
