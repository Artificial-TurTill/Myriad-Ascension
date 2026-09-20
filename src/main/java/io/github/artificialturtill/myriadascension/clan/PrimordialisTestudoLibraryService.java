package io.github.artificialturtill.myriadascension.clan;

import io.github.artificialturtill.myriadascension.cultivation.data.CultivatorData;
import io.github.artificialturtill.myriadascension.library.ClanLibraryBookDefinition;
import io.github.artificialturtill.myriadascension.library.LibraryAccessResult;
import io.github.artificialturtill.myriadascension.organization.OrganizationStanding;

public final class PrimordialisTestudoLibraryService {
    private PrimordialisTestudoLibraryService() {
    }

    public static LibraryAccessResult authorizeTakeout(
            CultivatorData memberData,
            PrimordialisTestudoRank overseerRank,
            ClanLibraryBookDefinition book) {

        if (!PrimordialisTestudoLibraryRules.mayVerifyBooks(overseerRank)) {
            return LibraryAccessResult.NOT_SELECTED_BY_OVERSEER;
        }

        OrganizationStanding standing = memberData.organizationStandings()
                .getOrCreate(PrimordialisTestudoLibrary.ORGANIZATION_ID);

        PrimordialisTestudoRank memberRank =
                PrimordialisTestudoRank.fromStandingRank(standing.rank());

        LibraryAccessResult access = PrimordialisTestudoLibraryRules.canUseVerifiedBook(
                standing,
                memberRank,
                book,
                true);

        if (access != LibraryAccessResult.AUTHORIZED) {
            return access;
        }

        // This deliberately replaces any earlier book authorization.
        memberData.libraryAuthorizations().authorize(
                PrimordialisTestudoLibrary.ORGANIZATION_ID,
                book.id());

        return LibraryAccessResult.AUTHORIZED;
    }

    public static LibraryAccessResult canUseCarriedBook(
            CultivatorData memberData,
            ClanLibraryBookDefinition book) {

        OrganizationStanding standing = memberData.organizationStandings()
                .getOrCreate(PrimordialisTestudoLibrary.ORGANIZATION_ID);

        PrimordialisTestudoRank memberRank =
                PrimordialisTestudoRank.fromStandingRank(standing.rank());

        boolean verified = memberData.libraryAuthorizations().isAuthorized(
                PrimordialisTestudoLibrary.ORGANIZATION_ID,
                book.id());

        return PrimordialisTestudoLibraryRules.canUseVerifiedBook(
                standing,
                memberRank,
                book,
                verified);
    }

    public static void returnAuthorizedBook(CultivatorData memberData) {
        memberData.libraryAuthorizations().revoke(
                PrimordialisTestudoLibrary.ORGANIZATION_ID);
    }
}
