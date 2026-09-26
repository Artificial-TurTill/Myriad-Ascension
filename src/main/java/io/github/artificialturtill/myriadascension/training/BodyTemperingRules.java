package io.github.artificialturtill.myriadascension.training;

import io.github.artificialturtill.myriadascension.clan.PrimordialisTestudoClan;
import io.github.artificialturtill.myriadascension.cultivation.data.CultivatorData;
import io.github.artificialturtill.myriadascension.cultivation.realm.CultivationRealm;
import io.github.artificialturtill.myriadascension.item.WearableTrainingWeightItem;
import io.github.artificialturtill.myriadascension.registry.ModItems;
import io.github.artificialturtill.myriadascension.stats.CultivatorStat;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public final class BodyTemperingRules {
    private static final double MIN_NOVELTY = 0.12D;
    private static final double MAX_DIFFICULTY = 3.0D;
    private static final double TRAINING_WEIGHT_LOAD = 10.0D;

    private static final BodyTemperingVector[] PHYSICAL_BREADTH_VECTORS = {
            BodyTemperingVector.STRENGTH,
            BodyTemperingVector.ENDURANCE,
            BodyTemperingVector.TOUGHNESS,
            BodyTemperingVector.COORDINATION,
            BodyTemperingVector.STABILITY,
            BodyTemperingVector.BREATH_CONTROL,
            BodyTemperingVector.RECOVERY
    };

    private BodyTemperingRules() {
    }

    public static boolean supports(CultivatorData data) {
        if (data == null) {
            return false;
        }
        if (!PrimordialisTestudoClan.PRIMORDIAL_TESTUDO_LONGEVITY_ART.id().toString()
                .equals(data.cultivationMethods().activeMethodId())) {
            return false;
        }
        return data.realm() == CultivationRealm.MORTAL
                || data.realm() == CultivationRealm.TEMPERED_BODY;
    }

    public static double trainingWeightLoad(ServerPlayer player, CultivatorData data) {
        if (player == null || data == null) {
            return 0.0D;
        }

        double load = 0.0D;

        if (data.looseTrainingWeightsEnabled()) {
            int count = 0;
            for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                ItemStack stack = player.getInventory().getItem(i);
                if (stack.is(ModItems.BASIC_TRAINING_WEIGHT.get())) {
                    count += stack.getCount();
                }
            }
            load += count * TRAINING_WEIGHT_LOAD;
        }

        for (ItemStack stack : player.getArmorSlots()) {
            if (stack.getItem() instanceof WearableTrainingWeightItem wearable) {
                load += wearable.trainingLoad();
            }
        }

        return load;
    }

    public static double adaptedLoadCapacity(CultivatorData data) {
        double strength = data.stats().get(CultivatorStat.STRENGTH);
        double physical = physicalFoundation(data);
        return Math.max(10.0D, 12.0D + strength * 6.0D + physical * 0.45D);
    }

    public static double effectiveLoadRatio(ServerPlayer player, CultivatorData data) {
        double load = trainingWeightLoad(player, data);
        if (load <= 0.0D) {
            return 0.0D;
        }
        return load / adaptedLoadCapacity(data);
    }

    public static double applyStimulus(
            CultivatorData data,
            TrainingActivity activity,
            BodyTemperingVector vector,
            double baseStimulus,
            double relativeDifficulty) {

        if (!supports(data) || activity == null || vector == null || baseStimulus <= 0.0D) {
            return 0.0D;
        }

        double difficulty = clamp(relativeDifficulty, 0.05D, MAX_DIFFICULTY);
        double novelty = noveltyMultiplier(data, activity);
        double fatigue = TestudoTrainingRules.fatigueEfficiency(data);

        double effective = baseStimulus * difficulty * novelty * fatigue;
        if (effective <= 0.0D) {
            return 0.0D;
        }

        data.bodyTempering().train(vector, effective);
        data.bodyTempering().addAdaptation(activity, baseStimulus * difficulty);
        improveCoreStats(data, vector, effective);
        return effective;
    }

    public static void trainFoundationStance(CultivatorData data, double loadRatio) {
        double load = loadStimulusMultiplier(loadRatio);
        double difficulty = 0.75D + loadRatio * 0.5D;

        applyStimulus(
                data,
                TrainingActivity.FOUNDATION_STANCE,
                BodyTemperingVector.STABILITY,
                0.0025D * load,
                difficulty);
        applyStimulus(
                data,
                TrainingActivity.FOUNDATION_STANCE,
                BodyTemperingVector.ENDURANCE,
                0.0012D * load,
                difficulty);
        applyStimulus(
                data,
                TrainingActivity.CONTROLLED_BREATHING,
                BodyTemperingVector.BREATH_CONTROL,
                0.0010D,
                0.8D);

        if (loadRatio >= 0.20D) {
            applyStimulus(
                    data,
                    TrainingActivity.FOUNDATION_STANCE,
                    BodyTemperingVector.STRENGTH,
                    0.0011D * load,
                    difficulty);
            applyStimulus(
                    data,
                    TrainingActivity.FOUNDATION_STANCE,
                    BodyTemperingVector.TOUGHNESS,
                    0.0007D * load,
                    difficulty);
        }
    }

    public static void trainRunning(CultivatorData data, double loadRatio) {
        double load = loadStimulusMultiplier(loadRatio);
        applyStimulus(
                data,
                loadRatio >= 0.20D ? TrainingActivity.WEIGHTED_MOVEMENT : TrainingActivity.SPRINTING,
                BodyTemperingVector.ENDURANCE,
                0.00045D * load,
                0.45D + loadRatio);
        if (loadRatio >= 0.20D) {
            applyStimulus(
                    data,
                    TrainingActivity.WEIGHTED_MOVEMENT,
                    BodyTemperingVector.STRENGTH,
                    0.00035D * load,
                    0.6D + loadRatio);
            applyStimulus(
                    data,
                    TrainingActivity.WEIGHTED_MOVEMENT,
                    BodyTemperingVector.STABILITY,
                    0.00022D * load,
                    0.5D + loadRatio);
        }
    }

    public static void trainSwimming(CultivatorData data, double loadRatio) {
        double load = loadStimulusMultiplier(loadRatio);
        double difficulty = 0.75D + loadRatio;
        applyStimulus(
                data,
                TrainingActivity.SWIMMING,
                BodyTemperingVector.ENDURANCE,
                0.00125D * load,
                difficulty);
        applyStimulus(
                data,
                TrainingActivity.SWIMMING,
                BodyTemperingVector.BREATH_CONTROL,
                0.00115D * load,
                difficulty);
        applyStimulus(
                data,
                TrainingActivity.SWIMMING,
                BodyTemperingVector.COORDINATION,
                0.00045D * load,
                difficulty);
    }

    public static void trainClimbing(CultivatorData data, double loadRatio) {
        double load = loadStimulusMultiplier(loadRatio);
        double difficulty = 0.7D + loadRatio;
        applyStimulus(
                data,
                TrainingActivity.CLIMBING,
                BodyTemperingVector.STRENGTH,
                0.00090D * load,
                difficulty);
        applyStimulus(
                data,
                TrainingActivity.CLIMBING,
                BodyTemperingVector.ENDURANCE,
                0.00070D * load,
                difficulty);
        applyStimulus(
                data,
                TrainingActivity.CLIMBING,
                BodyTemperingVector.COORDINATION,
                0.00040D * load,
                difficulty);
    }

    public static void trainStrike(
            CultivatorData data,
            double damage,
            double targetThreat,
            double equipmentAdvantage) {

        double threat = clamp(targetThreat, 0.15D, 2.5D);
        double equipmentPenalty = clamp(1.0D / Math.max(1.0D, equipmentAdvantage), 0.20D, 1.0D);
        double stimulus = clamp(damage, 0.0D, 20.0D) * 0.018D * equipmentPenalty;

        applyStimulus(
                data,
                TrainingActivity.STRIKING,
                BodyTemperingVector.STRENGTH,
                stimulus,
                threat);
        applyStimulus(
                data,
                TrainingActivity.COMBAT,
                BodyTemperingVector.COORDINATION,
                stimulus * 0.75D,
                threat);
        applyStimulus(
                data,
                TrainingActivity.COMBAT,
                BodyTemperingVector.STABILITY,
                stimulus * 0.35D,
                threat);
    }

    public static void trainDefensiveStress(
            CultivatorData data,
            double damageFractionOfMaxHealth,
            double sourceThreat) {

        double intensity = clamp(damageFractionOfMaxHealth, 0.0D, 1.5D);
        if (intensity < 0.02D) {
            return;
        }

        double stimulus = intensity * 0.65D;
        double difficulty = clamp(sourceThreat, 0.15D, 2.5D);

        applyStimulus(
                data,
                TrainingActivity.DEFENSIVE_STRESS,
                BodyTemperingVector.TOUGHNESS,
                stimulus,
                difficulty);
        applyStimulus(
                data,
                TrainingActivity.DEFENSIVE_STRESS,
                BodyTemperingVector.STABILITY,
                stimulus * 0.45D,
                difficulty);
    }

    public static void trainFallingImpact(CultivatorData data, double fallDistance) {
        double excess = Math.max(0.0D, fallDistance - 3.0D);
        if (excess <= 0.0D) {
            return;
        }

        double stimulus = Math.min(0.65D, excess * 0.045D);
        double difficulty = Math.min(2.5D, 0.6D + excess / 6.0D);

        applyStimulus(
                data,
                TrainingActivity.FALLING_IMPACT,
                BodyTemperingVector.TOUGHNESS,
                stimulus,
                difficulty);
        applyStimulus(
                data,
                TrainingActivity.FALLING_IMPACT,
                BodyTemperingVector.STABILITY,
                stimulus * 0.65D,
                difficulty);
    }

    public static void trainRecovery(CultivatorData data) {
        applyStimulus(
                data,
                TrainingActivity.RECOVERY,
                BodyTemperingVector.RECOVERY,
                0.00065D,
                0.55D);
    }

    public static void trainWorldEnergyFocus(
            CultivatorData data,
            double environmentMultiplier) {

        applyStimulus(
                data,
                TrainingActivity.WORLD_ENERGY_FOCUS,
                BodyTemperingVector.WORLD_ENERGY_PERCEPTION,
                0.0021D,
                clamp(environmentMultiplier, 0.5D, 2.5D));
    }

    public static void trainNaturalAbsorption(
            CultivatorData data,
            double absorbedAmount,
            double environmentMultiplier) {

        if (absorbedAmount <= 0.0D) {
            return;
        }

        double base = absorbedAmount * 0.55D;
        applyStimulus(
                data,
                TrainingActivity.NATURAL_ABSORPTION,
                BodyTemperingVector.VESSEL_DEVELOPMENT,
                base,
                clamp(environmentMultiplier, 0.5D, 2.5D));
    }

    public static double physicalFoundation(CultivatorData data) {
        double strength = data.bodyTempering().development(BodyTemperingVector.STRENGTH);
        double endurance = data.bodyTempering().development(BodyTemperingVector.ENDURANCE);
        double toughness = data.bodyTempering().development(BodyTemperingVector.TOUGHNESS);
        double coordination = data.bodyTempering().development(BodyTemperingVector.COORDINATION);
        double stability = data.bodyTempering().development(BodyTemperingVector.STABILITY);
        double breath = data.bodyTempering().development(BodyTemperingVector.BREATH_CONTROL);
        double recovery = data.bodyTempering().development(BodyTemperingVector.RECOVERY);

        // Testudo deliberately favors stability, toughness and patient endurance.
        double weighted = strength
                + endurance * 1.25D
                + toughness * 1.25D
                + coordination * 0.75D
                + stability * 1.35D
                + breath
                + recovery;
        return weighted / 7.60D;
    }

    public static double stageProgress(CultivatorData data) {
        int targetStage = targetStage(data);
        double physicalTarget = 6.0D + targetStage * 4.0D;
        double physicalFraction = clamp(physicalFoundation(data) / physicalTarget, 0.0D, 1.0D);

        double result;
        if (targetStage <= 3) {
            result = physicalFraction;
        } else if (targetStage <= 6) {
            double perceptionTarget = (targetStage - 3) * 8.0D;
            double perceptionFraction = clamp(
                    data.bodyTempering().development(BodyTemperingVector.WORLD_ENERGY_PERCEPTION)
                            / perceptionTarget,
                    0.0D,
                    1.0D);
            result = physicalFraction * 0.62D + perceptionFraction * 0.38D;
        } else {
            double perceptionTarget = 24.0D;
            double vesselTarget = (targetStage - 6) * 8.0D;
            double perceptionFraction = clamp(
                    data.bodyTempering().development(BodyTemperingVector.WORLD_ENERGY_PERCEPTION)
                            / perceptionTarget,
                    0.0D,
                    1.0D);
            double vesselFraction = clamp(
                    data.bodyTempering().development(BodyTemperingVector.VESSEL_DEVELOPMENT)
                            / vesselTarget,
                    0.0D,
                    1.0D);
            result = physicalFraction * 0.48D
                    + perceptionFraction * 0.20D
                    + vesselFraction * 0.32D;
        }

        if (!hasPhysicalBreadth(data, physicalTarget)) {
            result = Math.min(result, 0.99D);
        }

        return clamp(result * 100.0D, 0.0D, 100.0D);
    }

    public static boolean readyToAdvance(CultivatorData data) {
        return stageProgress(data) >= 99.999D;
    }

    public static double noveltyMultiplier(CultivatorData data, TrainingActivity activity) {
        double exposure = data.bodyTempering().adaptation(activity);
        return Math.max(MIN_NOVELTY, 1.0D / (1.0D + exposure / 30.0D));
    }

    public static double testudoEnvironmentMultiplier(ServerPlayer player, CultivatorData data) {
        if (player == null || data == null) {
            return 1.0D;
        }

        double multiplier = 1.0D;
        int seaLevel = player.level().getSeaLevel();

        if (player.getY() < seaLevel - 8) {
            multiplier += 0.20D;
        }
        if (player.getY() < 0) {
            multiplier += 0.15D;
        }

        int earth = data.affinities().get(io.github.artificialturtill.myriadascension.affinity.AffinityType.EARTH);
        multiplier += Math.min(0.30D, earth * 0.015D);
        return multiplier;
    }

    public static double loadStimulusMultiplier(double loadRatio) {
        if (loadRatio <= 0.0D) {
            return 1.0D;
        }
        if (loadRatio < 0.15D) {
            // The cultivator has adapted so thoroughly that these weights are nearly ordinary clothing.
            return 1.02D;
        }
        return 1.0D + Math.min(0.85D, loadRatio * 0.55D);
    }

    private static int targetStage(CultivatorData data) {
        if (data.realm() == CultivationRealm.MORTAL) {
            return 1;
        }
        if (data.realm() != CultivationRealm.TEMPERED_BODY) {
            return 1;
        }
        return Math.min(10, data.minorStage() + 1);
    }

    private static boolean hasPhysicalBreadth(CultivatorData data, double target) {
        double floor = target * 0.32D;
        int developed = 0;
        for (BodyTemperingVector vector : PHYSICAL_BREADTH_VECTORS) {
            if (data.bodyTempering().development(vector) >= floor) {
                developed++;
            }
        }

        // The Testudo method specifically refuses to accept a body that lacks its
        // characteristic stability/toughness/endurance foundation.
        boolean testudoCore = data.bodyTempering().development(BodyTemperingVector.STABILITY) >= floor
                && data.bodyTempering().development(BodyTemperingVector.TOUGHNESS) >= floor
                && data.bodyTempering().development(BodyTemperingVector.ENDURANCE) >= floor;

        return developed >= 4 && testudoCore;
    }

    private static void improveCoreStats(
            CultivatorData data,
            BodyTemperingVector vector,
            double amount) {

        switch (vector) {
            case STRENGTH ->
                    data.stats().add(CultivatorStat.STRENGTH, amount * 0.018D);
            case ENDURANCE, TOUGHNESS, RECOVERY ->
                    data.stats().add(CultivatorStat.VITALITY, amount * 0.010D);
            case COORDINATION ->
                    data.stats().add(CultivatorStat.AGILITY, amount * 0.018D);
            case STABILITY -> {
                data.stats().add(CultivatorStat.STRENGTH, amount * 0.004D);
                data.stats().add(CultivatorStat.VITALITY, amount * 0.006D);
            }
            case BREATH_CONTROL ->
                    data.stats().add(CultivatorStat.VITALITY, amount * 0.004D);
            case VESSEL_DEVELOPMENT -> {
                data.stats().add(CultivatorStat.MERIDIAN_QUALITY, amount * 0.004D);
                data.stats().add(CultivatorStat.DANTIAN_QUALITY, amount * 0.004D);
            }
            case WORLD_ENERGY_PERCEPTION -> {
                // Perception is deliberately not raw Spiritual Sense growth yet.
                // That stat becomes a much larger system later.
            }
        }
    }

    private static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }
}
