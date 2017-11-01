package vn.tiki.sample.noadapterviewholder;

import java.util.LinkedHashMap;
import java.util.Map;
import vn.tiki.noadapter2.TypeFactory;

class TypeFactoryIml implements TypeFactory {

  private final Map<Class<?>, Integer> typeMapping;

  TypeFactoryIml() {
    typeMapping = new LinkedHashMap<>();
    typeMapping.put(Loading.class, 10);
  }

  @Override
  public int typeOf(final Object item) {
    return typeMapping.get(item.getClass());
  }
}
