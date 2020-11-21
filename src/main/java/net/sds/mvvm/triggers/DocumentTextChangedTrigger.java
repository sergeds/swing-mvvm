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

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import net.sds.mvvm.bindings.Binding;
import net.sds.mvvm.bindings.Direction;

/**
 *
 */
public class DocumentTextChangedTrigger implements Trigger {
  private Document document;

  public DocumentTextChangedTrigger(Document document) {
    this.document = document;
  }

  @Override
  public void register(final Binding binding, final Direction direction) {
    document.addDocumentListener(new DocumentListener() {
      @Override
      public void insertUpdate(DocumentEvent e) {
        applyBinding();
      }

      @Override
      public void removeUpdate(DocumentEvent e) {
        applyBinding();
      }

      @Override
      public void changedUpdate(DocumentEvent e) {
        applyBinding();
      }

      private void applyBinding() {
        binding.apply(direction);
      }
    });
  }
}
