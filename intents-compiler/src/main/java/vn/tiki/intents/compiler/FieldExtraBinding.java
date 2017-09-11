package vn.tiki.intents.compiler;

import com.squareup.javapoet.TypeName;

class FieldExtraBinding {
  private final String name;
  private final TypeName type;

  FieldExtraBinding(String name, TypeName type) {
    this.name = name;
    this.type = type;
  }

  String getName() {
    return name;
  }

  public TypeName getType() {
    return type;
  }
}
