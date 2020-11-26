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

package examples.table;

import net.sds.mvvm.properties.Property;
import net.sds.mvvm.properties.PropertyFactory;

public class ViewModel {
  private Property<ExampleTableModel> model = PropertyFactory.createProperty("model", this, new ExampleTableModel());
  private Property<Integer> row = PropertyFactory.createProperty("row", this, Integer.class);

  public ViewModel() {
    model.get().add("A");
    model.get().add("B");
    model.get().add("C");
    model.get().add("D");
    model.get().add("E");
  }
}
