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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Objects;
import net.sds.mvvm.NotifyPropertyChanged;

/**
 * Abstract implementation of NotifyPropertyChanged that does the management of
 * the registeres PropertyChangedListeners.
 */
public abstract class AbstractNotifyPropertyChanged implements NotifyPropertyChanged {
  private PropertyChangeSupport support;

  protected AbstractNotifyPropertyChanged() {
    support = new PropertyChangeSupport(this);
  }

  public AbstractNotifyPropertyChanged(Object source) {
    support = new PropertyChangeSupport(source);
  }

  /**
   * Fires a PropertyChange event to all registered listeners. The event is only
   * fired if the values are different: one is null and other isn't or they are both
   * non-null and differ. The event is only dispatched to listeners registered without
   * a property name of to those registered with a matching property name.
   *
   * @param propertyName The name of the property that changed.
   * @param oldValue The (possibly null) old value.
   * @param newValue The (possibly null) new value.
   */
  public void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
    if (!Objects.equals(oldValue, newValue))
      support.firePropertyChange(propertyName, oldValue, newValue);
  }

  // ---------------------------------------------------------------------------
  // NotifyPropertyChangedListener implementation.
  // ---------------------------------------------------------------------------
  @Override
  public void addPropertyChangeListener(PropertyChangeListener listener) {
    support.addPropertyChangeListener(listener);
  }

  @Override
  public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
    support.addPropertyChangeListener(propertyName, listener);
  }

  @Override
  public void removePropertyChangeListener(PropertyChangeListener listener) {
    support.removePropertyChangeListener(listener);
  }

  @Override
  public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
    support.removePropertyChangeListener(propertyName, listener);
  }
}
