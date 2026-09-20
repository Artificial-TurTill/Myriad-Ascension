package io.github.artificialturtill.myriadascension.bloodline;

import io.github.artificialturtill.myriadascension.cultivation.realm.CultivationRealm;
import net.minecraft.resources.ResourceLocation;

public record BloodlineGrade(
        ResourceLocation id,
        String displayName,
        CultivationRealm effectiveThroughRealm,
        int sourcePowerTier) {

    public boolean isStrongerThan(BloodlineGrade other) {
        return other == null || sourcePowerTier > other.sourcePowerTier;
    }
}
