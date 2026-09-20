package io.github.artificialturtill.myriadascension.cultivation.qi;

import io.github.artificialturtill.myriadascension.technique.TechniquePathTrait;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import net.minecraft.nbt.CompoundTag;

public final class QiNatureProfile {
    private final EnumMap<TechniquePathTrait, Double> influences =
            new EnumMap<>(TechniquePathTrait.class);

    public QiNatureProfile() {
        for (TechniquePathTrait trait : TechniquePathTrait.values()) {
            influences.put(trait, 0.0D);
        }
    }

    public double get(TechniquePathTrait trait) {
        return influences.getOrDefault(trait, 0.0D);
    }

    public void set(TechniquePathTrait trait, double value) {
        influences.put(trait, Math.max(0.0D, value));
    }

    public void add(TechniquePathTrait trait, double amount) {
        set(trait, get(trait) + Math.max(0.0D, amount));
    }

    public Map<TechniquePathTrait, Double> view() {
        return Collections.unmodifiableMap(influences);
    }

    public TechniquePathTrait dominantTrait() {
        TechniquePathTrait best = TechniquePathTrait.NEUTRAL;
        double bestValue = -1.0D;
        for (TechniquePathTrait trait : TechniquePathTrait.values()) {
            double value = get(trait);
            if (value > bestValue) {
                best = trait;
                bestValue = value;
            }
        }
        return best;
    }

    public void copyFrom(QiNatureProfile other) {
        for (TechniquePathTrait trait : TechniquePathTrait.values()) {
            set(trait, other.get(trait));
        }
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        for (TechniquePathTrait trait : TechniquePathTrait.values()) {
            tag.putDouble(trait.name(), get(trait));
        }
        return tag;
    }

    public void load(CompoundTag tag) {
        for (TechniquePathTrait trait : TechniquePathTrait.values()) {
            set(trait, tag.getDouble(trait.name()));
        }
    }
}
