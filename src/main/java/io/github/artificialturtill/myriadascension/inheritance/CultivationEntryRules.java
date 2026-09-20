package io.github.artificialturtill.myriadascension.inheritance;

import io.github.artificialturtill.myriadascension.cultivation.data.CultivatorData;
import io.github.artificialturtill.myriadascension.cultivation.realm.CultivationRealm;

public final class CultivationEntryRules {
    private CultivationEntryRules() {
    }

    public static boolean canBeginTemperedBody(
            CultivatorData data,
            CultivationMethodDefinition method) {

        if (data.realm() != CultivationRealm.MORTAL) {
            return false;
        }

        if (!data.cultivationMethods().knows(method.id())
                || !data.cultivationMethods().isActive(method.id())) {
            return false;
        }

        if (method.maximumSupportedRealm().ordinal() < CultivationRealm.TEMPERED_BODY.ordinal()) {
            return false;
        }

        for (InheritanceRequirement requirement : method.requirements()) {
            if (!MonumentEligibilityRules.meetsElementRequirement(
                    data.innateAffinities(),
                    data.affinities(),
                    requirement)) {
                return false;
            }
        }

        return true;
    }

    public static boolean beginTemperedBody(
            CultivatorData data,
            CultivationMethodDefinition method) {
        if (!canBeginTemperedBody(data, method)) {
            return false;
        }

        data.setRealm(CultivationRealm.TEMPERED_BODY);
        data.setMinorStage(1);
        data.setCultivationProgress(0.0D);
        return true;
    }
}
