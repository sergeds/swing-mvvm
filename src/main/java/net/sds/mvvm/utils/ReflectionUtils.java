/*
 * Copyright 2020 Serge de Schaetzen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package net.sds.mvvm.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import net.sds.mvvm.bindings.BindingException;
import net.sds.mvvm.bindings.ValueSupplier;

public class ReflectionUtils {
  private ReflectionUtils() {
  }

  /**
   * Resolves the path specified and returns the object that the last path entry refers to.
   *
   * @param object The object to start from.
   * @param path The path.
   * @return The final object.
   */
  public static Object resolvePath(Object object, String[] path) {
    Object provider = object;
    for (int i = 0; i < path.length - 1; i++) {
      provider = getValue(provider, path[i]);
    }
    return provider;
  }

  /**
   * Returns the value of the specified path. If the path points to a method the method is invoked, else if it points to a field, the field value is
   * returned. If it doesn't point to a method or field, a BindingException is thrown.
   *
   * @param parent The parent to inspect.
   * @param path The path.
   * @return The object.
   */
  public static Object getValue(Object parent, String path) {
    Object result = null;
    while (result == null) {
      try {
        Optional<Method> method = getMethod(parent.getClass(), path);
        if (method.isPresent()) {
          return method.get().invoke(parent, path);
        }
        Optional<Field> field = getField(parent.getClass(), path);
        if (field.isPresent()) {
          Field f = field.get();
          if (!f.isAccessible()) {
            f.setAccessible(true);
          }
          return f.get(parent);
        }
      } catch (ReflectiveOperationException e) {
        throw new BindingException(String.format("Could not get the value of %s from class %s!", path, parent.getClass().getName()), e);
      }
    }
    throw new BindingException(String.format("Could not find a method of field named %s in class %S!", path, parent.getClass().getName()));
  }

  /**
   * Returns the Method with the specified name. The method must be public and can be inherited.
   *
   * @param source The class to inspect.
   * @param name The name of the method.
   * @param parameterTypes The method parameter types.
   * @return The method.
   */
  public static Optional<Method> getMethod(Class<?> source, String name, Class<?>... parameterTypes) {
    Class<?> cl = source;
    while (cl != null) {
      try {
        return Optional.of(cl.getMethod(name, parameterTypes));
      } catch (NoSuchMethodException e) {
        cl = cl.getSuperclass();
      }
    }
    return Optional.empty();
  }

  /**
   * Returns the field with the specified name. The field can be public, protected or private and can be inherited.
   *
   * @param source The class to inspect.
   * @param name The name of the field.
   * @return The field.
   */
  public static Optional<Field> getField(Class<?> source, String name) {
    Class<?> cl = source;
    while (cl != null) {
      try {
        return Optional.of(cl.getDeclaredField(name));
      } catch (NoSuchFieldException e) {
        cl = cl.getSuperclass();
      }
    }
    return Optional.empty();
  }

  /**
   * Returns all Fields that are annotated with the given annotation class. This returns all public, protected and private fields, declared and
   * inherited. Returns an empty collection if no field is found.
   *
   * @param source The class to inspect
   * @param annotationClass The annotation class.
   * @return The fields
   */
  public static Collection<Field> getAllAnnotatedFields(Class<?> source, Class<? extends Annotation> annotationClass) {
    Map<String, Field> fields = new HashMap<>();
    Class<?> cl = source;
    while (cl != null) {
      for (Field f : cl.getDeclaredFields()) {
        if (f.getAnnotationsByType(annotationClass).length > 0 && !fields.containsKey(f.getName())) {
          fields.put(f.getName(), f);
        }
      }
      cl = cl.getSuperclass();
    }
    return fields.values();
  }

}
