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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import net.sds.mvvm.NotifyPropertyChanged;

/**
 * Defines a Collection view that filters the elements using a predefined predicate.
 * If a propertyName is specified, the view will only listen for changes to the specified property.
 * @param <T>
 */
public class FilteredObservableCollectionView<T> extends AbstractObservableCollectionView<T> implements PropertyChangeListener {
  private Predicate<? super T> predicate;
  private String propertyName;

  public FilteredObservableCollectionView(Collection<T> source, Predicate<? super T> predicate, String propertyName) {
    super(source);
    this.predicate = predicate;
    this.propertyName = propertyName;
  }

  public FilteredObservableCollectionView(ObservableCollection<T> source, Predicate<? super T> predicate) {
    this(source, predicate, null);
  }

  @Override
  public boolean addAll(Collection<? extends T> c) {
    List<T> toAdd = new ArrayList<>();
    for (T t : c) {
      if (t instanceof NotifyPropertyChanged)
        registerPropertyChanged((NotifyPropertyChanged) t);
      if (predicate.test(t))
        toAdd.add(t);
    }

    return super.addAll(toAdd);
  }

  @Override
  public boolean add(T t) {
    if (t instanceof NotifyPropertyChanged)
      registerPropertyChanged((NotifyPropertyChanged) t);
    if (predicate.test(t))
      return super.add(t);
    else
      return false;
  }

  @Override
  public void add(int index, T t) {
    if (t instanceof NotifyPropertyChanged)
      registerPropertyChanged((NotifyPropertyChanged) t);

    if (predicate.test(t))
      super.add(index, t);
  }

  @Override
  public boolean addAll(int index, Collection<? extends T> c) {
    List<T> toAdd = new ArrayList<>();
    for (T t : c) {
      if (t instanceof NotifyPropertyChanged)
        registerPropertyChanged((NotifyPropertyChanged) t);
      if (predicate.test(t))
        toAdd.add(t);
    }

    return super.addAll(index, toAdd);
  }

  @Override
  public T remove(int index) {
    T t = super.remove(index);
    if (t != null && t instanceof NotifyPropertyChanged)
      unRegisterPropertyChanged((NotifyPropertyChanged) t);
    return t;
  }

  @Override
  public boolean remove(Object o) {
    return remove(o, true);
  }

  private boolean remove(Object o, boolean unregister) {
    if (unregister && o != null && o instanceof NotifyPropertyChanged)
      unRegisterPropertyChanged((NotifyPropertyChanged) o);
    return super.remove(o);
  }

  @Override
  public boolean removeAll(Collection<?> c) {
    for (Object o : c) {
      if (o != null && o instanceof NotifyPropertyChanged)
        unRegisterPropertyChanged((NotifyPropertyChanged) o);
    }

    return super.removeAll(c);
  }

  @Override
  public void removeRange(int fromIndex, int toIndex) {
    for (int i = fromIndex; i < toIndex; i++) {
      T t = get(i);
      if (t != null && t instanceof NotifyPropertyChanged)
        unRegisterPropertyChanged((NotifyPropertyChanged) t);
    }
    super.removeRange(fromIndex, toIndex);
  }

  // ---------------------------------------------------------------------------
  // region PropertyChangeListener:
  // ---------------------------------------------------------------------------
  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    T t = (T) evt.getSource();
    if (predicate.test(t) && !contains(t))
      add(t);
    else
      remove(t, false);
  }

  private void unRegisterPropertyChanged(NotifyPropertyChanged notifyPropertyChanged) {
    if (propertyName == null)
      notifyPropertyChanged.removePropertyChangeListener(this);
    else
      notifyPropertyChanged.removePropertyChangeListener(propertyName, this);
  }

  private void registerPropertyChanged(NotifyPropertyChanged notifyPropertyChanged) {
    if (propertyName == null)
      notifyPropertyChanged.addPropertyChangeListener(this);
    else
      notifyPropertyChanged.addPropertyChangeListener(propertyName, this);
  }
  // endregion
}
