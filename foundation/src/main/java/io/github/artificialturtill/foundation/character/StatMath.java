package io.github.artificialturtill.foundation.character;

import io.github.artificialturtill.foundation.content.StatDef;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * JRMCore 1.3.51 character-sheet maths, generalised from Dragonblock C+ LegacyStats / LegacyBaseStats /
 * LegacyGameplay so that any power type can use it: JRMCoreH.stat / statInc / attrCst / getPlayerLevel and the
 * JRMCoreComTickH regeneration. Numbers keep the original float/double order and rounding.
 */
public final class StatMath {
    /** JRMCore's passive TP chance per powered hit for Humans (JRMCoreH.jrmcExp, 25 %). */
    public static final int TP_CHANCE = 25;
    /** Non-player attackers pierce 10 % of a defense and deal at least 10 % of their hit (jrmcDam). */
    public static final int MOB_PENETRATION = 10;

    /** JRMCoreH.stat: per x attribute (+ race / class % + skill bonus), BigDecimal ROUND_UP to an integer. */
    public static int stat(StatDef def, int attribute, int bonusPercent, float skillBonus) {
        double bs = def.perAttribute() * (float) attribute;
        double value = bs + (double) bonusPercent * 0.01 * bs + bs * (double) skillBonus;
        return new BigDecimal(Double.toString(value)).setScale(0, RoundingMode.UP).min(BigDecimal.valueOf(Integer.MAX_VALUE)).intValue();
    }
    /** JRMCoreH.statInc: the increase of one attribute point, float maths, ROUND_DOWN to one decimal. */
    public static float statInc(StatDef def, int bonusPercent, float skillBonus) {
        float bs = def.perAttribute();
        float value = bs + (float) bonusPercent * 0.01F * bs + bs * skillBonus;
        return new BigDecimal(Float.toString(value)).setScale(1, RoundingMode.DOWN).floatValue();
    }
    /** Current output of a release-scaled statistic: (int)(stat x 0.01 x release). */
    public static int released(int stat, int release) { return (int) ((double) stat * 0.01D * (double) release); }
    /** StatPasDef: while not blocking only this percent of the defense protects. */
    public static int passive(int defense, int percent) { return (int) ((float) (defense * percent) * 0.01F); }

    /**
     * Character level. JRMCore uses sum / 5 - 11, which is level 1 for its 60 starting points; the Foundation
     * generalises it to 1 + (sum - starting sum) / 5 so power types with other starting values (Qi: 6) also start at 1.
     */
    public static int level(int[] attributes, int[] starting) {
        long gained = sum(attributes) - sum(starting);
        return (int) Math.max(1, 1 + gained / 5);
    }
    public static int attributesUntilNextLevel(int[] attributes, int[] starting) {
        long gained = Math.max(0, sum(attributes) - sum(starting));
        return (int) (5 - gained % 5);
    }
    private static long sum(int[] values) { long s = 0; for (int v : values) s += v; return s; }

    /** The UC button: 1, 10, 100 or 1000 points per click. */
    public static int upgradeMultiplier(int index) {
        return switch (index) { case 1 -> 10; case 2 -> 100; case 3 -> 1000; default -> 1; };
    }
    /** attrCst(int[], count): the summed price of the next 1/10/100/1000 points; 0 = cannot be bought. */
    public static int upgradeCost(int[] attributes, int multiplierIndex, int attributeLimit) {
        int att = weightedAttributes(attributes);
        long cap = (long) attributeLimit * attributes.length;
        long total = 0;
        for (int i = 0; i < upgradeMultiplier(multiplierIndex); i++) {
            int single = singleCost(att++, cap);
            if (single == 0) return 0;
            total += single;
            if (total > Integer.MAX_VALUE) return 0;
        }
        return (int) total;
    }
    /** attributeCostAboveLimit: float sum of attributes x 0.75, truncated. */
    static int weightedAttributes(int[] attributes) {
        float weighted = 0;
        for (int attribute : attributes) weighted += attribute * 0.75F;
        return (int) Math.min(Integer.MAX_VALUE, weighted);
    }
    /** attrCst(int): (att x 0.5) - max(0, 140 - att), at least 16; 0 once every attribute is at its limit. */
    static int singleCost(int att, long cap) {
        if (cap <= att) return 0;
        int am = (int) ((double) att * 0.5D);
        int dec = Math.max(0, 140 - att);
        return Math.max(16, am - dec);
    }

    /**
     * JRMCoreComTickH regeneration per 5-second step. resource 0 body, 1 energy, 2 stamina; {@code bonus} is the
     * class recovery percent. Body regenerates fully only at low release, energy slower the higher the release.
     */
    public static int recovery(int maximum, int bonus, int resource, int release) {
        double rate = 1D + maximum * 0.01D * (100 + bonus) * 0.01D;
        double amount = switch (resource) {
            case 0 -> rate * (release < 5 ? 1D : release < 10 ? 0.5D : release < 15 ? 0.25D : 0D);
            case 1 -> rate * 1.5D * (50 - release) * (double) 0.02F;
            case 2 -> rate * 16D;
            default -> throw new IllegalArgumentException("Unknown resource.");
        };
        return (int) Math.clamp(amount, 1D, Integer.MAX_VALUE);
    }

    /**
     * jrmcDam core: (int)((hit - (defense - defense x pen%)) x (1 - endurance)), at least 1 and at least
     * hit x pen% x that factor.
     */
    public static int bodyDamage(int hit, int defense, int penetration, double endurance) {
        int penetrated = (int) ((float) (defense * penetration) * 0.01F);
        double e = Math.max(0D, 1D - endurance);
        int damage = (int) ((double) (hit - (defense - penetrated)) * e);
        damage = Math.max(1, damage);
        double floor = (double) ((float) (hit * penetration) * 0.01F) * e;
        if (floor > (double) damage) damage = (int) floor;
        return damage;
    }
    /** JRMCoreEH.ApplyArmor: each armor piece protects hit x armor / (armor + 25), or its full armor from a hit of 30. */
    public static int armorProtection(double hit, float armor) {
        int protect = (int) (hit * (double) (armor / (armor + 25.0F)));
        return (int) (hit < 30.0D ? (float) protect : armor);
    }
    /** JRMCoreH.Algnmnt: 0 Good (> 66), 1 Neutral (33-66), 2 Evil (< 33). */
    public static int alignmentGroup(int alignment) { return alignment > 66 ? 0 : alignment >= 33 ? 1 : 2; }
    private StatMath() {}
}
