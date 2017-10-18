package vn.tiki.sample.collectionview

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import kotlinx.android.synthetic.main.activity_collection_view.vCollectionView
import vn.tiki.collectionview.Adapter
import vn.tiki.collectionview.DataProvider
import vn.tiki.collectionview.LoadingItem
import vn.tiki.noadapter2.OnlyAdapter
import vn.tiki.sample.R

class CollectionViewActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContentView(R.layout.activity_collection_view)
    ButterKnife.bind(this)

    val adapter = OnlyAdapter.builder()
        .typeFactory { item -> if (item is LoadingItem) 1 else 2 }
        .viewHolderFactory { parent, type ->
          if (type == 1) LoadingViewHolder.create(parent) else TextViewHolder.create(parent)
        }
        .build()

    vCollectionView.setAdapter(object : Adapter<String> {
      override fun onCreateDataProvider(): DataProvider<String> {
        return TodoDataProvider()
      }

      override fun onCreateLayoutManager(): RecyclerView.LayoutManager {
        return LinearLayoutManager(
            this@CollectionViewActivity,
            LinearLayoutManager.VERTICAL,
            false)
      }

      override fun onCreateRecyclerViewAdapter(): RecyclerView.Adapter<*>? {
        return adapter
      }

      override fun onBindItems(items: List<String>) {
        adapter.setItems(items)
      }

      override fun onCreateErrorView(parent: ViewGroup, throwable: Throwable): View {
        return layoutInflater.inflate(R.layout.view_error, parent, false)
      }
    })
  }
}
