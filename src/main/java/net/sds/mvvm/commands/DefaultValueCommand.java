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

package net.sds.mvvm.commands;

import java.util.function.Consumer;

/**
 * Default implementation of ValueCommand that wraps a Consumer that is invoked with a value when the command is executed.
 * Remark that if the command is not enabled, execution will not invoke the consumer.
 * @param <T>
 */
public class DefaultValueCommand<T> extends AbstractCommand implements ValueCommand<T> {
  private Consumer<T> consumer;

  public DefaultValueCommand(Consumer<T> consumer) {
    this.consumer = consumer;
  }

  @Override
  public void execute(T value) {
    if (getEnabledProperty().get()) {
      consumer.accept(value);
    }
  }
}
