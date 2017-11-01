package vn.tiki.noadapterviewholder.compiler;

import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PUBLIC;
import static vn.tiki.noadapterviewholder.compiler.ViewHolderProcessor.VIEW;
import static vn.tiki.noadapterviewholder.compiler.ViewHolderProcessor.VIEW_HOLDER_DELEGATE;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

class ViewHolderDelegateGenerator {

  private final ViewHolderInfo viewHolderInfo;

  ViewHolderDelegateGenerator(final ViewHolderInfo viewHolderInfo) {
    this.viewHolderInfo = viewHolderInfo;
  }

  JavaFile brewJava() {
    return JavaFile.builder(viewHolderInfo.getTargetType().packageName(), createType())
        .addFileComment("Generated code from NoAdapter ViewHolder. Do not modify!")
        .build();
  }

  private MethodSpec createBindMethod() {
    return MethodSpec.methodBuilder("bind")
        .addAnnotation(Override.class)
        .addModifiers(PUBLIC)
        .addParameter(Object.class, "item")
        .addStatement("super.bind(($T) item)", viewHolderInfo.getItemType())
        .build();
  }

  private MethodSpec createBindViewMethod() {
    return MethodSpec.methodBuilder("bindView")
        .addAnnotation(Override.class)
        .addModifiers(PUBLIC)
        .addParameter(VIEW, "view")
        .addStatement("super.bindView(view)")
        .build();
  }

  private MethodSpec createLayoutMethod() {
    return MethodSpec.methodBuilder("layout")
        .addAnnotation(Override.class)
        .addModifiers(PUBLIC)
        .returns(TypeName.INT)
        .addStatement("return $L", viewHolderInfo.getLayout())
        .build();
  }

  private MethodSpec createOnClickMethod() {
    final int[] onClick = viewHolderInfo.getOnClick();
    final StringBuilder idsBuilder = new StringBuilder("");
    if (onClick.length > 0) {
      idsBuilder.append("new int[] {");
      for (int id : onClick) {
        idsBuilder.append(id).append(",");
      }
      idsBuilder.setLength(idsBuilder.length() - 1);
      idsBuilder.append("}");
    } else {
      idsBuilder.append("new int[0]");
    }
    return MethodSpec.methodBuilder("onClick")
        .addAnnotation(Override.class)
        .addModifiers(PUBLIC)
        .returns(TypeName.get(int[].class))
        .addStatement("return $L", idsBuilder.toString())
        .build();
  }

  private TypeSpec createType() {
    TypeSpec.Builder result = TypeSpec.classBuilder(viewHolderInfo.getTargetType().simpleName() + "_ViewHolderDelegate")
        .addModifiers(PUBLIC, FINAL)
        .superclass(viewHolderInfo.getTargetType())
        .addSuperinterface(VIEW_HOLDER_DELEGATE);

    result.addMethod(createBindMethod());
    result.addMethod(createBindViewMethod());
    result.addMethod(createLayoutMethod());
    result.addMethod(createOnClickMethod());
    return result.build();
  }
}
