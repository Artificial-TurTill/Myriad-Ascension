package io.github.artificialturtill.foundation.network;

import io.github.artificialturtill.foundation.api.FoundationApi;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

/** Client to server: one character action (DBC+ CharacterAction). {@code id} names a skill or form where needed. */
public record CharacterActionPayload(int action, String id, int a, int b) implements CustomPacketPayload {
    public static final int REFRESH = 0, UPGRADE = 1, LEARN_SKILL = 2, FORGET_SKILL = 3, SELECT_FORM = 4, ASCEND = 5, DESCEND = 6, CHARGE = 7, TURBO = 8;
    public static final Type<CharacterActionPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(FoundationApi.MOD_ID, "character_action"));
    public static final StreamCodec<ByteBuf, CharacterActionPayload> CODEC = StreamCodec.composite(
        ByteBufCodecs.VAR_INT, CharacterActionPayload::action, ByteBufCodecs.stringUtf8(256), CharacterActionPayload::id,
        ByteBufCodecs.VAR_INT, CharacterActionPayload::a, ByteBufCodecs.VAR_INT, CharacterActionPayload::b, CharacterActionPayload::new);
    public static CharacterActionPayload of(int action) { return new CharacterActionPayload(action, "", 0, 0); }
    @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }
}
