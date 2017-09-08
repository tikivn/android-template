package vn.tiki.sample.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * Bind a field to the extra.
 * <pre><code>
 * {@literal @}Extra String title;
 * </code></pre>
 */
@Retention(CLASS) @Target(FIELD)
public @interface Extra {
}
