package io.github.artificialturtill.foundation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.artificialturtill.foundation.api.FoundationApi;
import io.github.artificialturtill.foundation.character.Appearance;
import io.github.artificialturtill.foundation.character.Fighter;
import io.github.artificialturtill.foundation.character.HairCode;
import io.github.artificialturtill.foundation.content.ContentSet;
import net.minecraft.nbt.CompoundTag;
import org.junit.jupiter.api.Test;

/** The hairstyle manager's data (JRMCore jrmcDNSH as in Dragonblock C+ LegacyHair). */
class HairCodeTest {
    private static final ContentSet CONTENT = TestContent.load();

    @Test void presetsAreValidSecondVersionCodes() {
        assertEquals(6, HairCode.PRESETS.length);
        for (String p : HairCode.PRESETS) assertTrue(HairCode.valid(p), p);
        assertEquals(1.0F, HairCode.presetVersion(1));  // a stored 392-digit version 1 preset
        assertEquals(2.0F, HairCode.version(HairCode.PRESETS[1]));
        assertTrue(HairCode.validOrBald(""));
        assertFalse(HairCode.valid("12ab"));
    }
    @Test void fieldsAreReadWrittenAndClamped() {
        String code = HairCode.set(HairCode.DEFAULT, 5, HairCode.X, 99);
        assertEquals(99, HairCode.get(code, 5, HairCode.X));
        assertEquals(82, HairCode.clamped(code, 5, HairCode.X));
        assertEquals(HairCode.LENGTH, code.length());
        assertNull(HairCode.strand(HairCode.set(code, 5, HairCode.L, 0), 5)); // hidden group
        HairCode.Strand s = HairCode.strand(HairCode.set(code, 5, HairCode.L, 50), 5);
        assertNotNull(s);
        assertEquals(1F, s.length()[0]);
        assertEquals((50 - 33) / 33F, s.length()[1], 1e-6F);
        assertEquals(0F, s.length()[2]);
        assertTrue(s.bendAboutZ()); // right side group
    }
    @Test void oldSavesAndValidation() {
        CompoundTag old = new CompoundTag();
        old.putInt("hairStyle", -1); old.putInt("skin", 0xFFE0C4);
        assertTrue(Appearance.load(old).bald());
        old.putInt("hairStyle", 3);
        assertEquals(HairCode.DEFAULT, Appearance.load(old).hair());
        var human = CONTENT.race(FoundationApi.HUMAN);
        Appearance look = Appearance.defaults(human);
        assertEquals(look, Appearance.load(look.save()));
        assertThrows(IllegalArgumentException.class, () -> look.withHair("123", 0).validate(human));
        look.withHair("", 0x123456).validate(human);          // bald with any colour
        look.withHair(HairCode.PRESETS[4], 0xABCDEF).validate(human);
    }
    @Test void hairstyleChangesOnTheCharacter() {
        Fighter f = Fighter.create(CONTENT, FoundationApi.HUMAN, "test:energy", "test:fighter", Appearance.defaults(CONTENT.race(FoundationApi.HUMAN)));
        f.setAppearance(f.appearance().withHair(HairCode.PRESETS[2], 0xFF00FF));
        Fighter back = Fighter.load(CONTENT, f.save());
        assertEquals(HairCode.PRESETS[2], back.appearance().hair());
        assertEquals(0xFF00FF, back.appearance().hairColor());
    }
}
