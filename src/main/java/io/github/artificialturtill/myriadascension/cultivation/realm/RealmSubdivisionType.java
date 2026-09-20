package io.github.artificialturtill.myriadascension.cultivation.realm;

public enum RealmSubdivisionType {
    NONE(""),
    STAGE("Stage"),
    ORDER("Order"),
    RANK("Rank");

    private final String displayName;

    RealmSubdivisionType(String displayName) {
        this.displayName = displayName;
    }

    public String displayName() {
        return displayName;
    }
}
