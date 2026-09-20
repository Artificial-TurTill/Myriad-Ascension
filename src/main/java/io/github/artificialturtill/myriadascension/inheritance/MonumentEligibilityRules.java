package io.github.artificialturtill.myriadascension.inheritance;

import io.github.artificialturtill.myriadascension.affinity.AffinityProfile;
import io.github.artificialturtill.myriadascension.affinity.AffinityType;
import io.github.artificialturtill.myriadascension.character.CharacterSex;

public final class MonumentEligibilityRules {
    private MonumentEligibilityRules() {
    }

    public static boolean meetsElementRequirement(
            AffinityProfile innate,
            AffinityProfile current,
            InheritanceRequirement requirement) {
        AffinityProfile source = requirement.mode() == AffinityRequirementMode.INNATE ? innate : current;
        return source.get(requirement.affinity()) >= requirement.minimumValue();
    }

    public static int matchingPolarityPotential(CharacterSex sex, AffinityProfile innate) {
        return switch (sex) {
            case MALE -> innate.get(AffinityType.YANG);
            case FEMALE -> innate.get(AffinityType.YIN);
            case UNSET -> 0;
        };
    }

    public static AffinityType matchingPolarity(CharacterSex sex) {
        return switch (sex) {
            case MALE -> AffinityType.YANG;
            case FEMALE -> AffinityType.YIN;
            case UNSET -> throw new IllegalArgumentException("Character sex must be set.");
        };
    }
}
