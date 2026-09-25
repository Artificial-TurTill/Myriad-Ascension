package io.github.artificialturtill.myriadascension.inheritance;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;

public final class CultivationMethodState {
    private final LinkedHashSet<String> knownMethods = new LinkedHashSet<>();
    private String activeMethod = "";

    public Set<String> knownMethods() {
        return Collections.unmodifiableSet(knownMethods);
    }

    public boolean knows(ResourceLocation id) {
        return id != null && knownMethods.contains(id.toString());
    }

    public boolean learn(ResourceLocation id) {
        return id != null && knownMethods.add(id.toString());
    }

    public boolean forget(ResourceLocation id) {
        if (id == null) {
            return false;
        }
        boolean removed = knownMethods.remove(id.toString());
        if (activeMethod.equals(id.toString())) {
            activeMethod = "";
        }
        return removed;
    }

    public boolean setActive(ResourceLocation id) {
        if (id == null || !knows(id)) {
            return false;
        }
        activeMethod = id.toString();
        return true;
    }

    public boolean isActive(ResourceLocation id) {
        return id != null && activeMethod.equals(id.toString());
    }

    public String activeMethodId() {
        return activeMethod;
    }

    public void clearActive() {
        activeMethod = "";
    }

    public void copyFrom(CultivationMethodState other) {
        knownMethods.clear();
        knownMethods.addAll(other.knownMethods);
        activeMethod = other.activeMethod;
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.putString("Active", activeMethod);

        ListTag known = new ListTag();
        for (String id : knownMethods) {
            known.add(StringTag.valueOf(id));
        }
        tag.put("Known", known);
        return tag;
    }

    public void load(CompoundTag tag) {
        knownMethods.clear();
        activeMethod = tag.getString("Active");

        ListTag known = tag.getList("Known", Tag.TAG_STRING);
        for (int i = 0; i < known.size(); i++) {
            String id = known.getString(i);
            if (!id.isBlank()) {
                knownMethods.add(id);
            }
        }

        if (!activeMethod.isBlank() && !knownMethods.contains(activeMethod)) {
            activeMethod = "";
        }
    }
}
