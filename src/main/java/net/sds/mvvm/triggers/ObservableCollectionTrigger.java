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

package net.sds.mvvm.triggers;

import net.sds.mvvm.bindings.Binding;
import net.sds.mvvm.bindings.Direction;
import net.sds.mvvm.collections.ObservableCollection;

public class ObservableCollectionTrigger implements Trigger {
  private ObservableCollection<?> collection;
  public ObservableCollectionTrigger(ObservableCollection<?> collection) {
    this.collection = collection;
  }

  @Override
  public void register(Binding binding, Direction direction) {
    this.collection.addListener(e -> {
      binding.apply(direction);
    });
  }
}
