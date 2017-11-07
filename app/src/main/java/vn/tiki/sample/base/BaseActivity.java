package vn.tiki.sample.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import vn.tiki.daggers.ActivityInjector;

public abstract class BaseActivity extends AppCompatActivity implements ActivityInjector {

  private ActivityDelegate activityDelegate = new ActivityDelegate();

  @Override
  protected void onCreate(@Nullable final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    activityDelegate.onCreate(this);
  }

  @Override
  protected void onResume() {
    super.onResume();
    activityDelegate.onResume(this);
  }

  @Override
  protected void onPause() {
    activityDelegate.onPause(this);
    super.onPause();
  }

  @Override
  protected void onDestroy() {
    activityDelegate.onDestroy(this);
    super.onDestroy();
  }

  @Override
  public Object activityModule() {
    return activityDelegate.makeActivityModule(this);
  }
}
