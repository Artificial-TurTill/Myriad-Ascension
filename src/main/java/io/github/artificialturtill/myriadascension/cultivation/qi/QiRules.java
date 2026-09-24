package io.github.artificialturtill.myriadascension.cultivation.qi;

public final class QiRules {
    public static final double BASE_PASSIVE_RECHARGE_CEILING = 0.10D;
    public static final double PASSIVE_RECHARGE_CEILING_PER_LEVEL = 0.04D;
    public static final int MAX_CORE_SKILL_LEVEL = 10;

    // Prototype tuning constants. These are mechanics, not permanent lore values.
    public static final double CIRCULATION_PERCENT_PER_CONTROL_PULSE = 2.0D;
    public static final int MIN_CIRCULATION_CONTROL_INTERVAL_TICKS = 3;
    public static final int MIN_BURST_TOGGLE_INTERVAL_TICKS = 6;

    // Tempered Body 7-9 has a real but sealed Yuan Qi reserve.
    // These capacities/rates are alpha tuning values and are intentionally isolated here.
    public static final double TEMPERED_BODY_STAGE_7_QI_CAPACITY = 25.0D;
    public static final double TEMPERED_BODY_STAGE_8_QI_CAPACITY = 50.0D;
    public static final double TEMPERED_BODY_STAGE_9_QI_CAPACITY = 75.0D;
    public static final double TEMPERED_BODY_STAGE_7_RECHARGE_FRACTION_PER_TICK = 0.00005D;
    public static final double TEMPERED_BODY_STAGE_8_RECHARGE_FRACTION_PER_TICK = 0.00010D;
    public static final double TEMPERED_BODY_STAGE_9_RECHARGE_FRACTION_PER_TICK = 0.00015D;

    private QiRules() {
    }

    public static int clampSkillLevel(int level) {
        return Math.max(0, Math.min(MAX_CORE_SKILL_LEVEL, level));
    }

    public static double passiveRechargeCeiling(int passiveQiRechargingLevel) {
        int level = clampSkillLevel(passiveQiRechargingLevel);
        return Math.min(1.0D, BASE_PASSIVE_RECHARGE_CEILING + PASSIVE_RECHARGE_CEILING_PER_LEVEL * level);
    }

    public static double temperedBodyPassiveCapacity(int stage) {
        return switch (Math.max(1, Math.min(9, stage))) {
            case 7 -> TEMPERED_BODY_STAGE_7_QI_CAPACITY;
            case 8 -> TEMPERED_BODY_STAGE_8_QI_CAPACITY;
            case 9 -> TEMPERED_BODY_STAGE_9_QI_CAPACITY;
            default -> 0.0D;
        };
    }

    public static double temperedBodyNaturalRechargeFractionPerTick(int stage) {
        return switch (Math.max(1, Math.min(9, stage))) {
            case 7 -> TEMPERED_BODY_STAGE_7_RECHARGE_FRACTION_PER_TICK;
            case 8 -> TEMPERED_BODY_STAGE_8_RECHARGE_FRACTION_PER_TICK;
            case 9 -> TEMPERED_BODY_STAGE_9_RECHARGE_FRACTION_PER_TICK;
            default -> 0.0D;
        };
    }
}
