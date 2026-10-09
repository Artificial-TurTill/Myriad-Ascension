package io.github.artificialturtill.foundation.network;

import io.github.artificialturtill.foundation.api.FoundationApi;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

/**
 * Server to owner: the full character (the saved fighter tag; empty = no character). The client rebuilds a Fighter
 * from it with its own copy of the content, so every number on the sheet is computed by the same code as the server.
 * {@code dormant}: a saved character exists but its add-on is missing.
 */
public record CharacterSyncPayload(CompoundTag fighter, boolean dormant) implements CustomPacketPayload {
    public static final Type<CharacterSyncPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(FoundationApi.MOD_ID, "character_sync"));
    public static final StreamCodec<ByteBuf, CharacterSyncPayload> CODEC = StreamCodec.composite(
        ByteBufCodecs.COMPOUND_TAG, CharacterSyncPayload::fighter, ByteBufCodecs.BOOL, CharacterSyncPayload::dormant, CharacterSyncPayload::new);
    @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }
}
