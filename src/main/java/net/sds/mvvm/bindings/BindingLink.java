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

package net.sds.mvvm.bindings;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Defines a link between a supplier (source) and a consumer (target).
 * When applied, the value of the supplier is passed on to the consumer.
 */
public class BindingLink {
  private Supplier supplier;
  private Consumer consumer;

  BindingLink(Supplier supplier, Consumer consumer) {
    this.supplier = supplier;
    this.consumer = consumer;
  }

  /**
   * Applies the link, transferring the value from the supplier to the consumer.
   */
  void applyLink() {
    consumer.accept(supplier.get());
  }
}
