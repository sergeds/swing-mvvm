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

import java.util.Arrays;
import net.sds.mvvm.collections.CollectionChangedEvent.ChangeType;
import org.junit.Assert;
import org.junit.Test;

public class ObservableCollectionTests {
  private CollectionChangedEvent<String> event;

  @Test
  public void anAddedEventShouldBeReceived() {
    // Given:
    ObservableCollection<String> col = ObservableCollectionBuilder.createCollection();
    col.addListener(e -> event = e);

    // When:
    col.add("a");

    // Then:
    Assert.assertNotNull(event);
    Assert.assertTrue(event instanceof CollectionElementsAddedEvent);
    Assert.assertTrue(((CollectionChangedEvent) event).getNewItems().contains("a"));
    Assert.assertEquals(1, ((CollectionChangedEvent) event).getNewItems().size());
  }

  @Test
  public void anAddedEventWithMultipleEntriesShouldBeReceived() {
    // Given:
    ObservableCollection<String> col = ObservableCollectionBuilder.createCollection();
    col.addListener(e -> event = e);

    // When:
    col.addAll(Arrays.asList("a", "b"));

    // Then:
    Assert.assertNotNull(event);
    Assert.assertTrue(event instanceof CollectionElementsAddedEvent);
    Assert.assertTrue(((CollectionChangedEvent) event).getNewItems().contains("a"));
    Assert.assertTrue(((CollectionChangedEvent) event).getNewItems().contains("b"));
    Assert.assertEquals(2, ((CollectionChangedEvent) event).getNewItems().size());
  }

  @Test
  public void anItemCanBeAddedAtAnIndex() {
    // Given:
    ObservableCollection<String> col = ObservableCollectionBuilder.createCollection();
    col.addListener(e -> event = e);

    // When:
    col.add(0, "a");

    // Then:
    Assert.assertNotNull(event);
    Assert.assertTrue(event instanceof CollectionElementsAddedEvent);
    Assert.assertTrue(((CollectionChangedEvent) event).getNewItems().contains("a"));
    Assert.assertEquals(1, ((CollectionChangedEvent) event).getNewItems().size());
  }

  @Test
  public void multipleItemsCanBeAddedAtAnIndex() {
    // Given:
    ObservableCollection<String> col = ObservableCollectionBuilder.createCollection();
    col.addListener(e -> event = e);

    // When:
    col.addAll(0, Arrays.asList("a", "b"));

    // Then:
    Assert.assertNotNull(event);
    Assert.assertTrue(event instanceof CollectionElementsAddedEvent);
    Assert.assertTrue(((CollectionChangedEvent) event).getNewItems().contains("a"));
    Assert.assertTrue(((CollectionChangedEvent) event).getNewItems().contains("b"));
    Assert.assertEquals(2, ((CollectionChangedEvent) event).getNewItems().size());
  }

  @Test
  public void anItemCanBeRemoved() {
    // Given:
    ObservableCollection<String> col = ObservableCollectionBuilder.createCollection();
    col.addListener(e -> event = e);

    // When:
    col.add("a");
    col.remove("a");

    // Then:
    Assert.assertNotNull(event);
    Assert.assertTrue(event instanceof CollectionElementsRemovedEvent);
    Assert.assertTrue(((CollectionElementsRemovedEvent) event).getOldItems().contains("a"));
    Assert.assertEquals(1, ((CollectionChangedEvent) event).getOldItems().size());
    Assert.assertEquals(0, col.size());
  }

  @Test
  public void anItemCanBeRemovedAtAnIndex() {
    // Given:
    ObservableCollection<String> col = ObservableCollectionBuilder.createCollection();
    col.addListener(e -> event = e);

    // When:
    col.add("a");
    col.add("b");
    col.remove(1);

    // Then:
    Assert.assertNotNull(event);
    Assert.assertTrue(event instanceof CollectionElementsRemovedEvent);
    Assert.assertTrue(((CollectionElementsRemovedEvent) event).getOldItems().contains("b"));
    Assert.assertEquals(1, ((CollectionChangedEvent) event).getOldItems().size());
    Assert.assertEquals(1, col.size());
  }

