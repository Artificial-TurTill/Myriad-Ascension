package io.github.artificialturtill.foundation.content;

import java.util.List;
import java.util.Map;

/**
 * A race (JRMCoreH.Races): genders, allowed power types (empty = all), statistic bonuses by statistic key, optional
 * starting attributes per power type, and the appearance options.
 */
public record Race(String id, List<String> genders, List<String> powerTypes, Map<String, Integer> statBonus,
                   Map<String, int[]> starting, AppearanceOptions appearance, int order) {
    public boolean allows(String powerType) { return powerTypes.isEmpty() || powerTypes.contains(powerType); }
    public int statBonus(String statKey) { return statBonus.getOrDefault(statKey, 0); }
}
