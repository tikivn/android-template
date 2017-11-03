package vn.tiki.viewholders;

import static com.google.common.truth.Truth.assertAbout;
import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;
import static com.google.testing.compile.JavaSourcesSubjectFactory.javaSources;

import com.google.testing.compile.JavaFileObjects;
import java.util.Arrays;
import javax.tools.JavaFileObject;
import org.junit.*;
import vn.tiki.viewholders.compiler.ViewHolderProcessor;

public class ViewHolderProcessorTest {

  private JavaFileObject source;

  @Before
  public void setUp() throws Exception {
    source = JavaFileObjects.forSourceString(
        "test.Test",
        ""
        + "package test;\n"
        + "import android.view.View;\n"
        + "import vn.tiki.viewholders.ViewHolder;\n"
        + "@ViewHolder(\n"
        + "    layout = 10,\n"
        + "    onClick = {1, 2},\n"
        + "    bindTo = String.class\n"
        + ")"
        + "public abstract class Test {"
        + "  void bindView(final View view) {}\n"
        + "  void bind(final String item) {}"
        + "}"
    );
  }

  @Test
  public void testGenerateNoAdapterFactory() {
    JavaFileObject noAdapterFactory = JavaFileObjects.forSourceString(
        "viewholders/NoAdapterFactory",
        ""
        + "package viewholders;\n"
        + "\n"
        + "import java.lang.InstantiationError;\n"
        + "import vn.tiki.noadapter2.OnlyAdapter;\n"
        + "import vn.tiki.viewholders.OnlyDiffCallback;"
        + "\n"
        + "public final class NoAdapterFactory {\n"
        + "\n"
        + "  private NoAdapterFactory() {\n"
        + "    throw new InstantiationError();\n"
        + "  }\n"
        + "\n"
        + "  public static OnlyAdapter.Builder builder() {\n"
        + "    return new OnlyAdapter.Builder()\n"
        + "        .typeFactory(new TypeFactoryImpl())\n"
        + "        .viewHolderFactory(new ViewHolderFactoryImpl())\n"
        + "        .diffCallback(new OnlyDiffCallback());\n"
        + "  }\n"
        + "}\n"
    );

    assertAbout(javaSource()).that(source)
        .withCompilerOptions("-Xlint:-processing")
        .processedWith(new ViewHolderProcessor())
        .compilesWithoutError()
        .and()
        .generatesSources(noAdapterFactory);
  }

  @Test
  public void testGenerateTypeFactory() {
    JavaFileObject source = JavaFileObjects.forSourceString(
        "test.Test",
        ""
        + "package test;\n"
        + "import android.view.View;\n"
        + "import vn.tiki.viewholders.ViewHolder;\n"
        + "@ViewHolder(\n"
        + "    layout = 10,\n"
        + "    bindTo = String.class\n"
        + ")"
        + "public abstract class Test {"
        + "  void bind(String item) {}"
        + "}"
    );
    JavaFileObject typeFactory = JavaFileObjects.forSourceString(
        "viewholders/TypeFactoryImpl",
        ""
        + "package viewholders;\n"
        + "import java.lang.Class;\n"
        + "import java.lang.IllegalArgumentException;\n"
        + "import java.lang.Integer;\n"
        + "import java.lang.Object;\n"
        + "import java.lang.Override;\n"
        + "import java.lang.String;\n"
        + "import java.util.LinkedHashMap;\n"
        + "import vn.tiki.noadapter2.TypeFactory;\n"
        + "\n"
        + "final class TypeFactoryImpl implements TypeFactory {\n"
        + "\n"
        + "  private final LinkedHashMap<Class, Integer> typeMapping;\n"
        + "\n"
        + "  TypeFactoryIml() {\n"
        + "    typeMapping = new LinkedHashMap<Class, Integer>();\n"
        + "    typeMapping.put(String.class, 10);\n"
        + "  }\n"
        + "\n"
        + "  @Override\n"
        + "  public int typeOf(Object item) {\n"
        + "    final Class<?> itemClass = item.getClass();\n"
        + "    if (typeMapping.containsKey(itemClass)) {\n"
        + "      return typeMapping.get(itemClass);\n"
        + "    } else {\n"
        + "      throw new IllegalArgumentException(\"unknown \" + itemClass);\n"
        + "    }\n"
        + "  }\n"
        + "}\n"
    );

    assertAbout(javaSource()).that(source)
        .withCompilerOptions("-Xlint:-processing")
        .processedWith(new ViewHolderProcessor())
        .compilesWithoutError()
        .and()
        .generatesSources(typeFactory);
  }

