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

        QuickMenuAction action = actionId >= 0 && actionId < QuickMenuAction.values().length
                ? QuickMenuAction.values()[actionId]
                : QuickMenuAction.CYCLE_METHOD;
        TechniqueCategory category = categoryId >= 0 && categoryId < TechniqueCategory.values().length
                ? TechniqueCategory.values()[categoryId]
                : null;

        return new QuickMenuActionPayload(action, category, direction);
    }
}
