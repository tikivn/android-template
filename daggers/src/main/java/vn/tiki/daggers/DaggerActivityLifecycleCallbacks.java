package vn.tiki.daggers;

import android.app.Activity;
import android.app.Application.ActivityLifecycleCallbacks;
import android.os.Bundle;

class DaggerActivityLifecycleCallbacks implements ActivityLifecycleCallbacks {

  private static final String DAGGERS_KEY_SCOPE_OPENED = "daggers:hasScope";
  private boolean scopeOpened;

  @Override
  public void onActivityCreated(final Activity activity, final Bundle savedInstanceState) {
    if (activity instanceof HasScope
        && scopeIsNotOpened(savedInstanceState)) {
      Daggers.openActivityScope(((HasScope) activity).module());
      scopeOpened = true;
    }
  }

  @Override
  public void onActivityDestroyed(final Activity activity) {
    if (activity instanceof HasScope && activity.isFinishing()) {
      Daggers.closeActivityScope();
      scopeOpened = false;
    }
  }

  @Override
  public void onActivityPaused(final Activity activity) {

  }

  @Override
  public void onActivityResumed(final Activity activity) {

  }

  @Override
  public void onActivitySaveInstanceState(final Activity activity, final Bundle outState) {
    if (activity instanceof HasScope) {
      outState.putBoolean(DAGGERS_KEY_SCOPE_OPENED, scopeOpened);
    }
  }

  @Override
  public void onActivityStarted(final Activity activity) {

  }

  @Override
  public void onActivityStopped(final Activity activity) {

  }

  private boolean scopeIsNotOpened(final Bundle savedInstanceState) {
    return savedInstanceState == null || !savedInstanceState.getBoolean(DAGGERS_KEY_SCOPE_OPENED);
  }
}
