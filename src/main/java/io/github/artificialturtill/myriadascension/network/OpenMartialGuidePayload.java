package io.github.artificialturtill.myriadascension.network;

import io.github.artificialturtill.myriadascension.MyriadAscension;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record OpenMartialGuidePayload() implements CustomPacketPayload {
    public static final Type<OpenMartialGuidePayload> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(
                    MyriadAscension.MOD_ID,
                    "open_martial_guide"));

    public static final StreamCodec<RegistryFriendlyByteBuf, OpenMartialGuidePayload> STREAM_CODEC =
            StreamCodec.of(
                    (buf, payload) -> {},
                    buf -> new OpenMartialGuidePayload());

    @Override
    public Type<OpenMartialGuidePayload> type() {
        return TYPE;
    }
}
