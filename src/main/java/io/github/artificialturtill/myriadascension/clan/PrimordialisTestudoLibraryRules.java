package io.github.artificialturtill.myriadascension.clan;

import io.github.artificialturtill.myriadascension.library.ClanLibraryBookDefinition;
import io.github.artificialturtill.myriadascension.library.LibraryAccessResult;
import io.github.artificialturtill.myriadascension.organization.OrganizationStanding;

public final class PrimordialisTestudoLibraryRules {
    private PrimordialisTestudoLibraryRules() {
    }

    public static boolean mayVerifyBooks(PrimordialisTestudoRank actorRank) {
        return actorRank == PrimordialisTestudoRank.CUSTODIAN
                || actorRank == PrimordialisTestudoRank.ELDER
                || actorRank == PrimordialisTestudoRank.CLAN_LEADER;
    }

    public static LibraryAccessResult canUseVerifiedBook(
            OrganizationStanding standing,
            PrimordialisTestudoRank memberRank,
            ClanLibraryBookDefinition book,
            boolean isCurrentlyVerified) {

        if (standing == null || !standing.member()) {
            return LibraryAccessResult.NOT_A_MEMBER;
        }

        if (book == null) {
            return LibraryAccessResult.BOOK_NOT_FOUND;
        }

        if (!memberRank.atLeast(book.minimumRank())) {
            return LibraryAccessResult.RANK_TOO_LOW;
        }

        if (!isCurrentlyVerified) {
            return LibraryAccessResult.NOT_SELECTED_BY_OVERSEER;
        }

        return LibraryAccessResult.AUTHORIZED;
    }
}
