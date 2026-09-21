package io.github.artificialturtill.myriadascension.technique;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

public final class TechniqueLoadoutState {
    private final EnumMap<TechniqueCategory, String> equipped =
            new EnumMap<>(TechniqueCategory.class);

    public TechniqueLoadoutState() {
        for (TechniqueCategory category : TechniqueCategory.values()) {
            equipped.put(category, "");
        }
    }

    public String equippedId(TechniqueCategory category) {
        return equipped.getOrDefault(category, "");
    }

    /**
     * Equipping always replaces the existing technique in that category.
     * Two techniques from the same category can never be active simultaneously.
     */
    public void equip(TechniqueCategory category, ResourceLocation techniqueId) {
        equipped.put(category, techniqueId == null ? "" : techniqueId.toString());
    }

    public void equipSerialized(TechniqueCategory category, String techniqueId) {
        equipped.put(category, techniqueId == null ? "" : techniqueId);
    }

    public void clear(TechniqueCategory category) {
        equipped.put(category, "");
    }

    public Map<TechniqueCategory, String> view() {
        return Collections.unmodifiableMap(equipped);
    }

    public void copyFrom(TechniqueLoadoutState other) {
        for (TechniqueCategory category : TechniqueCategory.values()) {
            equipped.put(category, other.equippedId(category));
        }
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        for (TechniqueCategory category : TechniqueCategory.values()) {
            tag.putString(category.name(), equippedId(category));
        }
        return tag;
    }

    public void load(CompoundTag tag) {
        for (TechniqueCategory category : TechniqueCategory.values()) {
            equipped.put(category, tag.getString(category.name()));
        }
    }
}
