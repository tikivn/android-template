package vn.tiki.sample.base

interface NetworkStatusObserver {
  fun onNetworkStatusChanged(isConnected: Boolean)
}
