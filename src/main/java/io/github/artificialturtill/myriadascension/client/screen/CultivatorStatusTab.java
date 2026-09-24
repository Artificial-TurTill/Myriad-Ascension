package io.github.artificialturtill.myriadascension.client.screen;

public enum CultivatorStatusTab {
    OVERVIEW("Overview"),
    STATS("Stats"),
    AFFINITIES("Affinities"),
    SKILLS("Skills"),
    TECHNIQUES("Techniques"),
    ABILITIES("Abilities"),
    CONDITIONS("Conditions");

    private final String displayName;

    CultivatorStatusTab(String displayName) {
        this.displayName = displayName;
    }

    public String displayName() {
        return displayName;
    }
}
