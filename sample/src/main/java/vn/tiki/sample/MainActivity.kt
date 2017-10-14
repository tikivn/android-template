package vn.tiki.sample

import android.os.Bundle
import android.view.Menu
import butterknife.ButterKnife
import kotlinx.android.synthetic.main.activity_main.btOpenCollectionView
import kotlinx.android.synthetic.main.activity_main.btOpenExtraInjection
import kotlinx.android.synthetic.main.activity_main.btOpenLogin
import kotlinx.android.synthetic.main.activity_main.btOpenProductListing
import vn.tiki.sample.base.BaseActivity
import vn.tiki.sample.collectionview.CollectionViewActivity
import vn.tiki.sample.extra.ExtraInjectionActivity
import vn.tiki.sample.login.LoginActivity
import vn.tiki.sample.productlist.ProductListingActivity
import vn.tiki.sample.util.onClick
import vn.tiki.sample.util.start

class MainActivity : BaseActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    ButterKnife.bind(this)

    onClick(btOpenExtraInjection) {
      start<ExtraInjectionActivity> {
        putExtra(Extras.NAME, "Giang")
        putExtra(Extras.AGE, 29)
      }
    }
    onClick(btOpenCollectionView) { start<CollectionViewActivity>() }
    onClick(btOpenLogin) { start<LoginActivity>() }
    onClick(btOpenProductListing) { start<ProductListingActivity>() }
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menuInflater.inflate(R.menu.menu_cart, menu)
    return true
  }
}
