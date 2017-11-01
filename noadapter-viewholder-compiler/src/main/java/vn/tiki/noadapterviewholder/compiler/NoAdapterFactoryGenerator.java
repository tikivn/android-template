package vn.tiki.noadapterviewholder.compiler;

import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.PUBLIC;
import static javax.lang.model.element.Modifier.STATIC;
import static vn.tiki.noadapterviewholder.compiler.ViewHolderProcessor.onItemClickListener;
import static vn.tiki.noadapterviewholder.compiler.ViewHolderProcessor.onlyAdapter;
import static vn.tiki.noadapterviewholder.compiler.ViewHolderProcessor.typeDiffCallback;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

class NoAdapterFactoryGenerator {

  JavaFile brewJava() {
    return JavaFile.builder("noadapterviewholder", createType())
        .addFileComment("Generated code from NoAdapter ViewHolder. Do not modify!")
        .build();
  }

  private MethodSpec createConstructor() {
    return MethodSpec.constructorBuilder()
        .addModifiers(PRIVATE)
        .addStatement("throw new $T()", InstantiationError.class)
        .build();
  }

  private MethodSpec createMakeAdapterMethod() {
    final ClassName typeFactory = ClassName.get("noadapterviewholder", TypeFactoryGenerator.NAME);
    final ClassName viewHolderFactory = ClassName.get("noadapterviewholder", ViewHolderFactoryGenerator.NAME);
    return MethodSpec.methodBuilder("makeAdapter")
        .addModifiers(PUBLIC, STATIC)
        .returns(onlyAdapter)
        .addParameter(onItemClickListener, "onItemClick")
        .addCode(CodeBlock.builder()
            .add("return new OnlyAdapter.Builder()")
            .add(".typeFactory(new $T())", typeFactory)
            .add(".viewHolderFactory(new $T())", viewHolderFactory)
            .add(".diffCallback(new $T())", typeDiffCallback)
            .add(".onItemClickListener(onItemClick)")
            .add(".build();")
            .build())
        .build();
  }

  private TypeSpec createType() {
    TypeSpec.Builder result = TypeSpec.classBuilder("NoAdapterFactory")
        .addModifiers(PUBLIC, FINAL);
    result.addMethod(createConstructor());
    result.addMethod(createMakeAdapterMethod());
    return result.build();
  }

}
