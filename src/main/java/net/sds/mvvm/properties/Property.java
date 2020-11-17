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

package net.sds.mvvm.properties;

import net.sds.mvvm.NotifyPropertyChanged;

/**
 * Defines a generic property that contains a value, and fires change events whenever the value changes.
 * @param <T>
 */
public interface Property<T> extends NotifyPropertyChanged {
  /**
   * Returns the name of the property. When the property value changes, this will be the property name
   * in the event.
   * @return The name.
   */
  String getName();

  /**
   * The (possibly null) value.
   * @return The value.
   */
  T get();

  /**
   * Sets the new value of the property. If the value is different from the previous one, a property change event
   * is fired.
   * @param t The new value.
   */
  void set(T t);

  /**
   * Returns true if the property has no value.
   * @return True if null.
   */
  boolean isNull();

  /**
   * Returns true if the property has a non value associated.
   * @return True of not null.
   */
  boolean isNotNull();
}
