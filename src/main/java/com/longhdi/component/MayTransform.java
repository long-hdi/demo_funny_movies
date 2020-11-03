package com.longhdi.component;

import java.util.Optional;
import java.util.function.Function;

@FunctionalInterface
public interface MayTransform<T> extends Function<T, Optional<T>> {
}
