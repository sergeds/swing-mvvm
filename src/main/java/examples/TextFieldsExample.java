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

package examples;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import net.sds.mvvm.bindings.Bind;
import net.sds.mvvm.bindings.Binder;
import net.sds.mvvm.bindings.BindingException;
import net.sds.mvvm.bindings.BindingType;
import net.sds.mvvm.bindings.Paths;

public class TextFieldsExample extends AbstractFrame {
  @Bind(value = Paths.TEXT, target = "input2." + Paths.TEXT, type = BindingType.BI_DIRECTIONAL)
  private JTextField input1 = new JTextField("sfsdfsdfsdf");

  private JTextField input2 = new JTextField();

  @Bind(value = Paths.SELECTED, target = "input1." + Paths.EDITABLE)
  @Bind(value = Paths.SELECTED, target = "input2." + Paths.EDITABLE)
  private JCheckBox editable = new JCheckBox("Editable");

  private TextFieldsExample() {
    JPanel p = new JPanel(new GridBagLayout());
    GridBagConstraints g = new GridBagConstraints(0, 0, 1, 1, 1, 0, GridBagConstraints.WEST, 1, new Insets(5, 5, 5, 5), 0, 0);
    p.add(input1, g);
    g.gridy++;
    p.add(input2, g);
    g.gridy++;
    p.add(editable, g);
    g.weighty = 1;
    g.gridy++;
    p.add(new JLabel(), g);

    add(p, BorderLayout.CENTER);
  }

  private void applyBindings() throws BindingException{
    Binder.bind(this, this);
  }

  public static void main(String... args) {
    try {
      TextFieldsExample example = new TextFieldsExample();
      example.applyBindings();
      example.setVisible(true);
    } catch (BindingException e){
      e.printStackTrace();
    }
  }
}
