package vn.tiki.noadapterviewholder;

import static com.google.common.truth.Truth.assertAbout;
import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;

import com.google.testing.compile.JavaFileObjects;
import javax.tools.JavaFileObject;
import org.junit.*;
import vn.tiki.noadapterviewholder.compiler.ViewHolderProcessor;

public class ViewHolderProcessorTest {

  private JavaFileObject source;

  @Before
  public void setUp() throws Exception {
    source = JavaFileObjects.forSourceString(
        "test.Test",
        ""
        + "package test;\n"
        + "import android.view.View;\n"
        + "import vn.tiki.noadapterviewholder.ViewHolder;\n"
        + "@ViewHolder(\n"
        + "    layout = 10,\n"
        + "    onClick = {1, 2}\n"
        + ")"
        + "public abstract class Test {"
        + "  void bindView(final View view) {}\n"
        + "  void bind(final String item) {}"
        + "}"
    );
  }

  @Test
  public void testGenerate() {
    JavaFileObject viewHolderDelegate = JavaFileObjects.forSourceString(
        "test/Test_ViewHolderDelegate",
        ""
        + "package test;\n"
        + "import android.view.View;\n"
        + "import java.lang.Object;\n"
        + "import java.lang.Override;\n"
        + "import java.lang.String;\n"
        + "import vn.tiki.noadapterviewholder.ViewHolderDelegate;\n"
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

    JavaFileObject typeFactory = JavaFileObjects.forSourceString(
        "noadapterviewholder/TypeFactoryImpl",
        ""
        + "package noadapterviewholder;\n"
        + "import java.lang.Class;\n"
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
        + "    return typeMapping.get(item.getClass());\n"
        + "  }\n"
        + "}\n"
    );

    JavaFileObject viewHolderFactory = JavaFileObjects.forSourceString(
        "noadapterviewholder/ViewHolderFactoryImpl",
        ""
        + "package noadapterviewholder;\n"
        + "\n"
        + "import android.view.ViewGroup;\n"
        + "import java.lang.IllegalArgumentException;\n"
        + "import java.lang.Override;\n"
        + "import test.Test_ViewHolderDelegate;\n"
        + "import vn.tiki.noadapter2.AbsViewHolder;\n"
        + "import vn.tiki.noadapter2.ViewHolderFactory;\n"
        + "import vn.tiki.noadapterviewholder.LastViewHolder;\n"
        + "import vn.tiki.noadapterviewholder.ViewHolderDelegate;\n"
        + "\n"
        + "final class ViewHolderFactoryImpl implements ViewHolderFactory {\n"
        + "\n"
        + "  @Override\n"
        + "  public AbsViewHolder viewHolderForType(ViewGroup parent, int type) {\n"
        + "    final ViewHolderDelegate viewHolderDelegate = makeViewHolderDelegate(type);\n"
        + "    return LastViewHolder.create(parent, viewHolderDelegate);\n"
        + "  }\n"
        + "\n"
        + "  private ViewHolderDelegate makeViewHolderDelegate(int type) {\n"
        + "    switch (type) {\n"
        + "      case 10:\n"
        + "        return new Test_ViewHolderDelegate();\n"
        + "      default:\n"
        + "        throw new IllegalArgumentException(\"unknown type: \" + type);\n"
        + "    }\n"
        + "  }\n"
        + "}\n"
    );

    JavaFileObject noAdapterFactory = JavaFileObjects.forSourceString(
        "noadapterviewholder/NoAdapterFactory",
        ""
        + "package noadapterviewholder;\n"
        + "\n"
        + "import java.lang.InstantiationError;\n"
        + "import vn.tiki.noadapter2.OnItemClickListener;\n"
        + "import vn.tiki.noadapter2.OnlyAdapter;\n"
        + "import vn.tiki.noadapterviewholder.TypeDiffCallback;\n"
        + "\n"
        + "public final class NoAdapterFactory {\n"
        + "\n"
        + "  private NoAdapterFactory() {\n"
        + "    throw new InstantiationError();\n"
        + "  }\n"
        + "\n"
        + "  public static OnlyAdapter makeAdapter(OnItemClickListener onItemClick) {\n"
        + "    return new OnlyAdapter.Builder()\n"
        + "        .typeFactory(new TypeFactoryImpl())\n"
        + "        .viewHolderFactory(new ViewHolderFactoryImpl())\n"
        + "        .diffCallback(new TypeDiffCallback())\n"
        + "        .onItemClickListener(onItemClick)\n"
        + "        .build();\n"
        + "  }\n"
        + "}\n"
    );

    assertAbout(javaSource()).that(source)
        .withCompilerOptions("-Xlint:-processing")
        .processedWith(new ViewHolderProcessor())
        .compilesWithoutError()
        .and()
        .generatesSources(typeFactory, viewHolderFactory, viewHolderDelegate, noAdapterFactory);
  }
}
