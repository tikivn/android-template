package vn.tiki.daggerhelper;

import android.content.Context;
import android.content.ContextWrapper;
import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.Map;

public final class Daggers {
  private static Map<Object, Object> componentMap = new Hashtable<>();
  private static AppInjector appInjector;

  private Daggers() {
    throw new InstantiationError();
  }

  public static void installAppInjector(AppInjector appInjector) {
    Daggers.appInjector = appInjector;
  }

  public static void installActivityInjector(ActivityInjector activityInjector) {
    componentMap.put(
        activityInjector,
        makeSubComponent(
            appInjector.appComponent(),
            activityInjector.activityModule()));
  }

  public static void uninstallActivityInjector(ActivityInjector activityInjector) {
    componentMap.remove(activityInjector);
  }

  public static void inject(Context context, Object target) {
    final Injector injector = findInjector(context);
    inject(injector, target);
  }

  private static void inject(Injector injector, Object target) {
    if (injector instanceof AppInjector) {
      inject(appInjector.appComponent(), target);
    } else {
      final Object component = componentMap.get(injector);
      inject(component, target);
    }
  }

  private static Injector findInjector(Context context) {
    if (context instanceof Injector) {
      return ((Injector) context);
    }

    if (context instanceof ContextWrapper) {
      return findInjector(((ContextWrapper) context).getBaseContext());
    } else {
      throw new IllegalArgumentException("context or baseContext must be instance of "
          + Injector.class.getName());
    }
  }

  /**
   * Find plus(module) on component then involve component.plus(module) to return sub-module.
   *
   * @param component the dagger component
   * @param module the dagger module
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

  /**
   * Find inject(target) method on component then involve component.inject(target)
   *
   * @param component the dagger component
   * @param target the target object
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
   * Find method with given name and parameter on the target object.
   *
   * @param target object
   * @param name method name
   * @param parameter method parameter
   * @return Method or throw Exception.
   */
  private static Method findMethod(Class<?> target, String name, Object parameter) {
    try {
      return target.getDeclaredMethod(name, parameter.getClass());
    } catch (NoSuchMethodException e) {
      throw new IllegalArgumentException(
          "Can not find method "
              + target.getName()
              + "."
              + name
              + "("
              + parameter
              + ")",
          e);
    }
  }
}