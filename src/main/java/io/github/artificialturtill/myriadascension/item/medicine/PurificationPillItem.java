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

public final class PurificationPillItem extends Item {
    private static final double IMPURITY_REDUCTION = 18.0D;
    private static final double DEMONIC_CONTAMINATION_REDUCTION = 6.0D;
    private static final double PURITY_RESTORATION = 4.0D;

    public PurificationPillItem(Properties properties) {
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
        double beforeImpurities = data.impurityLoad();
        double beforeContamination = data.demonicQiContamination();
        double beforePurity = data.vesselPurity();

        data.setImpurityLoad(beforeImpurities - IMPURITY_REDUCTION);
        data.setDemonicQiContamination(
                beforeContamination - DEMONIC_CONTAMINATION_REDUCTION);
        data.setVesselPurity(beforePurity + PURITY_RESTORATION);

        boolean changed = data.impurityLoad() != beforeImpurities
                || data.demonicQiContamination() != beforeContamination
                || data.vesselPurity() != beforePurity;

        if (!changed) {
            serverPlayer.displayClientMessage(
                    Component.literal("Your vessel has nothing this pill can purify."),
                    true);
            return InteractionResultHolder.fail(stack);
        }

        if (!serverPlayer.getAbilities().instabuild) {
            stack.shrink(1);
        }

        ModNetworking.syncPlayer(serverPlayer, data);
        serverPlayer.displayClientMessage(
                Component.literal(
                        "Purifying medicinal force washes impurities from your vessel."),
                true);
        return InteractionResultHolder.success(stack);
    }
}
