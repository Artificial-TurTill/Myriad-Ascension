package io.github.artificialturtill.foundation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.github.artificialturtill.foundation.character.StatMath;
import io.github.artificialturtill.foundation.content.StatDef;
import org.junit.jupiter.api.Test;

/** The generalised maths must give the same numbers as Dragonblock C+ (JRMCore 1.3.51 defaults). */
class StatMathTest {
    private static final StatDef MELEE = new StatDef("melee", 0, 2.5F, true, StatDef.MELEE);
    private static final StatDef KI_POOL = new StatDef("pool", 5, 40F, false, StatDef.ENERGY_POOL);
    private static final StatDef DEFENSE = new StatDef("defense", 1, 4F, true, StatDef.DEFENSE);

    @Test void statRoundsUpLikeJrmcore() {
        assertEquals(25, StatMath.stat(MELEE, 10, 0, 0F));       // Human Martial Artist, STR 10
        assertEquals(23, StatMath.stat(MELEE, 10, -10, 0F));     // Spiritualist: 22.5 -> 23
        assertEquals(28, StatMath.stat(MELEE, 10, 10, 0F));      // Warrior: 27.5 -> 28
        assertEquals(3, StatMath.stat(MELEE, 1, 0, 0F));         // Qi start: 2.5 -> 3
        assertEquals(404, StatMath.stat(KI_POOL, 10, 0, 0.01F)); // Ki Boost 1: 400 + 4
    }
    @Test void statIncreaseIsRoundedDownToOneDecimal() {
        assertEquals(2.5F, StatMath.statInc(MELEE, 0, 0F));
        assertEquals(2.2F, StatMath.statInc(MELEE, -10, 0F));
        assertEquals(4.4F, StatMath.statInc(DEFENSE, 10, 0F));
    }
    @Test void upgradeCostFollowsAttrCst() {
        int[] dbcStart = {10, 10, 10, 10, 10, 10};
        assertEquals(16, StatMath.upgradeCost(dbcStart, 0, 10_000));
        assertEquals(160, StatMath.upgradeCost(dbcStart, 1, 10_000));
        int[] trained = {1000, 1000, 1000, 1000, 1000, 1000};
        assertEquals(2250, StatMath.upgradeCost(trained, 0, 10_000)); // weighted 4500 x 0.5
        assertEquals(112, StatMath.upgradeCost(new int[]{100, 100, 100}, 0, 100)); // 225 x 0.5
    }
    @Test void levelStartsAtOneForEveryStartingSum() {
        assertEquals(1, StatMath.level(new int[]{10, 10, 10, 10, 10, 10}, new int[]{10, 10, 10, 10, 10, 10}));
        assertEquals(2, StatMath.level(new int[]{15, 10, 10, 10, 10, 10}, new int[]{10, 10, 10, 10, 10, 10}));
        assertEquals(1, StatMath.level(new int[]{1, 1, 1, 1, 1, 1}, new int[]{1, 1, 1, 1, 1, 1}));
        assertEquals(2, StatMath.level(new int[]{6, 1, 1, 1, 1, 1}, new int[]{1, 1, 1, 1, 1, 1}));
        assertEquals(4, StatMath.attributesUntilNextLevel(new int[]{2, 1, 1, 1, 1, 1}, new int[]{1, 1, 1, 1, 1, 1}));
    }
    @Test void damageAndArmorFollowJrmcDam() {
        assertEquals(82, StatMath.bodyDamage(100, 20, 10, 0));
        assertEquals(1, StatMath.bodyDamage(5, 100, 0, 0));
        assertEquals(10, StatMath.bodyDamage(100, 1000, 10, 0)); // mobs always deal 10 %
        assertEquals(56, StatMath.bodyDamage(100, 20, 0, 0.3)); // Endurance 10: -30 %
        assertEquals(2, StatMath.armorProtection(10, 8));
        assertEquals(8, StatMath.armorProtection(30, 8));
        assertEquals(4, StatMath.passive(20, 20));
    }
    @Test void recoveryFollowsJrmcoreComTickH() {
        assertEquals(3, StatMath.recovery(200, 0, 0, 0));
        assertEquals(1, StatMath.recovery(200, 0, 0, 50)); // no body regeneration at high release (minimum 1)
        assertEquals(4, StatMath.recovery(200, 0, 1, 0));  // 3 x 1.5 x 50 x 0.02
        assertEquals(64, StatMath.recovery(300, 0, 2, 0)); // 4 x 16
        assertEquals(0, StatMath.alignmentGroup(67));
        assertEquals(1, StatMath.alignmentGroup(50));
        assertEquals(2, StatMath.alignmentGroup(10));
    }
}
