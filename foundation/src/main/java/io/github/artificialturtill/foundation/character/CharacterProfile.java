package io.github.artificialturtill.foundation.character;

import io.github.artificialturtill.foundation.api.FoundationApi;
import io.github.artificialturtill.foundation.content.ContentSet;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

/**
 * The player attachment (Dragonblock C+ CharacterData). Without a created character the player is Natural in the
 * JRMCore sense: plain vanilla, no TP, nothing additional. A character whose race / power type / class is no longer
 * installed (an add-on was removed) is kept untouched as a dormant save and comes back when the add-on returns.
 */
public final class CharacterProfile {
    public static final int SCHEMA = 1;
    private Fighter fighter;
    private CompoundTag dormant = new CompoundTag();
    private long burstTick = Long.MIN_VALUE;
    private int burstCount;

    public boolean hasCharacter() { return fighter != null; }
    public Fighter fighter() {
        if (fighter == null) throw new IllegalArgumentException("Create a character first (V).");
        return fighter;
    }
    public Fighter fighterOrNull() { return fighter; }
    public boolean dormant() { return fighter == null && !dormant.isEmpty(); }
    public CompoundTag dormantData() { return dormant.copy(); }

    public void create(ContentSet content, String race, String powerType, String characterClass, Appearance look) {
        if (fighter != null || !dormant.isEmpty()) throw new IllegalArgumentException("A character already exists.");
        fighter = Fighter.create(content, race, powerType, characterClass, look);
    }
    /** Erase: the character is removed and the player is Natural again (dormant data is dropped too). */
    public void erase() { fighter = null; dormant = new CompoundTag(); }

    /** At most 4 menu clicks per tick are accepted (V screen bursts), like Dragonblock C+. */
    public boolean acceptClick(long tick) {
        if (tick != burstTick) { burstTick = tick; burstCount = 0; }
        return ++burstCount <= 4;
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("schema", SCHEMA);
        if (fighter != null) tag.put("fighter", fighter.save());
        else if (!dormant.isEmpty()) tag.put("fighter", dormant.copy());
        return tag;
    }
    public static CharacterProfile load(CompoundTag tag) { return load(FoundationApi.content(), tag); }
    public static CharacterProfile load(ContentSet content, CompoundTag tag) {
        int schema = tag.getInt("schema");
        if (schema > SCHEMA) throw new IllegalArgumentException("Unsupported Foundation character schema " + schema + "; expected " + SCHEMA);
        CharacterProfile profile = new CharacterProfile();
        if (tag.contains("fighter", Tag.TAG_COMPOUND)) {
            CompoundTag data = tag.getCompound("fighter");
            profile.fighter = Fighter.load(content, data);
            if (profile.fighter == null) profile.dormant = data.copy();
        }
        return profile;
    }
}
