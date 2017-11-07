package vn.tiki.sample.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import vn.tiki.architecture.mvp.Mvp;
import vn.tiki.architecture.mvp.MvpActivity;
import vn.tiki.daggers.ActivityInjector;

public abstract class BaseMvpActivity<V extends Mvp.View, P extends Mvp.Presenter<V>>
    extends MvpActivity<V, P> implements ActivityInjector {

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
    super.onPause();
    activityDelegate.onPause(this);
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
