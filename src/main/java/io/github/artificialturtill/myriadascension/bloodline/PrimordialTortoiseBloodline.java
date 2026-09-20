package io.github.artificialturtill.myriadascension.bloodline;

import io.github.artificialturtill.myriadascension.MyriadAscension;
import io.github.artificialturtill.myriadascension.cultivation.realm.CultivationRealm;
import java.util.EnumSet;
import java.util.Set;
import net.minecraft.resources.ResourceLocation;

public final class PrimordialTortoiseBloodline {
    public static final ResourceLocation TORTOISE_FAMILY_ID =
            ResourceLocation.fromNamespaceAndPath(MyriadAscension.MOD_ID, "tortoise");

    public static final BloodlineLineage LINEAGE =
            new BloodlineLineage(
                    ResourceLocation.fromNamespaceAndPath(
                            MyriadAscension.MOD_ID,
                            "primordial_tortoise"),
                    "Primordial Tortoise Bloodline",
                    TORTOISE_FAMILY_ID);

    /**
     * The currently defined test grade.
     *
     * <p>Earth Rank is useful through Saint. It remains part of the character
     * above Saint, but its power no longer keeps pace with higher realms.</p>
     */
    public static final BloodlineGrade EARTH_RANK =
            new BloodlineGrade(
                    ResourceLocation.fromNamespaceAndPath(
                            MyriadAscension.MOD_ID,
                            "earth_rank"),
                    "Earth Rank",
                    CultivationRealm.SAINT,
                    1);

    public static final BloodlinePhenotype PHENOTYPE = BloodlinePhenotype.INTERNAL_ONLY;

    public static final Set<BloodlineBenefitType> BENEFITS = Set.copyOf(EnumSet.of(
            BloodlineBenefitType.LONGEVITY,
            BloodlineBenefitType.DEFENSE,
            BloodlineBenefitType.REGENERATION,
            BloodlineBenefitType.EARTH_COMPATIBILITY,
            BloodlineBenefitType.RESISTANCE,
            BloodlineBenefitType.SHELL_TECHNIQUE_COMPATIBILITY,
            BloodlineBenefitType.BREAKTHROUGH_STABILITY));

    private PrimordialTortoiseBloodline() {
    }
}
