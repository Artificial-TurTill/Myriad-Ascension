package io.github.artificialturtill.myriadascension.stats;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import net.minecraft.nbt.CompoundTag;

public final class CultivatorStats {
    public static final double MINIMUM_STAT_VALUE = 1.0D;

    private final EnumMap<CultivatorStat, Double> values =
            new EnumMap<>(CultivatorStat.class);

    public CultivatorStats() {
        resetToMinimums();
    }

    public double get(CultivatorStat stat) {
        return Math.max(MINIMUM_STAT_VALUE, values.getOrDefault(stat, MINIMUM_STAT_VALUE));
    }

    public void set(CultivatorStat stat, double value) {
        values.put(stat, Math.max(MINIMUM_STAT_VALUE, value));
    }

    public void add(CultivatorStat stat, double amount) {
        set(stat, get(stat) + amount);
    }

    public Map<CultivatorStat, Double> view() {
        return Collections.unmodifiableMap(values);
    }

    public void copyFrom(CultivatorStats other) {
        for (CultivatorStat stat : CultivatorStat.values()) {
            set(stat, other.get(stat));
        }
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        for (CultivatorStat stat : CultivatorStat.values()) {
            tag.putDouble(stat.name(), get(stat));
        }
        return tag;
    }

    public void load(CompoundTag tag) {
        for (CultivatorStat stat : CultivatorStat.values()) {
            if (tag.contains(stat.name())) {
                set(stat, tag.getDouble(stat.name()));
            } else {
                set(stat, MINIMUM_STAT_VALUE);
            }
        }
    }

    private void resetToMinimums() {
        for (CultivatorStat stat : CultivatorStat.values()) {
            values.put(stat, MINIMUM_STAT_VALUE);
        }
    }
}
