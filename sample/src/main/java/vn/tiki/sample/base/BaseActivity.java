package vn.tiki.sample.base;

import android.support.v7.app.AppCompatActivity;
import vn.tiki.daggers.ActivityInjector;

public abstract class BaseActivity extends AppCompatActivity implements ActivityInjector {

  private ActivityDelegate activityDelegate = new ActivityDelegate();

  @Override protected void onPause() {
    super.onPause();
    activityDelegate.onPause(this);
  }

  @Override protected void onResume() {
    super.onResume();
    activityDelegate.onResume(this);
  }

  @Override public Object activityModule() {
    return activityDelegate.makeActivityModule(this);
  }
}
