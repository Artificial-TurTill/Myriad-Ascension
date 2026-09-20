package io.github.artificialturtill.myriadascension.clan;

public final class PrimordialisTestudoOffices {
    public static final int CLAN_LEADER_COUNT = 1;
    public static final int ELDER_COUNT = 6;
    public static final int CUSTODIAN_COUNT = 1;

    private PrimordialisTestudoOffices() {
    }

    public static boolean hasFixedSeatCount(PrimordialisTestudoRank rank) {
        return rank == PrimordialisTestudoRank.CLAN_LEADER
                || rank == PrimordialisTestudoRank.ELDER
                || rank == PrimordialisTestudoRank.CUSTODIAN;
    }

    public static int fixedSeatCount(PrimordialisTestudoRank rank) {
        return switch (rank) {
            case CLAN_LEADER -> CLAN_LEADER_COUNT;
            case ELDER -> ELDER_COUNT;
            case CUSTODIAN -> CUSTODIAN_COUNT;
            default -> -1;
        };
    }
}
