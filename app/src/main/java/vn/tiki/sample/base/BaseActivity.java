package vn.tiki.sample.base;

import android.support.v7.app.AppCompatActivity;
import vn.tiki.daggers.HasScope;

public abstract class BaseActivity extends AppCompatActivity implements HasScope {

  private ActivityDelegate activityDelegate = new ActivityDelegate();

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
  public Object module() {
    return activityDelegate.makeActivityModule(this);
  }
}
