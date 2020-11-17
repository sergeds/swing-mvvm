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

package net.sds.mvvm.utils;

/**
 * Class used to determine whether update events should be ignored, while active.
 */
public class EventSuppressor implements AutoCloseable {
  private int counter;

  /**
   * Opens the EventSuppressor.
   * @return
   */
  public synchronized EventSuppressor open() {
    counter++;
    return this;
  }

  @Override
  public synchronized void close() {
    counter--;
  }

  /**
   * Returns true if the instance
   * @return
   */
  public boolean isActive() {
    return counter > 0;
  }
}
