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

package examples.jcombobox;

import net.sds.mvvm.properties.Property;
import net.sds.mvvm.properties.PropertyFactory;

import javax.swing.*;

public class ViewModel {
  private Property<DefaultComboBoxModel<String>> model = PropertyFactory.createProperty("model", this, new DefaultComboBoxModel<>());
  private Property<String> selected = PropertyFactory.createProperty("selected", this, String.class, "B");

  public ViewModel() {
    model.get().addElement("A");
    model.get().addElement("B");
    model.get().addElement("C");
    model.get().addElement("D");
    model.get().addElement("E");
  }
}
