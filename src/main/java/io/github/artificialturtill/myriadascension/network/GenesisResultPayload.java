package io.github.artificialturtill.myriadascension.network;

import io.github.artificialturtill.myriadascension.MyriadAscension;
import io.github.artificialturtill.myriadascension.affinity.AffinityProfile;
import io.github.artificialturtill.myriadascension.affinity.AffinityType;
import io.github.artificialturtill.myriadascension.character.CharacterSex;
import io.github.artificialturtill.myriadascension.cultivation.data.CultivatorData;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record GenesisResultPayload(
        CharacterSex sex,
        int moralAlignment,
        int wood,
        int fire,
        int earth,
        int metal,
        int water,
        int yin,
        int yang) implements CustomPacketPayload {

    public static final Type<GenesisResultPayload> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(MyriadAscension.MOD_ID, "genesis_result"));

    public static final StreamCodec<RegistryFriendlyByteBuf, GenesisResultPayload> STREAM_CODEC =
            StreamCodec.of(
                    (buf, payload) -> {
                        buf.writeVarInt(payload.sex().ordinal());
                        buf.writeVarInt(payload.moralAlignment());
                        buf.writeVarInt(payload.wood());
                        buf.writeVarInt(payload.fire());
                        buf.writeVarInt(payload.earth());
                        buf.writeVarInt(payload.metal());
                        buf.writeVarInt(payload.water());
                        buf.writeVarInt(payload.yin());
                        buf.writeVarInt(payload.yang());
                    },
                    buf -> new GenesisResultPayload(
                            fromNetworkId(buf.readVarInt()),
                            buf.readVarInt(),
                            buf.readVarInt(),
                            buf.readVarInt(),
                            buf.readVarInt(),
                            buf.readVarInt(),
                            buf.readVarInt(),
                            buf.readVarInt(),
                            buf.readVarInt()));

    public static GenesisResultPayload from(CultivatorData data) {
        AffinityProfile affinities = data.affinities();
        return new GenesisResultPayload(
                data.characterSex(),
                (int) data.moralAlignment(),
                affinities.get(AffinityType.WOOD),
                affinities.get(AffinityType.FIRE),
                affinities.get(AffinityType.EARTH),
                affinities.get(AffinityType.METAL),
                affinities.get(AffinityType.WATER),
                affinities.get(AffinityType.YIN),
                affinities.get(AffinityType.YANG));
    }

    @Override
    public Type<GenesisResultPayload> type() {
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
