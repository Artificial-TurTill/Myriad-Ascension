package io.github.artificialturtill.myriadascension.bloodline;

import io.github.artificialturtill.myriadascension.clan.PrimordialisTestudoClan;
import io.github.artificialturtill.myriadascension.cultivation.data.CultivatorData;
import io.github.artificialturtill.myriadascension.cultivation.realm.CultivationRealm;

public final class TortoiseBloodlineSynergy {
    private TortoiseBloodlineSynergy() {
    }

    public static boolean applies(CultivatorData data) {
        return data.bloodline().hasBloodline()
                && data.bloodline().lineageFamilyId()
                        .equals(PrimordialTortoiseBloodline.TORTOISE_FAMILY_ID.toString())
                && data.cultivationMethods().isActive(
                        PrimordialisTestudoClan.PRIMORDIAL_TESTUDO_LONGEVITY_ART.id());
    }

    public static double cultivationSpeedMultiplier(CultivatorData data) {
        if (!applies(data)) {
            return 1.0D;
        }

        // Deliberately conservative prototype scaling. Purity matters.
        return 1.0D + (data.bloodline().purity() / 100.0D) * 0.35D;
    }

    public static boolean bloodlinePowerStillKeepsPace(CultivatorData data) {
        if (!data.bloodline().hasBloodline()) {
            return false;
        }

        CultivationRealm cap = CultivationRealm.fromSerializedName(
                data.bloodline().effectiveThroughRealm());
        return data.realm().ordinal() <= cap.ordinal();
    }
}
