package vn.tiki.sample.collectionview

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import vn.tiki.noadapter2.AbsViewHolder
import vn.tiki.sample.R
import vn.tiki.sample.util.inflate

class TextViewHolder private constructor(view: View) : AbsViewHolder(view) {

  override fun bind(item: Any) {
    super.bind(item)
    (itemView as TextView).text = item.toString()
  }

  companion object {
    fun create(parent: ViewGroup): TextViewHolder {
      val view = parent.inflate(R.layout.clv_item_text)
      return TextViewHolder(view)
    }
  }
}
