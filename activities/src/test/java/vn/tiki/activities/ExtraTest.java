package vn.tiki.activities;

import com.google.testing.compile.JavaFileObjects;
import javax.tools.JavaFileObject;
import org.junit.Test;
import vn.tiki.activities.compiler.ActivitiesProcessor;

import static com.google.common.truth.Truth.assertAbout;
import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;

public class ExtraTest {

  @Test public void bindingExtraForActivity() {
    JavaFileObject source = JavaFileObjects.forSourceString("test.Test", ""
        + "package test;\n"
        + "import android.app.Activity;\n"
        + "import vn.tiki.activities.Extra;\n"
        + "public class Test extends Activity {\n"
        + "    @Extra String name;\n"
        + "    @Extra int age;\n"
        + "}"
    );

    JavaFileObject bindingSource = JavaFileObjects.forSourceString("test/Test_", ""
        + "package test;\n"
        + "import android.os.Bundle;\n"
        + "import vn.tiki.activities.internal.Bundles;\n"
        + "public final class Test_ {\n"
        + "  private Test() {\n"
        + "  }\n"
        + "  public static void bindExtras(Test target) {\n"
        + "    bindExtras(target, target.getIntent().getExtras());\n"
        + "  }\n"
        + "  public static void bindExtras(Test target, Bundle source) {\n"
        + "    target.name = Bundles.get(source, \"name\");\n"
        + "    target.age = Bundles.get(source, \"age\");\n"
        + "  }\n"
        + "}"
    );

    assertAbout(javaSource()).that(source)
        .withCompilerOptions("-Xlint:-processing")
        .processedWith(new ActivitiesProcessor())
        .compilesWithoutWarnings()
        .and()
        .generatesSources(bindingSource);
  }
}
