package vn.tiki.sample.extra;

import android.content.Context;
import android.content.Intent;

/**
 * Created by Giang Nguyen on 9/3/17.
 */

public final class ExtraInjectionActivity_IntentBuilder {
  private final Intent intent;

  ExtraInjectionActivity_IntentBuilder(Context context) {
    intent = new Intent(context, ExtraInjectionActivity.class);
  }

  public ExtraInjectionActivity_IntentBuilder name(String name) {
    intent.putExtra("name", name);
    return this;
  }

  public ExtraInjectionActivity_IntentBuilder age(int age) {
    intent.putExtra("age", age);
    return this;
  }

  public Intent make() {
    return intent;
  }
}
