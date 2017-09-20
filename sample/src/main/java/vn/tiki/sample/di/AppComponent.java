package vn.tiki.sample.di;

import dagger.Component;
import javax.inject.Singleton;
import vn.tiki.sample.extra.ExtraInjectionModule;
import vn.tiki.sample.login.LoginModule;
import vn.tiki.sample.productlist.ProductListing;

/**
 * Created by Giang Nguyen on 8/25/17.
 */
@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {

  LoginModule.Component plus(LoginModule.Module __);

  ExtraInjectionModule.Component plus(ExtraInjectionModule.Module __);

  ProductListing.Component plus(ProductListing.Module __);
}
