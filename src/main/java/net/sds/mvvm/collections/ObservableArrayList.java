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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public class ObservableArrayList<T> extends ArrayList<T> implements ObservableCollection<T> {
  private Set<CollectionChangedListener<T>> listeners = new HashSet<>();

  public ObservableArrayList() {
  }

  public ObservableArrayList(Collection<T> data) {
    super(data);
  }

  @Override
  public T findEntry(Predicate<T> predicate) {
    Optional<T> result = stream().filter(predicate).findFirst();
    if (result.isPresent())
      return result.get();
    else
      return null;
  }

  // ---------------------------------------------------------------------------
  // region Overrides:
  // ---------------------------------------------------------------------------
  @Override
  public boolean add(T e) {
    boolean result = super.add(e);
    if (result)
      notifyListeners(new CollectionElementsAddedEvent(this, Arrays.asList(e), new int[]{size() - 1}));

    return result;
  }

  @Override
  public boolean addAll(Collection<? extends T> c) {
    int offset = super.size();
    boolean result = super.addAll(c);
    if (result) {
      int[] indices = new int[c.size()];
      for (int i = 0; i < c.size(); i++)
        indices[i] = offset + i;
      notifyListeners(new CollectionElementsAddedEvent(this, c, indices));
    }

    return result;
  }

  @Override
  public void add(int index, T e) {
    super.add(index, e);
    notifyListeners(new CollectionElementsAddedEvent(this, Arrays.asList(e), new int[]{index}));
  }

  @Override
  public boolean addAll(int index, Collection<? extends T> c) {
    boolean result = super.addAll(index, c);
    if (result) {
      int[] indices = new int[c.size()];
      for (int i = 0; i < c.size(); i++)
        indices[i] = index + i;
      notifyListeners(new CollectionElementsAddedEvent<>(this, c, indices));
    }
    return result;
  }

  @SuppressWarnings("unchecked")
  @Override
  public boolean remove(Object o) {
    int i = indexOf(o);
    boolean result = super.remove(o);
    if (result)
      notifyListeners(new CollectionElementsRemovedEvent<>(this, Arrays.asList((T) o), new int[]{i}));

    return result;
  }

  @Override
  public T remove(int index) {
    T t = super.remove(index);
    if (t != null)
      notifyListeners(new CollectionElementsRemovedEvent<>(this, Arrays.asList(t), new int[]{index}));
    return t;
  }

  @Override
  public boolean removeAll(Collection<?> c) {
    boolean result = super.removeAll(c);
    if (result)
      notifyListeners(new CollectionResetEvent<>(this));
    return result;
  }

  @Override
  public boolean removeIf(Predicate<? super T> filter) {
    boolean result = super.removeIf(filter);
    if (result)
      notifyListeners(new CollectionResetEvent<>(this));
    return result;
  }

  @Override
  public void removeRange(int fromIndex, int toIndex) {
    super.removeRange(fromIndex, toIndex);
    notifyListeners(new CollectionResetEvent<>(this));
  }

  @Override
  public void replaceAll(UnaryOperator<T> operator) {
    super.replaceAll(operator);
    notifyListeners(new CollectionResetEvent<>(this));
  }

  @Override
  public boolean retainAll(Collection<?> c) {
    boolean result = super.retainAll(c);
    if (result)
      notifyListeners(new CollectionResetEvent<>(this));
    return result;
  }

  @Override
  public void clear() {
    int size = size();
    Collection<T> old = new ArrayList<>(size());
    old.addAll(this);
    int[] indices = new int[size()];
    for (int i = 0; i < indices.length; i++)
      indices[i] = i;
    super.clear();
    if (size > 0)
      notifyListeners(new CollectionElementsRemovedEvent<>(this, old, indices));
  }
  // endregion

  // ---------------------------------------------------------------------------
  // region ObservableCollection implementation:
  // ---------------------------------------------------------------------------
  @Override
  public void addListener(CollectionChangedListener<T> l) {
    listeners.add(l);
  }

  @Override
  public void removeListener(CollectionChangedListener<T> l) {
    listeners.remove(l);
  }
  // endregion

  protected void notifyListeners(CollectionChangedEvent<T> e) {
    for (CollectionChangedListener<T> l : listeners)
      l.collectionChanged(e);
  }
}
