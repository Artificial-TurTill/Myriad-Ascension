package io.github.artificialturtill.myriadascension.affinity;

import io.github.artificialturtill.myriadascension.cultivation.realm.CultivationRealm;

public final class ElementalAttunementTrainingRules {
    private ElementalAttunementTrainingRules() {
    }

    /**
     * Tempered Body cultivators cannot practice true elemental Qi arts yet,
     * but aligned environments/materials may influence future/current attunement.
     */
    public static boolean mayBenefitFromAlignedEnvironment(CultivationRealm realm) {
        return realm.ordinal() >= CultivationRealm.TEMPERED_BODY.ordinal();
    }

    public static boolean mayPracticeActiveElementalQiArts(CultivationRealm realm) {
        return realm.ordinal() >= CultivationRealm.INITIAL_ELEMENT.ordinal();
    }
}
