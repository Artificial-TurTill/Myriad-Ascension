package io.github.artificialturtill.myriadascension.bloodline;

import io.github.artificialturtill.myriadascension.cultivation.realm.CultivationRealm;

public final class PrimordialTortoiseLegacy {
    /**
     * Future inheritance ceiling tied to the ancient era when spirit beasts
     * were the dominant species.
     */
    public static final CultivationRealm LEGACY_REALM = CultivationRealm.OPEN_HEAVEN;
    public static final int LEGACY_OPEN_HEAVEN_RANK = 9;

    private PrimordialTortoiseLegacy() {
    }

    public static boolean isAtLegacyPeak(CultivationRealm realm, int subdivision) {
        return realm == LEGACY_REALM && subdivision >= LEGACY_OPEN_HEAVEN_RANK;
    }
}
