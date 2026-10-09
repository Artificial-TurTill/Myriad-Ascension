package io.github.artificialturtill.foundation;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.artificialturtill.foundation.api.FoundationApi;
import io.github.artificialturtill.foundation.character.Appearance;
import io.github.artificialturtill.foundation.character.CharacterProfile;
import io.github.artificialturtill.foundation.character.Fighter;
import io.github.artificialturtill.foundation.content.ContentLoader;
import io.github.artificialturtill.foundation.content.ContentSet;
import java.util.List;
import org.junit.jupiter.api.Test;

class FighterTest {
    private static final ContentSet CONTENT = TestContent.load();
    private static Appearance look() { return Appearance.defaults(CONTENT.race(FoundationApi.HUMAN)); }
    private static Fighter tester() { return Fighter.create(CONTENT, FoundationApi.HUMAN, "test:energy", "test:fighter", look()); }

    @Test void naturalHumanIsPlainVanilla() {
        Fighter f = Fighter.create(CONTENT, FoundationApi.HUMAN, FoundationApi.NATURAL, FoundationApi.UNCLASSED, look());
        assertArrayEquals(new int[]{10, 10, 10}, f.attributes());
        assertEquals(new Fighter.Caps(0, 0, 0), f.caps());
        assertEquals(100, f.release()); // no release: always full
        assertEquals(3, f.output(0));   // Melee Dmg 0.3 x 10
        assertEquals(20, f.output(2));  // Health 2.0 x 10 = the vanilla 20
        assertEquals(1, f.level());
    }
    @Test void creationValidatesIdentityAndLook() {
        assertThrows(IllegalArgumentException.class, () -> Fighter.create(CONTENT, "test:nobody", "test:energy", "test:fighter", look()));
        assertThrows(IllegalArgumentException.class, () -> Fighter.create(CONTENT, FoundationApi.HUMAN, "test:energy", FoundationApi.UNCLASSED, look()));
        Appearance purple = new Appearance(0, 0x800080, 0, look().hairColor(), look().eyeColor(), 0, 100);
        assertThrows(IllegalArgumentException.class, () -> Fighter.create(CONTENT, FoundationApi.HUMAN, "test:energy", "test:fighter", purple));
        Appearance tall = new Appearance(0, look().skinColor(), 0, look().hairColor(), look().eyeColor(), 0, 140);
        assertThrows(IllegalArgumentException.class, () -> Fighter.create(CONTENT, FoundationApi.HUMAN, "test:energy", "test:fighter", tall));
    }
    @Test void bodyCharacterStartsFullWithClassBonuses() {
        Fighter f = tester();
        assertEquals(new Fighter.Caps(220, 360, 39), f.caps()); // body 200 +10 %, pool 400 -10 %, stamina 35 +10 % = 38.5 -> 39
        assertEquals(f.caps().body(), f.body());
        assertEquals(0, f.release());
        assertEquals(0, f.output(0));    // released Melee at 0 %
        assertEquals(28, f.fullStat(0)); // 25 +10 % = 27.5 -> 28
    }
    @Test void upgradesCostTpAndGrowResources() {
        Fighter f = tester();
        assertThrows(IllegalArgumentException.class, () -> f.upgrade(2, 0));
        f.addTp(16);
        f.upgrade(2, 0);
        assertEquals(11, f.attribute(2));
        assertEquals(0, f.tp());
        assertEquals(f.caps().body(), f.body()); // the new maximum is filled
        f.addTp(10_000);
        f.upgrade(0, 1);
        assertEquals(20, f.attribute(0));
        assertEquals(3, f.level()); // 11 points gained
    }
    @Test void skillsCostTpAndReserveCapacity() {
        Fighter f = tester();
        f.addTp(1000);
        f.learnSkill("test:boost", false);                  // reserves 10 MND of 10
        assertEquals(1, f.skill("test:boost"));
        assertEquals(364, f.caps().energy());               // 400 x (1 - 10 % + 1 %)
        var full = assertThrows(IllegalArgumentException.class, () -> f.learnSkill("test:boost", false));
        assertTrue(full.getMessage().contains("MND"), full.getMessage());
        f.setAttribute(3, 15);
        f.learnSkill("test:boost", false);
        assertEquals(15, f.reservedCapacity());
        assertThrows(IllegalArgumentException.class, () -> f.learnSkill("test:secret", false)); // teacher only
        f.learnSkill("test:secret", true);
        f.forgetSkill("test:boost");
        assertEquals(0, f.skill("test:boost"));
        assertEquals(360, f.caps().energy());
    }
    @Test void transformationsChainDrainAndMaster() {
        Fighter f = tester();
        var low = assertThrows(IllegalArgumentException.class, f::ascend);
        assertTrue(low.getMessage().contains("50"), low.getMessage());
        f.setRelease(50);
        assertEquals("test:first", f.ascend().id());
        assertEquals(20, f.formAttribute(0));
        assertEquals(StatMathTestValues.RELEASED_FIRST, f.output(0));
        var noSkill = assertThrows(IllegalArgumentException.class, f::ascend);
        assertTrue(noSkill.getMessage().contains("skill"), noSkill.getMessage());
        int energy = f.energy();
        assertFalse(f.tickFormSecond());
        assertEquals(energy - 36, f.energy()); // 10 % of 360
        assertEquals(1.0, f.mastery("test:first"));
        f.setMastery("test:first", 10.0);       // full mastery: half the drain
        f.tickFormSecond();
        assertEquals(energy - 54, f.energy());  // then 5 % of 360
        f.setRelease(45);
        assertTrue(f.tickFormSecond());          // below the minimum release: back to base
        assertTrue(f.inBaseForm());
    }
    @Test void selectedFormIsPreferredAndDescendReturnsToBase() {
        Fighter f = tester();
        f.setRelease(100);
        f.selectForm("test:first");
        f.ascend();
        assertTrue(f.descend());
        assertFalse(f.descend());
        assertThrows(IllegalArgumentException.class, () -> f.selectForm("test:unknown"));
    }
    @Test void saveLoadRoundTripAndDormantCharacters() {
        Fighter f = tester();
        f.addTp(500); f.learnSkill("test:boost", false); f.setRelease(60); f.ascend(); f.setAlignment(20);
        Fighter back = Fighter.load(CONTENT, f.save());
        assertEquals(f.save(), back.save());
        // The add-on with the power type is missing: the profile keeps the data untouched.
        ContentSet plain = ContentLoader.load(List.of(new ContentLoader.Source(FighterTest.class, "/data/ascension_foundation/arsn/foundation.arsn")));
        assertNull(Fighter.load(plain, f.save()));
        CharacterProfile profile = new CharacterProfile();
        profile.create(CONTENT, FoundationApi.HUMAN, "test:energy", "test:fighter", look());
        var tag = profile.save();
        CharacterProfile dormant = CharacterProfile.load(plain, tag);
        assertTrue(dormant.dormant());
        assertFalse(dormant.hasCharacter());
        assertEquals(tag, dormant.save());
        assertThrows(IllegalArgumentException.class, () -> dormant.create(plain, FoundationApi.HUMAN, FoundationApi.NATURAL, FoundationApi.UNCLASSED, look()));
        assertTrue(CharacterProfile.load(CONTENT, tag).hasCharacter());
    }
    @Test void naturalHasNoRelease() {
        Fighter f = Fighter.create(CONTENT, FoundationApi.HUMAN, FoundationApi.NATURAL, FoundationApi.UNCLASSED, look());
        assertThrows(IllegalArgumentException.class, () -> f.changeRelease(1));
        f.chargeSecond(false);
        assertEquals(100, f.release());
    }
    /** Released Melee in the first form at 50 %: STR 20 -> 2.5 x 20 +10 % = 55, half = 27. */
    private static final class StatMathTestValues { static final int RELEASED_FIRST = 27; }
}
