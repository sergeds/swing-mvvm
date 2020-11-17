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

import org.junit.Assert;
import org.junit.Test;

public class RunnableCommandTests {
  private Object result;

  @Test
  public void aCommandIsExecuted() {
    RunnableCommand command = CommandFactory.createCommand(() -> result = true);
    command.execute();
    Assert.assertEquals(true, result);
  }

  @Test
  public void aDisabledCommandDoesNotExecute() {
    result = null;
    RunnableCommand command = CommandFactory.createCommand(() -> result = true);
    command.getEnabledProperty().set(false);
    command.execute();
    Assert.assertNull(result);
  }
}
