package io.github.artificialturtill.myriadascension.technique;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;

public final class TechniqueKnowledgeState {
    private final LinkedHashSet<String> knownTechniques = new LinkedHashSet<>();

    public boolean knows(ResourceLocation techniqueId) {
        return techniqueId != null && knownTechniques.contains(techniqueId.toString());
    }

    public boolean learn(ResourceLocation techniqueId) {
        return techniqueId != null && knownTechniques.add(techniqueId.toString());
    }

    public Set<String> view() {
        return Collections.unmodifiableSet(knownTechniques);
    }

    public void copyFrom(TechniqueKnowledgeState other) {
        knownTechniques.clear();
        knownTechniques.addAll(other.knownTechniques);
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        ListTag known = new ListTag();
        for (String id : knownTechniques) {
            known.add(StringTag.valueOf(id));
        }
        tag.put("Known", known);
        return tag;
    }

    public void load(CompoundTag tag) {
        knownTechniques.clear();
        ListTag known = tag.getList("Known", Tag.TAG_STRING);
        for (int i = 0; i < known.size(); i++) {
            String id = known.getString(i);
            if (!id.isBlank()) {
                knownTechniques.add(id);
            }
        }
    }
}
