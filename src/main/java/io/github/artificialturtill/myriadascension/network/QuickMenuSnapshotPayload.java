package io.github.artificialturtill.myriadascension.network;

import io.github.artificialturtill.myriadascension.MyriadAscension;
import io.github.artificialturtill.myriadascension.cultivation.data.CultivatorData;
import io.github.artificialturtill.myriadascension.technique.TechniqueCategory;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record QuickMenuSnapshotPayload(
        String activeMethodId,
        String cultivationTechniqueId,
        String footworkTechniqueId,
        String weaponTechniqueId,
        String eyesightTechniqueId,
        boolean resourceScanningEnabled,
        boolean cultivationGaugeEnabled,
        boolean looseTrainingWeightsEnabled) implements CustomPacketPayload {

    public static final Type<QuickMenuSnapshotPayload> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(MyriadAscension.MOD_ID, "quick_menu_snapshot"));

    public static final StreamCodec<RegistryFriendlyByteBuf, QuickMenuSnapshotPayload> STREAM_CODEC =
            StreamCodec.of(QuickMenuSnapshotPayload::encode, QuickMenuSnapshotPayload::decode);

    public QuickMenuSnapshotPayload {
        activeMethodId = safe(activeMethodId);
        cultivationTechniqueId = safe(cultivationTechniqueId);
        footworkTechniqueId = safe(footworkTechniqueId);
        weaponTechniqueId = safe(weaponTechniqueId);
        eyesightTechniqueId = safe(eyesightTechniqueId);
    }

    public static QuickMenuSnapshotPayload from(CultivatorData data) {
        return new QuickMenuSnapshotPayload(
                data.cultivationMethods().activeMethodId(),
                data.techniqueLoadout().equippedId(TechniqueCategory.CULTIVATION),
                data.techniqueLoadout().equippedId(TechniqueCategory.FOOTWORK),
                data.techniqueLoadout().equippedId(TechniqueCategory.WEAPON),
                data.techniqueLoadout().equippedId(TechniqueCategory.EYESIGHT),
                data.resourceScanningEnabled(),
                data.cultivationGaugeEnabled(),
                data.looseTrainingWeightsEnabled());
    }

    @Override
    public Type<QuickMenuSnapshotPayload> type() {
        return TYPE;
    }

    private static void encode(RegistryFriendlyByteBuf buf, QuickMenuSnapshotPayload payload) {
        buf.writeUtf(payload.activeMethodId());
        buf.writeUtf(payload.cultivationTechniqueId());
        buf.writeUtf(payload.footworkTechniqueId());
        buf.writeUtf(payload.weaponTechniqueId());
        buf.writeUtf(payload.eyesightTechniqueId());
        buf.writeBoolean(payload.resourceScanningEnabled());
        buf.writeBoolean(payload.cultivationGaugeEnabled());
        buf.writeBoolean(payload.looseTrainingWeightsEnabled());
    }

    private static QuickMenuSnapshotPayload decode(RegistryFriendlyByteBuf buf) {
        return new QuickMenuSnapshotPayload(
                buf.readUtf(),
                buf.readUtf(),
                buf.readUtf(),
                buf.readUtf(),
                buf.readUtf(),
                buf.readBoolean(),
                buf.readBoolean(),
                buf.readBoolean());
    }

    private static String safe(String value) {
        return value == null ? "" : value;
    }
}
