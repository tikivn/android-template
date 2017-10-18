package vn.tiki.sample.productdetail

import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import butterknife.ButterKnife
import butterknife.OnClick
import kotlinx.android.synthetic.main.activity_product_detail.ivThumb
import kotlinx.android.synthetic.main.activity_product_detail.spSize
import kotlinx.android.synthetic.main.activity_product_detail.tvDescription
import kotlinx.android.synthetic.main.activity_product_detail.tvName
import kotlinx.android.synthetic.main.activity_product_detail.tvPrice
import vn.tiki.daggers.Daggers
import vn.tiki.sample.Extras
import vn.tiki.sample.R
import vn.tiki.sample.base.BaseMvpActivity
import vn.tiki.sample.entity.Product
import vn.tiki.sample.util.extra
import vn.tiki.sample.util.setHtml
import vn.tiki.sample.util.setImage
import vn.tiki.sample.util.setPrice
import vn.tiki.sample.util.toHtml
import javax.inject.Inject

class ProductDetailActivity : BaseMvpActivity<ProductDetailView, ProductDetailPresenter>(), ProductDetailView {

  @Inject internal lateinit var presenter: ProductDetailPresenter

  internal val product: Product by extra(Extras.PRODUCT)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    Daggers.inject(this)

    setContentView(R.layout.activity_product_detail)
    ButterKnife.bind(this)

    configureSizeOptions()
    connect(presenter, this)
    bind(product)
    presenter.setProduct(product)
  }

  @OnClick(R.id.btAddToCart)
  fun onAddToCartClicked() {
    presenter.onAddToCartClick()
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menuInflater.inflate(R.menu.menu_cart, menu)
    return true
  }

  private fun bind(product: Product) {
    tvName.text = product.title
    tvPrice.setPrice(product.price)
    tvDescription.setHtml(product.description.toHtml().toString())
    ivThumb.setImage(product.image)
  }

  private fun configureSizeOptions() {
    // Create an ArrayAdapter using the string array and a default spinner layout
    val adapter = ArrayAdapter.createFromResource(this,
        R.array.size_options, android.R.layout.simple_spinner_item)
    // Specify the layout to use when the list of choices appears
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    // Apply the adapter to the spinner
    spSize.adapter = adapter
    spSize.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
      override fun onItemSelected(adapterView: AdapterView<*>, view: View, position: Int, id: Long) {
        val selectedOption = adapter.getItem(position) ?: return
        presenter.onSizeSelected(selectedOption.toString())
      }

      override fun onNothingSelected(adapterView: AdapterView<*>) {

      }
    }
  }
}
