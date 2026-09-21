package io.github.artificialturtill.myriadascension.stats;

public enum CultivatorStat {
    STRENGTH("Strength"),
    VITALITY("Vitality"),
    AGILITY("Agility"),
    SPIRITUAL_SENSE("Spiritual Sense"),
    MERIDIAN_QUALITY("Meridian Quality"),
    DANTIAN_QUALITY("Dantian Quality"),
    SOUL_STRENGTH("Soul Strength");

    private final String displayName;

    CultivatorStat(String displayName) {
        this.displayName = displayName;
    }

    public String displayName() {
        return displayName;
    }
}
