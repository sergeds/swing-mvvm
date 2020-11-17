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
 * Command factory.
 */
public class CommandFactory {
  private CommandFactory() {}

  /**
   * Creates a RunnableCommand that wraps the given runnable.
   * @param runnable
   * @return The Command.
   */
  public static RunnableCommand createCommand(Runnable runnable) {
    return new DefaultRunnableCommand(runnable);
  }

  /**
   * Creates a ValueCommand that wraps the given consumer.
   * @param consumer
   * @param <T>
   * @return The Command.
   */
  public static <T> ValueCommand<T> createCommand(Consumer<T> consumer) {
    return new DefaultValueCommand<>(consumer);
  }
}
