package vn.tiki.sample.productlist

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.LayoutManager
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import io.reactivex.Single
import kotlinx.android.synthetic.main.activity_product_listing.toolbar
import kotlinx.android.synthetic.main.activity_product_listing.vCollectionView
import vn.tiki.collectionview.Adapter
import vn.tiki.collectionview.DataProvider
import vn.tiki.collectionview.ListData
import vn.tiki.daggers.Daggers
import vn.tiki.noadapter2.DiffCallback
import vn.tiki.noadapter2.OnlyAdapter
import vn.tiki.sample.Extras
import vn.tiki.sample.R
import vn.tiki.sample.base.BaseActivity
import vn.tiki.sample.base.NetworkStatusObserver
import vn.tiki.sample.entity.Product
import vn.tiki.sample.productdetail.ProductDetailActivity
import vn.tiki.sample.repository.ProductRepository
import vn.tiki.sample.util.bindString
import vn.tiki.sample.util.start
import javax.inject.Inject

class ProductListingActivity : BaseActivity(), NetworkStatusObserver {

  private val textProductListing: String by bindString(R.string.product_listing)

  @Inject internal lateinit var productRepository: ProductRepository

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    Daggers.inject(this)

    setContentView(R.layout.activity_product_listing)
    ButterKnife.bind(this)

    configureToolbar()
    configureCollectionView()
  }

  override fun onNetworkStatusChanged(isConnected: Boolean) {

  }

  private fun configureCollectionView() {
    val adapter = OnlyAdapter.Builder()
        .viewHolderFactory { parent, _ -> ProductViewHolder.create(parent) }
        .diffCallback(object : DiffCallback {
          override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
            return oldItem == newItem
          }

          override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
            return (oldItem is Product
                && newItem is Product
                && oldItem.id == newItem.id)
          }
        })
        .onItemClickListener { _, item, _ ->
          start<ProductDetailActivity> {
            putExtra(Extras.PRODUCT, item as Product)
          }
        }
        .build()

    vCollectionView.setAdapter(object : Adapter<Product> {
      override fun onCreateLayoutManager(): LayoutManager {
        return LinearLayoutManager(
            this@ProductListingActivity,
            LinearLayoutManager.VERTICAL,
            false)
      }

      override fun onCreateRecyclerViewAdapter(): RecyclerView.Adapter<*> {
        return adapter
      }

      override fun onBindItems(items: MutableList<Product>?) {
        adapter.setItems(items)
      }

      override fun onCreateErrorView(parent: ViewGroup?, throwable: Throwable?): View {
        return layoutInflater.inflate(R.layout.view_error, parent, false)
      }

      override fun onCreateDataProvider(): DataProvider<Product> {
        return object : DataProvider<Product> {
          override fun fetch(page: Int): Single<out ListData<Product>> {
            return productRepository.getProducts(page, false)
          }

          override fun fetchNewest(): Single<out ListData<Product>> {
            return productRepository.getProducts(1, true)
          }
        }
      }
    })
  }

  private fun configureToolbar() {
    setSupportActionBar(toolbar)
    supportActionBar?.title = textProductListing
  }
}
