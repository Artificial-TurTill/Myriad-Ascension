package io.github.artificialturtill.myriadascension.alignment;

public final class AffiliationRelations {
    private AffiliationRelations() {
    }

    public static AffiliationRelation relation(CultivationAffiliation first, CultivationAffiliation second) {
        if (first == second) {
            return AffiliationRelation.SAME;
        }

        if (first == CultivationAffiliation.UNDECIDED
                || second == CultivationAffiliation.UNDECIDED
                || first == CultivationAffiliation.UNAFFILIATED
                || second == CultivationAffiliation.UNAFFILIATED) {
            return AffiliationRelation.NEUTRAL;
        }

        if (pair(first, second, CultivationAffiliation.RIGHTEOUS, CultivationAffiliation.UNORTHODOX)
                || pair(first, second, CultivationAffiliation.BUDDHIST, CultivationAffiliation.DEMONIC)) {
            return AffiliationRelation.MORTAL_ENEMY;
        }

        if (pair(first, second, CultivationAffiliation.RIGHTEOUS, CultivationAffiliation.BUDDHIST)
                || pair(first, second, CultivationAffiliation.RIGHTEOUS, CultivationAffiliation.IMPERIAL)) {
            return AffiliationRelation.COMPATIBLE;
        }

        if (pair(first, second, CultivationAffiliation.BUDDHIST, CultivationAffiliation.UNORTHODOX)
                || pair(first, second, CultivationAffiliation.RIGHTEOUS, CultivationAffiliation.DEMONIC)
                || pair(first, second, CultivationAffiliation.IMPERIAL, CultivationAffiliation.UNORTHODOX)
                || pair(first, second, CultivationAffiliation.IMPERIAL, CultivationAffiliation.DEMONIC)) {
            return AffiliationRelation.HOSTILE;
        }

        return AffiliationRelation.NEUTRAL;
    }

    private static boolean pair(
            CultivationAffiliation first,
            CultivationAffiliation second,
            CultivationAffiliation left,
            CultivationAffiliation right) {
        return (first == left && second == right) || (first == right && second == left);
    }
}
