package io.github.artificialturtill.myriadascension.item.medicine;

public record MedicineProfile(
        double vanillaHealing,
        double bodyInjuryReduction,
        double meridianInjuryReduction,
        double soulInjuryReduction,
        double recoveryDebtReduction,
        double meridianLoadReduction,
        double fatigueReduction,
        double impurityGain) {
}
