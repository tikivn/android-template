package vn.tiki.sample.extra;

import android.content.Context;
import android.os.Bundle;

/**
 * Created by Giang Nguyen on 9/3/17.
 */

public final class ExtraInjectionActivity_ {
  private ExtraInjectionActivity_() {
    throw new InstantiationError();
  }

  static void bindExtras(ExtraInjectionActivity activity) {
    final Bundle extras = activity.getIntent().getExtras();
    bindExtras(activity, extras);
  }

  private static void bindExtras(ExtraInjectionActivity target, Bundle extras) {
    target.name = ExtraInjection.get(extras, "name");
    target.age = ExtraInjection.get(extras, "age");
  }

  public static ExtraInjectionActivity_IntentBuilder intentBuilder(Context context) {
    return new ExtraInjectionActivity_IntentBuilder(context);
  }
}
