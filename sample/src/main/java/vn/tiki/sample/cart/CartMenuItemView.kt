package vn.tiki.sample.cart

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.cart_view_menu_item.view.tvItemCount
import kotlinx.android.synthetic.main.cart_view_menu_item.view.vProgress
import vn.tiki.daggers.Daggers
import vn.tiki.sample.R
import vn.tiki.sample.repository.CartRepository
import javax.inject.Inject

class CartMenuItemView(
    context: Context,
    attrs: AttributeSet?) : FrameLayout(context, attrs) {

  @Inject internal lateinit var cartRepository: CartRepository

  private var disposable: Disposable? = null

  init {
    inflate(context, R.layout.cart_view_menu_item, this)
    Daggers.inject(this)
  }

  override fun onAttachedToWindow() {
    super.onAttachedToWindow()
    showLoading()
    disposable = cartRepository.allItems
        .filter { it.isNotEmpty() }
        .map {
          it.map { it.quantity }
              .reduce { i1, i2 -> i1 + i2 }
              .toString()
        }
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe { text ->
          hideLoading()
          tvItemCount.text = text
        }
  }

  override fun onDetachedFromWindow() {
    super.onDetachedFromWindow()
    disposable?.dispose()
  }

  private fun showLoading() {
    vProgress.visibility = View.VISIBLE
    tvItemCount.visibility = View.GONE
  }

  private fun hideLoading() {
    vProgress.visibility = View.GONE
    tvItemCount.visibility = View.VISIBLE
  }
}
