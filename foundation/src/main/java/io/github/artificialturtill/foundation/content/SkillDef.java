package io.github.artificialturtill.foundation.content;

/**
 * A learnable skill (JRMCore DBCSkills): TP cost and capacity (DBC Mind) reservation per level step, the last entry
 * repeating. {@code effect} names what the engine does per level ({@link #ENERGY_POOL}, ...); "" is a concept only.
 */
public record SkillDef(String id, String powerType, int maxLevel, int[] tpCost, int[] capacityCost, String effect,
                       float perLevel, String learn, int order) {
    public static final String ENERGY_POOL = "energy_pool", MEDITATION = "meditation", ENDURANCE = "endurance", RECOVERY = "recovery";
    public static final String LEARN_SHEET = "sheet", LEARN_TEACHER = "teacher";

    public boolean availableTo(String powerTypeId) { return powerType.isEmpty() || powerType.equals(powerTypeId); }
    /** TP for the step from {@code level} to level + 1. */
    public int tpCost(int level) { return tpCost[Math.clamp(level, 0, tpCost.length - 1)]; }
    public int capacityCost(int level) { return capacityCost[Math.clamp(level, 0, capacityCost.length - 1)]; }
    /** Capacity reserved by {@code level} learned levels. */
    public int reservedCapacity(int level) {
        int sum = 0;
        for (int i = 0; i < level; i++) sum += capacityCost(i);
        return sum;
    }
}
