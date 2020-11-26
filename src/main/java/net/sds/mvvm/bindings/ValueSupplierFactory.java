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

package net.sds.mvvm.bindings;

import static net.sds.mvvm.utils.ReflectionUtils.getField;
import static net.sds.mvvm.utils.ReflectionUtils.getMethod;
import static net.sds.mvvm.utils.ReflectionUtils.resolvePath;

import java.awt.Component;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import javax.swing.AbstractButton;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.text.JTextComponent;
import net.sds.mvvm.properties.Property;

public class ValueSupplierFactory {
  private static List<SupplierRegistrator> registrators = new ArrayList<>();

  private ValueSupplierFactory() {
  }

  public static <T> ValueSupplier<T> create(Object source, String path) throws BindingException {
    for (SupplierRegistrator registrator : registrators) {
      if (registrator.predicate.test(source, path)) {
        return registrator.factory.apply(source, path);
      }
    }
    return getSupplier(source, path);
  }

  /**
   * Returns a ValueSupplier for the path specified. The path can point to nested fields/methods of the object given using a '.' delimited path.
   * Methods referenced in the path should have 0 parameters. If no supplier could be found a BindingException is thrown.
   *
   * @param object The object to parse.
   * @param path The path to use.
   * @return The supplier.
   */
  private static ValueSupplier getSupplier(Object object, String path) throws BindingException {

    Optional<ValueSupplier> supplier = getMethodSupplier(object, path);
    if (supplier.isPresent()) {
      return supplier.get();
    }

    supplier = getFieldSupplier(object, path);
    if (supplier.isPresent()) {
      return supplier.get();
    }

    throw new BindingException(String.format("Could not find a ValueProvider for path %s in class %s", path, object.getClass().getName()));
  }


  /**
   * Returns a ValueSupplier for the method with the given name and owner, or Optional.empty() if the method is not found.
   *
   * @param owner The owner.
   * @param name The method name.
   * @return The ValueProvider
   */
  private static Optional<ValueSupplier> getMethodSupplier(final Object owner, String name) {
    final Optional<Method> method = getMethod(owner.getClass(), name);
    if (method.isPresent()) {
      return Optional.of(() -> {
        try {
          return method.get().invoke(owner);
        } catch (ReflectiveOperationException e) {
          throw new BindingValueException(String.format("Could not get the value of %s from %s!", method.get().getName(), owner));
        }
      });
    }
    return Optional.empty();
  }

  /**
   * Returns a ValueSupplier for the method with the given name and owner, or Optional.empty() if the method is not found.
   *
   * @param owner The owner.
   * @param name The method name.
   * @return The ValueProvider
   */
  private static Optional<ValueSupplier> getFieldSupplier(final Object owner, String name) {
    final Optional<Field> field = getField(owner.getClass(), name);
    if (field.isPresent()) {
      final Field f = field.get();
      if (!f.isAccessible()) {
        f.setAccessible(true);
      }
      return Optional.of(() -> {
        try {
          return f.get(owner);
        } catch (ReflectiveOperationException e) {
          throw new BindingValueException(String.format("Could not get the value of %s from %s!", f.getName(), owner));
        }
      });
    }
    return Optional.empty();
  }

