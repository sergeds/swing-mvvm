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

import static net.sds.mvvm.utils.ReflectionUtils.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import net.sds.mvvm.triggers.TriggerFactory;

public class Binder {
  private Binder() {
  }

  public static void bind(Object source, Object target) {
    for (Field f : getAllAnnotatedFields(source.getClass(), Bind.class)) {
      Bind[] binds = f.getAnnotationsByType(Bind.class);
      for (Bind bind : binds) {
        if (!f.isAccessible()) {
          f.setAccessible(true);
        }
        try {
          Object o = resolvePath(f.get(source), bind.value().split("\\."));
          Binding b;
          Direction d = Direction.UP;
          if (bind.type().equals(BindingType.SOURCE_TO_TARGET)) {
            b = createUniDirectionalBinding(o, bind.value(), target, bind.target());

          } else if (bind.type().equals(BindingType.TARGET_TO_SOURCE)) {
            b = createUniDirectionalBinding(target, bind.target(), o, bind.value());
            d = Direction.DOWN;

          } else {
            b = createBiDirectionalBinding(o, bind.value(), target, bind.target());
          }
          b.apply(d);

        } catch (ReflectiveOperationException e) {
          throw new BindingException("Could not create binding!", e);
        }
      }
    }
  }

  private static Binding createBiDirectionalBinding(Object source, String sourcePath, Object target, String targetPath) {
    R r = new R(source, sourcePath, target, targetPath);
    return new BindingBuilder<>()
        .withSourceSupplier(ValueSupplierFactory.create(r.resolvedSource, r.resolvedSourcePath))
        .withSourceConsumer(ValueConsumerFactory.create(r.resolvedSource, r.resolvedSourcePath))
        .withSourceTrigger(TriggerFactory.create(r.resolvedSource, r.resolvedSourcePath))
        .withTargetConsumer(ValueConsumerFactory.create(r.resolvedTarget, r.resolvedTargetPath))
        .withTargetSupplier(ValueSupplierFactory.create(r.resolvedTarget, r.resolvedTargetPath))
        .withTargetTrigger(TriggerFactory.create(r.resolvedTarget, r.resolvedTargetPath))
        .build();
  }

  private static Binding createUniDirectionalBinding(Object source, String sourcePath, Object target, String targetPath) {
    R r = new R(source, sourcePath, target, targetPath);
    return new BindingBuilder<>()
        .withSourceSupplier(ValueSupplierFactory.create(r.resolvedSource, r.resolvedSourcePath))
        .withTargetConsumer(ValueConsumerFactory.create(r.resolvedTarget, r.resolvedTargetPath))
        .withSourceTrigger(TriggerFactory.create(r.resolvedSource, r.resolvedSourcePath))
        .build();
  }


  private static class R {
    private Object resolvedSource;
    private Object resolvedTarget;
    private String resolvedSourcePath;
    private String resolvedTargetPath;
    private R(Object source, String sourcePath, Object target, String targetPath) {
      String[] split = sourcePath.split("\\.");
      resolvedSource = resolvePath(source, split);
      resolvedSourcePath = split[split.length - 1];
      split = targetPath.split("\\.");
      resolvedTarget = resolvePath(target, split);
      resolvedTargetPath = split[split.length - 1];
    }
  }
}
