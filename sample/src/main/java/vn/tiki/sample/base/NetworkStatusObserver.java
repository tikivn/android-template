package vn.tiki.sample.base;

public interface NetworkStatusObserver {
  void onNetworkStatusChanged(boolean isConnected);
}
