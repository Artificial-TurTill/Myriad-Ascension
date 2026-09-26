package io.github.artificialturtill.myriadascension.training;

import io.github.artificialturtill.myriadascension.clan.PrimordialisTestudoClan;
import io.github.artificialturtill.myriadascension.cultivation.data.CultivatorData;
import io.github.artificialturtill.myriadascension.cultivation.realm.CultivationRealm;
import io.github.artificialturtill.myriadascension.cultivation.realm.TemperedBodyBand;
import io.github.artificialturtill.myriadascension.cultivation.realm.TemperedBodyRules;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;

public final class TestudoTrainingRules {
    public static final double STAGE_PROGRESS_REQUIRED = 100.0D;
    public static final double BASE_PROGRESS_PER_TICK = 0.025D;
    public static final double FATIGUE_PER_TICK = 0.020D;
    public static final double REST_RECOVERY_PER_TICK = 0.020D;
    public static final double MAX_FATIGUE = 100.0D;
    public static final int FOCUS_PULSE_GRACE_TICKS = 8;

    // Provisional alpha values for the pre-Initial-Element Yuan Qi vessel.
    // Tempered Body 7-9 can store Qi only through deliberate G gathering.
    public static final double STAGE_7_YUAN_QI_CAPACITY = 25.0D;
    public static final double STAGE_8_YUAN_QI_CAPACITY = 50.0D;
    public static final double STAGE_9_YUAN_QI_CAPACITY = 75.0D;

    private static final double STAGE_7_CONSCIOUS_YUAN_QI_PER_PULSE = 0.015D;
    private static final double STAGE_8_CONSCIOUS_YUAN_QI_PER_PULSE = 0.025D;
    private static final double STAGE_9_CONSCIOUS_YUAN_QI_PER_PULSE = 0.040D;

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

    public static boolean requiresFocusPulse(CultivatorData data) {
        return data.realm() == CultivationRealm.TEMPERED_BODY
                && TemperedBodyRules.bandForStage(data.minorStage())
                        == TemperedBodyBand.ENERGY_SENSING;
    }

    public static boolean hasRecentFocusPulse(CultivatorData data, long gameTime) {
        long pulse = data.lastTrainingBreathPulseTick();
        return pulse != Long.MIN_VALUE
                && gameTime >= pulse
                && gameTime - pulse <= FOCUS_PULSE_GRACE_TICKS;
    }

    public static double temperedYuanQiCapacity(int stage) {
        return switch (Math.max(1, Math.min(9, stage))) {
            case 7 -> STAGE_7_YUAN_QI_CAPACITY;
            case 8 -> STAGE_8_YUAN_QI_CAPACITY;
            case 9 -> STAGE_9_YUAN_QI_CAPACITY;
            default -> 0.0D;
        };
    }

    public static double consciousYuanQiPerPulse(int stage) {
        return switch (Math.max(1, Math.min(9, stage))) {
            case 7 -> STAGE_7_CONSCIOUS_YUAN_QI_PER_PULSE;
            case 8 -> STAGE_8_CONSCIOUS_YUAN_QI_PER_PULSE;
            case 9 -> STAGE_9_CONSCIOUS_YUAN_QI_PER_PULSE;
            default -> 0.0D;
        };
    }

    public static String modeName(CultivatorData data) {
        if (data.realm() == CultivationRealm.MORTAL) {
            return "Physical Foundation";
        }

        return switch (TemperedBodyRules.bandForStage(data.minorStage())) {
            case BODY_STRENGTHENING -> "Physical Foundation";
            case ENERGY_SENSING -> "World-Energy Perception";
            case NATURAL_GATHERING -> "Natural-Gathering Consolidation";
        };
    }

    public static double fatigueEfficiency(CultivatorData data) {
        double fraction = Math.max(
                0.0D,
                Math.min(1.0D, data.trainingFatigue() / MAX_FATIGUE));

        return Math.max(0.25D, 1.0D - 0.75D * fraction);
    }

    public static double continuityMultiplier(CultivatorData data) {
        // A stable, uninterrupted posture becomes up to 25% more efficient
        // over the first two minutes of a session.
        double fraction = Math.min(1.0D, data.trainingSessionTicks() / 2400.0D);
        return 1.0D + 0.25D * fraction;
    }

    public static double progressPerTick(CultivatorData data) {
        return BASE_PROGRESS_PER_TICK
                * fatigueEfficiency(data)
                * continuityMultiplier(data);
    }
}
