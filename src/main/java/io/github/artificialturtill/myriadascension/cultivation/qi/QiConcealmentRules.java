package io.github.artificialturtill.myriadascension.cultivation.qi;

import io.github.artificialturtill.myriadascension.cultivation.realm.CultivationRealm;

public final class QiConcealmentRules {
    public static final int MAX_LEVEL = 10;

    private QiConcealmentRules() {
    }

    /**
     * Resolves only the hard guarantees currently defined by design.
     * CONTESTED means future spiritual-sense/perception rules must decide the result.
     */
    public static ConcealmentOutcome evaluate(
            CultivationRealm concealedRealm,
            int concealedMinorStage,
            int concealmentLevel,
            CultivationRealm observerRealm,
            QiExposureType exposureType) {

        if (observerRealm == CultivationRealm.WORLD_CREATION) {
            return ConcealmentOutcome.DETECTED;
        }

        if (exposureType == QiExposureType.BURST && concealmentLevel < MAX_LEVEL) {
            return ConcealmentOutcome.DETECTED;
        }

        if (concealmentLevel <= 0) {
            return QiExposureRules.baseStrength(exposureType) == QiExposureStrength.HIDDEN
                    ? ConcealmentOutcome.CONTESTED
                    : ConcealmentOutcome.DETECTED;
        }

        int realmDifference = observerRealm.ordinal() - concealedRealm.ordinal();

        if (concealmentLevel == MAX_LEVEL) {
            if (realmDifference <= 0) {
                return ConcealmentOutcome.CONCEALED;
            }

            if (realmDifference == 1 && concealedMinorStage > 1) {
                return ConcealmentOutcome.CONCEALED;
            }

            return ConcealmentOutcome.DETECTED;
        }

        // Lower levels intentionally remain contested until spiritual-sense and
        // technique-mastery scaling are implemented rather than inventing hidden probabilities.
        if (realmDifference < 0 && exposureType != QiExposureType.BURST) {
            return ConcealmentOutcome.CONTESTED;
        }

        return ConcealmentOutcome.CONTESTED;
    }
}
