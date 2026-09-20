package io.github.artificialturtill.myriadascension.technique;

import io.github.artificialturtill.myriadascension.affinity.AffinityType;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

public final class TechniqueSignature {
    private final EnumSet<TechniquePathTrait> pathTraits;
    private final EnumSet<AffinityType> elements;
    private final EnumSet<TechniqueNatureTrait> natureTraits;

    public TechniqueSignature(
            Set<TechniquePathTrait> pathTraits,
            Set<AffinityType> elements,
            Set<TechniqueNatureTrait> natureTraits) {
        this.pathTraits = copy(pathTraits, TechniquePathTrait.class);
        this.elements = copy(elements, AffinityType.class);
        this.natureTraits = copy(natureTraits, TechniqueNatureTrait.class);

        if (this.pathTraits.isEmpty()) {
            this.pathTraits.add(TechniquePathTrait.NEUTRAL);
        }
    }

    public Set<TechniquePathTrait> pathTraits() {
        return Collections.unmodifiableSet(pathTraits);
    }

    public Set<AffinityType> elements() {
        return Collections.unmodifiableSet(elements);
    }

    public Set<TechniqueNatureTrait> natureTraits() {
        return Collections.unmodifiableSet(natureTraits);
    }

    public boolean hasPath(TechniquePathTrait trait) {
        return pathTraits.contains(trait);
    }

    private static <E extends Enum<E>> EnumSet<E> copy(Set<E> values, Class<E> type) {
        EnumSet<E> result = EnumSet.noneOf(type);
        if (values != null) {
            result.addAll(values);
        }
        return result;
    }
}
