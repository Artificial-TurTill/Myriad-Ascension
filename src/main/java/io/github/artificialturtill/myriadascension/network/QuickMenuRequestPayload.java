package io.github.artificialturtill.myriadascension.network;

import io.github.artificialturtill.myriadascension.MyriadAscension;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record QuickMenuRequestPayload() implements CustomPacketPayload {
    public static final Type<QuickMenuRequestPayload> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(MyriadAscension.MOD_ID, "quick_menu_request"));

    public static final StreamCodec<RegistryFriendlyByteBuf, QuickMenuRequestPayload> STREAM_CODEC =
            StreamCodec.of(
                    (buf, payload) -> {},
                    buf -> new QuickMenuRequestPayload());

    @Override
    public Type<QuickMenuRequestPayload> type() {
        return TYPE;
    }
}
