package io.github.artificialturtill.myriadascension.technique;

public final class ImperialTechniqueRules {
    private ImperialTechniqueRules() {
    }

    public static boolean receivesImperialBonus(
            TechniqueSignature signature,
            boolean userBelongsToEmpire,
            boolean targetIsDeclaredEnemyOfEmpire) {
        return signature.hasPath(TechniquePathTrait.IMPERIAL)
                && userBelongsToEmpire
                && targetIsDeclaredEnemyOfEmpire;
    }
}
