package io.github.artificialturtill.myriadascension.training;

import io.github.artificialturtill.myriadascension.clan.PrimordialisTestudoClan;
import io.github.artificialturtill.myriadascension.cultivation.data.CultivatorData;
import io.github.artificialturtill.myriadascension.cultivation.realm.CultivationRealm;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;

public final class TestudoTrainingRules {
    public static final double STAGE_PROGRESS_REQUIRED = 100.0D;
    public static final double BASE_PROGRESS_PER_TICK = 0.025D;
    public static final double FATIGUE_PER_TICK = 0.020D;
    public static final double REST_RECOVERY_PER_TICK = 0.020D;
    public static final double MAX_FATIGUE = 100.0D;
    public static final int FOCUS_PULSE_GRACE_TICKS = 8;

    private static final double MAX_HORIZONTAL_SPEED_SQUARED = 0.0009D;

    private TestudoTrainingRules() {
    }

    public static boolean supports(CultivatorData data) {
        String activeMethod = data.cultivationMethods().activeMethodId();
        if (!PrimordialisTestudoClan.PRIMORDIAL_TESTUDO_LONGEVITY_ART.id().toString()
                .equals(activeMethod)) {
            return false;
        }

        return data.realm() == CultivationRealm.MORTAL
                || data.realm() == CultivationRealm.TEMPERED_BODY;
    }

    public static boolean postureIsValid(ServerPlayer player) {
        Vec3 movement = player.getDeltaMovement();
        double horizontalSpeedSquared =
                movement.x * movement.x + movement.z * movement.z;

        return player.onGround()
                && !player.isSprinting()
                && !player.isSwimming()
                && !player.isFallFlying()
                && !player.isPassenger()
                && horizontalSpeedSquared <= MAX_HORIZONTAL_SPEED_SQUARED
                && player.getMainHandItem().isEmpty()
                && player.getOffhandItem().isEmpty();
    }

    public static boolean requiresWorldEnergyFocus(CultivatorData data) {
        return data.realm() == CultivationRealm.TEMPERED_BODY
                && data.minorStage() >= 4
                && data.minorStage() <= 6;
    }

    public static boolean hasRecentFocusPulse(CultivatorData data, long gameTime) {
        long pulse = data.lastTrainingFocusPulseTick();
        return pulse != Long.MIN_VALUE
                && gameTime >= pulse
                && gameTime - pulse <= FOCUS_PULSE_GRACE_TICKS;
    }

    public static String trainingModeName(CultivatorData data) {
        if (data.realm() == CultivationRealm.MORTAL || data.minorStage() <= 3) {
            return "Testudo Physical Foundation";
        }
        if (data.minorStage() <= 6) {
            return "Testudo World Energy Perception";
        }
        return "Testudo Vessel Consolidation";
    }

    public static double fatigueEfficiency(CultivatorData data) {
        double fraction = Math.max(
                0.0D,
                Math.min(1.0D, data.trainingFatigue() / MAX_FATIGUE));

        return Math.max(0.25D, 1.0D - 0.75D * fraction);
    }

    public static double continuityMultiplier(CultivatorData data) {
        double fraction = Math.min(1.0D, data.trainingSessionTicks() / 2400.0D);
        return 1.0D + 0.25D * fraction;
    }

    public static double progressPerTick(CultivatorData data) {
        return BASE_PROGRESS_PER_TICK
                * fatigueEfficiency(data)
                * continuityMultiplier(data);
    }
}
