package vn.tiki.sample.extra

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import vn.tiki.sample.Extras
import vn.tiki.sample.R
import vn.tiki.sample.util.extraOrElse

class ExtraInjectionFragment : Fragment() {

  @BindView(R.id.tvHeight) internal lateinit var tvHeight: TextView
  @BindView(R.id.tvWeight) internal lateinit var tvWeight: TextView

  internal val height: Int by extraOrElse(Extras.HEIGHT, 0)
  internal val weight: Int by extraOrElse(Extras.WEIGHT, 0)

  override fun onCreateView(
      inflater: LayoutInflater?,
      container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    return inflater?.inflate(R.layout.fragment_extra_injection, container, false)
  }

  override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    ButterKnife.bind(this, view!!)

    tvHeight.text = "$height cm"
    tvWeight.text = "$weight kg"
  }
}
