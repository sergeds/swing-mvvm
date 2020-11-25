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
import java.util.Comparator;
import java.util.function.Predicate;
import net.sds.mvvm.NotifyPropertyChanged;

/**
 * Builder class for ObservableCollections.
 */
public class ObservableCollectionFactory<T> {
  private ObservableCollectionFactory() {}

  /**
   * Creates an empty collection.
   * @return The collection.
   */
  public static <T> ObservableCollection<T> createCollection() {
    return new ObservableArrayList<>();
  }

  /**
   * Creates a collection containing the given elements.
   * @param initialData The elements to add.
   * @return The collection.
   */
  public static <T> ObservableCollection<T> createCollection(Collection<T> initialData) {
    return new ObservableArrayList<>(initialData);
  }

  /**
   * Returns a sorted view of an Observable Collection.
   * @param source The source to use for this view.
   * @param comparator The comparator used for sorting.
   * @return The collection view.
   */
  public static <T> ObservableCollection<T> createCollection(ObservableCollection<T> source, Comparator<T> comparator) {
    SortedObservableCollectionView<T> coll = new SortedObservableCollectionView(source, comparator);
    coll.initialize();
    return coll;
  }

  /**
   * Returns a view that filters the elements of the source collection.
   * @param source The collection to filter.
   * @param predicate The predicate used for filtering.
   * @return The collection view.
   */
  public static <T> ObservableCollection<T> createCollection(ObservableCollection<T> source, Predicate<T> predicate) {
    FilteredObservableCollectionView<T> coll = new FilteredObservableCollectionView<>(source, predicate);
    coll.initialize();
    return coll;
  }

  /**
   * Returns a view that filters the elements of the source collection. When NotifyPropertyChanged elements are added, the collection
   * will register a listener using the given property name.
   * @param source The collection to filter.
   * @param predicate The predicate used for filtering.
   * @param propertName The name of the property to use for change events.
   * @return The collection view.
   */
  public static <T extends NotifyPropertyChanged> ObservableCollection<T> createCollection(ObservableCollection<T> source, Predicate<T> predicate, String propertName) {
    FilteredObservableCollectionView<T> coll = new FilteredObservableCollectionView<>(source, predicate, propertName);
    coll.initialize();
    return coll;
  }
}
