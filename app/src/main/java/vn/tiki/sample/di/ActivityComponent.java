package vn.tiki.sample.di;

import dagger.Subcomponent;
import vn.tiki.sample.cart.CartMenuItemView;
import vn.tiki.sample.daggers.DaggerDemoActivity;
import vn.tiki.sample.login.LoginActivity;
import vn.tiki.sample.productdetail.ProductDetailActivity;
import vn.tiki.sample.productlist.ProductListingActivity;

@ActivityScope
@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {

  void inject(ProductListingActivity __);

  void inject(LoginActivity __);

  void inject(ProductDetailActivity __);

  void inject(CartMenuItemView __);

  void inject(DaggerDemoActivity __);
}
