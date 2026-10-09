package io.github.artificialturtill.foundation.lib.arsn;

/** A host-provided function called by an ARSN expression such as {@code $double(2)}. */
@FunctionalInterface
public interface RuntimeFunction {
  Object apply(Object... arguments);
}
