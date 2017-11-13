package vn.tiki.daggers;

import android.support.annotation.VisibleForTesting;
import java.lang.reflect.Method;
import java.util.Stack;

public final class Daggers {

  private static Stack<Object> componentStack = new Stack<>();
  @VisibleForTesting static Object appComponent;

  public static void clear() {
    appComponent = null;
    componentStack.clear();
  }

  public static void closeActivityScope() {
    if (componentStack.empty()) {
      return;
    }
    componentStack.pop();
  }

  public static void inject(final Object target) {
    inject(getTopScope(), target);
  }

  public static void injectAppDependencies(final Object target) {
    inject(appComponent, target);
  }

  public static void openActivityScope(final Object activityModule) {
    final Object activityComponent = makeSubComponent(appComponent, activityModule);
    componentStack.push(activityComponent);
  }

  public static void openAppScope(final Object appComponent) {
    Daggers.appComponent = appComponent;
  }

  private Daggers() {
    throw new InstantiationError();
  }

  @VisibleForTesting
  static Object getTopScope() {
    if (componentStack.empty()) {
      throw new IllegalStateException("there is no opened scope");
    }
    return componentStack.peek();
  }

  /**
   * Find method with given name and parameter on the target object.
   *
   * @param target    object
   * @param name      method name
   * @param parameter method parameter
   * @return Method or throw Exception.
   */
  private static Method findMethod(Class<?> target, String name, Object parameter) {
    final Method[] methods = target.getDeclaredMethods();
    for (Method method : methods) {
      if (method.getName().equals(name)) {
        final Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes.length == 1 && parameterTypes[0].isAssignableFrom(parameter.getClass())) {
          return method;
        }
      }
    }
    throw new IllegalArgumentException(
        "Can not find method "
        + target.getName()
        + "."
        + name
        + "("
        + parameter
        + ")");
  }

  /**
   * Find inject(target) method on component then involve component.inject(target)
   *
   * @param component the dagger component
   * @param target    the target object
   */
  private static void inject(Object component, Object target) {
    final Class<?> componentClass = component.getClass();
    final Class<?> componentInterfaceClass;
    if (componentClass.isInterface()) {
      componentInterfaceClass = componentClass;
    } else {
      componentInterfaceClass = componentClass.getInterfaces()[0];
    }
    final Method injectMethod = findMethod(componentInterfaceClass, "inject", target);
    try {
      injectMethod.invoke(component, target);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Find plus(module) on component then involve component.plus(module) to return sub-module.
   *
   * @param component the dagger component
   * @param module    the dagger module
   * @return sub-component from component.plus(module)
   */
  private static Object makeSubComponent(Object component, Object module) {
    final Class<?> componentClass = component.getClass();
    final Method plusMethod = findMethod(componentClass, "plus", module);
    try {
      return plusMethod.invoke(component, module);
    } catch (Exception e) {
      throw new IllegalArgumentException("can not create component with " + module.getClass()
          .getName());
    }
  }
}