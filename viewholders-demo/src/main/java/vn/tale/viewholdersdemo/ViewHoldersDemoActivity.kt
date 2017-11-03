package vn.tale.viewholdersdemo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.GridLayoutManager.SpanSizeLookup
import android.support.v7.widget.RecyclerView
import viewholders.NoAdapterFactory
import vn.tale.viewholdersdemo.viewholder.ButtonModel
import vn.tale.viewholdersdemo.viewholder.ColorModel
import vn.tale.viewholdersdemo.viewholder.TextModel
import vn.tiki.noadapter2.OnlyAdapter
import java.util.ArrayList
import java.util.Collections
import java.util.Random

class ViewHoldersDemoActivity : AppCompatActivity() {

  private lateinit var adapter: OnlyAdapter
  private val items = ArrayList<Any>()
  private val random = Random()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_view_holders_demo)

    val rvGrid = findViewById<RecyclerView>(R.id.rvGrid)

    val gridLayoutManager = GridLayoutManager(this, 5, GridLayoutManager.VERTICAL, false)
    gridLayoutManager.spanSizeLookup = object : SpanSizeLookup() {
      override fun getSpanSize(position: Int): Int {
        return if (items[position] is ButtonModel) {
          5
        } else {
          1
        }
      }
    }
    rvGrid.layoutManager = gridLayoutManager
    rvGrid.setHasFixedSize(true)
    adapter = NoAdapterFactory.builder()
        .onItemClickListener { view, item, _ ->
          if (item is ButtonModel) {
            val id = view.id
            when (id) {
              R.id.btAdd -> {
                val randomItem = randomItem(items.size)
                items.add(randomItem)
                updateList()
              }
              R.id.btRemove -> {
                items.removeAt(items.lastIndex)
                updateList()
              }
              R.id.btShuffle -> {
                Collections.shuffle(items)
                updateList()
              }
            }
          }
        }
        .build()
    rvGrid.adapter = adapter

    // init with controller buttons
    init()
  }

  private fun init() {
    items.add(ButtonModel())
    updateList()
  }

  private fun randomColor(): Int {
    val r = random.nextInt(256)
    val g = random.nextInt(256)
    val b = random.nextInt(256)

    return android.graphics.Color.rgb(r, g, b)
  }

  private fun randomItem(i: Int): Any {
    return if (i % 3 == 0) {
      TextModel("Text #" + i)
    } else {
      ColorModel(randomColor())
    }
  }

  private fun updateList() {
    adapter.setItems(ArrayList(items))
  }

}
