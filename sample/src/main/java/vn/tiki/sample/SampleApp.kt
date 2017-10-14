package vn.tiki.sample

import android.app.Application
import android.util.Log
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import io.fabric.sdk.android.Fabric
import timber.log.Timber
import vn.tiki.daggers.AppInjector
import vn.tiki.daggers.Daggers
import vn.tiki.sample.di.AppComponent
import vn.tiki.sample.di.AppModule
import vn.tiki.sample.di.DaggerAppComponent

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

    val list1 = listOf(1, 2, 3, 4)
    val list2 = ArrayList<Int>()
    list1.mapTo(list2) { it }
  }

  override fun appComponent(): Any {
    return appComponent
  }

  protected fun configureDagger() {
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
