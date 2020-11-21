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

/**
 * Defines a binding between a source and a target.
 */
public interface Binding {
  /**
   * Applies the binding using the given direction. If the direction is {@link Direction#UP} the binding is applied from
   * source to target. When the direction is {@link Direction#DOWN} the binding is applied from target to source.
   * @param direction
   */
  void apply(Direction direction);
}
