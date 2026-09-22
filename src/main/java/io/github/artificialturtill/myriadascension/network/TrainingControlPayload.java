package io.github.artificialturtill.myriadascension.network;

import io.github.artificialturtill.myriadascension.MyriadAscension;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record TrainingControlPayload(boolean training) implements CustomPacketPayload {
    public static final Type<TrainingControlPayload> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(
                    MyriadAscension.MOD_ID,
                    "training_control"));

    public static final StreamCodec<RegistryFriendlyByteBuf, TrainingControlPayload> STREAM_CODEC =
            StreamCodec.of(
                    (buf, payload) -> buf.writeBoolean(payload.training()),
                    buf -> new TrainingControlPayload(buf.readBoolean()));

    @Override
    public Type<TrainingControlPayload> type() {
        return TYPE;
    }
}
