package vn.tiki.daggers.di;

import dagger.Subcomponent;

@ActivityScope
@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {

  void inject(Activity __);

}
