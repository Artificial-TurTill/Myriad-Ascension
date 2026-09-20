package io.github.artificialturtill.myriadascension.cultivation.qi;

public final class QiRules {
    public static final double BASE_PASSIVE_RECHARGE_CEILING = 0.10D;
    public static final double PASSIVE_RECHARGE_CEILING_PER_LEVEL = 0.04D;
    public static final int MAX_CORE_SKILL_LEVEL = 10;

    // Prototype tuning constants. These are mechanics, not permanent lore values.
    public static final double CIRCULATION_PERCENT_PER_CONTROL_PULSE = 2.0D;
    public static final int MIN_CIRCULATION_CONTROL_INTERVAL_TICKS = 3;
    public static final int MIN_BURST_TOGGLE_INTERVAL_TICKS = 6;

    private QiRules() {
    }

    public static int clampSkillLevel(int level) {
        return Math.max(0, Math.min(MAX_CORE_SKILL_LEVEL, level));
    }

    public static double passiveRechargeCeiling(int passiveQiRechargingLevel) {
        int level = clampSkillLevel(passiveQiRechargingLevel);
        return Math.min(1.0D, BASE_PASSIVE_RECHARGE_CEILING + PASSIVE_RECHARGE_CEILING_PER_LEVEL * level);
    }
}
