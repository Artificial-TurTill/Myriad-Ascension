package io.github.artificialturtill.myriadascension.artifact;

import io.github.artificialturtill.myriadascension.alignment.CultivationAffiliation;
import io.github.artificialturtill.myriadascension.cultivation.realm.CultivationRealm;

/**
 * Logical description of a concealment/disguise artifact.
 * Item implementation is intentionally deferred.
 */
public record QiConcealmentArtifact(
        CultivationRealm artifactRealm,
        int artifactMinorStage,
        CultivationAffiliation disguisedAffiliation) {

    public boolean canMaskAgainst(CultivationRealm observerRealm, int observerMinorStage) {
        if (observerRealm.ordinal() < artifactRealm.ordinal()) {
            return true;
        }

        if (observerRealm == artifactRealm) {
            return observerMinorStage <= Math.max(1, artifactMinorStage);
        }

        return false;
    }
}
