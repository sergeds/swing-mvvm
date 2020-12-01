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

package net.sds.mvvm.bindings;

import net.sds.mvvm.properties.Property;
import net.sds.mvvm.properties.PropertyFactory;
import net.sds.mvvm.triggers.PropertyTrigger;
import org.junit.Assert;
import org.junit.Test;

public class DefaultBindingTests {
  @Test
  public void aBindingWorks() throws BindingException {
    Property<Boolean> sourceProp = PropertyFactory.createProperty("source", this, false);
    Property<Boolean> targetProp = PropertyFactory.createProperty("target", this, false);
    Binding binding = new BindingBuilder<Boolean, Boolean>()
        .withSourceSupplier(() -> sourceProp.get())
        .withTargetConsumer(b -> targetProp.set(b))
        .build();

    Assert.assertFalse(targetProp.get());
    sourceProp.set(true);
    binding.apply(Direction.UP);
    Assert.assertTrue(targetProp.get());
  }

  @Test
  public void aBindingCanBeBiDirectional() throws BindingException {
    Property<Boolean> sourceProp = PropertyFactory.createProperty("source", this, false);
    Property<Boolean> targetProp = PropertyFactory.createProperty("target", this, false);
    Binding binding = new BindingBuilder<Boolean, Boolean>()
        .withSourceSupplier(sourceProp::get)
        .withTargetConsumer(targetProp::set)
        .withTargetSupplier(targetProp::get)
        .withSourceConsumer(sourceProp::set)
        .build();

    Assert.assertFalse(targetProp.get());
    sourceProp.set(true);
    binding.apply(Direction.UP);
    Assert.assertTrue(targetProp.get());
    targetProp.set(false);
    binding.apply(Direction.DOWN);
    Assert.assertFalse(sourceProp.get());
  }

  @Test
  public void bindingsCanBeAutomaticallyApplied() throws BindingException {
    Property<Boolean> sourceProp = PropertyFactory.createProperty("source", this, false);
    Property<Boolean> targetProp = PropertyFactory.createProperty("target", this, false);
    new BindingBuilder<Boolean, Boolean>()
        .withSourceSupplier(() -> sourceProp.get())
        .withSourceTrigger((b, d) -> sourceProp.addPropertyChangeListener(e -> b.apply(d)))
        .withTargetConsumer(b -> targetProp.set(b))
        .build();

    Assert.assertFalse(targetProp.get());
    sourceProp.set(true);
    Assert.assertTrue(targetProp.get());
  }

  @Test
  public void biDirectionalBindingsCanBeAutomaticallyApplied() throws BindingException {
    Property<Boolean> sourceProp = PropertyFactory.createProperty("source", this, false);
    Property<Boolean> targetProp = PropertyFactory.createProperty("target", this, false);
    new BindingBuilder<Boolean, Boolean>()
        .withSourceSupplier(() -> sourceProp.get())
        .withSourceTrigger((b, d) -> sourceProp.addPropertyChangeListener(e -> b.apply(d)))
        .withTargetConsumer(b -> targetProp.set(b))
        .withTargetSupplier(targetProp::get)
        .withTargetTrigger((b, d) -> targetProp.addPropertyChangeListener(e -> b.apply(d)))
        .withSourceConsumer(sourceProp::set)
        .build();

    Assert.assertFalse(targetProp.get());
    sourceProp.set(true);
    Assert.assertTrue(targetProp.get());
    targetProp.set(false);
    Assert.assertFalse(sourceProp.get());
  }

  @Test
  public void aPropertyTriggerCanBeUsed() throws BindingException {
    Property<Boolean> sourceProp = PropertyFactory.createProperty("source", this, false);
    Property<Boolean> targetProp = PropertyFactory.createProperty("target", this, false);
    new BindingBuilder<Boolean, Boolean>()
        .withSourceSupplier(() -> sourceProp.get())
        .withSourceTrigger(new PropertyTrigger(sourceProp))
        .withTargetConsumer(targetProp::set)
        .build();
    Assert.assertFalse(targetProp.get());
    sourceProp.set(true);
    Assert.assertTrue(targetProp.get());
  }
}
