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

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public class ExampleTableModel extends DefaultTableModel {
  private List<String> records = new ArrayList<>();

  @Override
  public int getRowCount() {
    return records != null ? records.size() : 0;
  }

  @Override
  public int getColumnCount() {
    return 1;
  }

  @Override
  public String getColumnName(int column) {
    return "Value";
  }

  @Override
  public Object getValueAt(int row, int column) {
    return records.get(row);
  }

  public void add(String s) {
    records.add(s);
    fireTableRowsInserted(records.size() - 1, records.size() - 1);
  }
}
