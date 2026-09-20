package io.github.artificialturtill.myriadascension.clan;

public final class PrimordialisTestudoGovernanceRules {
    private PrimordialisTestudoGovernanceRules() {
    }

    public static boolean mayIssueClanWideCommands(PrimordialisTestudoRank rank) {
        return rank == PrimordialisTestudoRank.CLAN_LEADER;
    }

    public static boolean receivesLeaderCommandsInCouncil(PrimordialisTestudoRank rank) {
        return rank == PrimordialisTestudoRank.ELDER;
    }

    public static boolean isSeniorAdministrativeOffice(PrimordialisTestudoRank rank) {
        return rank == PrimordialisTestudoRank.CLAN_LEADER
                || rank == PrimordialisTestudoRank.ELDER
                || rank == PrimordialisTestudoRank.CUSTODIAN;
    }
}
