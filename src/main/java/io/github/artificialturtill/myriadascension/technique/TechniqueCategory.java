package io.github.artificialturtill.myriadascension.technique;

public enum TechniqueCategory {
    CULTIVATION("Cultivation"),
    FOOTWORK("Footwork"),
    WEAPON("Weapon"),
    EYESIGHT("Eyesight");

    private final String displayName;

    TechniqueCategory(String displayName) {
        this.displayName = displayName;
    }

    public String displayName() {
        return displayName;
    }
}
