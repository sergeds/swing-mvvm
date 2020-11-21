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

import java.awt.Color;
import java.awt.Font;
import javax.swing.JLabel;
import net.sds.mvvm.properties.Property;
import net.sds.mvvm.properties.PropertyFactory;
import org.junit.Assert;
import org.junit.Test;

public class ComponentTests {
  private Color color;
  private Font font;

  @Test
  public void anyPropertyCanBeBound() {
    Property<Color> colorProperty = PropertyFactory.createProperty("Color", this, Color.class);
    JLabel label = new JLabel();
    new BindingBuilder<Color, Color>()
        .withSourceSupplier(() -> colorProperty.get())
        .withSourceTrigger((b, d) -> colorProperty.addPropertyChangeListener(e -> b.apply(d)))
        .withTargetConsumer(c -> label.setForeground(c))
        .build();

    colorProperty.set(Color.RED);
    Assert.assertEquals(Color.RED, label.getForeground());
  }

  @Test
  public void theSourceCanBeAComponent() {
    Property<Color> colorProperty = PropertyFactory.createProperty("Color", this, Color.class);
    JLabel label = new JLabel();
    new BindingBuilder<Color, Color>()
        .withSourceSupplier(() -> label.getForeground())
        .withSourceTrigger((b, d) -> label.addPropertyChangeListener("foreground", e -> b.apply(d)))
        .withTargetConsumer(colorProperty::set)
        .build();

    label.setForeground(Color.RED);
    Assert.assertEquals(Color.RED, label.getForeground());
  }
}
