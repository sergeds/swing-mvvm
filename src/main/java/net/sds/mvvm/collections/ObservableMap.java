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

package net.sds.mvvm.collections;

import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;
import net.sds.mvvm.NotifyPropertyChanged;
import net.sds.mvvm.utils.DefaultNotifyPropertyChanged;

/**
 * Observable hashmap. Will fire property change events when an element is added, removed or updated.
 * The property name in the change events, is the key of the added/removed/updated element. If the action causes
 * the size of the map to change, an additional event, with property name 'size', is fired.
 * @param <T>
 */
public class ObservableMap<T> extends HashMap<String, T> implements NotifyPropertyChanged {
  private final DefaultNotifyPropertyChanged notify = new DefaultNotifyPropertyChanged(this);
  private int size = 0;

  @Override
  public T put(String key, T value) {
    T oldValue = super.put(key, value);

    firePropertyChanged(key, oldValue, value);
    if (size != size()) {
      firePropertyChanged("size", size, size());
      size = size();
    }

    return oldValue;
  }

  @Override
  public void putAll(Map<? extends String, ? extends T> m) {
    for (Entry<? extends String, ? extends T> e : m.entrySet())
      put(e.getKey(), e.getValue());
  }

  @Override
  public T putIfAbsent(String key, T value) {
    T result = super.putIfAbsent(key, value);
    if (result == null) {
      firePropertyChanged(key, result, value);
      if (size != size()) {
        firePropertyChanged("size", size, size());
        size = size();
      }
    }
    return result;
  }

  @Override
  public T remove(Object key) {
    T result = super.remove(key);
    if (size != size()) {
      firePropertyChanged("size", size, size());
      size = size();
    }
    return result;
  }

  @Override
  public boolean remove(Object key, Object value) {
    boolean result = super.remove(key, value);
    if (result) {
      if (size != size()) {
        firePropertyChanged("size", size, size());
        size = size();
      }
    }
    return result;
  }

  @Override
  public void clear() {
    super.clear();
    if (size != size()) {
      firePropertyChanged("size", size, size());
      size = size();
    }
  }

  // ---------------------------------------------------------------------------
  // region Listener notification:
  // ---------------------------------------------------------------------------
  private void firePropertyChanged(String key, Object oldValue, Object newValue) {
    notify.firePropertyChange(key, oldValue, newValue);
  }
  // endregion

  // ---------------------------------------------------------------------------
  // region NotifyPropertyChanged implementation:
  // ---------------------------------------------------------------------------

  @Override
  public void addPropertyChangeListener(PropertyChangeListener listener) {
    notify.addPropertyChangeListener(listener);
  }

  @Override
  public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
    notify.addPropertyChangeListener(propertyName, listener);
  }

  @Override
  public void removePropertyChangeListener(PropertyChangeListener listener) {
    notify.removePropertyChangeListener(listener);
  }

  @Override
  public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
    notify.removePropertyChangeListener(propertyName, listener);
  }

  @Override
  public void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
    notify.firePropertyChange(propertyName, oldValue, newValue);
  }
  // endregion
}
