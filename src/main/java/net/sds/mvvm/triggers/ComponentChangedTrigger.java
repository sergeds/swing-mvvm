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

import javax.swing.JComponent;
import net.sds.mvvm.bindings.Binding;
import net.sds.mvvm.bindings.Direction;

/**
 * Registers a PropertyChangeListener to the component, listener for specific property events,
 * and applies the binding whenever a matching PropertyChangeEvent is fired.
 */
public class ComponentChangedTrigger implements Trigger {
  private JComponent component;
  private String propertyName;

  public ComponentChangedTrigger(JComponent component, String propertyName) {
    this.component = component;
    this.propertyName = propertyName;
  }

  @Override
  public void register(Binding binding, Direction direction) {
    component.addPropertyChangeListener(propertyName, e -> {
      binding.apply(direction);
    });
  }
}
