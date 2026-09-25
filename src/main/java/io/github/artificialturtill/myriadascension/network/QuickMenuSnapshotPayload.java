package io.github.artificialturtill.myriadascension.network;

import io.github.artificialturtill.myriadascension.MyriadAscension;
import io.github.artificialturtill.myriadascension.cultivation.data.CultivatorData;
import io.github.artificialturtill.myriadascension.registry.ModItems;
import io.github.artificialturtill.myriadascension.technique.TechniqueCategory;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record QuickMenuSnapshotPayload(
        List<String> knownMethods,
        String activeMethodId,
        List<String> cultivationTechniques,
        List<String> footworkTechniques,
        List<String> weaponTechniques,
        List<String> eyesightTechniques,
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
        knownMethods = List.copyOf(knownMethods == null ? List.of() : knownMethods);
        cultivationTechniques = List.copyOf(cultivationTechniques == null ? List.of() : cultivationTechniques);
        footworkTechniques = List.copyOf(footworkTechniques == null ? List.of() : footworkTechniques);
        weaponTechniques = List.copyOf(weaponTechniques == null ? List.of() : weaponTechniques);
        eyesightTechniques = List.copyOf(eyesightTechniques == null ? List.of() : eyesightTechniques);
        activeMethodId = safe(activeMethodId);
        cultivationTechniqueId = safe(cultivationTechniqueId);
        footworkTechniqueId = safe(footworkTechniqueId);
        weaponTechniqueId = safe(weaponTechniqueId);
        eyesightTechniqueId = safe(eyesightTechniqueId);
    }

    public static QuickMenuSnapshotPayload from(CultivatorData data) {
        return new QuickMenuSnapshotPayload(
                withNone(data.cultivationMethods().knownMethods()),
                data.cultivationMethods().activeMethodId(),
                techniquesFor(data, TechniqueCategory.CULTIVATION),
                techniquesFor(data, TechniqueCategory.FOOTWORK),
                techniquesFor(data, TechniqueCategory.WEAPON),
                techniquesFor(data, TechniqueCategory.EYESIGHT),
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

    public List<String> techniques(TechniqueCategory category) {
        return switch (category) {
            case CULTIVATION -> cultivationTechniques;
            case FOOTWORK -> footworkTechniques;
            case WEAPON -> weaponTechniques;
            case EYESIGHT -> eyesightTechniques;
        };
    }

    public String equipped(TechniqueCategory category) {
        return switch (category) {
            case CULTIVATION -> cultivationTechniqueId;
            case FOOTWORK -> footworkTechniqueId;
            case WEAPON -> weaponTechniqueId;
            case EYESIGHT -> eyesightTechniqueId;
        };
    }

    private static List<String> techniquesFor(
            CultivatorData data,
            TechniqueCategory category) {

        ArrayList<String> ids = new ArrayList<>();
        ids.add("");

        for (String raw : data.techniqueKnowledge().view()) {
            ResourceLocation id = ResourceLocation.tryParse(raw);
            if (id != null && ModItems.techniqueCategory(id) == category && !ids.contains(raw)) {
                ids.add(raw);
            }
        }

        if (category == TechniqueCategory.CULTIVATION) {
            for (String method : data.cultivationMethods().knownMethods()) {
                if (!method.isBlank() && !ids.contains(method)) {
                    ids.add(method);
                }
            }
        }

        String equipped = data.techniqueLoadout().equippedId(category);
        if (!equipped.isBlank() && !ids.contains(equipped)) {
            ids.add(equipped);
        }

        return List.copyOf(ids);
    }

    private static List<String> withNone(Iterable<String> source) {
        ArrayList<String> result = new ArrayList<>();
        result.add("");
        if (source != null) {
            for (String value : source) {
                if (value != null && !value.isBlank() && !result.contains(value)) {
                    result.add(value);
                }
            }
        }
        return List.copyOf(result);
    }

    private static void encode(RegistryFriendlyByteBuf buf, QuickMenuSnapshotPayload payload) {
        writeStrings(buf, payload.knownMethods());
        buf.writeUtf(payload.activeMethodId());
        writeStrings(buf, payload.cultivationTechniques());
        writeStrings(buf, payload.footworkTechniques());
        writeStrings(buf, payload.weaponTechniques());
        writeStrings(buf, payload.eyesightTechniques());
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
                readStrings(buf),
                buf.readUtf(),
                readStrings(buf),
                readStrings(buf),
                readStrings(buf),
                readStrings(buf),
                buf.readUtf(),
                buf.readUtf(),
                buf.readUtf(),
                buf.readUtf(),
                buf.readBoolean(),
                buf.readBoolean(),
                buf.readBoolean());
    }

    private static void writeStrings(RegistryFriendlyByteBuf buf, List<String> values) {
        buf.writeVarInt(values.size());
        for (String value : values) {
            buf.writeUtf(safe(value));
        }
    }

    private static List<String> readStrings(RegistryFriendlyByteBuf buf) {
        int size = Math.max(0, Math.min(256, buf.readVarInt()));
        ArrayList<String> result = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            result.add(buf.readUtf());
        }
        return List.copyOf(result);
    }

    private static String safe(String value) {
        return value == null ? "" : value;
    }
}
