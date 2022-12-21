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

import net.sds.mvvm.triggers.Trigger;

/**
 * Builder for Bindings.
 * When only source Supplier and target Consumer are provided a unidirectional binding is created.
 * When a target Supplier and source Consumer are also provider, a bidirectional bininds is created.
 *
 * A Source and Target can be specified, and will be monitored, so that the binding is applied automatically
 * when the source changes. (or the target in a bi-directional binding).
 */
public class BindingBuilder<S, T> {
  private ValueSupplier sourceSupplier;
  private ValueConsumer sourceConsumer;
  private ValueSupplier targetSupplier;
  private ValueConsumer targetConsumer;
  private Trigger sourceTrigger;
  private Trigger targetTrigger;

  /**
   * Assigns a trigger monitoring the source.
   * @param trigger
   * @return
   */
  public BindingBuilder<S, T> withSourceTrigger(Trigger trigger) {
    this.sourceTrigger = trigger;
    return this;
  }

  /**
   * Assigns a trigger monitoring the target. (for bidirectional bindings)
   * @param trigger
   * @return
   */
  public BindingBuilder<S, T> withTargetTrigger(Trigger trigger) {
    this.targetTrigger = trigger;
    return this;
  }

  /**
   * Assigns a source supplier.
   * @param supplier
   * @return
   */
  public BindingBuilder<S, T> withSourceSupplier(ValueSupplier<S> supplier) {
    this.sourceSupplier = supplier;
    return this;
  }

  /**
   * Assigns a source consumer (for bidirectional links)
   * @param consumer
   * @return
   */
  public BindingBuilder<S, T> withSourceConsumer(ValueConsumer<T> consumer) {
    this.sourceConsumer = consumer;
    return this;
  }

  /**
   * Assigns a target consumer.
   * @param consumer
   * @return
   */
  public BindingBuilder<S, T> withTargetConsumer(ValueConsumer<T> consumer) {
    this.targetConsumer = consumer;
    return this;
  }

  /**
   * Assigns a target supplier (for bidirectional links)
   * @param supplier
   * @return
   */
  public BindingBuilder<S, T> withTargetSupplier(ValueSupplier<S> supplier) {
    this.targetSupplier = supplier;
    return this;
  }

  /**
   * Builds and returns the binding.
   * @return The binding.
   */
  public Binding build()  throws BindingException {
    DefaultBinding binding = new DefaultBinding();


    if (sourceSupplier != null && targetConsumer != null) {
      createLink(binding, sourceSupplier, targetConsumer, Direction.UP);
    }

    if (targetSupplier != null && sourceConsumer != null) {
      createLink(binding, targetSupplier, sourceConsumer, Direction.DOWN);
    }

    if (sourceTrigger != null) {
      sourceTrigger.register(binding, Direction.UP);
    }

    if (targetTrigger != null) {
      targetTrigger.register(binding, Direction.DOWN);
    }

    return binding;
  }

  private DefaultBinding createLink(DefaultBinding binding, ValueSupplier<T> supplier, ValueConsumer<T> consumer, Direction direction) {
    BindingLink link = new BindingLink(supplier, consumer);
    return binding.withBindingLink(direction, link);
  }
}
