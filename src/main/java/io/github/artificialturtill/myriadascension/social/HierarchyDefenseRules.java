package io.github.artificialturtill.myriadascension.social;

import io.github.artificialturtill.myriadascension.alignment.CultivationAffiliation;

public final class HierarchyDefenseRules {
    private HierarchyDefenseRules() {
    }

    /**
     * Returns whether a subordinate should join the defense of a superior.
     *
     * <p>The caller is responsible for establishing that {@code defenderIsSubjectToVictim}
     * represents a real hierarchy relationship, such as disciple -> master or cult member -> leader.
     * Merely being lower-ranked is not enough.</p>
     */
    public static boolean shouldDefendSuperior(
            CultivationAffiliation affiliation,
            boolean defenderIsSubjectToVictim,
            ConflictType conflictType) {

        if (!defenderIsSubjectToVictim) {
            return false;
        }

        if (conflictType == ConflictType.RANK_CHALLENGE
                || conflictType == ConflictType.HUMILIATION_MATCH
                || conflictType == ConflictType.DEATH_MATCH) {
            return false;
        }

        return affiliation == CultivationAffiliation.DEMONIC;
    }

    public static boolean isConflictAllowed(
            CultivationAffiliation affiliation,
            ConflictType conflictType) {

        if (affiliation == CultivationAffiliation.BUDDHIST
                && conflictType == ConflictType.DEATH_MATCH) {
            return false;
        }

        return true;
    }
}
