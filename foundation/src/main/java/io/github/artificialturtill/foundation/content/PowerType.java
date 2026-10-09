package io.github.artificialturtill.foundation.content;

import java.util.List;

/**
 * A power type (JRMCore Pwrtyp): Natural (built in), Ki in Dragonblock C+, Qi in Myriad Realm. It owns its attributes,
 * statistics, starting attributes and classes. {@code bodyLife}: Body is the only life (DBC); otherwise vanilla hearts.
 * {@code turbo}: the power type has DBC Turbo (Myriad Realm names it Qi Surge for Qi).
 */
public record PowerType(String id, String energy, boolean bodyLife, List<AttributeDef> attributes, List<StatDef> stats,
                        int[] starting, int attributeLimit, boolean release, int capacityAttribute, int tpAttribute,
                        int passiveDefense, boolean turbo, int order) {
    public boolean hasEnergy() { return !energy.isEmpty(); }
    public int attributeCount() { return attributes.size(); }
    /** Index of the first statistic with that role, or -1. */
    public int statIndex(String role) {
        for (int i = 0; i < stats.size(); i++) if (stats.get(i).role().equals(role)) return i;
        return -1;
    }
    public StatDef stat(String role) {
        int i = statIndex(role);
        return i < 0 ? null : stats.get(i);
    }
}
