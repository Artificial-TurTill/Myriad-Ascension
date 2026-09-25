package io.github.artificialturtill.myriadascension.training;

import io.github.artificialturtill.myriadascension.cultivation.data.CultivatorData;
import io.github.artificialturtill.myriadascension.cultivation.data.ModAttachments;
import io.github.artificialturtill.myriadascension.cultivation.realm.CultivationRealm;
import io.github.artificialturtill.myriadascension.cultivation.realm.TemperedBodyBand;
import io.github.artificialturtill.myriadascension.cultivation.realm.TemperedBodyRules;
import io.github.artificialturtill.myriadascension.network.ModNetworking;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.event.entity.living.LivingFallEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

public final class PlayerTrainingEvents {
    private static final double MOVEMENT_THRESHOLD_SQUARED = 0.0016D;

    private PlayerTrainingEvents() {
    }

    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        CultivatorData data = player.getData(ModAttachments.CULTIVATOR_DATA);
        long gameTime = player.level().getGameTime();

        boolean supported = BodyTemperingRules.supports(data);
        boolean exerting = false;

        if (supported) {
            double loadRatio = BodyTemperingRules.effectiveLoadRatio(player, data);
            double horizontalSpeedSquared = horizontalSpeedSquared(player);

            if (player.isSwimming()) {
                BodyTemperingRules.trainSwimming(data, loadRatio);
                data.setTrainingFatigue(data.trainingFatigue() + 0.0025D * (1.0D + loadRatio));
                exerting = true;
            } else if (player.onClimbable() && Math.abs(player.getDeltaMovement().y) > 0.015D) {
                BodyTemperingRules.trainClimbing(data, loadRatio);
                data.setTrainingFatigue(data.trainingFatigue() + 0.0020D * (1.0D + loadRatio));
                exerting = true;
            } else if (player.isSprinting() && horizontalSpeedSquared > MOVEMENT_THRESHOLD_SQUARED) {
                BodyTemperingRules.trainRunning(data, loadRatio);
                data.setTrainingFatigue(data.trainingFatigue() + 0.0010D * (1.0D + loadRatio));
                exerting = true;
            } else if (loadRatio >= 0.20D && horizontalSpeedSquared > MOVEMENT_THRESHOLD_SQUARED) {
                // Deliberate loaded walking remains meaningful, but less efficient than running/swimming.
                BodyTemperingRules.applyStimulus(
                        data,
                        TrainingActivity.WEIGHTED_MOVEMENT,
                        BodyTemperingVector.STRENGTH,
                        0.00025D * BodyTemperingRules.loadStimulusMultiplier(loadRatio),
                        0.45D + loadRatio);
                BodyTemperingRules.applyStimulus(
                        data,
                        TrainingActivity.WEIGHTED_MOVEMENT,
                        BodyTemperingVector.ENDURANCE,
                        0.00020D * BodyTemperingRules.loadStimulusMultiplier(loadRatio),
                        0.40D + loadRatio);
                data.setTrainingFatigue(data.trainingFatigue() + 0.0008D * (1.0D + loadRatio));
                exerting = true;
            }

            if (data.trainingRequested()) {
                exerting |= handleFoundationStance(player, data, loadRatio, gameTime);
            }

            if (!exerting
                    && horizontalSpeedSquared <= MOVEMENT_THRESHOLD_SQUARED
                    && player.onGround()) {
                recover(data);
                BodyTemperingRules.trainRecovery(data);
            }
        } else {
            recover(data);
        }

        boolean energyChanged = updateTemperedBodyEnergyState(player, data);
        boolean progressed = updateTrainingProgress(player, data);

        if ((energyChanged || progressed) && gameTime % 10L == 0L) {
            ModNetworking.syncPlayer(player, data);
        }

