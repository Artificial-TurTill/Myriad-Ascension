package io.github.artificialturtill.myriadascension.organization;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import net.minecraft.nbt.CompoundTag;

public final class OrganizationStandingState {
    private final LinkedHashMap<String, OrganizationStanding> standings = new LinkedHashMap<>();

    public OrganizationStanding getOrCreate(String organizationId) {
        return standings.computeIfAbsent(organizationId, id -> new OrganizationStanding());
    }

    public Map<String, OrganizationStanding> view() {
        return Collections.unmodifiableMap(standings);
    }

    public void copyFrom(OrganizationStandingState other) {
        standings.clear();
        other.standings.forEach((id, standing) -> standings.put(id, standing.copy()));
    }

    public CompoundTag save() {
        CompoundTag root = new CompoundTag();

        standings.forEach((id, standing) -> {
            CompoundTag tag = new CompoundTag();
            tag.putInt("ServiceMerit", standing.serviceMerit());
            tag.putBoolean("Member", standing.member());
            tag.putString("Rank", standing.rank());
            root.put(id, tag);
        });

        return root;
    }

    public void load(CompoundTag root) {
        standings.clear();

        for (String id : root.getAllKeys()) {
            CompoundTag tag = root.getCompound(id);
            OrganizationStanding standing = new OrganizationStanding();
            standing.addServiceMerit(tag.getInt("ServiceMerit"));
            standing.setMember(tag.getBoolean("Member"));
            standing.setRank(tag.getString("Rank"));
            standings.put(id, standing);
        }
    }
}
