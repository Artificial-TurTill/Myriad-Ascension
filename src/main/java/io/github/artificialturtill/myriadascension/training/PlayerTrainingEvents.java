package io.github.artificialturtill.myriadascension.training;

import io.github.artificialturtill.myriadascension.cultivation.data.CultivatorData;
import io.github.artificialturtill.myriadascension.cultivation.data.ModAttachments;
import io.github.artificialturtill.myriadascension.cultivation.realm.CultivationRealm;
import io.github.artificialturtill.myriadascension.network.ModNetworking;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

public final class PlayerTrainingEvents {
    private PlayerTrainingEvents() {
    }

    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        CultivatorData data = player.getData(ModAttachments.CULTIVATOR_DATA);

        if (!data.trainingRequested()) {
            recover(data);
            return;
        }

        if (!TestudoTrainingRules.supports(data)) {
            data.resetTrainingSession();
            return;
        }

        long gameTime = player.level().getGameTime();
        boolean postureValid = TestudoTrainingRules.postureIsValid(player);
        boolean breathing = TestudoTrainingRules.hasRecentBreathPulse(data, gameTime);
        boolean exhausted = data.trainingFatigue() >= TestudoTrainingRules.MAX_FATIGUE;

        if (!postureValid || !breathing || exhausted) {
            data.resetTrainingSession();

            if (gameTime % 20L == 0L) {
                String reason = exhausted
                        ? "Too fatigued — release the stance and recover."
                        : !postureValid
                                ? "Foundation Stance broken — stand still, grounded, with empty hands."
                                : "Control your breathing with G while holding the stance.";
                player.displayClientMessage(Component.literal(reason), true);
            }
            return;
        }

        data.incrementTrainingSessionTicks();
        data.setTrainingFatigue(
                data.trainingFatigue() + TestudoTrainingRules.FATIGUE_PER_TICK);

        data.setCultivationProgress(
                Math.min(
                        TestudoTrainingRules.STAGE_PROGRESS_REQUIRED,
                        data.cultivationProgress() + TestudoTrainingRules.progressPerTick(data)));

        if (data.cultivationProgress() >= TestudoTrainingRules.STAGE_PROGRESS_REQUIRED) {
            advanceFoundation(player, data);
        }

        if (gameTime % 20L == 0L) {
            player.displayClientMessage(
                    Component.literal(
                            "Testudo Foundation Stance  |  "
                                    + "Progress " + percent(data.cultivationProgress()) + "%  |  "
                                    + "Fatigue " + percent(data.trainingFatigue()) + "%  |  "
                                    + "Session " + (data.trainingSessionTicks() / 20) + "s"),
                    true);
            ModNetworking.syncPlayer(player, data);
        }
    }

    private static void recover(CultivatorData data) {
        data.resetTrainingSession();
        data.setTrainingFatigue(
                data.trainingFatigue() - TestudoTrainingRules.REST_RECOVERY_PER_TICK);
    }

    private static void advanceFoundation(ServerPlayer player, CultivatorData data) {
        if (data.realm() == CultivationRealm.MORTAL) {
            data.setRealm(CultivationRealm.TEMPERED_BODY);
            data.setMinorStage(1);
            data.setCultivationProgress(0.0D);
            data.resetTrainingSession();

            player.displayClientMessage(
                    Component.literal(
                            "Your mortal body crosses the threshold: Tempered Body — Stage 1."),
                    false);
            ModNetworking.syncPlayer(player, data);
            return;
        }

        if (data.realm() != CultivationRealm.TEMPERED_BODY) {
            return;
        }

        if (data.minorStage() < 9) {
            int nextStage = data.minorStage() + 1;
            data.setMinorStage(nextStage);
            data.setCultivationProgress(0.0D);
            data.resetTrainingSession();

            player.displayClientMessage(
                    Component.literal("Tempered Body foundation advanced to Stage " + nextStage + "."),
                    false);
            ModNetworking.syncPlayer(player, data);
            return;
        }

        // Stage 9 does not automatically break into Initial Element.
        data.setCultivationProgress(TestudoTrainingRules.STAGE_PROGRESS_REQUIRED);
        data.resetTrainingSession();

        player.displayClientMessage(
                Component.literal(
                        "Tempered Body Stage 9 is fully consolidated. Initial Element requires a breakthrough."),
                false);
        ModNetworking.syncPlayer(player, data);
    }

    private static int percent(double value) {
        return (int) Math.round(Math.max(0.0D, Math.min(100.0D, value)));
    }
}
