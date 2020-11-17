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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;

public class ObservableMapTests {

  @Test
  public void aMapFiresEvents() {
    // Given:
    final List<String> events = new ArrayList<>();
    ObservableMap<String> map = new ObservableMap<>();
    map.addPropertyChangeListener(e -> events.add(e.getPropertyName()));

    // When:
    map.put("test", "value");

    // Then:
    Assert.assertEquals(2, events.size());
    Assert.assertEquals("test", events.get(0));
    Assert.assertEquals("size", events.get(1));

    // When:
    map.put("test", "newValue");

    // Then:
    Assert.assertEquals(3, events.size());
    Assert.assertEquals("test", events.get(2));

    Map<String, String> m = new HashMap<>();
    m.put("A", "1");
    m.put("B", "2");
    m.put("C", "3");
    // When
    map.putAll(m);

    // Then:
    Assert.assertEquals(9, events.size());
    Assert.assertEquals("size", events.get(8));

    // When:
    events.clear();
    map.putAll(m);

    // Then:
    Assert.assertEquals(0, events.size());

    // When:
    events.clear();
    map.clear();

    // Then:
    Assert.assertEquals(1, events.size());
    Assert.assertEquals("size", events.get(0));

    // When:
    events.clear();
    map.clear();

    // Then:
    Assert.assertEquals(0, events.size());

    // When:
    map.put("A", "1");
    events.clear();
    map.putIfAbsent("A", "2");

    // Then:
    Assert.assertEquals(0, events.size());

    // When:
    map.putIfAbsent("B", "2");

    // Then:
    Assert.assertEquals(2, events.size());

    // When:
    map.remove("B");

    // Then:
    Assert.assertEquals(3, events.size());

    // When:
    map.remove("A", "2");

    // Then:
    Assert.assertEquals(3, events.size());

    // When:
    map.remove("A", "1");

    // Then:
    Assert.assertEquals(4, events.size());
  }
}
