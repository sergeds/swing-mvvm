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

/**
 * Default implementation of RunnableCommand. Wraps a runnable that is invoked when the command
 * is executed.
 * Remark that if the command is not enable, execution will not invoke the runnable.
 */
public class DefaultRunnableCommand extends AbstractCommand implements RunnableCommand {
  private Runnable runnable;
  public DefaultRunnableCommand(Runnable runnable) {
    this.runnable = runnable;
  }

  @Override
  public void execute() {
    if (getEnabledProperty().get()) {
      runnable.run();
    }
  }
}
