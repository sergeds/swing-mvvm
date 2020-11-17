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

import net.sds.mvvm.properties.Property;

/**
 * Defines a Command that can be used when defining bindings with any component that can issue an ActionPerformed event.
 */
public interface Command {
  /**
   * Returns the property that defines whether the command is enabled or not.
   * @return The enabled property.
   */
  Property<Boolean> getEnabledProperty();
}
