package vn.tiki.intents;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * Bind an extra value from Intent to the field. The value will automatically be cast to the field
 * type.
 * <pre><code>
 * {@literal @}Extra String title;
 * </code></pre>
 */
@Retention(CLASS)
@Target(FIELD)
public @interface Extra {}
