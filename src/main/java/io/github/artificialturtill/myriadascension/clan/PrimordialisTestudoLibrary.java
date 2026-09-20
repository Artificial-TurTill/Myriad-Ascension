package io.github.artificialturtill.myriadascension.clan;

import io.github.artificialturtill.myriadascension.MyriadAscension;
import io.github.artificialturtill.myriadascension.library.ClanLibraryBookDefinition;
import io.github.artificialturtill.myriadascension.library.LibraryBookCategory;
import java.util.List;
import net.minecraft.resources.ResourceLocation;

public final class PrimordialisTestudoLibrary {
    public static final String ORGANIZATION_ID = "myriad_ascension:primordialis_testudo_clan";

    public static final ClanLibraryBookDefinition CLAN_HISTORY =
            book("clan_history", "Annals of the Primordialis Testudo Clan",
                    LibraryBookCategory.HISTORY, PrimordialisTestudoRank.SERVANT, null);

    public static final ClanLibraryBookDefinition CULTIVATION_FUNDAMENTALS =
            book("cultivation_fundamentals", "Fundamentals of the Cultivation Path",
                    LibraryBookCategory.FUNDAMENTALS, PrimordialisTestudoRank.JUNIOR_DISCIPLE, null);

    public static final ClanLibraryBookDefinition EARTH_QI_FACTS =
            book("earth_qi_facts", "Observations on Earth-Aligned Spiritual Energy",
                    LibraryBookCategory.FACTS, PrimordialisTestudoRank.JUNIOR_DISCIPLE, null);

    /**
     * Sparse prototype technique shelf. More techniques should be added deliberately,
     * not procedurally filled just to give players free choice.
     */
    public static final List<ClanLibraryBookDefinition> TECHNIQUE_MANUALS = List.of();

    public static final List<ClanLibraryBookDefinition> GENERAL_BOOKS = List.of(
            CLAN_HISTORY,
            CULTIVATION_FUNDAMENTALS,
            EARTH_QI_FACTS);

    private PrimordialisTestudoLibrary() {
    }

    private static ClanLibraryBookDefinition book(
            String path,
            String displayName,
            LibraryBookCategory category,
            PrimordialisTestudoRank minimumRank,
            ResourceLocation techniqueId) {
        return new ClanLibraryBookDefinition(
                ResourceLocation.fromNamespaceAndPath(
                        MyriadAscension.MOD_ID,
                        "primordialis_testudo/library/" + path),
                displayName,
                category,
                minimumRank,
                techniqueId);
    }
}
