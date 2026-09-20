package io.github.artificialturtill.myriadascension.network;

import io.github.artificialturtill.myriadascension.MyriadAscension;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public final class OpenGenesisPayload implements CustomPacketPayload {
    public static final OpenGenesisPayload INSTANCE = new OpenGenesisPayload();
    public static final Type<OpenGenesisPayload> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(MyriadAscension.MOD_ID, "open_genesis"));
    public static final StreamCodec<RegistryFriendlyByteBuf, OpenGenesisPayload> STREAM_CODEC =
            StreamCodec.unit(INSTANCE);

    private OpenGenesisPayload() {
    }

    @Override
    public Type<OpenGenesisPayload> type() {
        return TYPE;
    }
}
