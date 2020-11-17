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

import java.lang.reflect.Array;
import net.sds.mvvm.utils.DefaultNotifyPropertyChanged;

/**
 * Generic implementation of Property.
 */
public class GenericProperty<T> extends DefaultNotifyPropertyChanged implements Property<T> {
  private T value;
  private String name;
  private Class<T> propertyType;

  GenericProperty(String name, Object owner, Class<T> propertyType, T initialValue) {
    super(owner);
    this.name = name;
    this.propertyType = propertyType;
    if (initialValue == null) {
      initialValue = (T) Array.get(Array.newInstance(this.propertyType, 1), 0);
    }
    this.value = initialValue;
  }

  public Class<T> getPropertyType() {
    return propertyType;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public T get() {
    return value;
  }

  @Override
  public void set(T t) {
    T old = get();
    value = t;
    T newValue = get();
    firePropertyChange(name, old, newValue);
  }

  @Override
  public boolean isNull() {
    return value == null;
  }

  @Override
  public boolean isNotNull() {
    return !isNull();
  }
}
