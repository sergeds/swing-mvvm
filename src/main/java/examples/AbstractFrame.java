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
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class AbstractFrame extends JFrame {
  protected AbstractFrame() {
    setSize(800, 600);
    setLocationRelativeTo(null);
    setLayout(new BorderLayout());
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    add(getButtonsPanel(), BorderLayout.SOUTH);
  }
  protected JComponent getButtonsPanel() {
    JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    JButton close = new JButton("Close");
    close.addActionListener(e -> System.exit(0));
    p.add(close);
    return p;
  }
}
