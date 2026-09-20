package io.github.artificialturtill.myriadascension.library;

import io.github.artificialturtill.myriadascension.clan.PrimordialisTestudoRank;
import net.minecraft.resources.ResourceLocation;

public record ClanLibraryBookDefinition(
        ResourceLocation id,
        String displayName,
        LibraryBookCategory category,
        PrimordialisTestudoRank minimumRank,
        ResourceLocation techniqueId) {

    public boolean isTechniqueManual() {
        return category == LibraryBookCategory.TECHNIQUE && techniqueId != null;
    }
}
