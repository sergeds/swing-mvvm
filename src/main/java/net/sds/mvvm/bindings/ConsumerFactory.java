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
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import net.sds.mvvm.properties.Property;

/**
 * Tries to figure out a Consumer for an object.
 */
public class ConsumerFactory {
  private static Map<Class<?>, Function<Object, Consumer>> consumers = new HashMap<>();
  private ConsumerFactory() {}

  static Consumer getConsumer(Object o) {
    if (o instanceof Property) {
      return getConsumer(Property.class, o);
    }

    return getConsumer(o.getClass(), o);
  }

  static Consumer getConsumer(Class<?> cl, Object o) {
    Function<Object, Consumer> function = consumers.get(cl);
    if (function != null) {
      return function.apply(o);
    }
    return null;
  }

  static {
    consumers.put(Property.class, o -> s -> Property.class.cast(o).set(s));
  }
}
