package io.github.artificialturtill.myriadascension.cultivation.realm;

public final class TemperedBodyRules {
    private TemperedBodyRules() {
    }

    public static TemperedBodyBand bandForStage(int stage) {
        int clamped = Math.max(1, Math.min(9, stage));
        if (clamped <= 3) {
            return TemperedBodyBand.BODY_STRENGTHENING;
        }
        if (clamped <= 6) {
            return TemperedBodyBand.ENERGY_SENSING;
        }
        return TemperedBodyBand.NATURAL_GATHERING;
    }

    public static boolean strengthensBodyOnly(int stage) {
        return bandForStage(stage) == TemperedBodyBand.BODY_STRENGTHENING;
    }

    public static boolean sensesWorldEnergy(int stage) {
        return stage >= 4;
    }

    public static boolean naturallyGathersPreQiEnergy(int stage) {
        return stage >= 7;
    }

    public static boolean canActivelySpendQi(int stage) {
        // Myriad Ascension design override: Tempered Body prepares the vessel
        // but does not yet grant active Qi usage.
        return false;
    }
}
