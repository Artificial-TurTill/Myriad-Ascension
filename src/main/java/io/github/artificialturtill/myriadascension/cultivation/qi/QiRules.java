package io.github.artificialturtill.myriadascension.cultivation.qi;

public final class QiRules {
    public static final double BASE_PASSIVE_RECHARGE_CEILING = 0.10D;
    public static final double PASSIVE_RECHARGE_CEILING_PER_LEVEL = 0.04D;
    public static final int MAX_CORE_SKILL_LEVEL = 10;

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
