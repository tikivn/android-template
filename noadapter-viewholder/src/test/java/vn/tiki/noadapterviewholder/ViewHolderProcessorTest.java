package vn.tiki.noadapterviewholder;

import static com.google.common.truth.Truth.assertAbout;
import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;

import com.google.testing.compile.JavaFileObjects;
import javax.tools.JavaFileObject;
import org.junit.*;
import vn.tiki.noadapterviewholder.compiler.ViewHolderProcessor;

public class ViewHolderProcessorTest {

  @Test
  public void generateViewHolderDelegate() {
    JavaFileObject source = JavaFileObjects.forSourceString(
        "test.Test",
        ""
        + "package test;\n"
        + "import android.view.View;\n"
        + "import vn.tiki.noadapterviewholder.ViewHolder;\n"
        + "@ViewHolder(\n"
        + "    layout = 10\n"
        + ")"
        + "public abstract class Test {"
        + "  void bindView(final View view) {}\n"
        + "  void bind(final String item) {}"
        + "}"
    );

    JavaFileObject viewHolderDelegate = JavaFileObjects.forSourceString(
        "test/Test_ViewHolderDelegate",
        ""
        + "package test;\n"
        + "import android.view.View;\n"
        + "import java.lang.Object;\n"
        + "import java.lang.Override;\n"
        + "import java.lang.String;\n"
        + "import vn.tiki.noadapterviewholder.ViewHolderDelegate;\n"
        + "final class Test_ViewHolderDelegate extends Test implements ViewHolderDelegate {\n"
        + "  @Override\n"
        + "  public void bind(Object item) {\n"
        + "    super.bind((String) item);\n"
        + "  }\n"
        + "  @Override\n"
        + "  public void bindView(View view) {\n"
        + "    super.bindView(view);\n"
        + "  }\n"
        + "  @Override\n"
        + "  public int layout() {\n"
        + "    return 10;\n"
        + "  }\n"
        + "  @Override\n"
        + "  public int[] onClick() {\n"
        + "    return new int[0];\n"
        + "  }"
        + "}"
    );

    assertAbout(javaSource())
        .that(source)
        .withCompilerOptions("-Xlint:-processing")
        .processedWith(new ViewHolderProcessor())
        .compilesWithoutError()
        .and()
        .generatesSources(viewHolderDelegate);
  }
}
