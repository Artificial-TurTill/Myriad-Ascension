package io.github.artificialturtill.myriadascension.clan;

public enum PrimordialisTestudoRank {
    SERVANT(0),
    JUNIOR_DISCIPLE(10),
    SENIOR_DISCIPLE(20),
    MASTER(30),
    CUSTODIAN(40),
    ELDER(50),
    CLAN_LEADER(60);

    private final int authority;

    PrimordialisTestudoRank(int authority) {
        this.authority = authority;
    }

    public int authority() {
        return authority;
    }

    public boolean atLeast(PrimordialisTestudoRank other) {
        return authority >= other.authority;
    }

    public String displayName() {
        return switch (this) {
            case SERVANT -> "Servant";
            case JUNIOR_DISCIPLE -> "Junior Disciple";
            case SENIOR_DISCIPLE -> "Senior Disciple";
            case MASTER -> "Master";
            case CUSTODIAN -> "Custodian";
            case ELDER -> "Elder";
            case CLAN_LEADER -> "Clan Leader";
        };
    }

    public static PrimordialisTestudoRank fromStandingRank(String value) {
        if (value == null || value.isBlank()) {
            return SERVANT;
        }

        for (PrimordialisTestudoRank rank : values()) {
            if (rank.displayName().equalsIgnoreCase(value)) {
                return rank;
            }
        }

        return SERVANT;
    }
}
