package io.github.artificialturtill.myriadascension.training;

import io.github.artificialturtill.myriadascension.cultivation.data.CultivatorData;
import io.github.artificialturtill.myriadascension.cultivation.data.ModAttachments;
import io.github.artificialturtill.myriadascension.cultivation.qi.QiRules;
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
        long gameTime = player.level().getGameTime();

        boolean passiveQiChanged = updatePassiveTemperedBodyQi(data);
        if (passiveQiChanged && gameTime % 20L == 0L) {
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
        boolean focusRequired = TestudoTrainingRules.requiresWorldEnergyFocus(data);
        boolean focusValid = !focusRequired || TestudoTrainingRules.hasRecentFocusPulse(data, gameTime);
        boolean exhausted = data.trainingFatigue() >= TestudoTrainingRules.MAX_FATIGUE;

        if (!postureValid || !focusValid || exhausted) {
            data.resetTrainingSession();

            if (gameTime % 20L == 0L) {
                String reason = exhausted
                        ? "Too fatigued — release the stance and recover."
                        : !postureValid
                                ? "Training posture broken — stand still, grounded, with empty hands."
                                : "Focus on the surrounding World Energy with G while holding the stance.";
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
            String energySuffix = data.realm() == CultivationRealm.TEMPERED_BODY
                            && data.minorStage() >= 7
                    ? "  |  Yuan Qi " + oneDecimal(data.currentQi())
                            + "/" + oneDecimal(data.maximumQi()) + " (Sealed)"
                    : "";

            player.displayClientMessage(
                    Component.literal(
                            TestudoTrainingRules.trainingModeName(data) + "  |  "
                                    + "Progress " + percent(data.cultivationProgress()) + "%  |  "
                                    + "Fatigue " + percent(data.trainingFatigue()) + "%  |  "
                                    + "Session " + (data.trainingSessionTicks() / 20) + "s"
                                    + energySuffix),
                    true);
            ModNetworking.syncPlayer(player, data);
        }
    }

    private static boolean updatePassiveTemperedBodyQi(CultivatorData data) {
        if (data.realm() != CultivationRealm.TEMPERED_BODY) {
            return false;
        }

        int stage = data.minorStage();
        double desiredCapacity = QiRules.temperedBodyPassiveCapacity(stage);
        boolean changed = false;

        if (Math.abs(data.maximumQi() - desiredCapacity) > 0.0001D) {
            data.setMaximumQi(desiredCapacity);
            changed = true;
        }

        if (data.circulationPercent() != 0.0D) {
            data.setCirculationPercent(0.0D);
            changed = true;
        }
        if (data.burstMode()) {
            data.setBurstMode(false);
            changed = true;
        }

        if (desiredCapacity <= 0.0D) {
            if (data.currentQi() != 0.0D) {
                data.setCurrentQi(0.0D);
                changed = true;
            }
            return changed;
        }

        double rechargeFraction = QiRules.temperedBodyNaturalRechargeFractionPerTick(stage);
        if (rechargeFraction > 0.0D && data.currentQi() < desiredCapacity) {
            double next = Math.min(
                    desiredCapacity,
                    data.currentQi() + desiredCapacity * rechargeFraction);
            if (next > data.currentQi()) {
                data.setCurrentQi(next);
                changed = true;
            }
        }

        return changed;
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
            data.setMaximumQi(0.0D);
            data.setCurrentQi(0.0D);
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
            data.setMaximumQi(QiRules.temperedBodyPassiveCapacity(nextStage));
            data.setCurrentQi(Math.min(data.currentQi(), data.maximumQi()));
            data.setCirculationPercent(0.0D);
            data.setBurstMode(false);
            data.resetTrainingSession();

            String suffix = nextStage == 4
                    ? " World Energy perception training is now possible."
                    : nextStage == 7
                            ? " Natural Yuan Qi generation has begun; the reserve is sealed."
                            : "";

            player.displayClientMessage(
                    Component.literal(
                            "Tempered Body foundation advanced to Stage " + nextStage + "." + suffix),
                    false);
            ModNetworking.syncPlayer(player, data);
            return;
        }

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
