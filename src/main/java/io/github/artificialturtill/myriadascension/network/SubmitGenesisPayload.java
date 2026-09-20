package io.github.artificialturtill.myriadascension.network;

import io.github.artificialturtill.myriadascension.MyriadAscension;
import io.github.artificialturtill.myriadascension.character.CharacterSex;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record SubmitGenesisPayload(CharacterSex sex, boolean benevolent) implements CustomPacketPayload {
    public static final Type<SubmitGenesisPayload> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(MyriadAscension.MOD_ID, "submit_genesis"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SubmitGenesisPayload> STREAM_CODEC =
            StreamCodec.of(
                    (buf, payload) -> {
                        buf.writeVarInt(payload.sex().ordinal());
                        buf.writeBoolean(payload.benevolent());
                    },
                    buf -> new SubmitGenesisPayload(
                            fromNetworkId(buf.readVarInt()),
                            buf.readBoolean()));

    @Override
    public Type<SubmitGenesisPayload> type() {
        return TYPE;
    }

    private static CharacterSex fromNetworkId(int id) {
        CharacterSex[] values = CharacterSex.values();
        if (id < 0 || id >= values.length) {
            return CharacterSex.UNSET;
        }
        return values[id];
    }
}