  @Test
  public void allItemsCanBeRemoved() {
    // Given:
    ObservableCollection<String> col = ObservableCollectionBuilder.createCollection();
    col.addListener(e -> event = e);

    // When:
    col.add("a");
    col.add("b");
    col.removeAll(Arrays.asList("a", "b"));

    // Then:
    Assert.assertNotNull(event);
    Assert.assertTrue(event instanceof CollectionResetEvent);
    Assert.assertEquals(0, col.size());
  }

  @Test
  public void itemsCanBeRemovedSelectively() {
    // Given:
    ObservableCollection<String> col = ObservableCollectionBuilder.createCollection();
    col.addListener(e -> event = e);

    // When:
    col.add("a");
    col.add("b");
    col.removeIf(s -> s.equalsIgnoreCase("a"));

    // Then:
    Assert.assertNotNull(event);
    Assert.assertTrue(event instanceof CollectionResetEvent);
    Assert.assertTrue(col.contains("b"));
    Assert.assertEquals(1, col.size());
  }

  @Test
  public void aRangeCanBeRemoved() {
    // Given:
    ObservableCollection<String> col = ObservableCollectionBuilder.createCollection();
    col.addListener(e -> event = e);

    // When:
    col.add("a");
    col.add("b");
    col.removeRange(0, 1);

    // Then:
    Assert.assertNotNull(event);
    Assert.assertTrue(event instanceof CollectionResetEvent);
    Assert.assertTrue(col.contains("b"));
    Assert.assertEquals(1, col.size());
  }

  @Test
  public void aEntryCanBeReplaced() {
    // Given:
    ObservableCollection<String> col = ObservableCollectionBuilder.createCollection();
    col.addListener(e -> event = e);

    // When:
    col.add("a");
    col.add("b");
    col.replaceAll(s ->{
      if (s.equalsIgnoreCase("a"))
        return "c";
      else
        return s;
    });

    // Then:
    Assert.assertNotNull(event);
    Assert.assertTrue(event instanceof CollectionResetEvent);
    Assert.assertTrue(col.contains("b"));
    Assert.assertTrue(col.contains("c"));
    Assert.assertEquals(2, col.size());
  }

  @Test
  public void itemsCanBeRetained() {
    // Given:
    ObservableCollection<String> col = ObservableCollectionBuilder.createCollection();
    col.addListener(e -> event = e);

    // When:
    col.add("a");
    col.add("b");
    col.retainAll(Arrays.asList("b"));

    // Then:
    Assert.assertNotNull(event);
    Assert.assertTrue(event instanceof CollectionResetEvent);
    Assert.assertTrue(col.contains("b"));
    Assert.assertEquals(1, col.size());
  }

  @Test
  public void aCollectionCanBeCleared() {
    // Given:
    ObservableCollection<String> col = ObservableCollectionBuilder.createCollection();
    col.addListener(e -> event = e);

    // When:
    col.add("a");
    col.add("b");
    col.clear();

    // Then:
    Assert.assertNotNull(event);
    Assert.assertTrue(event instanceof CollectionElementsRemovedEvent);
    Assert.assertSame(event.getSource(), col);
    Assert.assertSame(event.getChangeType(), ChangeType.REMOVE);
    Assert.assertEquals(0, col.size());
  }

  @Test
  public void aListenerCanBeRemoved() {
    // Given:
    CollectionChangedListener l = e -> event = e;
    ObservableCollection<String> col = ObservableCollectionBuilder.createCollection();
    col.addListener(l);

    // When:
    col.add("a");

    // Then:
    Assert.assertNotNull(event);
    event = null;
    col.removeListener(l);

    // When:
    col.clear();

    // Then:
    Assert.assertNull(event);
  }
}