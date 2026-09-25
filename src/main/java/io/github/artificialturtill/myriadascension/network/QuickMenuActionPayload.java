package io.github.artificialturtill.myriadascension.network;

import io.github.artificialturtill.myriadascension.MyriadAscension;
import io.github.artificialturtill.myriadascension.technique.TechniqueCategory;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record QuickMenuActionPayload(
        QuickMenuAction action,
        TechniqueCategory category,
        int direction) implements CustomPacketPayload {

    public static final Type<QuickMenuActionPayload> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(MyriadAscension.MOD_ID, "quick_menu_action"));

    public static final StreamCodec<RegistryFriendlyByteBuf, QuickMenuActionPayload> STREAM_CODEC =
            StreamCodec.of(QuickMenuActionPayload::encode, QuickMenuActionPayload::decode);

    @Override
    public Type<QuickMenuActionPayload> type() {
        return TYPE;
    }

    private static void encode(RegistryFriendlyByteBuf buf, QuickMenuActionPayload payload) {
        buf.writeVarInt(payload.action().ordinal());
        buf.writeVarInt(payload.category() == null ? -1 : payload.category().ordinal());
        buf.writeInt(payload.direction());
    }

    private static QuickMenuActionPayload decode(RegistryFriendlyByteBuf buf) {
        int actionId = buf.readVarInt();
        int categoryId = buf.readVarInt();
        int direction = buf.readInt();

        QuickMenuAction action = safeEnum(
                QuickMenuAction.values(),
                actionId,
                QuickMenuAction.CYCLE_METHOD);
        TechniqueCategory category = categoryId < 0
                ? null
                : safeEnum(TechniqueCategory.values(), categoryId, TechniqueCategory.CULTIVATION);
        return new QuickMenuActionPayload(action, category, direction);
    }

    private static <T> T safeEnum(T[] values, int id, T fallback) {
        return id >= 0 && id < values.length ? values[id] : fallback;
    }
}
