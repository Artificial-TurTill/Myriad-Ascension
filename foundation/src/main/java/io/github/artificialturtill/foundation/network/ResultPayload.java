package io.github.artificialturtill.foundation.network;

import io.github.artificialturtill.foundation.api.FoundationApi;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

/** Server to client: the result of a menu action ("" = success, otherwise the reason it was refused). */
public record ResultPayload(String message) implements CustomPacketPayload {
    public static final Type<ResultPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(FoundationApi.MOD_ID, "result"));
    public static final StreamCodec<ByteBuf, ResultPayload> CODEC = ByteBufCodecs.stringUtf8(512).map(ResultPayload::new, ResultPayload::message);
    @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }
}
