package io.github.artificialturtill.myriadascension.item.medicine;

import io.github.artificialturtill.myriadascension.cultivation.data.CultivatorData;
import io.github.artificialturtill.myriadascension.cultivation.data.ModAttachments;
import io.github.artificialturtill.myriadascension.network.ModNetworking;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public final class AntidotePillItem extends Item {
    public AntidotePillItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(
            Level level,
            Player player,
            InteractionHand hand) {

        ItemStack stack = player.getItemInHand(hand);
        if (level.isClientSide) {
            return InteractionResultHolder.success(stack);
        }

        if (!(player instanceof ServerPlayer serverPlayer)) {
            return InteractionResultHolder.pass(stack);
        }

        serverPlayer.removeEffect(MobEffects.POISON);
        CultivatorData data = serverPlayer.getData(ModAttachments.CULTIVATOR_DATA);
        data.setBodyInjury(data.bodyInjury() - 2.0D);
        data.setMeridianInjury(data.meridianInjury() - 1.0D);
        data.setImpurityLoad(data.impurityLoad() + 0.25D);

        if (!serverPlayer.getAbilities().instabuild) {
            stack.shrink(1);
        }

        ModNetworking.syncPlayer(serverPlayer, data);
        return InteractionResultHolder.success(stack);
    }
}
