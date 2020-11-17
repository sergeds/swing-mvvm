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

package net.sds.mvvm.properties;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

public class PropertyTests {

  @Test
  public void primitivePropertiesHaveAValue() {
    Property<Integer> intProp = PropertyFactory.createProperty("test", this, int.class);
    Assert.assertTrue(intProp.isNotNull());
    Assert.assertEquals(0, intProp.get().intValue());

    Property<Double> doubleProp = PropertyFactory.createProperty("test", this, double.class);
    Assert.assertTrue(doubleProp.isNotNull());
    Assert.assertEquals(0.0, doubleProp.get().doubleValue(), 0);

    Property<Boolean> booleanProp = PropertyFactory.createProperty("test", this, boolean.class);
    Assert.assertTrue(booleanProp.isNotNull());
    Assert.assertEquals(false, booleanProp.get().booleanValue());
  }

  @Test
  public void propertyTypeIsInitializedFromValue() {
    Property<Integer> intProp = PropertyFactory.createProperty("test", this, 1);
    Assert.assertEquals(Integer.class, GenericProperty.class.cast(intProp).getPropertyType());
  }

  @Test
  public void propertiesCanBeInitializedWithValue() {
    Property<String> property = PropertyFactory.createProperty("test", this, "Initial value");
    Assert.assertTrue(property.isNotNull());
    Assert.assertEquals("Initial value", property.get());
  }

  @Test
  public void propertiesFireEventsWhenChanged() {
    List<PropertyChangeEvent> events = new ArrayList<>();
    Property<String> property = PropertyFactory.createProperty("test", this, String.class);
    PropertyChangeListener listener = e -> events.add(e);
    property.addPropertyChangeListener(listener);

    property.set("New value");
    Assert.assertEquals(1, events.size());

    property.removePropertyChangeListener(listener);
    property.set("Changed value");
    Assert.assertEquals(1, events.size());

    property.addPropertyChangeListener("test", listener);
    property.set("Different value");
    Assert.assertEquals(2, events.size());

    property.removePropertyChangeListener("test", listener);
    property.addPropertyChangeListener("wrongname", listener);

    property.set("Different value 2");
    Assert.assertEquals(2, events.size());
  }

  @Test
  public void propertyNameIsUsedInEvent() {
    Property<String> property = PropertyFactory.createProperty("test", this, String.class);
    List<PropertyChangeEvent> events = new ArrayList<>();
    property.addPropertyChangeListener(events::add);
    property.set("ABC");

    Assert.assertEquals(property.getName(), events.get(0).getPropertyName());
  }
}
