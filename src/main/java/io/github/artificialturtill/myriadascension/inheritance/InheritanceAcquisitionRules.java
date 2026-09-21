package io.github.artificialturtill.myriadascension.inheritance;

import io.github.artificialturtill.myriadascension.cultivation.data.CultivatorData;
import io.github.artificialturtill.myriadascension.technique.TechniqueCategory;

public final class InheritanceAcquisitionRules {
    private InheritanceAcquisitionRules() {
    }

    public static boolean grantMethod(
            CultivatorData data,
            CultivationMethodDefinition method,
            boolean activateImmediately) {
        boolean newlyLearned = data.cultivationMethods().learn(method.id());

        if (activateImmediately) {
            data.cultivationMethods().setActive(method.id());
            data.techniqueLoadout().equip(TechniqueCategory.CULTIVATION, method.id());
        }

        return newlyLearned;
    }
}
