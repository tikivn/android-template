package vn.tiki.sample.extra;

import vn.tiki.sample.di.ActivityScope;

public interface ExtraInjectionModule {
  @ActivityScope
  @dagger.Subcomponent(modules = Module.class)
  interface Component {
    void inject(ExtraInjectionActivity __);
  }

  @dagger.Module
  class Module {
  }
}
