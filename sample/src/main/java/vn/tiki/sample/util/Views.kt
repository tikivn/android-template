package vn.tiki.sample.util

import android.view.View

fun onClick(v: View, onClick: (View) -> Unit) {
  v.setOnClickListener(onClick)
}