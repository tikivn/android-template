package vn.tiki.sample.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import vn.tiki.architecture.mvp.Mvp;
import vn.tiki.architecture.mvp.MvpActivity;
import vn.tiki.sample.R;
import vn.tiki.sample.util.NetworkUtil;

public abstract class BaseMvpActivity<V extends Mvp.View, P extends Mvp.Presenter<V>>
    extends MvpActivity<V, P> {

  private Snackbar sbOfflineNotification;
  @NonNull private final BroadcastReceiver networkStatusReceiver = new BroadcastReceiver() {
    @Override public void onReceive(Context context, Intent intent) {
      final boolean connected = NetworkUtil.isConnected(context);
      setNetworkStatus(connected);
    }
  };

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override protected void onPause() {
    super.onPause();
    if (this instanceof NetworkStatusObserver) {
      unregisterReceiver(networkStatusReceiver);
    }
  }

  @Override protected void onResume() {
    super.onResume();
    if (this instanceof NetworkStatusObserver) {
      registerReceiver(
          networkStatusReceiver,
          new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }
  }

  private void setNetworkStatus(boolean connected) {
    ((NetworkStatusObserver) this).onNetworkStatusChanged(connected);
    if (connected) {
      if (sbOfflineNotification == null) {
        return;
      }
      sbOfflineNotification.dismiss();
    } else {
      if (sbOfflineNotification == null) {
        sbOfflineNotification = Snackbar.make(
            findViewById(android.R.id.content),
            R.string.msg_you_are_offline,
            Snackbar.LENGTH_INDEFINITE);
      }
      sbOfflineNotification.show();
    }
  }
}
