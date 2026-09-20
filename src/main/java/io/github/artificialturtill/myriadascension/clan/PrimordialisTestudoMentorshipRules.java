package io.github.artificialturtill.myriadascension.clan;

/**
 * Direct Clan Leader mentorship is exceptional rather than normal progression.
 *
 * <p>Senior Disciple is the current minimum-rank gate. Additional merit,
 * talent, service, quest, or relationship requirements can be layered on later.</p>
 */
public final class PrimordialisTestudoMentorshipRules {
    public static final PrimordialisTestudoRank MINIMUM_RANK_FOR_LEADER_MENTORSHIP =
            PrimordialisTestudoRank.SENIOR_DISCIPLE;

    private PrimordialisTestudoMentorshipRules() {
    }

    public static boolean mayBeConsideredForClanLeaderMentorship(
            PrimordialisTestudoRank rank) {
        return rank.atLeast(MINIMUM_RANK_FOR_LEADER_MENTORSHIP);
    }
}
