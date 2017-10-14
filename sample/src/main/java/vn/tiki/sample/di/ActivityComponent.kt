package vn.tiki.sample.di

import dagger.Subcomponent
import vn.tiki.sample.cart.CartMenuItemView
import vn.tiki.sample.login.LoginActivity
import vn.tiki.sample.productdetail.ProductDetailActivity
import vn.tiki.sample.productlist.ProductListingActivity

@ActivityScope
@Subcomponent(modules = arrayOf(ActivityModule::class))
interface ActivityComponent {

  fun inject(ignored: ProductListingActivity)

  fun inject(ignored: LoginActivity)

  fun inject(ignored: ProductDetailActivity)

  fun inject(ignored: CartMenuItemView)
}
