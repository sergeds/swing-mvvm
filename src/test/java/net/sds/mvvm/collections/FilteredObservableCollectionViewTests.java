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

import net.sds.mvvm.utils.AbstractNotifyPropertyChanged;
import org.junit.Assert;
import org.junit.Test;

public class FilteredObservableCollectionViewTests {

  @Test
  public void theListWorks() {
    // Given:
    ObservableCollection<TestBean> source = ObservableCollectionBuilder.createCollection();
    source.add(new TestBean(true));
    source.add(new TestBean(true));
    source.add(new TestBean(true));
    ObservableCollection<TestBean> filtered = ObservableCollectionBuilder.createCollection(source, b -> b.isEnabled());

    // When:
    int size = filtered.size();

    // Then:
    Assert.assertEquals(source.size(), size);

    // When/Then:
    source.add(new TestBean(false));
    Assert.assertEquals(4, source.size());
    Assert.assertEquals(3, filtered.size());

    // When/Then:
    source.get(0).setEnabled(false);
    Assert.assertEquals(4, source.size());
    Assert.assertEquals(2, filtered.size());

    // When/Then:
    source.get(3).setEnabled(true);
    source.get(0).setEnabled(true);
    Assert.assertEquals(4, source.size());
    Assert.assertEquals(4, filtered.size());

    // When/Then:
    source.get(0).setEnabled(false);
    Assert.assertEquals(4, source.size());
    Assert.assertEquals(3, filtered.size());

    // When/Then:
    source.remove(0);
    Assert.assertEquals(3, source.size());
    Assert.assertEquals(3, filtered.size());
  }

  private static class TestBean extends AbstractNotifyPropertyChanged {
    public boolean enabled;
    public TestBean(boolean enabled) {
      this.enabled = enabled;
    }

    public boolean isEnabled() {
      return enabled;
    }

    public void setEnabled(boolean enabled) {
      boolean old = this.enabled;
      this.enabled = enabled;
      firePropertyChange("enable", old, this.enabled);
    }
  }
}
