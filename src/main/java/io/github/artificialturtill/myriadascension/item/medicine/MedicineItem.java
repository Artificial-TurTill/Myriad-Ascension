package io.github.artificialturtill.myriadascension.item.medicine;

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

public class MedicineItem extends Item {
    private final MedicineProfile profile;

    public MedicineItem(MedicineProfile profile, Properties properties) {
        super(properties);
        this.profile = profile;
    }

    public MedicineProfile profile() {
        return profile;
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

        if (profile.vanillaHealing() > 0.0D) {
            serverPlayer.heal((float) profile.vanillaHealing());
        }

        data.setBodyInjury(data.bodyInjury() - profile.bodyInjuryReduction());
        data.setMeridianInjury(data.meridianInjury() - profile.meridianInjuryReduction());
        data.setSoulInjury(data.soulInjury() - profile.soulInjuryReduction());
        data.setRecoveryDebt(data.recoveryDebt() - profile.recoveryDebtReduction());
        data.setMeridianLoad(data.meridianLoad() - profile.meridianLoadReduction());
        data.setTrainingFatigue(data.trainingFatigue() - profile.fatigueReduction());
        data.setImpurityLoad(data.impurityLoad() + profile.impurityGain());

        if (!serverPlayer.getAbilities().instabuild) {
            stack.shrink(1);
        }

        ModNetworking.syncPlayer(serverPlayer, data);
        serverPlayer.displayClientMessage(
                Component.literal(profile.impurityGain() > 0.0D
                        ? "Medicine takes effect, but leaves medicinal impurities."
                        : "Medicine takes effect."),
                true);

        return InteractionResultHolder.success(stack);
    }
}
