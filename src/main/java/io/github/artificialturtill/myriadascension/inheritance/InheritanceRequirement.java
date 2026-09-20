package io.github.artificialturtill.myriadascension.inheritance;

import io.github.artificialturtill.myriadascension.affinity.AffinityType;

public record InheritanceRequirement(
        AffinityType affinity,
        int minimumValue,
        AffinityRequirementMode mode) {

    public InheritanceRequirement {
        if (minimumValue < 0) {
            throw new IllegalArgumentException("minimumValue must be >= 0");
        }
    }
}
