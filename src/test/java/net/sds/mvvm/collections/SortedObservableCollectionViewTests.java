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
import java.util.Comparator;
import org.junit.Assert;
import org.junit.Test;

public class SortedObservableCollectionViewTests {

  @Test
  public void theCollectionRemainsSorted() {
    // Given:
    ObservableCollection<String> source = ObservableCollectionBuilder.createCollection();
    source.addAll(Arrays.asList("D", "E", "F"));
    ObservableCollection<String> sorted = ObservableCollectionBuilder.createCollection(source, Comparator.naturalOrder());

    // When/Then:
    source.add("B");
    Assert.assertEquals(0, sorted.indexOf("B"));
    Assert.assertEquals(3, source.indexOf("B"));

    // When/Then:
    source.add("A");
    Assert.assertEquals(0, sorted.indexOf("A"));
    Assert.assertEquals(4, source.indexOf("A"));

    // When/Then:
    source.add("C");
    Assert.assertEquals(2, sorted.indexOf("C"));
    Assert.assertEquals(5, source.indexOf("C"));

    // When/Then:
    source.add("G");
    Assert.assertEquals(6, sorted.indexOf("G"));
    Assert.assertEquals(6, source.indexOf("G"));

    // When/Then:
    source.add("C1");
    Assert.assertEquals(3, sorted.indexOf("C1"));
    Assert.assertEquals(7, source.indexOf("C1"));


    source.addAll(Arrays.asList("D", "sdf", "54", "SDFDSF","z"));
    // When/Then:
    for (int i = 0; i < sorted.size() - 1; i++) {
      Assert.assertTrue(sorted.get(i).compareTo(sorted.get(i + 1)) <= 0);
    }
  }
}
