package vn.tiki.viewholders.compiler;

import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PUBLIC;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import java.util.List;

class TypeFactoryGenerator {

  static final String NAME = "TypeFactoryImpl";
  private final List<ViewHolderInfo> viewHolderInfoList;

  TypeFactoryGenerator(final List<ViewHolderInfo> viewHolderInfoList) {
    this.viewHolderInfoList = viewHolderInfoList;
  }

  JavaFile brewJava() {
    return JavaFile.builder("viewholders", createType())
        .addFileComment("Generated code from NoAdapter ViewHolder. Do not modify!")
        .build();
  }

  private TypeSpec createType() {
    final ClassName typeFactoryType = ClassName.get("vn.tiki.noadapter2", "TypeFactory");
    TypeSpec.Builder result = TypeSpec.classBuilder(NAME)
        .addModifiers(FINAL)
        .addSuperinterface(typeFactoryType);

    result.addMethod(createTypeOfMethod());
    return result.build();
  }

  private MethodSpec createTypeOfMethod() {

    final CodeBlock.Builder ifStatementBuilder = CodeBlock.builder();

    for (int i = 0; i < viewHolderInfoList.size(); i++) {
      ViewHolderInfo viewHolderInfo = viewHolderInfoList.get(i);
      if (i == 0) {
        ifStatementBuilder.beginControlFlow("if (item instanceof $T)", viewHolderInfo.getModelType());
      } else {
        ifStatementBuilder.beginControlFlow("else if (item instanceof $T)", viewHolderInfo.getModelType());
      }
      ifStatementBuilder.addStatement("return $L", viewHolderInfo.getLayout().code)
          .endControlFlow();
    }

    ifStatementBuilder
        .beginControlFlow("else")
        .addStatement("throw new $T(\"unknown \" + item.getClass())", IllegalArgumentException.class)
        .endControlFlow();

    return MethodSpec.methodBuilder("typeOf")
        .addAnnotation(Override.class)
        .addModifiers(PUBLIC)
        .returns(TypeName.INT)
        .addParameter(Object.class, "item")
        .addCode(ifStatementBuilder.build())
        .build();
  }
}
