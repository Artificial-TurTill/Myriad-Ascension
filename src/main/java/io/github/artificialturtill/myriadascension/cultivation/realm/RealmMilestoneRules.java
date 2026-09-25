package io.github.artificialturtill.myriadascension.cultivation.realm;

/**
 * Progression unlocks modeled from the Martial Peak realm structure while preserving
 * Myriad Ascension's explicit rule that Tempered Body cannot actively use Qi.
 */
public final class RealmMilestoneRules {
    private RealmMilestoneRules() {
    }

    public static boolean canSenseAmbientEnergy(CultivationRealm realm, int subdivision) {
        return realm.ordinal() > CultivationRealm.TEMPERED_BODY.ordinal()
                || (realm == CultivationRealm.TEMPERED_BODY && subdivision >= 4);
    }

    public static boolean canConsciouslyGatherPreQiEnergy(CultivationRealm realm, int subdivision) {
        return realm.ordinal() > CultivationRealm.TEMPERED_BODY.ordinal()
                || (realm == CultivationRealm.TEMPERED_BODY && subdivision >= 7);
    }

    public static boolean canActivelyUseQi(CultivationRealm realm) {
        return realm.ordinal() >= CultivationRealm.INITIAL_ELEMENT.ordinal();
    }

    public static boolean canProjectQiOutsideBody(CultivationRealm realm) {
        return realm.ordinal() >= CultivationRealm.QI_TRANSFORMATION.ordinal();
    }

    public static boolean canFlyByOwnCultivation(CultivationRealm realm) {
        return realm.ordinal() >= CultivationRealm.TRUE_ELEMENT.ordinal();
    }

    public static boolean hasKnowledgeSeaAndDivineSense(CultivationRealm realm) {
        return realm.ordinal() >= CultivationRealm.IMMORTAL_ASCENSION.ordinal();
    }

    public static boolean canReplaceSleepWithCultivation(CultivationRealm realm) {
        return realm.ordinal() >= CultivationRealm.QI_TRANSFORMATION.ordinal();
    }
}
