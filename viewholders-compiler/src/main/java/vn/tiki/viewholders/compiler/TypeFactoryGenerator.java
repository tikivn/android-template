package vn.tiki.viewholders.compiler;

import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.PUBLIC;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.MethodSpec.Builder;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import java.util.List;

class TypeFactoryGenerator {

  public static final String NAME = "TypeFactoryImpl";
  private final TypeName mapType;
  private final List<ViewHolderInfo> viewHolderInfoList;

  TypeFactoryGenerator(final List<ViewHolderInfo> viewHolderInfoList) {
    this.viewHolderInfoList = viewHolderInfoList;
    ClassName map = ClassName.get("java.util", "LinkedHashMap");
    mapType = ParameterizedTypeName.get(map, ClassName.get(Class.class), ClassName.get(Integer.class));
  }

  JavaFile brewJava() {
    return JavaFile.builder("viewholders", createType())
        .addFileComment("Generated code from NoAdapter ViewHolder. Do not modify!")
        .build();
  }

  private MethodSpec createConstructor() {
    final Builder result = MethodSpec.constructorBuilder()
        .addStatement("typeMapping = new $T()", mapType);

    for (ViewHolderInfo viewHolderInfo : viewHolderInfoList) {
      result.addStatement("typeMapping.put($T.class, $L)", viewHolderInfo.getItemType(), viewHolderInfo.getLayout());
    }
    return result.build();
  }

  private TypeSpec createType() {
    final ClassName typeFactoryType = ClassName.get("vn.tiki.noadapter2", "TypeFactory");
    TypeSpec.Builder result = TypeSpec.classBuilder(NAME)
        .addModifiers(FINAL)
        .addSuperinterface(typeFactoryType)
        .addField(mapType, "typeMapping", PRIVATE, FINAL);

    result.addMethod(createConstructor());
    result.addMethod(createTypeOfMethod());
    return result.build();
  }

  private MethodSpec createTypeOfMethod() {

    return MethodSpec.methodBuilder("typeOf")
        .addAnnotation(Override.class)
        .addModifiers(PUBLIC)
        .returns(TypeName.INT)
        .addParameter(Object.class, "item")
        .addCode(
            CodeBlock.builder()
                .addStatement("final Class<?> itemClass = item.getClass()")
                .add("if (typeMapping.containsKey(itemClass)) {\n")
                .addStatement("return typeMapping.get(itemClass)")
                .add("} else {\n")
                .addStatement("throw new $T(\"unknown \" + itemClass)", IllegalArgumentException.class)
                .add("}")
                .build())
        .build();
  }
}
