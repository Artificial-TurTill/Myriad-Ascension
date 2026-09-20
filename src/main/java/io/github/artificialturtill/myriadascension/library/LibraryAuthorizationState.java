package io.github.artificialturtill.myriadascension.library;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

public final class LibraryAuthorizationState {
    private final LinkedHashMap<String, String> authorizedBookByOrganization = new LinkedHashMap<>();

    public String authorizedBookId(String organizationId) {
        return authorizedBookByOrganization.getOrDefault(organizationId, "");
    }

    public boolean isAuthorized(String organizationId, ResourceLocation bookId) {
        return bookId != null && bookId.toString().equals(authorizedBookId(organizationId));
    }

    /**
     * Replaces any prior authorization for this organization.
     * A player can therefore only have one verified take-out book per library at a time.
     */
    public void authorize(String organizationId, ResourceLocation bookId) {
        if (organizationId == null || organizationId.isBlank() || bookId == null) {
            return;
        }
        authorizedBookByOrganization.put(organizationId, bookId.toString());
    }

    public void revoke(String organizationId) {
        authorizedBookByOrganization.remove(organizationId);
    }

    public Map<String, String> view() {
        return Collections.unmodifiableMap(authorizedBookByOrganization);
    }

    public void copyFrom(LibraryAuthorizationState other) {
        authorizedBookByOrganization.clear();
        authorizedBookByOrganization.putAll(other.authorizedBookByOrganization);
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        authorizedBookByOrganization.forEach(tag::putString);
        return tag;
    }

    public void load(CompoundTag tag) {
        authorizedBookByOrganization.clear();
        for (String organizationId : tag.getAllKeys()) {
            String bookId = tag.getString(organizationId);
            if (!bookId.isBlank()) {
                authorizedBookByOrganization.put(organizationId, bookId);
            }
        }
    }
}
