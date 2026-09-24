package io.github.artificialturtill.myriadascension.power;

import io.github.artificialturtill.myriadascension.cultivation.data.CultivatorData;
import io.github.artificialturtill.myriadascension.cultivation.realm.CultivationRealm;
import io.github.artificialturtill.myriadascension.stats.CultivatorStat;

public final class CultivationPowerRules {
    private CultivationPowerRules() {
    }

    public static double realmPotential(CultivationRealm realm, int subdivision) {
        int s = Math.max(1, subdivision);

        return switch (realm) {
            case MORTAL -> 1.0D;

            case TEMPERED_BODY -> pick(s,
                    1.25D, 1.50D, 1.80D, 2.20D, 2.60D, 3.10D, 3.70D, 4.30D, 5.00D);
            case INITIAL_ELEMENT -> pick(s,
                    6.0D, 7.5D, 9.0D, 11.0D, 13.5D, 15.5D, 17.0D, 18.5D, 20.0D);
            case QI_TRANSFORMATION -> pick(s,
                    25D, 30D, 36D, 44D, 54D, 65D, 77D, 89D, 100D);
            case SEPARATION_AND_REUNION -> pick(s,
                    120D, 140D, 165D, 190D, 220D, 245D, 265D, 285D, 300D);
            case TRUE_ELEMENT -> pick(s,
                    400D, 500D, 620D, 760D, 920D, 1080D, 1240D, 1380D, 1500D);
            case IMMORTAL_ASCENSION -> pick(s,
                    1800D, 2200D, 2700D, 3300D, 4000D, 4700D, 5500D, 6500D, 7500D);

            case TRANSCENDENT -> pick(s, 15_000D, 28_000D, 50_000D);
            case SAINT -> pick(s, 100_000D, 180_000D, 300_000D);
            case SAINT_KING -> pick(s, 700_000D, 1_200_000D, 2_000_000D);
            case ORIGIN_RETURNING -> pick(s, 5_000_000D, 10_000_000D, 20_000_000D);
            case ORIGIN_KING -> pick(s, 50_000_000D, 100_000_000D, 200_000_000D);
            case DAO_SOURCE -> pick(s, 600_000_000D, 1_500_000_000D, 3_000_000_000D);
            case EMPEROR -> pick(s, 20_000_000_000D, 50_000_000_000D, 100_000_000_000D);

            case PSEUDO_GREAT_EMPEROR -> 500_000_000_000D;
            case DAO_SEAL -> 3_000_000_000_000D;
            case HALF_STEP_OPEN_HEAVEN -> 20_000_000_000_000D;

            case OPEN_HEAVEN -> pick(s,
                    100_000_000_000_000D,
                    300_000_000_000_000D,
                    1_000_000_000_000_000D,
                    10_000_000_000_000_000D,
                    100_000_000_000_000_000D,
                    1_000_000_000_000_000_000D,
                    100_000_000_000_000_000_000D,
                    10_000_000_000_000_000_000_000D,
                    1_000_000_000_000_000_000_000_000D);

            case WORLD_CREATION -> 1.0E30D;
        };
    }

    public static PowerBreakdown calculate(CultivatorData data) {
        double realmPotential = realmPotential(data.realm(), data.minorStage());

        double physicalFactor = physicalFactor(data);
        double energyFactor = energyFactor(data);
        double soulFactor = soulFactor(data);
        double foundationFactor = foundationFactor(data);
        double conditionFactor = conditionFactor(data);
        double battleFactor = battleFactor(data);

        double current = realmPotential
                * physicalFactor
                * energyFactor
                * soulFactor
                * foundationFactor
                * conditionFactor
                * battleFactor;

        return new PowerBreakdown(
                realmPotential,
                Math.max(0.01D, current),
                physicalFactor,
                energyFactor,
                soulFactor,
                foundationFactor,
                conditionFactor,
                battleFactor);
    }

    public static double physicalFactor(CultivatorData data) {
        double weighted = data.stats().get(CultivatorStat.STRENGTH) * 0.40D
                + data.stats().get(CultivatorStat.VITALITY) * 0.35D
                + data.stats().get(CultivatorStat.AGILITY) * 0.25D;

        return clamp(1.0D + (Math.sqrt(weighted) - 1.0D) * 0.20D, 0.75D, 2.50D);
    }

    public static double energyFactor(CultivatorData data) {
        if (data.realm().ordinal() < CultivationRealm.INITIAL_ELEMENT.ordinal()) {
            // Yuan Qi may exist at Tempered Body 7-9, but it is not yet usable
            // as conscious combat output.
            return 1.0D;
        }

        double reserve = data.maximumQi() <= 0.0D
                ? 0.0D
                : clamp(data.currentQi() / data.maximumQi(), 0.0D, 1.0D);
        double output = clamp(data.circulationPercent() / 100.0D, 0.0D, 1.0D);

        double factor = (0.40D + 0.60D * reserve)
                * (0.35D + 0.65D * output);

        if (data.burstMode()) {
            factor *= 1.25D;
        }

        return clamp(factor, 0.10D, 1.40D);
    }

    public static double soulFactor(CultivatorData data) {
        double weighted = data.stats().get(CultivatorStat.SPIRITUAL_SENSE) * 0.55D
                + data.stats().get(CultivatorStat.SOUL_STRENGTH) * 0.45D;

        double scaling = data.realm().ordinal() >= CultivationRealm.IMMORTAL_ASCENSION.ordinal()
                ? 0.15D
                : 0.05D;

        return clamp(1.0D + (Math.sqrt(weighted) - 1.0D) * scaling, 0.80D, 2.00D);
    }

    public static double foundationFactor(CultivatorData data) {
        double foundationStats = (
                data.stats().get(CultivatorStat.MERIDIAN_QUALITY)
                        + data.stats().get(CultivatorStat.DANTIAN_QUALITY)) / 2.0D;

        double statFactor = 1.0D + (Math.sqrt(foundationStats) - 1.0D) * 0.10D;
        double purityFactor = 0.70D + 0.30D * clamp(data.vesselPurity() / 100.0D, 0.0D, 1.0D);
        double impurityFactor = 1.0D / (1.0D + data.impurityLoad() / 100.0D);

        return clamp(statFactor * purityFactor * impurityFactor, 0.35D, 1.80D);
    }

    public static double conditionFactor(CultivatorData data) {
        double penalty = data.bodyInjury() / 220.0D
                + data.meridianInjury() / 260.0D
                + data.soulInjury() / 320.0D
                + data.recoveryDebt() / 450.0D
                + data.meridianLoad() / 500.0D;

        return clamp(1.0D - penalty, 0.10D, 1.0D);
    }

    public static double battleFactor(CultivatorData data) {
        return 1.0D + Math.min(0.50D, Math.log1p(data.battleComprehension()) / 20.0D);
    }

    public static double combatGapRatio(double attackerPower, double defenderPower) {
        if (defenderPower <= 0.0D) {
            return Double.POSITIVE_INFINITY;
        }
        return attackerPower / defenderPower;
    }

    private static double pick(int subdivision, double... values) {
        int index = Math.max(1, Math.min(values.length, subdivision)) - 1;
        return values[index];
    }

    private static double clamp(double value, double minimum, double maximum) {
        return Math.max(minimum, Math.min(maximum, value));
    }
}
