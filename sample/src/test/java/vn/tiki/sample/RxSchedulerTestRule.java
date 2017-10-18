package vn.tiki.sample;

import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;
import org.junit.rules.*;
import org.junit.runner.*;
import org.junit.runners.model.*;

public class RxSchedulerTestRule implements TestRule {

  @Override
  public Statement apply(
      final Statement base, Description description) {
    return new Statement() {
      @Override
      public void evaluate() throws Throwable {
        RxJavaPlugins.setIoSchedulerHandler(scheduler -> Schedulers.trampoline());
        RxJavaPlugins.setComputationSchedulerHandler(scheduler -> Schedulers.trampoline());
        RxJavaPlugins.setNewThreadSchedulerHandler(scheduler -> Schedulers.trampoline());
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(schedulerCallable -> Schedulers.trampoline());

        try {
          base.evaluate();
        } finally {
          RxJavaPlugins.reset();
          RxAndroidPlugins.reset();
        }
      }
    };
  }
}