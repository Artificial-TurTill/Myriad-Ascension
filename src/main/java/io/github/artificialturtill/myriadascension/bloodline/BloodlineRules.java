package io.github.artificialturtill.myriadascension.bloodline;

public final class BloodlineRules {
    private BloodlineRules() {
    }

    public static BloodlineApplicationResult apply(
            BloodlineState current,
            BloodlineLineage incomingLineage,
            BloodlineGrade incomingGrade,
            BloodlineSource source) {

        double effectivePurity = purityFor(source);

        if (!current.hasBloodline()) {
            current.install(incomingLineage, incomingGrade, effectivePurity);
            return BloodlineApplicationResult.INSTALLED;
        }

        if (current.isSameFamily(incomingLineage)) {
            if (incomingGrade.sourcePowerTier() > current.gradePowerTier()) {
                current.overwrite(incomingLineage, incomingGrade, effectivePurity);
                return BloodlineApplicationResult.OVERWRITTEN_BY_STRONGER_SAME_LINEAGE;
            }

            if (incomingGrade.sourcePowerTier() == current.gradePowerTier()
                    && effectivePurity > current.purity()) {
                current.refinePurity(effectivePurity);
                return BloodlineApplicationResult.REFINED_SAME_LINEAGE;
            }

            return BloodlineApplicationResult.REJECTED_WEAKER_OR_IMPURE_SAME_LINEAGE;
        }

        // Different beast families do not overwrite the established lineage.
        // The attempted refinement damages the bloodline instead.
        double gradePressure = Math.max(1.0D, incomingGrade.sourcePowerTier());
        current.addConflictDamage(10.0D * gradePressure);
        return BloodlineApplicationResult.INCOMPATIBLE_LINEAGE_CONFLICT;
    }

    public static double purityFor(BloodlineSource source) {
        double sourcePurity = source.purity();

        return switch (source.acquisitionType()) {
            case NATURAL_LAST_BREATH -> Math.min(100.0D, sourcePurity);
            case SPIRIT_PACT -> Math.min(100.0D, sourcePurity * 0.95D);
            case BEAST_KILL -> Math.min(100.0D, sourcePurity * 0.70D);
            case NPC_FAMILY_INHERITANCE -> Math.min(100.0D, sourcePurity * 0.90D);
            case QUEST, ANCIENT_CULTIVATOR_TOMB, MONUMENT, AUCTION -> sourcePurity;
        };
    }
}
