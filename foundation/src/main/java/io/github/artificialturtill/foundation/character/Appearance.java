package io.github.artificialturtill.foundation.character;

import io.github.artificialturtill.foundation.content.AppearanceOptions;
import io.github.artificialturtill.foundation.content.Race;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

/**
 * The look chosen at character creation (JRMCore jrmcDNS / jrmcDNSH concept): gender, skin colour, hair style and
 * colour, eye colour, body type and height (percent). Colours are 0xRRGGBB; hairStyle -1 = bald.
 */
public record Appearance(int gender, int skinColor, int hairStyle, int hairColor, int eyeColor, int bodyType, int height) {
    public static final int MALE = 0, FEMALE = 1;
    public static final Appearance DEFAULT = new Appearance(MALE, 0xF2C9A0, 0, 0x3B2414, 0x2B1B0E, 0, 100);

    public static final StreamCodec<ByteBuf, Appearance> CODEC = new StreamCodec<>() {
        @Override public Appearance decode(ByteBuf buf) {
            return new Appearance(ByteBufCodecs.VAR_INT.decode(buf), ByteBufCodecs.INT.decode(buf), ByteBufCodecs.VAR_INT.decode(buf),
                ByteBufCodecs.INT.decode(buf), ByteBufCodecs.INT.decode(buf), ByteBufCodecs.VAR_INT.decode(buf), ByteBufCodecs.VAR_INT.decode(buf));
        }
        @Override public void encode(ByteBuf buf, Appearance a) {
            ByteBufCodecs.VAR_INT.encode(buf, a.gender); ByteBufCodecs.INT.encode(buf, a.skinColor); ByteBufCodecs.VAR_INT.encode(buf, a.hairStyle);
            ByteBufCodecs.INT.encode(buf, a.hairColor); ByteBufCodecs.INT.encode(buf, a.eyeColor); ByteBufCodecs.VAR_INT.encode(buf, a.bodyType);
            ByteBufCodecs.VAR_INT.encode(buf, a.height);
        }
    };

    /** The first options of a race (what the creation wizard starts with). */
    public static Appearance defaults(Race race) {
        AppearanceOptions o = race.appearance();
        int gender = race.genders().contains("male") ? MALE : FEMALE;
        return new Appearance(gender, o.skinColors().get(0), o.canHaveHair() ? 0 : -1, o.canHaveHair() ? o.hairColors().get(0) : 0,
            o.eyeColors().get(0), 0, Math.clamp(100, o.minHeight(), o.maxHeight()));
    }

    /** Server check of a requested look: every value must be one the race offers. */
    public Appearance validate(Race race) {
        AppearanceOptions o = race.appearance();
        if (gender != MALE && gender != FEMALE || !race.genders().contains(gender == MALE ? "male" : "female"))
            throw new IllegalArgumentException("This race cannot have that gender.");
        if (!o.skinColors().contains(skinColor)) throw new IllegalArgumentException("Unknown skin colour.");
        if (o.canHaveHair()) {
            if (hairStyle < -1 || hairStyle >= o.hairStyles()) throw new IllegalArgumentException("Unknown hair style.");
            if (!o.hairColors().contains(hairColor)) throw new IllegalArgumentException("Unknown hair colour.");
        } else if (hairStyle != -1) throw new IllegalArgumentException("This race has no hair.");
        if (!o.eyeColors().contains(eyeColor)) throw new IllegalArgumentException("Unknown eye colour.");
        if (bodyType < 0 || bodyType >= o.bodyTypes()) throw new IllegalArgumentException("Unknown body type.");
        if (height < o.minHeight() || height > o.maxHeight()) throw new IllegalArgumentException("Height must be " + o.minHeight() + "-" + o.maxHeight() + " %.");
        return this;
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("gender", gender); tag.putInt("skin", skinColor); tag.putInt("hairStyle", hairStyle); tag.putInt("hairColor", hairColor);
        tag.putInt("eyes", eyeColor); tag.putInt("bodyType", bodyType); tag.putInt("height", height);
        return tag;
    }
    public static Appearance load(CompoundTag tag) {
        if (tag.isEmpty()) return DEFAULT;
        return new Appearance(tag.getInt("gender") == FEMALE ? FEMALE : MALE, tag.getInt("skin") & 0xFFFFFF, tag.getInt("hairStyle"),
            tag.getInt("hairColor") & 0xFFFFFF, tag.getInt("eyes") & 0xFFFFFF, Math.max(0, tag.getInt("bodyType")),
            tag.contains("height") ? Math.clamp(tag.getInt("height"), 50, 200) : 100);
    }
}
