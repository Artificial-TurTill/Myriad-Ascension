package io.github.artificialturtill.myriadascension.training;

public enum BodyTemperingVector {
    STRENGTH("Strength"),
    ENDURANCE("Endurance"),
    TOUGHNESS("Toughness"),
    COORDINATION("Coordination"),
    STABILITY("Stability"),
    BREATH_CONTROL("Breath Control"),
    RECOVERY("Recovery"),
    VESSEL_DEVELOPMENT("Vessel Development"),
    WORLD_ENERGY_PERCEPTION("World Energy Perception");

    private final String displayName;

    BodyTemperingVector(String displayName) {
        this.displayName = displayName;
    }

    public String displayName() {
        return displayName;
    }
}
