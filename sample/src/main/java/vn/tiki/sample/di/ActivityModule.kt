package vn.tiki.sample.di

import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class ActivityModule(private val context: Context) {

  @ActivityScope
  @Provides internal fun provideContext(): Context {
    return context
  }
}
