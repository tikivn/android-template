package vn.tiki.sample

import android.app.Application
import android.os.StrictMode
import android.util.Log
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import com.facebook.stetho.Stetho
import com.squareup.leakcanary.LeakCanary
import com.squareup.leakcanary.RefWatcher
import io.fabric.sdk.android.Fabric
import timber.log.Timber
import vn.tiki.daggers.AppInjector
import vn.tiki.daggers.Daggers
import vn.tiki.sample.di.AppComponent
import vn.tiki.sample.di.AppModule

open class SampleApp : Application(), AppInjector {

  private val appComponent: AppComponent by lazy {
    DaggerAppComponent.builder()
        .appModule(AppModule(this))
        .build()
  }

  override fun onCreate() {
    super.onCreate()

    configureDagger()
    configureFabric()
    configureTimber()
    configureLeakCanary()
    configureStrictMode()
    configureStetho()
  }

  private fun configureStetho() {
    if (BuildConfig.DEBUG) {
      Stetho.initializeWithDefaults(this)
    }
  }

  private fun configureStrictMode() {
    if (BuildConfig.DEBUG) {
      StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder() //
          .detectAll() //
          .penaltyLog() //
          .penaltyDeath() //
          .build())
    }
  }

  private fun configureLeakCanary(): RefWatcher? {
    return if (LeakCanary.isInAnalyzerProcess(this)) {
      RefWatcher.DISABLED
    } else LeakCanary.install(this)
  }

  override fun appComponent(): Any {
    return appComponent
  }

  private fun configureDagger() {
    Daggers.configure(this)
  }

  private fun configureFabric() {
    val crashlyticsKit = Crashlytics.Builder()
        .core(CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build())
        .build()
    Fabric.with(this, crashlyticsKit)
  }

  private fun configureTimber() {
    if (BuildConfig.DEBUG) {
      Timber.plant(Timber.DebugTree())
    } else {
      Timber.plant(object : Timber.Tree() {
        override fun log(priority: Int, tag: String, message: String, t: Throwable?) {
          if (priority == Log.VERBOSE || priority == Log.DEBUG) {
            return
          }

          Crashlytics.log(priority, tag, message)

          if (t != null) {
            Crashlytics.logException(t)
          }
        }
      })
    }
  }
}
