package vn.tiki.intents;

import com.google.testing.compile.JavaFileObjects;
import javax.tools.JavaFileObject;
import org.junit.Test;
import vn.tiki.intents.compiler.IntentsProcessor;

import static com.google.common.truth.Truth.assertAbout;
import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;

public class BindExtraTest {

  @Test public void bindingExtraForActivity() {
    JavaFileObject source = JavaFileObjects.forSourceString("test.Test", ""
        + "package test;\n"
        + "import android.app.Activity;\n"
        + "import vn.tiki.intents.BindExtra;\n"
        + "public class Test extends Activity {\n"
        + "    @BindExtra String name;\n"
        + "    @BindExtra int age;\n"
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
            + "  public Test_IntentBuilder(Context context) {\n"
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

    JavaFileObject bindingSource = JavaFileObjects.forSourceString("test/Test_ExtraBinding", ""
        + "package test;\n"
        + "import android.os.Bundle;\n"
        + "import vn.tiki.intents.internal.Bundles;\n"
        + "public final class Test_ExtraBinding {\n"
        + "  public static void bindExtras(Test target, Bundle source) {\n"
        + "    if (Bundles.has(source, \"name\")) {\n"
        + "      target.name = Bundles.get(source, \"name\");\n"
        + "    }\n"
        + "    if (Bundles.has(source, \"age\")) {\n"
        + "      target.age = Bundles.get(source, \"age\");\n"
        + "    }\n"
        + "  }\n"
        + "}"
    );

    JavaFileObject intentsSource = JavaFileObjects.forSourceString("intents/Intents", ""
        + "package intents;\n"
        + "import android.content.Context;\n"
        + "import android.os.Bundle;\n"
        + "import test.Test;\n"
        + "import test.Test_ExtraBinding;\n"
        + "import test.Test_IntentBuilder;\n"
        + "public final class Intents {\n"
        + "  private Intents() {}\n"
        + "  public static void bind(Test target) {\n"
        + "    Bundle source = target.getIntent().getExtras();\n"
        + "    Test_ExtraBinding.bindExtras(target, source);\n"
        + "  }\n"
        + "  public static Test_IntentBuilder test(Context context) {\n"
        + "    return new Test_IntentBuilder(context);\n"
        + "  }\n"
        + "}"
    );

    assertAbout(javaSource()).that(source)
        .withCompilerOptions("-Xlint:-processing")
        .processedWith(new IntentsProcessor())
        .compilesWithoutWarnings()
        .and()
        .generatesSources(intentBuilderSource, bindingSource, intentsSource);
  }

  @Test public void bindingExtraForFragment() {
    JavaFileObject source = JavaFileObjects.forSourceString("test.Test", ""
        + "package test;\n"
        + "import android.app.Fragment;\n"
        + "import vn.tiki.intents.BindExtra;\n"
        + "public class Test extends Fragment {\n"
        + "    @BindExtra String name;\n"
        + "    @BindExtra int age;\n"
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
            + "  public Test_Builder() {\n"
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

    JavaFileObject bindingSource = JavaFileObjects.forSourceString("test/Test_ExtraBinding", ""
        + "package test;\n"
        + "import android.os.Bundle;\n"
        + "import vn.tiki.intents.internal.Bundles;\n"
        + "public final class Test_ExtraBinding {\n"
        + "  public static void bindExtras(Test target, Bundle source) {\n"
        + "    if (Bundles.has(source, \"name\")) {\n"
        + "      target.name = Bundles.get(source, \"name\");\n"
        + "    }\n"
        + "    if (Bundles.has(source, \"age\")) {\n"
        + "      target.age = Bundles.get(source, \"age\");\n"
        + "    }\n"
        + "  }\n"
        + "}"
    );

    JavaFileObject intentsSource = JavaFileObjects.forSourceString("intents/Intents", ""
        + "package intents;\n"
        + "import android.os.Bundle;\n"
        + "import test.Test;\n"
        + "import test.Test_Builder;\n"
        + "import test.Test_ExtraBinding;\n"
        + "public final class Intents {\n"
        + "  private Intents() {}\n"
        + "  public static void bind(Test target) {\n"
        + "    Bundle source = target.getArguments();\n"
        + "    Test_ExtraBinding.bindExtras(target, source);\n"
        + "  }\n"
        + "  public static Test_Builder test() {\n"
        + "    return new Test_Builder();\n"
        + "  }\n"
        + "}"
    );

    assertAbout(javaSource()).that(source)
        .withCompilerOptions("-Xlint:-processing")
        .processedWith(new IntentsProcessor())
        .compilesWithoutWarnings()
        .and()
        .generatesSources(fragmentBuilderSource, bindingSource, intentsSource);
  }
}
