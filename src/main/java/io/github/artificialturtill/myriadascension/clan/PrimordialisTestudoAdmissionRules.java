package io.github.artificialturtill.myriadascension.clan;

import io.github.artificialturtill.myriadascension.organization.OrganizationStanding;

/**
 * Prototype admission values. They are intentionally data-like tuning constants,
 * not permanent lore constraints.
 */
public final class PrimordialisTestudoAdmissionRules {
    public static final int REQUIRED_SERVICE_MERIT = 3;
    public static final String INITIAL_MEMBER_RANK = "Junior Disciple";

    private PrimordialisTestudoAdmissionRules() {
    }

    public static boolean mayBeAccepted(OrganizationStanding standing) {
        return !standing.member() && standing.serviceMerit() >= REQUIRED_SERVICE_MERIT;
    }

    public static boolean acceptAsJuniorDisciple(OrganizationStanding standing) {
        if (!mayBeAccepted(standing)) {
            return false;
        }

        standing.setMember(true);
        standing.setRank(INITIAL_MEMBER_RANK);
        return true;
    }
}
