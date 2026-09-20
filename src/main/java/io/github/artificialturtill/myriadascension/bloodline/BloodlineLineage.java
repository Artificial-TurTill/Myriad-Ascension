package io.github.artificialturtill.myriadascension.bloodline;

import net.minecraft.resources.ResourceLocation;

public record BloodlineLineage(
        ResourceLocation id,
        String displayName,
        ResourceLocation familyId) {

    public boolean isSameFamily(BloodlineLineage other) {
        return other != null && familyId.equals(other.familyId);
    }
}