  static {
    registrators.add(new SupplierRegistrator((o, s) -> o instanceof Property && s.equals(Paths.VALUE)
        , (o, s) -> () -> Property.class.cast(o).get()));

    registrators.add(new SupplierRegistrator((o, s) -> o instanceof JTextComponent && s.equals(Paths.TEXT)
        , (o, s) -> () -> JTextComponent.class.cast(o).getText()));

    registrators.add(new SupplierRegistrator((o, s) -> o instanceof JLabel && s.equals(Paths.TEXT)
        , (o, s) -> () -> JLabel.class.cast(o).getText()));

    registrators.add(new SupplierRegistrator((o, s) -> o instanceof JTextComponent && s.equals(Paths.EDITABLE)
        , (o, s) -> () -> JTextComponent.class.cast(o).isEditable()));

    registrators.add(new SupplierRegistrator((o, s) -> o instanceof Component && s.equals(Paths.ENABLED)
        , (o, s) -> () -> Component.class.cast(o).isEnabled()));

    registrators.add(new SupplierRegistrator((o, s) -> o instanceof Component && s.equals(Paths.VISIBLE)
        , (o, s) -> () -> Component.class.cast(o).isVisible()));

    registrators.add(new SupplierRegistrator((o, s) -> o instanceof Component && s.equals(Paths.FOREGROUND)
        , (o, s) -> () -> Component.class.cast(o).getForeground()));

    registrators.add(new SupplierRegistrator((o, s) -> o instanceof Component && s.equals(Paths.BACKGROUND)
        , (o, s) -> () -> Component.class.cast(o).getBackground()));

    registrators.add(new SupplierRegistrator((o, s) -> o instanceof Component && s.equals(Paths.FONT)
        , (o, s) -> () -> Component.class.cast(o).getFont()));

    registrators.add(new SupplierRegistrator((o, s) -> o instanceof AbstractButton && s.equals(Paths.SELECTED)
        , (o, s) -> () -> AbstractButton.class.cast(o).isSelected()));

    registrators.add(new SupplierRegistrator((o, s) -> o instanceof JComboBox && s.equals(Paths.SELECTED_ITEM)
        , (o, s) -> () -> JComboBox.class.cast(o).getSelectedItem()));

    registrators.add(new SupplierRegistrator((o, s) -> o instanceof JComboBox && s.equals(Paths.MODEL)
        , (o, s) -> () -> JComboBox.class.cast(o).getModel()));

    registrators.add(new SupplierRegistrator((o, s) -> o instanceof JList && s.equals(Paths.SELECTED_INDEX)
        , (o, s) -> () -> JList.class.cast(o).getSelectedIndex()));

    registrators.add(new SupplierRegistrator((o, s) -> o instanceof JList && s.equals(Paths.SELECTED_INDICES)
        , (o, s) -> () -> JList.class.cast(o).getSelectedIndices()));

    registrators.add(new SupplierRegistrator((o, s) -> o instanceof JList && s.equals(Paths.MODEL)
        , (o, s) -> () -> JList.class.cast(o).getModel()));

    registrators.add(new SupplierRegistrator((o, s) -> o instanceof JTable && s.equals(Paths.MODEL)
        , (o, s) -> () -> JTable.class.cast(o).getModel()));

    registrators.add(new SupplierRegistrator((o, s) -> o instanceof JTable && s.equals(Paths.SELECTED_ROW)
        , (o, s) -> () -> {
      int[] rows = getSelectedModelRows(JTable.class.cast(o));
      if (rows.length > 0) {
        return rows[0];
      }
      else {
        return -1;
      }
    }));

    registrators.add(new SupplierRegistrator((o, s) -> o instanceof JTable && s.equals(Paths.SELECTED_ROWS)
        , (o, s) -> () -> getSelectedModelRows(JTable.class.cast(o))));
  }

  public static int[] getSelectedModelRows(JTable table) {
    int[] rows = table.getSelectedRows();
    int[] modelRows = new int[rows.length];
    for (int i = 0; i < rows.length; i++) {
      modelRows[i] = table.convertRowIndexToModel(rows[i]);
    }
    return modelRows;
  }

  public static void registerSupplierFactory(BiPredicate<Object, String> predicate, BiFunction<Object, String, ValueSupplier> factory) {
    registerSupplierFactory(predicate, factory, -1);
  }

  public static void registerSupplierFactory(BiPredicate<Object, String> predicate, BiFunction<Object, String, ValueSupplier> factory, int index) {
    SupplierRegistrator registrator = new SupplierRegistrator(predicate, factory);
    if (index < 0) {
      registrators.add(registrator);
    } else {
      registrators.add(index, registrator);
    }
  }

  private static class SupplierRegistrator {
    final private BiPredicate<Object, String> predicate;
    final private BiFunction<Object, String, ValueSupplier> factory;

    private SupplierRegistrator(BiPredicate<Object, String> predicate, BiFunction<Object, String, ValueSupplier> factory) {
      this.predicate = predicate;
      this.factory = factory;
    }
  }
}
