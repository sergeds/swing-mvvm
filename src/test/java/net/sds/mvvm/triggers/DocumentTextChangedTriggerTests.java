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

package net.sds.mvvm.triggers;

import javax.swing.JTextField;
import net.sds.mvvm.bindings.BindingBuilder;
import net.sds.mvvm.bindings.BindingException;
import net.sds.mvvm.properties.Property;
import net.sds.mvvm.properties.PropertyFactory;
import org.junit.Assert;
import org.junit.Test;

public class DocumentTextChangedTriggerTests {

  @Test
  public void textChangesArePropagated() throws BindingException {
    final StringBuilder b = new StringBuilder();
    JTextField field = new JTextField();
    new BindingBuilder<String, String>()
        .withSourceSupplier(() -> field.getText())
        .withSourceTrigger(new DocumentTextChangedTrigger(field.getDocument()))
        .withTargetConsumer(t -> b.append(t))
        .build();

    field.setText("New text");
    Assert.assertEquals("New text", b.toString());
  }

  @Test
  public void textChangesCanBeBidirectional() throws BindingException {
    Property<String> prop = PropertyFactory.createProperty("Text", this, String.class);
    JTextField field = new JTextField();
    new BindingBuilder<String, String>()
        .withSourceSupplier(field::getText)
        .withSourceTrigger(new DocumentTextChangedTrigger(field.getDocument()))
        .withSourceConsumer(field::setText)
        .withTargetConsumer(prop::set)
        .withTargetSupplier(prop::get)
        .withTargetTrigger(new PropertyTrigger(prop))
        .build();

    field.setText("New text");
    Assert.assertEquals("New text", prop.get());
    prop.set("Changed it");
    Assert.assertEquals("Changed it", field.getText());
  }
}
