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

/**
 * Defines an event that is fired whenever a change is made to the ObservableCollection.
 * @param <T>
 */
public abstract class CollectionChangedEvent<T> {
  private ChangeType changeType;
  private Collection<? extends T> newItems;
  private int[] indices;
  private Collection<? extends T> oldItems;
  private ObservableCollection<T> source;

  CollectionChangedEvent(ObservableCollection<T> source, ChangeType changeType, Collection<? extends T> newItems, Collection<? extends T> oldItems, int[] indices){
    this.changeType = changeType;
    this.newItems = newItems;
    this.oldItems = oldItems;
    this.source = source;
    this.indices = indices;
  }

  /**
   * Returns the type of event.
   * @return The type of event.
   */
  public ChangeType getChangeType() {
    return changeType;
  }

  /**
   * Returns the collection of new items. This will only contain elements if it concerns
   * a ChangeType.ADD event.
   * @return The new items.
   */
  public Collection<? extends T> getNewItems() {
    return newItems;
  }

  /**
   * Returns the collectionof old items. This will only contain elements if it concerns
   * a ChangeType.REMOVE event.
   * @return The old (removed) items.
   */
  public Collection<? extends T> getOldItems() {
    return oldItems;
  }

  /**
   * Returns the collection that fired the event.
   * @return The source of the event.
   */
  public ObservableCollection<T> getSource() {
    return source;
  }

  /**
   * Returns the indices of the added or removed rows. In case of a RESET,
   * the indices will be null.
   * @return The indices.
   */
  public int[] getIndices() {
    return indices;
  }

  // ---------------------------------------------------------------------------
  // Event type
  // ---------------------------------------------------------------------------
  public enum ChangeType {
    ADD,
    REMOVE,
    RESET
  }
}