package io.github.artificialturtill.foundation.network;

import io.github.artificialturtill.foundation.api.FoundationApi;
import io.github.artificialturtill.foundation.character.HairCode;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

/** Client to server: the Barber Menu's Done (DBC+ HairUpdate): the hairstyle code ("" = bald) and the hair colour. */
public record HairUpdatePayload(String code, int color) implements CustomPacketPayload {
    public static final Type<HairUpdatePayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(FoundationApi.MOD_ID, "hair_update"));
    public static final StreamCodec<ByteBuf, HairUpdatePayload> CODEC = StreamCodec.composite(
        ByteBufCodecs.stringUtf8(HairCode.LENGTH), HairUpdatePayload::code, ByteBufCodecs.INT, HairUpdatePayload::color, HairUpdatePayload::new);
    @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }
}
