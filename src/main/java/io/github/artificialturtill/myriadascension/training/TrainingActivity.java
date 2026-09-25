package io.github.artificialturtill.myriadascension.training;

public enum TrainingActivity {
    FOUNDATION_STANCE("Foundation Stance"),
    CONTROLLED_BREATHING("Controlled Breathing"),
    SPRINTING("Running"),
    SWIMMING("Swimming"),
    CLIMBING("Climbing"),
    WEIGHTED_MOVEMENT("Weighted Movement"),
    STRIKING("Striking"),
    COMBAT("Combat"),
    DEFENSIVE_STRESS("Defensive Stress"),
    RECOVERY("Recovery"),
    WORLD_ENERGY_FOCUS("World Energy Focus"),
    NATURAL_ABSORPTION("Natural Absorption");

    private final String displayName;

    TrainingActivity(String displayName) {
        this.displayName = displayName;
    }

    public String displayName() {
        return displayName;
    }
}
