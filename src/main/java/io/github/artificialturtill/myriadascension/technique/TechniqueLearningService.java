package io.github.artificialturtill.myriadascension.technique;

import io.github.artificialturtill.myriadascension.clan.PrimordialisTestudoClan;
import io.github.artificialturtill.myriadascension.cultivation.data.CultivatorData;
import io.github.artificialturtill.myriadascension.inheritance.InheritanceAcquisitionRules;
import net.minecraft.resources.ResourceLocation;

public final class TechniqueLearningService {
    private TechniqueLearningService() {
    }

    public static boolean learn(
            CultivatorData data,
            ResourceLocation techniqueId,
            TechniqueCategory category) {

        boolean newlyLearned = data.techniqueKnowledge().learn(techniqueId);

        if (techniqueId.equals(PrimordialisTestudoClan.PRIMORDIAL_TESTUDO_LONGEVITY_ART.id())) {
            boolean activate = data.cultivationMethods().activeMethodId().isBlank();
            InheritanceAcquisitionRules.grantMethod(
                    data,
                    PrimordialisTestudoClan.PRIMORDIAL_TESTUDO_LONGEVITY_ART,
                    activate);
        }

        if (data.techniqueLoadout().equippedId(category).isBlank()) {
            data.techniqueLoadout().equip(category, techniqueId);
        }

        return newlyLearned;
    }
}
