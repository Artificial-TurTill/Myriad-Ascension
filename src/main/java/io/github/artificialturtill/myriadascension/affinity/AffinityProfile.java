package io.github.artificialturtill.myriadascension.affinity;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import net.minecraft.nbt.CompoundTag;

public final class AffinityProfile {
    private final EnumMap<AffinityType, Integer> values = new EnumMap<>(AffinityType.class);

    public AffinityProfile() {
        for (AffinityType type : AffinityType.values()) {
            values.put(type, 0);
        }
    }

    public int get(AffinityType type) {
        return values.getOrDefault(type, 0);
    }

    public void set(AffinityType type, int value) {
        values.put(type, Math.max(0, value));
    }

    public int total() {
        return values.values().stream().mapToInt(Integer::intValue).sum();
    }

    public Map<AffinityType, Integer> view() {
        return Collections.unmodifiableMap(values);
    }

    public void copyFrom(AffinityProfile other) {
        for (AffinityType type : AffinityType.values()) {
            set(type, other.get(type));
        }
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        for (AffinityType type : AffinityType.values()) {
            tag.putInt(type.name(), get(type));
        }
        return tag;
    }

    public void load(CompoundTag tag) {
        for (AffinityType type : AffinityType.values()) {
            set(type, tag.getInt(type.name()));
        }
    }
}
