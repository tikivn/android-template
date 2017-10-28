package vn.tiki.daggers;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.ContextWrapper;
import android.view.View;
import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.Map;

public final class Daggers {

  private static Map<Injector, Object> componentMap = new Hashtable<>();
  private static AppInjector appInjector;

  public static void inject(Activity activity) {
    inject(activity, activity);
  }

  public static void inject(Fragment fragment) {
    inject(fragment, fragment.getActivity());
  }

  public static void inject(View view) {
    inject(view, view.getContext());
  }

  public static void inject(Object target, Context context) {
    final Injector injector = findInjector(context);
    inject(target, injector);
  }

  public static void installActivityInjector(ActivityInjector activityInjector) {
    if (componentMap.containsKey(activityInjector)) {
      return;
    }
    componentMap.put(
        activityInjector,
        makeSubComponent(
            appInjector.appComponent(),
            activityInjector.activityModule()));
  }

  public static void installAppInjector(AppInjector appInjector) {
    Daggers.appInjector = appInjector;
  }

  public static void uninstallActivityInjector(ActivityInjector activityInjector) {
    componentMap.remove(activityInjector);
  }

  private Daggers() {
    throw new InstantiationError();
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
   * Find method with given name and parameter on the target object.
   *
   * @param target    object
   * @param name      method name
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

  private static void inject(Object target, Injector injector) {
    if (injector instanceof AppInjector) {
      inject(appInjector.appComponent(), target);
    } else {
      final Object component = componentMap.get(injector);
      inject(component, target);
    }
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