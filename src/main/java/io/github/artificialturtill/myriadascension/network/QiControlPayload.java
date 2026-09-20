package io.github.artificialturtill.myriadascension.network;

import io.github.artificialturtill.myriadascension.MyriadAscension;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record QiControlPayload(QiControlAction action) implements CustomPacketPayload {
    public static final Type<QiControlPayload> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(MyriadAscension.MOD_ID, "qi_control"));

    public static final StreamCodec<RegistryFriendlyByteBuf, QiControlPayload> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.VAR_INT.map(QiControlAction::fromNetworkId, QiControlAction::ordinal),
                    QiControlPayload::action,
                    QiControlPayload::new);

    @Override
    public Type<QiControlPayload> type() {
        return TYPE;
    }
}
