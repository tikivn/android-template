package vn.tiki.sample.daggers

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_dagger_demo.btDecrease
import kotlinx.android.synthetic.main.activity_dagger_demo.btIncrease
import kotlinx.android.synthetic.main.activity_dagger_demo.tvValue
import vn.tiki.daggers.Daggers
import vn.tiki.sample.R
import vn.tiki.sample.base.BaseMvpActivity
import javax.inject.Inject

class DaggerDemoActivity : BaseMvpActivity<DaggerDemoView, DaggerDemoPresenter>(), DaggerDemoView {

  @Inject lateinit var presenter: DaggerDemoPresenter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_dagger_demo)

    Daggers.inject(this)

    btIncrease.setOnClickListener { presenter.onIncreaseClick() }
    btDecrease.setOnClickListener { presenter.onDecreaseClick() }

    connect(presenter, this)
  }

  override fun showValue(value: String) {
    tvValue.text = value
  }
}
