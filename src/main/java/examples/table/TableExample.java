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

import examples.AbstractFrame;
import examples.TextFieldsExample;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import net.sds.mvvm.bindings.Bind;
import net.sds.mvvm.bindings.Binder;
import net.sds.mvvm.bindings.BindingException;
import net.sds.mvvm.bindings.BindingType;

public class TableExample extends AbstractFrame {
  @Bind(value = "model", target = "model.value", type = BindingType.TARGET_TO_SOURCE)
  @Bind(value = "selectedRow", target = "row.value", type = BindingType.BI_DIRECTIONAL)
  private JTable table = new JTable();

  @Bind(value = "text", target = "row.value", type = BindingType.BI_DIRECTIONAL)
  private JTextField rowLabel = new JTextField();

  public TableExample() {
    JScrollPane sp = new JScrollPane(table);
    add(sp, BorderLayout.CENTER);

    add(rowLabel, BorderLayout.SOUTH);
  }

  public static void main(String... args) {
    try {
      TableExample example = new TableExample();
      ViewModel vm = new ViewModel();
      Binder.bind(example, vm);
      example.setVisible(true);
    } catch (BindingException e){
      e.printStackTrace();
    }
  }

}
