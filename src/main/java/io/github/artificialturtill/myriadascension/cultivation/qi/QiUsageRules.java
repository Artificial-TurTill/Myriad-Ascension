package io.github.artificialturtill.myriadascension.cultivation.qi;

import io.github.artificialturtill.myriadascension.cultivation.data.CultivatorData;

public final class QiUsageRules {
    private QiUsageRules() {
    }

    public static double activePowerFraction(CultivatorData data) {
        return activePowerFraction(data.circulationPercent());
    }

    public static double activePowerFraction(double activePowerPercent) {
        return Math.max(0.0D, Math.min(1.0D, activePowerPercent / 100.0D));
    }

    /**
     * Technique/resource costs are authored at 100% active power and scale
     * linearly with the currently circulated percentage.
     */
    public static double scaleQiCost(double fullPowerCost, CultivatorData data) {
        return scaleQiCost(fullPowerCost, data.circulationPercent());
    }

    public static double scaleQiCost(double fullPowerCost, double activePowerPercent) {
        return Math.max(0.0D, fullPowerCost) * activePowerFraction(activePowerPercent);
    }

    public static double scaleOutput(double fullPowerOutput, CultivatorData data) {
        return Math.max(0.0D, fullPowerOutput) * activePowerFraction(data);
    }

    public static boolean hasActivePower(CultivatorData data) {
        return data.circulationPercent() > 0.0D && data.currentQi() > 0.0D;
    }
}
