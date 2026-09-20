package io.github.artificialturtill.myriadascension.clan;

import io.github.artificialturtill.myriadascension.MyriadAscension;
import io.github.artificialturtill.myriadascension.affinity.AffinityType;
import io.github.artificialturtill.myriadascension.cultivation.realm.CultivationRealm;
import io.github.artificialturtill.myriadascension.inheritance.AffinityRequirementMode;
import io.github.artificialturtill.myriadascension.inheritance.CultivationMethodDefinition;
import io.github.artificialturtill.myriadascension.inheritance.InheritanceRequirement;
import io.github.artificialturtill.myriadascension.technique.TechniqueNatureTrait;
import io.github.artificialturtill.myriadascension.technique.TechniquePathTrait;
import io.github.artificialturtill.myriadascension.technique.TechniqueSignature;
import java.util.EnumSet;
import java.util.List;
import net.minecraft.resources.ResourceLocation;

public final class PrimordialisTestudoClan {
    public static final String NAME = "Primordialis Testudo Clan";

    /**
     * Prototype full inheritance.
     *
     * <p>Balance values are intentionally provisional. The design identity is fixed:
     * Earth-aligned, neutral, slow cultivation, exceptional foundation/longevity,
     * compatible through Transcendent, and accelerated by a future Tortoise bloodline hook.</p>
     */
    public static final CultivationMethodDefinition PRIMORDIAL_TESTUDO_LONGEVITY_ART =
            new CultivationMethodDefinition(
                    ResourceLocation.fromNamespaceAndPath(
                            MyriadAscension.MOD_ID,
                            "primordial_testudo_longevity_art"),
                    "Primordialis Testudo Longevity Art",
                    CultivationRealm.TRANSCENDENT,
                    0.70D,
                    1.25D,
                    1.50D,
                    new TechniqueSignature(
                            EnumSet.of(TechniquePathTrait.NEUTRAL),
                            EnumSet.of(AffinityType.EARTH),
                            EnumSet.of(
                                    TechniqueNatureTrait.BODY_TEMPERING,
                                    TechniqueNatureTrait.LONGEVITY,
                                    TechniqueNatureTrait.DEFENSIVE)),
                    List.of(new InheritanceRequirement(
                            AffinityType.EARTH,
                            1,
                            AffinityRequirementMode.CURRENT)));

    private PrimordialisTestudoClan() {
    }
}
