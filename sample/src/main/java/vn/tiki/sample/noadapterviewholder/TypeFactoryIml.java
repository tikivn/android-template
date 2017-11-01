package vn.tiki.sample.noadapterviewholder;

import java.util.LinkedHashMap;
import vn.tiki.noadapter2.TypeFactory;

class TypeFactoryIml implements TypeFactory {

  private final LinkedHashMap<Class, Integer> typeMapping;

  TypeFactoryIml() {
    typeMapping = new LinkedHashMap<>();
    typeMapping.put(String.class, 10);
  }

  @Override
  public int typeOf(Object item) {
    return typeMapping.get(item.getClass());
  }
}
