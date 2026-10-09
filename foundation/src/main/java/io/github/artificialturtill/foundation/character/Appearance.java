package io.github.artificialturtill.foundation.character;

import io.github.artificialturtill.foundation.content.AppearanceOptions;
import io.github.artificialturtill.foundation.content.Race;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

/**
 * The look chosen at character creation (JRMCore jrmcDNS / jrmcDNSH concept): gender, skin colour, the custom
 * hairstyle code ({@link HairCode}, "" = bald) and its colour (any 0xRRGGBB, picked with the colour picker), eye
 * colour, body type and height (percent).
 */
public record Appearance(int gender, int skinColor, String hair, int hairColor, int eyeColor, int bodyType, int height) {
    public static final int MALE = 0, FEMALE = 1;
    public static final Appearance DEFAULT = new Appearance(MALE, 0xF2C9A0, HairCode.DEFAULT, 0x3B2414, 0x2B1B0E, 0, 100);

    public static final StreamCodec<ByteBuf, Appearance> CODEC = new StreamCodec<>() {
        @Override public Appearance decode(ByteBuf buf) {
            return new Appearance(ByteBufCodecs.VAR_INT.decode(buf), ByteBufCodecs.INT.decode(buf), ByteBufCodecs.stringUtf8(HairCode.LENGTH).decode(buf),
                ByteBufCodecs.INT.decode(buf), ByteBufCodecs.INT.decode(buf), ByteBufCodecs.VAR_INT.decode(buf), ByteBufCodecs.VAR_INT.decode(buf));
        }
        @Override public void encode(ByteBuf buf, Appearance a) {
            ByteBufCodecs.VAR_INT.encode(buf, a.gender); ByteBufCodecs.INT.encode(buf, a.skinColor); ByteBufCodecs.stringUtf8(HairCode.LENGTH).encode(buf, a.hair);
            ByteBufCodecs.INT.encode(buf, a.hairColor); ByteBufCodecs.INT.encode(buf, a.eyeColor); ByteBufCodecs.VAR_INT.encode(buf, a.bodyType);
            ByteBufCodecs.VAR_INT.encode(buf, a.height);
        }
    };

    public boolean bald() { return hair.isEmpty(); }
    public Appearance withHair(String code, int color) { return new Appearance(gender, skinColor, code, color & 0xFFFFFF, eyeColor, bodyType, height); }

    /** The first options of a race (what the creation wizard starts with). */
    public static Appearance defaults(Race race) {
        AppearanceOptions o = race.appearance();
        int gender = race.genders().contains("male") ? MALE : FEMALE;
        return new Appearance(gender, o.skinColors().get(0), o.hair() ? HairCode.DEFAULT : "", o.hair() ? o.hairColors().get(0) : 0,
            o.eyeColors().get(0), 0, Math.clamp(100, o.minHeight(), o.maxHeight()));
    }

    /** Server check of a requested look: every value must be one the race offers; the hair colour is free. */
    public Appearance validate(Race race) {
        AppearanceOptions o = race.appearance();
        if (gender != MALE && gender != FEMALE || !race.genders().contains(gender == MALE ? "male" : "female"))
            throw new IllegalArgumentException("This race cannot have that gender.");
        if (!o.skinColors().contains(skinColor)) throw new IllegalArgumentException("Unknown skin colour.");
        if (o.hair()) {
            if (!HairCode.validOrBald(hair)) throw new IllegalArgumentException("Invalid hairstyle code.");
            if (hairColor < 0 || hairColor > 0xFFFFFF) throw new IllegalArgumentException("Invalid hair colour.");
        } else if (!hair.isEmpty()) throw new IllegalArgumentException("This race has no hair.");
        if (!o.eyeColors().contains(eyeColor)) throw new IllegalArgumentException("Unknown eye colour.");
        if (bodyType < 0 || bodyType >= o.bodyTypes()) throw new IllegalArgumentException("Unknown body type.");
        if (height < o.minHeight() || height > o.maxHeight()) throw new IllegalArgumentException("Height must be " + o.minHeight() + "-" + o.maxHeight() + " %.");
        return this;
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("gender", gender); tag.putInt("skin", skinColor); tag.putString("hair", hair); tag.putInt("hairColor", hairColor);
        tag.putInt("eyes", eyeColor); tag.putInt("bodyType", bodyType); tag.putInt("height", height);
        return tag;
    }
    /** Schema 1 saves had "hairStyle" (-1 bald, otherwise a style number): they get bald or the default preset. */
    public static Appearance load(CompoundTag tag) {
        if (tag.isEmpty()) return DEFAULT;
        String hair = tag.contains("hair") ? HairCode.upgrade(tag.getString("hair")) : tag.getInt("hairStyle") < 0 ? "" : HairCode.DEFAULT;
        if (!HairCode.validOrBald(hair)) hair = HairCode.DEFAULT;
        return new Appearance(tag.getInt("gender") == FEMALE ? FEMALE : MALE, tag.getInt("skin") & 0xFFFFFF, hair,
            tag.getInt("hairColor") & 0xFFFFFF, tag.getInt("eyes") & 0xFFFFFF, Math.max(0, tag.getInt("bodyType")),
            tag.contains("height") ? Math.clamp(tag.getInt("height"), 50, 200) : 100);
    }
}
