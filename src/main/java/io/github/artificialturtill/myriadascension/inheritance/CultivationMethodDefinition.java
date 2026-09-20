package io.github.artificialturtill.myriadascension.inheritance;

import io.github.artificialturtill.myriadascension.cultivation.realm.CultivationRealm;
import io.github.artificialturtill.myriadascension.technique.TechniqueSignature;
import java.util.List;
import net.minecraft.resources.ResourceLocation;

public record CultivationMethodDefinition(
        ResourceLocation id,
        String displayName,
        CultivationRealm maximumSupportedRealm,
        double cultivationSpeedMultiplier,
        double foundationQualityMultiplier,
        double lifespanMultiplier,
        TechniqueSignature signature,
        List<InheritanceRequirement> requirements) {

    public CultivationMethodDefinition {
        requirements = requirements == null ? List.of() : List.copyOf(requirements);
        if (cultivationSpeedMultiplier <= 0.0D
                || foundationQualityMultiplier <= 0.0D
                || lifespanMultiplier <= 0.0D) {
            throw new IllegalArgumentException("Cultivation multipliers must be positive.");
        }
    }
}
