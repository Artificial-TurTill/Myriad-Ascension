package io.github.artificialturtill.foundation.content;

/**
 * One statistic computed from one attribute (JRMCoreH.attrStat / statInc): {@code perAttribute} per attribute point.
 * {@code released} statistics are scaled by the power release and use the transformed attributes.
 */
public record StatDef(String key, int attribute, float perAttribute, boolean released, String role) {
    public static final String MELEE = "melee", DEFENSE = "defense", BODY = "body", STAMINA = "stamina",
        ENERGY_POWER = "energy_power", ENERGY_POOL = "energy_pool", HEALTH = "health", SPEED = "speed";
}
