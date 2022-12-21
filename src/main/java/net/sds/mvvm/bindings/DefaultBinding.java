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

import java.util.HashMap;
import java.util.Map;
import net.sds.mvvm.utils.EventSuppressor;

/**
 * Defines a default binding that manages the association of {@link BindingLink} with a {@link Direction}.
 */
public class DefaultBinding implements Binding {
  private EventSuppressor suppressor = new EventSuppressor();
  private Map<Direction, BindingLink> links = new HashMap<>();

  /**
   * Associates a BindingLink with a direction.
   * @param direction
   * @param link
   * @return
   */
  DefaultBinding withBindingLink(Direction direction, BindingLink link) {
    links.put(direction, link);
    return this;
  }

  @Override
  public void apply(Direction direction) {
    if (suppressor.isActive()) {
      return;
    }

    // In case of a Bi-Directional binding, this will prevent the event being sent
    // back from the target.
    try (EventSuppressor supp = suppressor.open()) {
      BindingLink link = links.get(direction);
      if (link != null) {
        link.applyLink();
      }
    }
  }
}
