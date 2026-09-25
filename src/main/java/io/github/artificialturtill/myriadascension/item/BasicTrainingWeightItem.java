package io.github.artificialturtill.myriadascension.item;

import io.github.artificialturtill.myriadascension.cultivation.data.CultivatorData;
import io.github.artificialturtill.myriadascension.cultivation.data.ModAttachments;
import io.github.artificialturtill.myriadascension.network.ModNetworking;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * Loose training weights only contribute load after the player deliberately
 * secures/enables them. Merely storing them in inventory no longer trains.
 */
public final class BasicTrainingWeightItem extends Item {
    public BasicTrainingWeightItem(Properties properties) {
        super(properties);
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
        boolean enabled = data.toggleLooseTrainingWeights();

        ModNetworking.syncPlayer(serverPlayer, data);
        serverPlayer.displayClientMessage(
                Component.literal(enabled
                        ? "Loose training weights secured. Inventory weights now add training load."
                        : "Loose training weights released. Inventory weights no longer add training load."),
                true);

        return InteractionResultHolder.success(stack);
    }
}
