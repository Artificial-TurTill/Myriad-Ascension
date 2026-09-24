package io.github.artificialturtill.myriadascension.item.poison;

import io.github.artificialturtill.myriadascension.cultivation.data.CultivatorData;
import io.github.artificialturtill.myriadascension.cultivation.data.ModAttachments;
import io.github.artificialturtill.myriadascension.network.ModNetworking;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public final class MeridianPoisonItem extends Item {
    public MeridianPoisonItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult interactLivingEntity(
            ItemStack stack,
            Player player,
            LivingEntity target,
            InteractionHand hand) {

        if (player.level().isClientSide) {
            return InteractionResult.SUCCESS;
        }

        target.addEffect(new MobEffectInstance(MobEffects.POISON, 20 * 12, 0));

        if (target instanceof ServerPlayer serverTarget) {
            CultivatorData data = serverTarget.getData(ModAttachments.CULTIVATOR_DATA);
            data.setBodyInjury(data.bodyInjury() + 4.0D);
            data.setMeridianInjury(data.meridianInjury() + 5.0D);
            data.setMeridianLoad(data.meridianLoad() + 8.0D);
            ModNetworking.syncPlayer(serverTarget, data);
        }

        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }

        return InteractionResult.CONSUME;
    }
}