  @Test
  public void testGenerateTypeFactoryGeneric() {
    JavaFileObject source = JavaFileObjects.forSourceString(
        "test.Test",
        ""
        + "package test;\n"
        + "import vn.tiki.viewholders.ViewHolder;\n"
        + "@ViewHolder(\n"
        + "    layout = 10,\n"
        + "    bindTo = String.class"
        + ")"
        + "public abstract class Test {"
        + "  void bind(String item) {}"
        + "}"
    );
    JavaFileObject typeFactory = JavaFileObjects.forSourceString(
        "viewholders/TypeFactoryImpl",
        ""
        + "package viewholders;\n"
        + "import java.lang.Class;\n"
        + "import java.lang.IllegalArgumentException;\n"
        + "import java.lang.Integer;\n"
        + "import java.lang.Object;\n"
        + "import java.lang.Override;\n"
        + "import java.lang.String;\n"
        + "import java.util.LinkedHashMap;\n"
        + "import vn.tiki.noadapter2.TypeFactory;\n"
        + "\n"
        + "final class TypeFactoryImpl implements TypeFactory {\n"
        + "\n"
        + "  private final LinkedHashMap<Class, Integer> typeMapping;\n"
        + "\n"
        + "  TypeFactoryIml() {\n"
        + "    typeMapping = new LinkedHashMap<Class, Integer>();\n"
        + "    typeMapping.put(String.class, 10);\n"
        + "  }\n"
        + "\n"
        + "  @Override\n"
        + "  public int typeOf(Object item) {\n"
        + "    final Class<?> itemClass = item.getClass();\n"
        + "    if (typeMapping.containsKey(itemClass)) {\n"
        + "      return typeMapping.get(itemClass);\n"
        + "    } else {\n"
        + "      throw new IllegalArgumentException(\"unknown \" + itemClass);\n"
        + "    }\n"
        + "  }\n"
        + "}\n"
    );

    assertAbout(javaSource())
        .that(source)
        .withCompilerOptions("-Xlint:-processing")
        .processedWith(new ViewHolderProcessor())
        .compilesWithoutError()
        .and()
        .generatesSources(typeFactory);
  }

  @Test
  public void testGenerateViewHolderDelegate() {
    JavaFileObject viewHolderDelegate = JavaFileObjects.forSourceString(
        "test/Test_ViewHolderDelegate",
        ""
        + "package test;\n"
        + "import android.view.View;\n"
        + "import java.lang.Object;\n"
        + "import java.lang.Override;\n"
        + "import java.lang.String;\n"
        + "import vn.tiki.viewholders.ViewHolderDelegate;\n"
        + "public final class Test_ViewHolderDelegate extends Test implements ViewHolderDelegate {\n"
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
        + "    return new int[] { 1, 2 };\n"
        + "  }"
        + "}"
    );

    assertAbout(javaSource()).that(source)
        .withCompilerOptions("-Xlint:-processing")
        .processedWith(new ViewHolderProcessor())
        .compilesWithoutError()
        .and()
        .generatesSources(viewHolderDelegate);
  }

  @Test
  public void testGenerateViewHolderDelegateExtends() {

    final JavaFileObject base = JavaFileObjects.forSourceString(
        "test.Base",
        ""
        + "package test;\n"
        + "import android.view.View;\n"
        + "public abstract class Base {"
        + "  void bindView(View view) {}"
        + "}"
    );
    final JavaFileObject source = JavaFileObjects.forSourceString(
        "test.Test",
        ""
        + "package test;\n"
        + "import android.view.View;\n"
        + "import vn.tiki.viewholders.ViewHolder;\n"
        + "@ViewHolder(\n"
        + "    layout = 10,\n"
        + "    onClick = { 1, 2 },\n"
        + "    bindTo = String.class\n"
        + ")"
        + "public abstract class Test extends Base {"
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
        + "import vn.tiki.viewholders.ViewHolderDelegate;\n"
        + "public final class Test_ViewHolderDelegate extends Test implements ViewHolderDelegate {\n"
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
        + "    return new int[] { 1, 2 };\n"
        + "  }"
        + "}"
    );

    assertAbout(javaSources())
        .that(Arrays.asList(base, source))
        .withCompilerOptions("-Xlint:-processing")
        .processedWith(new ViewHolderProcessor())
        .compilesWithoutError()
        .and()
        .generatesSources(viewHolderDelegate);
  }

