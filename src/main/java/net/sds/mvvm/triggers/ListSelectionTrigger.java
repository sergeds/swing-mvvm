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

import java.util.Optional;
import javax.swing.JList;
import javax.swing.JTable;
import net.sds.mvvm.bindings.Binding;
import net.sds.mvvm.bindings.BindingException;
import net.sds.mvvm.bindings.Direction;

public class ListSelectionTrigger implements Trigger {
  private Optional<JList> list = Optional.empty();
  private Optional<JTable> table = Optional.empty();
  public ListSelectionTrigger(JList list) {
    this.list = Optional.of(list);
  }
  public ListSelectionTrigger(JTable table) {
    this.table = Optional.of(table);
  }

  @Override
  public void register(final Binding binding, final Direction direction) throws BindingException {
    list.ifPresent(l -> l.addListSelectionListener(e -> {
      if (!e.getValueIsAdjusting()) {
        binding.apply(direction);
      }
    }));

    table.ifPresent(t -> t.getSelectionModel().addListSelectionListener(e -> {
      if (!e.getValueIsAdjusting()) {
        binding.apply(direction);
      }
    }));
  }
}