        if (supported && gameTime % 20L == 0L && (data.trainingRequested() || exerting)) {
            displayTrainingStatus(player, data);
            ModNetworking.syncPlayer(player, data);
        }
    }

    public static void onLivingFall(LivingFallEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        CultivatorData data = player.getData(ModAttachments.CULTIVATOR_DATA);
        if (!BodyTemperingRules.supports(data)) {
            return;
        }

        double distance = event.getDistance();
        if (distance <= 3.0D) {
            return;
        }

        BodyTemperingRules.trainFallingImpact(data, distance);
        data.setTrainingFatigue(
                data.trainingFatigue() + Math.min(0.45D, (distance - 3.0D) * 0.025D));
        updateTrainingProgress(player, data);
        ModNetworking.syncPlayer(player, data);
    }

    public static void onLivingIncomingDamage(LivingIncomingDamageEvent event) {
        float amount = event.getAmount();
        if (amount <= 0.0F) {
            return;
        }

        LivingEntity target = event.getEntity();
        Entity sourceEntity = event.getSource().getEntity();

        if (sourceEntity instanceof ServerPlayer attacker && attacker != target) {
            CultivatorData attackerData = attacker.getData(ModAttachments.CULTIVATOR_DATA);
            if (BodyTemperingRules.supports(attackerData)) {
                double targetThreat = target.getMaxHealth()
                        / Math.max(1.0F, attacker.getMaxHealth());
                double equipmentAdvantage = attacker.getMainHandItem().isEmpty() ? 1.0D : 1.35D;

                BodyTemperingRules.trainStrike(
                        attackerData,
                        amount,
                        targetThreat,
                        equipmentAdvantage);
                attackerData.setTrainingFatigue(
                        attackerData.trainingFatigue() + Math.min(0.35D, amount * 0.015D));
                updateTrainingProgress(attacker, attackerData);
                ModNetworking.syncPlayer(attacker, attackerData);
            }
        }

        if (target instanceof ServerPlayer defender) {
            CultivatorData defenderData = defender.getData(ModAttachments.CULTIVATOR_DATA);
            if (!BodyTemperingRules.supports(defenderData)) {
                return;
            }

            double damageFraction = amount / Math.max(1.0F, defender.getMaxHealth());
            double sourceThreat = 0.5D;
            if (sourceEntity instanceof LivingEntity livingSource) {
                sourceThreat = livingSource.getMaxHealth()
                        / Math.max(1.0F, defender.getMaxHealth());
            }

            BodyTemperingRules.trainDefensiveStress(
                    defenderData,
                    damageFraction,
                    sourceThreat);
            defenderData.setTrainingFatigue(
                    defenderData.trainingFatigue() + Math.min(0.50D, damageFraction * 1.5D));
            updateTrainingProgress(defender, defenderData);
            ModNetworking.syncPlayer(defender, defenderData);
        }
    }

    private static boolean handleFoundationStance(
            ServerPlayer player,
            CultivatorData data,
            double loadRatio,
            long gameTime) {

        boolean postureValid = TestudoTrainingRules.postureIsValid(player);
        boolean exhausted = data.trainingFatigue() >= TestudoTrainingRules.MAX_FATIGUE;

        if (!postureValid || exhausted) {
            data.resetTrainingSession();

            if (gameTime % 20L == 0L) {
                player.displayClientMessage(
                        Component.literal(exhausted
                                ? "Too fatigued — release the stance and recover."
                                : "Foundation Stance broken — stand still, grounded, with empty hands."),
                        true);
            }
            return false;
        }

        data.incrementTrainingSessionTicks();
        BodyTemperingRules.trainFoundationStance(data, loadRatio);
        data.setTrainingFatigue(
                data.trainingFatigue()
                        + TestudoTrainingRules.FATIGUE_PER_TICK * (1.0D + Math.min(1.5D, loadRatio)));

        if (data.realm() == CultivationRealm.TEMPERED_BODY
                && TemperedBodyRules.bandForStage(data.minorStage())
                        == TemperedBodyBand.ENERGY_SENSING) {

            if (TestudoTrainingRules.hasRecentFocusPulse(data, gameTime)) {
                BodyTemperingRules.trainWorldEnergyFocus(
                        data,
                        BodyTemperingRules.testudoEnvironmentMultiplier(player, data));
            } else if (gameTime % 20L == 0L) {
                player.displayClientMessage(
                        Component.literal(
                                "Your body is training; use G to deliberately focus on the World Energy."),
                        true);
            }
        }

        return true;
    }

    private static boolean updateTemperedBodyEnergyState(
            ServerPlayer player,
            CultivatorData data) {

        if (data.realm() != CultivationRealm.TEMPERED_BODY) {
            return false;
        }

        int stage = data.minorStage();

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

        double baselineCapacity = TestudoTrainingRules.naturalYuanQiCapacity(stage);
        boolean changed = false;
        if (data.maximumQi() < baselineCapacity) {
            data.setMaximumQi(baselineCapacity);
            changed = true;
        }

        // Stages 7-9 have a vessel capable of storing Yuan Qi, but storage no longer
        // fills itself. The cultivator must consciously gather with G. Even then,
        // the Qi remains sealed from true internal circulation until Initial Element.
        if (data.circulationPercent() != 0.0D || data.burstMode()) {
            data.setCirculationPercent(0.0D);
            data.setBurstMode(false);
            changed = true;
        }

        return changed;
    }

    private static boolean updateTrainingProgress(ServerPlayer player, CultivatorData data) {
        if (!BodyTemperingRules.supports(data)) {
            return false;
        }

        double before = data.cultivationProgress();
        double calculated = BodyTemperingRules.stageProgress(data);
        if (calculated > before) {
            data.setCultivationProgress(calculated);
        }

        if (before < TestudoTrainingRules.STAGE_PROGRESS_REQUIRED
                && BodyTemperingRules.readyToAdvance(data)) {
            advanceFoundation(player, data);
            return true;
        }

        return data.cultivationProgress() != before;
    }

    private static void recover(CultivatorData data) {
        data.resetTrainingSession();
        data.setTrainingFatigue(
                data.trainingFatigue() - TestudoTrainingRules.REST_RECOVERY_PER_TICK);
    }

    private static void displayTrainingStatus(ServerPlayer player, CultivatorData data) {
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
                                + "Body " + oneDecimal(BodyTemperingRules.physicalFoundation(data)) + "  |  "
                                + "Fatigue " + percent(data.trainingFatigue()) + "%"
                                + energySuffix),
                true);
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
            data.bodyTempering().resetStageWork();
            data.resetTrainingSession();

            player.displayClientMessage(
                    Component.literal(
                            "Your conditioned mortal body crosses the threshold: Tempered Body — Stage 1."),
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
            data.bodyTempering().resetStageWork();
            data.resetTrainingSession();
            updateTemperedBodyEnergyState(player, data);

            String milestone = switch (nextStage) {
                case 4 -> " World Energy perception can now be deliberately trained with G.";
                case 7 -> " Your vessel can now consciously gather Yuan Qi with G. Storing even a little is a feat; true circulation waits for Initial Element.";
                default -> "";
            };

            player.displayClientMessage(
                    Component.literal(
                            "Tempered Body advanced to Stage " + nextStage + "." + milestone),
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

    private static double horizontalSpeedSquared(ServerPlayer player) {
        double x = player.getDeltaMovement().x;
        double z = player.getDeltaMovement().z;
        return x * x + z * z;
    }

    private static int percent(double value) {
        return (int) Math.round(Math.max(0.0D, Math.min(100.0D, value)));
    }

    private static String oneDecimal(double value) {
        return String.format(java.util.Locale.ROOT, "%.1f", value);
    }
}
