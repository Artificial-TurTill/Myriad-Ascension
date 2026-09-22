package io.github.artificialturtill.myriadascension.item;

import io.github.artificialturtill.myriadascension.cultivation.data.CultivatorData;
import io.github.artificialturtill.myriadascension.cultivation.data.ModAttachments;
import io.github.artificialturtill.myriadascension.network.ModNetworking;
import io.github.artificialturtill.myriadascension.technique.TechniqueCategory;
import io.github.artificialturtill.myriadascension.technique.TechniqueLearningService;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public final class TechniqueManualItem extends Item {
    private final ResourceLocation techniqueId;
    private final TechniqueCategory category;
    private final String techniqueDisplayName;

    public TechniqueManualItem(
            ResourceLocation techniqueId,
            TechniqueCategory category,
            String techniqueDisplayName,
            Properties properties) {
        super(properties);
        this.techniqueId = techniqueId;
        this.category = category;
        this.techniqueDisplayName = techniqueDisplayName;
    }

    public ResourceLocation techniqueId() {
        return techniqueId;
    }

    public TechniqueCategory category() {
        return category;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(
            Level level,
            Player player,
            InteractionHand usedHand) {

        ItemStack stack = player.getItemInHand(usedHand);

        if (level.isClientSide) {
            return InteractionResultHolder.success(stack);
        }

        if (!(player instanceof ServerPlayer serverPlayer)) {
            return InteractionResultHolder.pass(stack);
        }

        CultivatorData data = serverPlayer.getData(ModAttachments.CULTIVATOR_DATA);
        boolean learned = TechniqueLearningService.learn(
                data,
                techniqueId,
                category);

        if (learned) {
            serverPlayer.displayClientMessage(
                    Component.literal("Learned technique: " + techniqueDisplayName),
                    true);
        } else {
            serverPlayer.displayClientMessage(
                    Component.literal("Technique already known: " + techniqueDisplayName),
                    true);
        }

        ModNetworking.syncPlayer(serverPlayer, data);
        return InteractionResultHolder.success(stack);
    }
}
