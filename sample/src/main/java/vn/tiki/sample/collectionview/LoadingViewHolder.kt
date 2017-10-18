package vn.tiki.sample.collectionview

import android.view.View
import android.view.ViewGroup
import vn.tiki.noadapter2.AbsViewHolder
import vn.tiki.sample.R
import vn.tiki.sample.util.inflate

class LoadingViewHolder private constructor(view: View) : AbsViewHolder(view) {
  companion object {
    fun create(parent: ViewGroup): LoadingViewHolder {
      val view = parent.inflate(R.layout.clv_item_loading)
      return LoadingViewHolder(view)
    }
  }
}
