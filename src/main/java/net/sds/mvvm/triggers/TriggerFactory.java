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

package net.sds.mvvm.triggers;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import net.sds.mvvm.bindings.BindingException;
import net.sds.mvvm.bindings.Paths;
import net.sds.mvvm.collections.ObservableCollection;
import net.sds.mvvm.properties.Property;

public class TriggerFactory {
  private static final List<TriggerRegistrator> registrators = new ArrayList<>();

  private TriggerFactory() {
  }

  public static Trigger create(Object source, String property) throws BindingException {
    for (TriggerRegistrator registrator : registrators) {
      if (registrator.predicate.test(source, property)) {
        return registrator.factory.apply(source, property);
      }
    }

    throw new BindingException(String.format("Could not find a Trigger for class %s and property %s!", source.getClass(), property));
  }

  static {
    registerTriggerFactory((o, p) -> o instanceof Document && p.equals(Paths.TEXT)
        , (o, p) -> new DocumentTextChangedTrigger(Document.class.cast(o)));

    registerTriggerFactory((o, p) -> o instanceof JTextComponent && p.equals(Paths.TEXT)
        , (o, p) -> new DocumentTextChangedTrigger(JTextComponent.class.cast(o).getDocument()));

    registerTriggerFactory((o, p) -> o instanceof ObservableCollection
        , (o, p) -> new ObservableCollectionTrigger(ObservableCollection.class.cast(o)));

    registerTriggerFactory((o, p) -> o instanceof Property
        , (o, p) -> new PropertyTrigger(Property.class.cast(o)));

    registerTriggerFactory((o, p) -> o instanceof JList && (p.equals(Paths.SELECTED_INDEX) || p.equals(Paths.SELECTED_INDICES))
        , (o, p) -> new ListSelectionTrigger(JList.class.cast(o)));

    registerTriggerFactory((o, p) -> o instanceof JTable && (p.equals(Paths.SELECTED_ROW) || p.equals(Paths.SELECTED_ROWS))
        , (o, p) -> new ListSelectionTrigger(JTable.class.cast(o)));

    registerTriggerFactory((o, p) -> o instanceof AbstractButton && p.equals(Paths.SELECTED)
        , (o, p) -> new ItemSelectedTrigger(AbstractButton.class.cast(o)));

    registerTriggerFactory((o, p) -> o instanceof JComponent
        , (o, p) -> new ComponentChangedTrigger(JComponent.class.cast(o), p));
  }

  public static void registerTriggerFactory(BiPredicate<Object, String> predicate, BiFunction<Object, String, Trigger> factory) {
    registerTriggerFactory(predicate, factory, -1);
  }

  public static void registerTriggerFactory(BiPredicate<Object, String> predicate, BiFunction<Object, String, Trigger> factory, int index) {
    TriggerRegistrator registrator = new TriggerRegistrator(predicate, factory);
    if (index < 0) {
      registrators.add(registrator);
    } else {
      registrators.add(index, registrator);
    }
  }

  private static class TriggerRegistrator {
    final private BiPredicate<Object, String> predicate;
    final private BiFunction<Object, String, Trigger> factory;

    private TriggerRegistrator(BiPredicate<Object, String> predicate, BiFunction<Object, String, Trigger> factory) {
      this.predicate = predicate;
      this.factory = factory;
    }
  }
}
