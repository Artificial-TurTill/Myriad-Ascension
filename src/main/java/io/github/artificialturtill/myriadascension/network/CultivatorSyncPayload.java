package io.github.artificialturtill.myriadascension.network;

import io.github.artificialturtill.myriadascension.MyriadAscension;
import io.github.artificialturtill.myriadascension.affinity.AffinityProfile;
import io.github.artificialturtill.myriadascension.affinity.AffinityType;
import io.github.artificialturtill.myriadascension.alignment.CultivationAffiliation;
import io.github.artificialturtill.myriadascension.character.CharacterSex;
import io.github.artificialturtill.myriadascension.cultivation.data.CultivatorData;
import io.github.artificialturtill.myriadascension.cultivation.realm.CultivationRealm;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record CultivatorSyncPayload(
        CharacterSex sex,
        CultivationAffiliation affiliation,
        CultivationRealm realm,
        int minorStage,
        double moralAlignment,
        double karma,
        int wood,
        int fire,
        int earth,
        int metal,
        int water,
        int yin,
        int yang,
        double currentQi,
        double maximumQi,
        double circulationPercent,
        boolean burstMode,
        double cultivationProgress,
        double cultivationComprehension,
        double battleComprehension,
        double bodyInjury,
        double meridianInjury,
        double soulInjury,
        double recoveryDebt,
        double vesselPurity,
        double impurityLoad,
        double demonicQiContamination,
        int passiveQiRechargingLevel,
        int meditationLevel,
        int qiConcealmentLevel) implements CustomPacketPayload {

    public static final Type<CultivatorSyncPayload> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(MyriadAscension.MOD_ID, "cultivator_sync"));

    public static final StreamCodec<RegistryFriendlyByteBuf, CultivatorSyncPayload> STREAM_CODEC =
            StreamCodec.of(CultivatorSyncPayload::encode, CultivatorSyncPayload::decode);

    public static CultivatorSyncPayload from(CultivatorData data) {
        AffinityProfile a = data.affinities();
        return new CultivatorSyncPayload(
                data.characterSex(),
                data.affiliation(),
                data.realm(),
                data.minorStage(),
                data.moralAlignment(),
                data.karma(),
                a.get(AffinityType.WOOD),
                a.get(AffinityType.FIRE),
                a.get(AffinityType.EARTH),
                a.get(AffinityType.METAL),
                a.get(AffinityType.WATER),
                a.get(AffinityType.YIN),
                a.get(AffinityType.YANG),
                data.currentQi(),
                data.maximumQi(),
                data.circulationPercent(),
                data.burstMode(),
                data.cultivationProgress(),
                data.cultivationComprehension(),
                data.battleComprehension(),
                data.bodyInjury(),
                data.meridianInjury(),
                data.soulInjury(),
                data.recoveryDebt(),
                data.vesselPurity(),
                data.impurityLoad(),
                data.demonicQiContamination(),
                data.passiveQiRechargingLevel(),
                data.meditationLevel(),
                data.qiConcealmentLevel());
    }

    @Override
    public Type<CultivatorSyncPayload> type() {
        return TYPE;
    }

    private static void encode(RegistryFriendlyByteBuf buf, CultivatorSyncPayload p) {
        buf.writeVarInt(p.sex().ordinal());
        buf.writeVarInt(p.affiliation().ordinal());
        buf.writeVarInt(p.realm().ordinal());
        buf.writeVarInt(p.minorStage());
        buf.writeDouble(p.moralAlignment());
        buf.writeDouble(p.karma());

        buf.writeVarInt(p.wood());
        buf.writeVarInt(p.fire());
        buf.writeVarInt(p.earth());
        buf.writeVarInt(p.metal());
        buf.writeVarInt(p.water());
        buf.writeVarInt(p.yin());
        buf.writeVarInt(p.yang());

        buf.writeDouble(p.currentQi());
        buf.writeDouble(p.maximumQi());
        buf.writeDouble(p.circulationPercent());
        buf.writeBoolean(p.burstMode());

        buf.writeDouble(p.cultivationProgress());
        buf.writeDouble(p.cultivationComprehension());
        buf.writeDouble(p.battleComprehension());

        buf.writeDouble(p.bodyInjury());
        buf.writeDouble(p.meridianInjury());
        buf.writeDouble(p.soulInjury());
        buf.writeDouble(p.recoveryDebt());

        buf.writeDouble(p.vesselPurity());
        buf.writeDouble(p.impurityLoad());
        buf.writeDouble(p.demonicQiContamination());

        buf.writeVarInt(p.passiveQiRechargingLevel());
        buf.writeVarInt(p.meditationLevel());
        buf.writeVarInt(p.qiConcealmentLevel());
    }

    private static CultivatorSyncPayload decode(RegistryFriendlyByteBuf buf) {
        return new CultivatorSyncPayload(
                safeEnum(CharacterSex.values(), buf.readVarInt(), CharacterSex.UNSET),
                safeEnum(CultivationAffiliation.values(), buf.readVarInt(), CultivationAffiliation.UNDECIDED),
                safeEnum(CultivationRealm.values(), buf.readVarInt(), CultivationRealm.MORTAL),
                buf.readVarInt(),
                buf.readDouble(),
                buf.readDouble(),
                buf.readVarInt(),
                buf.readVarInt(),
                buf.readVarInt(),
                buf.readVarInt(),
                buf.readVarInt(),
                buf.readVarInt(),
                buf.readVarInt(),
                buf.readDouble(),
                buf.readDouble(),
                buf.readDouble(),
                buf.readBoolean(),
                buf.readDouble(),
                buf.readDouble(),
                buf.readDouble(),
                buf.readDouble(),
                buf.readDouble(),
                buf.readDouble(),
                buf.readDouble(),
                buf.readDouble(),
                buf.readDouble(),
                buf.readDouble(),
                buf.readVarInt(),
                buf.readVarInt(),
                buf.readVarInt());
    }

    private static <T> T safeEnum(T[] values, int id, T fallback) {
        return id >= 0 && id < values.length ? values[id] : fallback;
    }
}
