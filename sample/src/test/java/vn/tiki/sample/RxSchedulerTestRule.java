package vn.tiki.sample;

import io.reactivex.Scheduler;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;
import java.util.concurrent.Callable;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class RxSchedulerTestRule implements TestRule {

  @Override public Statement apply(
      final Statement base, Description description) {
    return new Statement() {
      @Override public void evaluate() throws Throwable {
        RxJavaPlugins.setIoSchedulerHandler(new Function<Scheduler, Scheduler>() {
          @Override public Scheduler apply(@NonNull Scheduler scheduler) throws Exception {
            return Schedulers.trampoline();
          }
        });
        RxJavaPlugins.setComputationSchedulerHandler(new Function<Scheduler, Scheduler>() {
          @Override public Scheduler apply(@NonNull Scheduler scheduler) throws Exception {
            return Schedulers.trampoline();
          }
        });
        RxJavaPlugins.setNewThreadSchedulerHandler(new Function<Scheduler, Scheduler>() {
          @Override public Scheduler apply(@NonNull Scheduler scheduler) throws Exception {
            return Schedulers.trampoline();
          }
        });

        RxAndroidPlugins.setInitMainThreadSchedulerHandler(new Function<Callable<Scheduler>,
            Scheduler>() {
          @Override public Scheduler apply(
              @NonNull Callable<Scheduler> schedulerCallable) throws Exception {
            return Schedulers.trampoline();
          }
        });

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