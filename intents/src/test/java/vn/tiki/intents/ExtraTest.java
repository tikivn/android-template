package vn.tiki.intents;

import com.google.testing.compile.JavaFileObjects;
import javax.tools.JavaFileObject;
import org.junit.Test;
import vn.tiki.intents.compiler.IntentsProcessor;

import static com.google.common.truth.Truth.assertAbout;
import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;

public class ExtraTest {

  @Test public void bindingExtraForActivity() {
    JavaFileObject source = JavaFileObjects.forSourceString("test.Test", ""
        + "package test;\n"
        + "import android.app.Activity;\n"
        + "import vn.tiki.intents.Extra;\n"
        + "public class Test extends Activity {\n"
        + "    @Extra String name;\n"
        + "    @Extra int age;\n"
        + "}"
    );

    JavaFileObject intentBuilderSource = JavaFileObjects.forSourceString(
        "test/Test_IntentBuilder",
        ""
            + "package test;\n"
            + "import android.content.Context;\n"
            + "import android.content.Intent;\n"
            + "import java.lang.String;\n"
            + "public final class Test_IntentBuilder {\n"
            + "  private final Intent intent;\n"
            + "  Test_IntentBuilder(Context context) {\n"
            + "    intent = new Intent(context, Test.class);\n"
            + "  }\n"
            + "  public Test_IntentBuilder name(String name) {\n"
            + "    intent.putExtra(\"name\", name);\n"
            + "    return this;\n"
            + "  }\n"
            + "  public Test_IntentBuilder age(int age) {\n"
            + "    intent.putExtra(\"age\", age);\n"
            + "    return this;\n"
            + "  }\n"
            + "  public Intent make() {\n"
            + "    return intent;\n"
            + "  }\n"
            + "}"
    );

    JavaFileObject bindingSource = JavaFileObjects.forSourceString("test/Test_", ""
        + "package test;\n"
        + "import android.content.Context;\n"
        + "import android.os.Bundle;\n"
        + "import vn.tiki.intents.internal.Bundles;\n"
        + "public final class Test_ {\n"
        + "  private Test_() {\n"
        + "  }\n"
        + "  public static void bindExtras(Test target) {\n"
        + "    bindExtras(target, target.getIntent().getExtras());\n"
        + "  }\n"
        + "  public static void bindExtras(Test target, Bundle source) {\n"
        + "    target.name = Bundles.get(source, \"name\");\n"
        + "    target.age = Bundles.get(source, \"age\");\n"
        + "  }\n"
        + "  public static Test_IntentBuilder intentBuilder(Context context) {\n"
        + "    return new Test_IntentBuilder(context);\n"
        + "  }\n"
        + "}"
    );

    assertAbout(javaSource()).that(source)
        .withCompilerOptions("-Xlint:-processing")
        .processedWith(new IntentsProcessor())
        .compilesWithoutWarnings()
        .and()
        .generatesSources(intentBuilderSource, bindingSource);
  }

  @Test public void bindingExtraForFragment() {
    JavaFileObject source = JavaFileObjects.forSourceString("test.Test", ""
        + "package test;\n"
        + "import android.app.Fragment;\n"
        + "import vn.tiki.intents.Extra;\n"
        + "public class Test extends Fragment {\n"
        + "    @Extra String name;\n"
        + "    @Extra int age;\n"
        + "}"
    );

    JavaFileObject fragmentBuilderSource = JavaFileObjects.forSourceString(
        "test/Test_Builder",
        ""
            + "package test;\n"
            + "import android.content.Intent;\n"
            + "import java.lang.String;\n"
            + "public final class Test_Builder {\n"
            + "  private final Intent args;\n"
            + "  Test_Builder() {\n"
            + "    args = new Intent();\n"
            + "  }\n"
            + "  public Test_Builder name(String name) {\n"
            + "    args.putExtra(\"name\", name);\n"
            + "    return this;\n"
            + "  }\n"
            + "  public Test_Builder age(int age) {\n"
            + "    args.putExtra(\"age\", age);\n"
            + "    return this;\n"
            + "  }\n"
            + "  public Test make() {\n"
            + "    Test target = new Test();\n"
            + "    target.setArguments(args.getExtras());\n"
            + "    return target;\n"
            + "  }\n"
            + "}"
    );

    JavaFileObject bindingSource = JavaFileObjects.forSourceString("test/Test_", ""
        + "package test;\n"
        + "import android.os.Bundle;\n"
        + "import vn.tiki.intents.internal.Bundles;\n"
        + "public final class Test_ {\n"
        + "  private Test_() {\n"
        + "  }\n"
        + "  public static void bindExtras(Test target) {\n"
        + "    bindExtras(target, target.getArguments());\n"
        + "  }\n"
        + "  public static void bindExtras(Test target, Bundle source) {\n"
        + "    target.name = Bundles.get(source, \"name\");\n"
        + "    target.age = Bundles.get(source, \"age\");\n"
        + "  }\n"
        + "  public static Test_Builder builder() {\n"
        + "    return new Test_Builder();\n"
        + "  }\n"
        + "}"
    );

    assertAbout(javaSource()).that(source)
        .withCompilerOptions("-Xlint:-processing")
        .processedWith(new IntentsProcessor())
        .compilesWithoutWarnings()
        .and()
        .generatesSources(fragmentBuilderSource, bindingSource);
  }
}
