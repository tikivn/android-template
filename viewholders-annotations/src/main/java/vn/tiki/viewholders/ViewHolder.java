package vn.tiki.viewholders;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.CLASS;

import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(CLASS)
@Target(TYPE)
public @interface ViewHolder {

  /**
   * Layout ID to inflate
   */
  @LayoutRes int layout() default 0;

  /**
   * View IDs to which the method will be bound.
   */
  @IdRes int[] onClick() default {};
}
