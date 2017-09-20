package vn.tiki.sample.productlist;

import android.content.Context;
import dagger.Provides;
import vn.tiki.sample.di.ActivityScope;

public interface ProductListing {

  @ActivityScope
  @dagger.Subcomponent(modules = Module.class)
  interface Component {
    void inject(ProductListingActivity __);
  }

  @dagger.Module
  class Module {
    private final Context context;

    public Module(Context context) {
      this.context = context;
    }

    @Provides @ActivityScope Context provideContext() {
      return context;
    }
  }
}

