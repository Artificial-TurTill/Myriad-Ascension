package io.github.artificialturtill.foundation.network;

import io.github.artificialturtill.foundation.character.Appearance;
import io.github.artificialturtill.foundation.character.Fighter;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

/**
 * What every client needs to draw a player (DBC+ BodyLook / HairLook / FormLook in one): whether a character
 * exists, its race and power type, the appearance, the active transformation and its colour, and whether the
 * player is charging (aura). Synced to the owner and every tracking client.
 */
public record PublicLook(boolean active, String race, String powerType, Appearance appearance, String form, int formColor,
                         boolean charging, int release) {
    public static final PublicLook NONE = new PublicLook(false, "", "", Appearance.DEFAULT, "", 0, false, 0);
    public static final StreamCodec<ByteBuf, PublicLook> CODEC = new StreamCodec<>() {
        @Override public PublicLook decode(ByteBuf buf) {
            boolean active = ByteBufCodecs.BOOL.decode(buf);
            if (!active) return NONE;
            return new PublicLook(true, ByteBufCodecs.STRING_UTF8.decode(buf), ByteBufCodecs.STRING_UTF8.decode(buf), Appearance.CODEC.decode(buf),
                ByteBufCodecs.STRING_UTF8.decode(buf), ByteBufCodecs.INT.decode(buf), ByteBufCodecs.BOOL.decode(buf), ByteBufCodecs.VAR_INT.decode(buf));
        }
        @Override public void encode(ByteBuf buf, PublicLook look) {
            ByteBufCodecs.BOOL.encode(buf, look.active);
            if (!look.active) return;
            ByteBufCodecs.STRING_UTF8.encode(buf, look.race); ByteBufCodecs.STRING_UTF8.encode(buf, look.powerType);
            Appearance.CODEC.encode(buf, look.appearance); ByteBufCodecs.STRING_UTF8.encode(buf, look.form);
            ByteBufCodecs.INT.encode(buf, look.formColor); ByteBufCodecs.BOOL.encode(buf, look.charging); ByteBufCodecs.VAR_INT.encode(buf, look.release);
        }
    };
    public static PublicLook of(Fighter f, boolean charging) {
        var form = f.activeForm();
        return new PublicLook(true, f.race().id(), f.powerType().id(), f.appearance(), f.form(), form == null ? 0 : form.color(), charging, f.release());
    }
}
