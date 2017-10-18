package vn.tiki.sample.extra

import android.os.Bundle
import butterknife.ButterKnife
import kotlinx.android.synthetic.main.activity_extra_injection.tvAge
import kotlinx.android.synthetic.main.activity_extra_injection.tvName
import vn.tiki.sample.Extras
import vn.tiki.sample.R
import vn.tiki.sample.base.BaseActivity
import vn.tiki.sample.util.extra
import vn.tiki.sample.util.extraOrElse
import vn.tiki.sample.util.setFragment

/**
 * Created by Giang Nguyen on 9/3/17.
 */
class ExtraInjectionActivity : BaseActivity() {

  private val name: String by extra(Extras.NAME)
  private val age: Int by extraOrElse(Extras.AGE, 0)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContentView(R.layout.activity_extra_injection)
    ButterKnife.bind(this)

    tvName.text = name
    tvAge.text = age.toString()

    if (savedInstanceState == null) {
      setFragment(
          R.id.fragmentContainer,
          ExtraInjectionFragment().apply {
            arguments = Bundle().apply {
              putInt(Extras.HEIGHT, 170)
              putInt(Extras.WEIGHT, 60)
            }
          })
    }
  }
}
