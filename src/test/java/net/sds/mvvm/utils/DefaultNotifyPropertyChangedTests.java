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

package net.sds.mvvm.utils;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.junit.Assert;
import org.junit.Test;

public class DefaultNotifyPropertyChangedTests implements PropertyChangeListener {
  private PropertyChangeEvent event;

  @Test
  public void eventsAreDispatched() {
    // Given:
    event = null;
    DefaultNotifyPropertyChanged prop = new DefaultNotifyPropertyChanged(this);
    prop.addPropertyChangeListener(this);

    // When:
    prop.firePropertyChange("test", "A", "B");

    // Then:
    Assert.assertNotNull(event);
    Assert.assertEquals("test", event.getPropertyName());

    // When:
    event = null;
    prop.removePropertyChangeListener(this);
    prop.firePropertyChange("test", "A", "B");
    Assert.assertNull(event);
  }

  @Test
  public void eventsCanBeSelective() {
    // Given:
    event = null;
    DefaultNotifyPropertyChanged prop = new DefaultNotifyPropertyChanged(this);
    prop.addPropertyChangeListener("selective", this);

    // When:
    prop.firePropertyChange("test", "A", "B");
    Assert.assertNull(event);

    // When:
    prop.firePropertyChange("selective", "A", "B");

    // Then:
    Assert.assertNotNull(event);
    Assert.assertEquals("selective", event.getPropertyName());

    // When:
    event = null;
    prop.removePropertyChangeListener("selective", this);
    prop.firePropertyChange("selective", "A", "B");
    Assert.assertNull(event);
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    this.event = evt;
  }
}
