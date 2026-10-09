package io.github.artificialturtill.foundation.content;

/**
 * A transformation (DBC form): reached from {@code previous} ("" = base form), needs {@code skill} at
 * {@code skillLevel} and at least {@code minRelease} %, multiplies attributes for released statistics, drains energy
 * and stamina (percent of maximum per second); mastery reduces the drain.
 */
public record Transformation(String id, String powerType, String race, String previous, String skill, int skillLevel,
                             float[] multipliers, float energyDrain, float staminaDrain, int minRelease, float masteryGain,
                             float masteryMax, float masteryRelief, int color, int order) {
    public float multiplier(int attribute) { return attribute < multipliers.length ? multipliers[attribute] : 1F; }
    /** Drain factor at a mastery level: 1 at 0 mastery, (1 - relief) at full mastery. */
    public double drainFactor(double mastery) {
        double share = masteryMax <= 0 ? 1 : Math.clamp(mastery / masteryMax, 0, 1);
        return 1.0 - masteryRelief * share;
    }
}
