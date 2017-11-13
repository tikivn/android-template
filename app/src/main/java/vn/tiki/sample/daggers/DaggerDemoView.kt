package vn.tiki.sample.daggers

import vn.tiki.architecture.mvp.Mvp.View

interface DaggerDemoView : View {
  fun showValue(value: String)
}