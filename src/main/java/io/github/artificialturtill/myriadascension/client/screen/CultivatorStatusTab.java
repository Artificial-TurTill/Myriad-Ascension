package io.github.artificialturtill.myriadascension.client.screen;

public enum CultivatorStatusTab {
    OVERVIEW("Overview"),
    STATS_AFFINITIES("Stats & Affinities"),
    SKILLS("Skills"),
    TECHNIQUES("Techniques");

    private final String displayName;

    CultivatorStatusTab(String displayName) {
        this.displayName = displayName;
    }

    public String displayName() {
        return displayName;
    }
}
