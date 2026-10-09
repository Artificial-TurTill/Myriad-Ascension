package io.github.artificialturtill.foundation;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.artificialturtill.foundation.api.FoundationApi;
import io.github.artificialturtill.foundation.content.ContentLoader;
import io.github.artificialturtill.foundation.content.ContentSet;
import io.github.artificialturtill.foundation.content.PowerType;
import java.util.List;
import org.junit.jupiter.api.Test;

class ContentTest {
    @Test void builtInContentIsNaturalAndHuman() {
        ContentSet set = ContentLoader.load(List.of(new ContentLoader.Source(ContentTest.class, "/data/ascension_foundation/arsn/foundation.arsn")));
        assertEquals(List.of(FoundationApi.HUMAN), set.raceList().stream().map(r -> r.id()).toList());
        PowerType natural = set.powerType(FoundationApi.NATURAL);
        assertFalse(natural.bodyLife());
        assertFalse(natural.hasEnergy());
        assertArrayEquals(new int[]{10, 10, 10}, natural.starting());
        assertEquals(0.3F, natural.stat("melee").perAttribute());
        assertEquals(List.of(FoundationApi.UNCLASSED), set.classesOf(FoundationApi.NATURAL).stream().map(c -> c.id()).toList());
        assertTrue(set.race(FoundationApi.HUMAN).allows(FoundationApi.NATURAL));
        assertEquals(0xFFE0C4, set.race(FoundationApi.HUMAN).appearance().skinColors().get(0));
    }
    @Test void testContentLoadsWithNegativeBonusesAndChains() {
        ContentSet set = TestContent.load();
        assertArrayEquals(new int[]{10, -10, 10, 10, -10}, set.characterClass("test:fighter").statBonus());
        assertEquals("test:first", set.transformation("test:second").previous());
        assertEquals(List.of("test:first", "test:second"), set.transformationsOf("test:energy", FoundationApi.HUMAN).stream().map(t -> t.id()).toList());
        assertEquals(2, set.skillsOf("test:energy").size());
        assertEquals(1, set.skillsOf(FoundationApi.NATURAL).size()); // the power-type-free teacher skill
    }
    @Test void brokenReferencesAreReportedWithTheirReason() {
        var e = assertThrows(IllegalStateException.class, () -> new ContentLoader().add("bad.arsn",
            "CharacterClass c { id: \"x:c\", power_type: \"x:missing\", stat_bonus: [] }").build());
        assertTrue(e.getMessage().contains("unknown power type x:missing"), e.getMessage());
        var length = assertThrows(IllegalStateException.class, () -> new ContentLoader().add("t.arsn", TestContent.TEST
            .replace("multipliers: [3.0, 2.0, 1.0, 1.0, 1.0]", "multipliers: [3.0]")).build());
        assertTrue(length.getMessage().contains("multipliers need 5 entries"), length.getMessage());
    }
    @Test void syntaxAndTypeErrorsKeepTheArsnCode() {
        var syntax = assertThrows(IllegalStateException.class, () -> new ContentLoader().add("s.arsn", "Race r {"));
        assertTrue(syntax.getMessage().startsWith("Invalid content file s.arsn: ARSN-"), syntax.getMessage());
        var constraint = assertThrows(IllegalStateException.class, () -> new ContentLoader().add("c.arsn", TestContent.TEST.replace("life: \"body\"", "life: \"wings\"")));
        assertTrue(constraint.getMessage().contains("ARSN-100"), constraint.getMessage());
        var duplicate = assertThrows(IllegalStateException.class, () -> new ContentLoader().add("a.arsn", TestContent.TEST).add("b.arsn", TestContent.TEST));
        assertTrue(duplicate.getMessage().contains("duplicate id"), duplicate.getMessage());
        assertThrows(IllegalArgumentException.class, () -> ContentLoader.color("FFFFFF"));
    }
}
