package vn.tiki.sample.di;

import dagger.Subcomponent;
import vn.tiki.sample.extra.ExtraInjectionActivity;
import vn.tiki.sample.login.LoginActivity;
import vn.tiki.sample.productdetail.ProductDetailActivity;
import vn.tiki.sample.productlist.ProductListingActivity;

@ActivityScope
@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {

  void inject(ProductListingActivity __);

  void inject(ExtraInjectionActivity __);

  void inject(LoginActivity __);

  void inject(ProductDetailActivity __);
}
