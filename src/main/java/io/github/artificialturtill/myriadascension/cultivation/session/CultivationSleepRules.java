package io.github.artificialturtill.myriadascension.cultivation.session;

import io.github.artificialturtill.myriadascension.cultivation.realm.CultivationRealm;
import io.github.artificialturtill.myriadascension.cultivation.realm.RealmMilestoneRules;

public final class CultivationSleepRules {
    public static final int REQUIRED_CULTIVATION_MINUTES = 60;
    public static final int REQUIRED_CULTIVATION_TICKS = REQUIRED_CULTIVATION_MINUTES * 60 * 20;

    private CultivationSleepRules() {
    }

    public static boolean mayCountAsSleeping(CultivationRealm realm, int uninterruptedCultivationTicks) {
        return RealmMilestoneRules.canReplaceSleepWithCultivation(realm)
                && uninterruptedCultivationTicks >= REQUIRED_CULTIVATION_TICKS;
    }
}
