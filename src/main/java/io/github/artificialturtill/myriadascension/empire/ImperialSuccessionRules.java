package io.github.artificialturtill.myriadascension.empire;

public final class ImperialSuccessionRules {
    private ImperialSuccessionRules() {
    }

    public static boolean deathCanEnablePlayerUsurpation(ImperialDeathContext context) {
        return context == ImperialDeathContext.POLITICAL_BATTLE
                || context == ImperialDeathContext.ENEMY_ATTACK;
    }

    public static boolean mayClaimThrone(
            boolean emperorDeadInQualifyingBattle,
            boolean crownPrinceDeadInQualifyingBattle,
            boolean playerIsStrongestEligibleCultivatorInEmpire) {
        return emperorDeadInQualifyingBattle
                && crownPrinceDeadInQualifyingBattle
                && playerIsStrongestEligibleCultivatorInEmpire;
    }
}
