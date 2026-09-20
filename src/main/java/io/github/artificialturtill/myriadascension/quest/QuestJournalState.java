package io.github.artificialturtill.myriadascension.quest;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;

public final class QuestJournalState {
    private final LinkedHashSet<String> active = new LinkedHashSet<>();
    private final LinkedHashMap<String, Integer> completionCounts = new LinkedHashMap<>();

    public boolean accept(ResourceLocation questId) {
        if (questId == null) {
            return false;
        }
        return active.add(questId.toString());
    }

    public boolean isActive(ResourceLocation questId) {
        return questId != null && active.contains(questId.toString());
    }

    public boolean complete(ResourceLocation questId) {
        if (questId == null || !active.remove(questId.toString())) {
            return false;
        }

        completionCounts.merge(questId.toString(), 1, Integer::sum);
        return true;
    }

    public int completionCount(ResourceLocation questId) {
        return questId == null ? 0 : completionCounts.getOrDefault(questId.toString(), 0);
    }

    public Set<String> activeQuestIds() {
        return Collections.unmodifiableSet(active);
    }

    public Map<String, Integer> completionCounts() {
        return Collections.unmodifiableMap(completionCounts);
    }

    public void copyFrom(QuestJournalState other) {
        active.clear();
        active.addAll(other.active);
        completionCounts.clear();
        completionCounts.putAll(other.completionCounts);
    }

    public CompoundTag save() {
        CompoundTag root = new CompoundTag();

        ListTag activeList = new ListTag();
        for (String id : active) {
            activeList.add(StringTag.valueOf(id));
        }
        root.put("Active", activeList);

        CompoundTag completed = new CompoundTag();
        completionCounts.forEach(completed::putInt);
        root.put("Completed", completed);

        return root;
    }

    public void load(CompoundTag root) {
        active.clear();
        completionCounts.clear();

        ListTag activeList = root.getList("Active", Tag.TAG_STRING);
        for (int i = 0; i < activeList.size(); i++) {
            String id = activeList.getString(i);
            if (!id.isBlank()) {
                active.add(id);
            }
        }

        CompoundTag completed = root.getCompound("Completed");
        for (String id : completed.getAllKeys()) {
            completionCounts.put(id, Math.max(0, completed.getInt(id)));
        }
    }
}
