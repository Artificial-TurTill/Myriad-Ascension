package io.github.artificialturtill.myriadascension.training;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import net.minecraft.nbt.CompoundTag;

public final class BodyTemperingState {
    private final EnumMap<BodyTemperingVector, Double> development =
            new EnumMap<>(BodyTemperingVector.class);
    private final EnumMap<TrainingActivity, Double> adaptation =
            new EnumMap<>(TrainingActivity.class);

    public BodyTemperingState() {
        for (BodyTemperingVector vector : BodyTemperingVector.values()) {
            development.put(vector, 0.0D);
        }
        for (TrainingActivity activity : TrainingActivity.values()) {
            adaptation.put(activity, 0.0D);
        }
    }

    public double development(BodyTemperingVector vector) {
        return Math.max(0.0D, development.getOrDefault(vector, 0.0D));
    }

    public void train(BodyTemperingVector vector, double amount) {
        if (vector == null || amount <= 0.0D) {
            return;
        }
        development.put(vector, development(vector) + amount);
    }

    public void setDevelopmentForTesting(BodyTemperingVector vector, double value) {
        if (vector != null) {
            development.put(vector, Math.max(0.0D, value));
        }
    }

    public double adaptation(TrainingActivity activity) {
        return Math.max(0.0D, adaptation.getOrDefault(activity, 0.0D));
    }

    public void addAdaptation(TrainingActivity activity, double amount) {
        if (activity == null || amount <= 0.0D) {
            return;
        }
        adaptation.put(activity, adaptation(activity) + amount);
    }

    public void setAdaptationForTesting(TrainingActivity activity, double value) {
        if (activity != null) {
            adaptation.put(activity, Math.max(0.0D, value));
        }
    }

    public Map<BodyTemperingVector, Double> developmentView() {
        return Collections.unmodifiableMap(development);
    }

    public Map<TrainingActivity, Double> adaptationView() {
        return Collections.unmodifiableMap(adaptation);
    }

    public void copyFrom(BodyTemperingState other) {
        for (BodyTemperingVector vector : BodyTemperingVector.values()) {
            development.put(vector, other.development(vector));
        }
        for (TrainingActivity activity : TrainingActivity.values()) {
            adaptation.put(activity, other.adaptation(activity));
        }
    }

    public CompoundTag save() {
        CompoundTag root = new CompoundTag();
        CompoundTag developmentTag = new CompoundTag();
        CompoundTag adaptationTag = new CompoundTag();

        for (BodyTemperingVector vector : BodyTemperingVector.values()) {
            developmentTag.putDouble(vector.name(), development(vector));
        }
        for (TrainingActivity activity : TrainingActivity.values()) {
            adaptationTag.putDouble(activity.name(), adaptation(activity));
        }

        root.put("Development", developmentTag);
        root.put("Adaptation", adaptationTag);
        return root;
    }

    public void load(CompoundTag root) {
        CompoundTag developmentTag = root.getCompound("Development");
        CompoundTag adaptationTag = root.getCompound("Adaptation");

        for (BodyTemperingVector vector : BodyTemperingVector.values()) {
            development.put(
                    vector,
                    Math.max(0.0D, developmentTag.getDouble(vector.name())));
        }
        for (TrainingActivity activity : TrainingActivity.values()) {
            adaptation.put(
                    activity,
                    Math.max(0.0D, adaptationTag.getDouble(activity.name())));
        }
    }
}
