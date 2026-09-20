package io.github.artificialturtill.myriadascension.cultivation.qi;

import io.github.artificialturtill.myriadascension.technique.TechniquePathTrait;
import io.github.artificialturtill.myriadascension.technique.TechniqueSignature;

public final class QiNatureRules {
    private QiNatureRules() {
    }

    /**
     * Applies a small amount of a practiced technique's path nature to the cultivator's Qi.
     * Exact accumulation rates are balance values and should be supplied by technique data later.
     */
    public static void imprintTechnique(
            QiNatureProfile profile,
            TechniqueSignature signature,
            double intensity) {
        double safeIntensity = Math.max(0.0D, intensity);
        for (TechniquePathTrait trait : signature.pathTraits()) {
            if (trait != TechniquePathTrait.NEUTRAL) {
                profile.add(trait, safeIntensity);
            }
        }
    }

    public static boolean carriesDemonicTrait(QiNatureProfile profile) {
        return profile.get(TechniquePathTrait.DEMONIC) > 0.0D;
    }

    public static boolean carriesBuddhistTrait(QiNatureProfile profile) {
        return profile.get(TechniquePathTrait.BUDDHIST) > 0.0D;
    }

    public static boolean carriesUnorthodoxTrait(QiNatureProfile profile) {
        return profile.get(TechniquePathTrait.UNORTHODOX) > 0.0D;
    }
}
