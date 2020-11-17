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

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import net.sds.mvvm.utils.EventSuppressor;

/**
 * Wraps an ObservableCollection and offers a view that will always be sorted using the Comparator given. Note that this is a view, so all changes
 * made to this view will be reflected in the wrapped collection.
 */
public class SortedObservableCollectionView<T> extends AbstractObservableCollectionView<T> {
  private Comparator<T> comparator;

  public SortedObservableCollectionView(ObservableCollection<T> source, Comparator<T> comparator) {
    super(source);
    this.comparator = comparator;
  }

  private int insertIndexOf(T t) {
    int low = 0;
    int high = size() - 1;
    while (low <= high) {
      int mid = (low + high) / 2;
      T midVal = get(mid);
      int cmp = comparator.compare(midVal, t);

      if (cmp < 0)
        low = mid + 1;
      else if (cmp > 0)
        high = mid - 1;
      else
        return mid; // key found
    }
    return low;  // key not found  }
  }

  // ---------------------------------------------------------------------------
  // region Overrides:
  // ---------------------------------------------------------------------------
  @Override
  public boolean add(T t) {
    addSorted(t);
    return true;
  }

  @Override
  public void add(int index, T e) {
    addSorted(e);
  }

  private int addSorted(T t) {
    int i = insertIndexOf(t);
    if (i < 0)
      i = 0;
    super.add(i, t);
    return i;
  }

  @Override
  public boolean addAll(Collection<? extends T> c) {
    return addAll(0, c);
  }

  @Override
  public boolean addAll(int index, Collection<? extends T> c) {
    int[] indices = new int[c.size()];
    try (EventSuppressor suppressor = openSuppressor()) {
      int i = 0;
      for (T t : c) {
        indices[i++] = addSorted(t);
      }
    }
    notifyListeners(new CollectionElementsAddedEvent<>(this, c, indices));
    return true;
  }

  @Override
  public T set(int index, T element) {
    T result = super.set(index, element);
    Collections.sort(this, comparator);
    notifyListeners(new CollectionResetEvent(this));
    return result;
  }
  // endregion
}
