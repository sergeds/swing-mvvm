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
import net.sds.mvvm.utils.EventSuppressor;
import org.apache.commons.lang3.ObjectUtils;

/**
 * Defines a view on an ObservableCollection, allowing eg. to filter the elements, or sort them.
 * @param <T>
 */
public abstract class AbstractObservableCollectionView<T> extends ObservableArrayList<T> implements CollectionChangedListener<T> {
  private Collection<T> sourceList;
  private EventSuppressor suppressor = new EventSuppressor();

  protected AbstractObservableCollectionView(Collection<T> sourceList) {
    this.sourceList = sourceList;
    if (sourceList instanceof ObservableCollection) {
      ObservableCollection.class.cast(this.sourceList).addListener(this);
    }
  }

  void initialize() {
    addAll(sourceList);
  }

  protected void reset() {
    try (EventSuppressor closeable = openSuppressor()) {
      clear();
      addAll(sourceList);
    }
  }

  @Override
  protected void notifyListeners(CollectionChangedEvent<T> e) {
    if (!suppressor.isActive()) {
      super.notifyListeners(e);
    }
  }

  protected EventSuppressor openSuppressor() {
    return suppressor.open();
  }

  // ---------------------------------------------------------------------------
  // region CollectionChangedListener implementation:
  // ---------------------------------------------------------------------------
  @Override
  public void collectionChanged(CollectionChangedEvent<T> e) {
    switch (e.getChangeType()) {
      case ADD:
        for (T t : e.getNewItems())
          add(t);
        break;
      case REMOVE:
        for (T t : e.getOldItems())
          remove(t);
        break;
      case RESET:
        reset();
        break;
    }
  }
  // endregion

  // ---------------------------------------------------------------------------
  // region Utility class used to suppress event notification:
  // ---------------------------------------------------------------------------
  private static class NotifySuppressor {}
  // endregion


  // ---------------------------------------------------------------------------
  // region Overridden as multiple views can register with the same source
  // collection. If not overridden, only the first one would remain in the
  // listener set of the source.
  // ---------------------------------------------------------------------------
  @Override
  public boolean equals(Object o) {
    return o == this;
  }

  @Override
  public int hashCode() {
    return ObjectUtils.identityToString(this).hashCode();
  }
  // endregion
}
