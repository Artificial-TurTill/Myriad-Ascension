package io.github.artificialturtill.foundation.network;

import io.github.artificialturtill.foundation.api.FoundationApi;
import io.github.artificialturtill.foundation.character.Appearance;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

/** Client to server: the creation wizard's Accept (race, power type, class, appearance). The server validates all. */
public record CreateCharacterPayload(String race, String powerType, String characterClass, Appearance appearance) implements CustomPacketPayload {
    public static final Type<CreateCharacterPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(FoundationApi.MOD_ID, "create_character"));
    public static final StreamCodec<ByteBuf, CreateCharacterPayload> CODEC = StreamCodec.composite(
        ByteBufCodecs.stringUtf8(256), CreateCharacterPayload::race, ByteBufCodecs.stringUtf8(256), CreateCharacterPayload::powerType,
        ByteBufCodecs.stringUtf8(256), CreateCharacterPayload::characterClass, Appearance.CODEC, CreateCharacterPayload::appearance,
        CreateCharacterPayload::new);
    @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }
}
