package io.github.artificialturtill.myriadascension.alignment;

public final class MoralAlignment {
    public static final double MINIMUM = -100.0D;
    public static final double MAXIMUM = 100.0D;
    public static final double NEUTRAL = 0.0D;

    private MoralAlignment() {
    }

    public static double clamp(double value) {
        return Math.max(MINIMUM, Math.min(MAXIMUM, value));
    }

    public static boolean isMalicious(double value) {
        return value < NEUTRAL;
    }

    public static boolean isBenevolent(double value) {
        return value > NEUTRAL;
    }

    public static boolean isNeutral(double value) {
        return value == NEUTRAL;
    }
}
