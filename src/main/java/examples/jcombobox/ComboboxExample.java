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

import examples.AbstractFrame;
import net.sds.mvvm.bindings.Bind;
import net.sds.mvvm.bindings.Binder;
import net.sds.mvvm.bindings.BindingException;
import net.sds.mvvm.bindings.BindingType;

import javax.swing.*;
import java.awt.*;

public class ComboboxExample extends AbstractFrame {
    @Bind(value = "model", target = "model.value", type = BindingType.TARGET_TO_SOURCE)
    @Bind(value = "selectedItem", target = "selected.value", type = BindingType.BI_DIRECTIONAL)
    private JComboBox<String> combo = new JComboBox();

    @Bind(value = "text", target = "selected.value", type = BindingType.BI_DIRECTIONAL)
    private JTextField rowLabel = new JTextField();

    public ComboboxExample() {
        JScrollPane sp = new JScrollPane(combo);
        add(sp, BorderLayout.NORTH);

        add(rowLabel, BorderLayout.SOUTH);
    }

    public static void main(String... args) {
        try {
            ComboboxExample example = new ComboboxExample();
            ViewModel vm = new ViewModel();
            Binder.bind(example, vm);
            example.setVisible(true);
        } catch (BindingException e){
            e.printStackTrace();
        }
    }
}
