package io.github.artificialturtill.myriadascension.training;

import io.github.artificialturtill.myriadascension.cultivation.data.CultivatorData;
import io.github.artificialturtill.myriadascension.cultivation.data.ModAttachments;
import io.github.artificialturtill.myriadascension.cultivation.realm.CultivationRealm;
import io.github.artificialturtill.myriadascension.cultivation.realm.TemperedBodyRules;
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
        long gameTime = player.level().getGameTime();

        boolean energyChanged = updateTemperedBodyEnergyState(data);
        if (energyChanged && gameTime % 20L == 0L) {
            ModNetworking.syncPlayer(player, data);
        }

        if (!data.trainingRequested()) {
            recover(data);
            return;
        }

        if (!TestudoTrainingRules.supports(data)) {
            data.setTrainingRequested(false);
            recover(data);
            return;
        }

        boolean postureValid = TestudoTrainingRules.postureIsValid(player);
        boolean focusRequired = TestudoTrainingRules.requiresFocusPulse(data);
        boolean focusValid = !focusRequired
                || TestudoTrainingRules.hasRecentFocusPulse(data, gameTime);
        boolean exhausted = data.trainingFatigue() >= TestudoTrainingRules.MAX_FATIGUE;

        if (!postureValid || !focusValid || exhausted) {
            data.resetTrainingSession();

            if (gameTime % 20L == 0L) {
                String reason = exhausted
                        ? "Too fatigued — release the stance and recover."
                        : !postureValid
                                ? "Foundation Stance broken — stand still, grounded, with empty hands."
                                : "Focus on the World Energy with G while holding the stance.";
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
            String energySuffix = "";
            if (data.realm() == CultivationRealm.TEMPERED_BODY
                    && TemperedBodyRules.naturallyGathersPreQiEnergy(data.minorStage())) {
                energySuffix = "  |  Yuan Qi "
                        + oneDecimal(data.currentQi())
                        + "/"
                        + oneDecimal(data.maximumQi());
            }

            player.displayClientMessage(
                    Component.literal(
                            "Testudo " + TestudoTrainingRules.modeName(data) + "  |  "
                                    + "Progress " + percent(data.cultivationProgress()) + "%  |  "
                                    + "Fatigue " + percent(data.trainingFatigue()) + "%  |  "
                                    + "Session " + (data.trainingSessionTicks() / 20) + "s"
                                    + energySuffix),
                    true);
            ModNetworking.syncPlayer(player, data);
        }
    }

    private static boolean updateTemperedBodyEnergyState(CultivatorData data) {
        if (data.realm() != CultivationRealm.TEMPERED_BODY) {
            return false;
        }

        int stage = data.minorStage();

        // Stages 1-6 have no Yuan Qi reserve.
        if (!TemperedBodyRules.naturallyGathersPreQiEnergy(stage)) {
            boolean changed = data.maximumQi() != 0.0D
                    || data.currentQi() != 0.0D
                    || data.circulationPercent() != 0.0D
                    || data.burstMode();
            data.setMaximumQi(0.0D);
            data.setCurrentQi(0.0D);
            data.setCirculationPercent(0.0D);
            data.setBurstMode(false);
            return changed;
        }

        // Stages 7-9 naturally form Yuan Qi. It exists, but cannot be consciously
        // circulated or spent until Initial Element.
        double baselineCapacity = TestudoTrainingRules.naturalYuanQiCapacity(stage);
        if (data.maximumQi() < baselineCapacity) {
            data.setMaximumQi(baselineCapacity);
        }

        double before = data.currentQi();
        data.setCurrentQi(Math.min(
                data.maximumQi(),
                data.currentQi() + TestudoTrainingRules.naturalYuanQiPerTick(stage)));
        data.setCirculationPercent(0.0D);
        data.setBurstMode(false);

        return data.currentQi() != before;
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
            data.setMaximumQi(0.0D);
            data.setCurrentQi(0.0D);
            data.setCirculationPercent(0.0D);
            data.setBurstMode(false);
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
            updateTemperedBodyEnergyState(data);

            String milestone = switch (nextStage) {
                case 4 -> " World Energy can now be perceived; use G only as a focus aid.";
                case 7 -> " Natural Yuan Qi formation has begun; absorption is passive.";
                default -> "";
            };

            player.displayClientMessage(
                    Component.literal(
                            "Tempered Body advanced to Stage " + nextStage + "." + milestone),
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

    private static String oneDecimal(double value) {
        return String.format(java.util.Locale.ROOT, "%.1f", value);
    }
}