  @Test
  public void testGenerateViewHolderDelegateNoBind() {

    final JavaFileObject source = JavaFileObjects.forSourceString(
        "test.Test",
        ""
        + "package test;\n"
        + "import android.view.View;\n"
        + "import vn.tiki.viewholders.ViewHolder;\n"
        + "@ViewHolder(\n"
        + "    layout = 10,\n"
        + "    bindTo = String.class\n"
        + ")"
        + "public abstract class Test {"
        + "}"
    );
    JavaFileObject viewHolderDelegate = JavaFileObjects.forSourceString(
        "test/Test_ViewHolderDelegate",
        ""
        + "package test;\n"
        + "\n"
        + "import android.view.View;\n"
        + "import java.lang.Object;\n"
        + "import java.lang.Override;\n"
        + "import vn.tiki.viewholders.ViewHolderDelegate;\n"
        + "\n"
        + "public final class Test_ViewHolderDelegate extends Test implements ViewHolderDelegate {\n"
        + "  @Override\n"
        + "  public void bind(Object item) {\n"
        + "  }\n"
        + "  @Override\n"
        + "  public void bindView(View view) {\n"
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


  @Test
  public void testGenerateViewHolderDelegateNoBindView() {

    final JavaFileObject source = JavaFileObjects.forSourceString(
        "test.Test",
        ""
        + "package test;\n"
        + "import android.view.View;\n"
        + "import vn.tiki.viewholders.ViewHolder;\n"
        + "@ViewHolder(\n"
        + "    layout = 10,\n"
        + "    bindTo = String.class\n"
        + ")"
        + "public abstract class Test {"
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
        + "import vn.tiki.viewholders.ViewHolderDelegate;\n"
        + "public final class Test_ViewHolderDelegate extends Test implements ViewHolderDelegate {\n"
        + "  @Override\n"
        + "  public void bind(Object item) {\n"
        + "    super.bind((String) item);\n"
        + "  }\n"
        + "  @Override\n"
        + "  public void bindView(View view) {\n"
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

  @Test
  public void testGenerateViewHolderDelegateR2() {

    JavaFileObject nonFinal = JavaFileObjects.forSourceString(
        "test.R",
        ""
        + "package test;\n"
        + "public final class R {\n"
        + "  public static final class layout {\n"
        + "    public static final int product = 0x7f040001;\n"
        + "  }\n"
        + "  public static final class id {\n"
        + "    public static final int name = 0x7f040004;\n"
        + "    public static final int image = 0x7f040005;\n"
        + "  }\n"
        + "}");

    JavaFileObject r2Final = JavaFileObjects.forSourceString(
        "test.R2",
        ""
        + "package test;\n"
        + "public final class R2 {\n"
        + "  public static final class layout {\n"
        + "    public static final int product = 0x7f040001;\n"
        + "  }\n"
        + "  public static final class id {\n"
        + "    public static final int name = 0x7f040004;\n"
        + "    public static final int image = 0x7f040005;\n"
        + "  }\n"
        + "}");
    final JavaFileObject source = JavaFileObjects.forSourceString(
        "test.Test",
        ""
        + "package test;\n"
        + "import android.view.View;\n"
        + "import vn.tiki.viewholders.ViewHolder;\n"
        + "@ViewHolder(\n"
        + "    layout = R2.layout.product,\n"
        + "    onClick = {R2.id.name, R2.id.image},\n"
        + "    bindTo = String.class\n"
        + ")"
        + "public abstract class Test {"
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
        + "import vn.tiki.viewholders.ViewHolderDelegate;\n"
        + "public final class Test_ViewHolderDelegate extends Test implements ViewHolderDelegate {\n"
        + "  @Override\n"
        + "  public void bind(Object item) {\n"
        + "    super.bind((String) item);\n"
        + "  }\n"
        + "  @Override\n"
        + "  public void bindView(View view) {\n"
        + "  }\n"
        + "  @Override\n"
        + "  public int layout() {\n"
        + "    return R.layout.product;\n"
        + "  }\n"
        + "  @Override\n"
        + "  public int[] onClick() {\n"
        + "    return new int[]{test.R.id.name, test.R.id.image};\n"
        + "  }"
        + "}"
    );

    assertAbout(javaSources())
        .that(Arrays.asList(nonFinal, r2Final, source))
        .withCompilerOptions("-Xlint:-processing")
        .processedWith(new ViewHolderProcessor())
        .compilesWithoutError()
        .and()
        .generatesSources(viewHolderDelegate);
  }

  @Test
  public void testGenerateViewHolderFactory() {
    JavaFileObject viewHolderFactory = JavaFileObjects.forSourceString(
        "viewholders/ViewHolderFactoryImpl",
        ""
        + "package viewholders;\n"
        + "\n"
        + "import android.view.ViewGroup;\n"
        + "import java.lang.IllegalArgumentException;\n"
        + "import java.lang.Override;\n"
        + "import test.Test_ViewHolderDelegate;\n"
        + "import vn.tiki.noadapter2.AbsViewHolder;\n"
        + "import vn.tiki.noadapter2.ViewHolderFactory;\n"
        + "import vn.tiki.viewholders.OnlyViewHolder;\n"
        + "import vn.tiki.viewholders.ViewHolderDelegate;\n"
        + "\n"
        + "final class ViewHolderFactoryImpl implements ViewHolderFactory {\n"
        + "\n"
        + "  @Override\n"
        + "  public AbsViewHolder viewHolderForType(ViewGroup parent, int type) {\n"
        + "    final ViewHolderDelegate viewHolderDelegate = makeViewHolderDelegate(type);\n"
        + "    return OnlyViewHolder.create(parent, viewHolderDelegate);\n"
        + "  }\n"
        + "\n"
        + "  private ViewHolderDelegate makeViewHolderDelegate(int type) {\n"
        + "    if (type == 10) {\n"
        + "      return new Test_ViewHolderDelegate();\n"
        + "    } else {\n"
        + "      throw new IllegalArgumentException(\"unknown type: \" + type);\n"
        + "    }\n"
        + "  }\n"
        + "}\n"
    );

    assertAbout(javaSource()).that(source)
        .withCompilerOptions("-Xlint:-processing")
        .processedWith(new ViewHolderProcessor())
        .compilesWithoutError()
        .and()
        .generatesSources(viewHolderFactory);
  }
}
