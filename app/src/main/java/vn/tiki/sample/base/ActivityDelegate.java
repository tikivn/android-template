package vn.tiki.sample.base;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.View;
import vn.tiki.daggers.HasScope;
import vn.tiki.daggers.Daggers;
import vn.tiki.sample.R;
import vn.tiki.sample.di.ActivityModule;
import vn.tiki.sample.util.NetworkUtil;

class ActivityDelegate {

  private NetworkStatusObserver networkStatusObserver;
  private View rootView;
  private Snackbar sbOfflineNotification;
  @NonNull private final BroadcastReceiver networkStatusReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      final boolean connected = NetworkUtil.isConnected(context);
      setNetworkStatus(connected);
    }
  };

  Object makeActivityModule(Activity activity) {
    return new ActivityModule(activity);
  }

  void onPause(Activity activity) {
    if (activity instanceof NetworkStatusObserver) {
      activity.unregisterReceiver(networkStatusReceiver);
      networkStatusObserver = null;
    }
  }

  void onResume(Activity activity) {
    if (activity instanceof NetworkStatusObserver) {
      networkStatusObserver = ((NetworkStatusObserver) activity);
      if (rootView == null) {
        rootView = activity.findViewById(android.R.id.content);
      }
      activity.registerReceiver(
          networkStatusReceiver,
          new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }
  }

  private void setNetworkStatus(boolean connected) {
    if (networkStatusObserver != null) {
      networkStatusObserver.onNetworkStatusChanged(connected);
    }
    if (connected) {
      if (sbOfflineNotification == null) {
        return;
      }
      sbOfflineNotification.dismiss();
    } else {
      if (sbOfflineNotification == null) {
        sbOfflineNotification = Snackbar.make(
            rootView,
            R.string.msg_you_are_offline,
            Snackbar.LENGTH_INDEFINITE);
      }
      sbOfflineNotification.show();
    }
  }
}
