package io.github.artificialturtill.myriadrealm;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.artificialturtill.foundation.api.FoundationApi;
import io.github.artificialturtill.foundation.character.Appearance;
import io.github.artificialturtill.foundation.character.Fighter;
import io.github.artificialturtill.foundation.content.ContentLoader;
import io.github.artificialturtill.foundation.content.ContentSet;
import io.github.artificialturtill.foundation.content.PowerType;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

/** Myriad Realm content as the Foundation loads it in game: Foundation built-ins first, then every add-on file. */
class QiContentTest {
    static ContentSet load() {
        List<ContentLoader.Source> sources = new ArrayList<>();
        sources.add(new ContentLoader.Source(FoundationApi.class, "/data/ascension_foundation/arsn/foundation.arsn"));
        for (String file : MyriadRealm.CONTENT) sources.add(new ContentLoader.Source(MyriadRealm.class, "/data/myriad_realm/arsn/" + file + ".arsn"));
        return ContentLoader.load(sources);
    }
    private static final ContentSet CONTENT = load();
    private static Fighter qi(String cls) {
        return Fighter.create(CONTENT, FoundationApi.HUMAN, MyriadRealm.QI, "myriad_realm:" + cls, Appearance.defaults(CONTENT.race(FoundationApi.HUMAN)));
    }

    @Test void onlyHumansAndQiIsItsOwnPowerType() {
        assertEquals(List.of(FoundationApi.HUMAN), CONTENT.raceList().stream().map(r -> r.id()).toList());
        assertEquals(List.of(FoundationApi.NATURAL, MyriadRealm.QI), CONTENT.powerTypeList().stream().map(PowerType::id).toList());
        PowerType qi = CONTENT.powerType(MyriadRealm.QI);
        assertEquals("qi", qi.energy());
        assertTrue(qi.bodyLife());
        assertTrue(qi.release());
        assertEquals(List.of("myriad_realm:martial_cultivator", "myriad_realm:spirit_cultivator", "myriad_realm:body_cultivator"),
            CONTENT.classesOf(MyriadRealm.QI).stream().map(c -> c.id()).toList());
    }
    @Test void everyQiAttributeStartsAtOne() {
        for (String cls : List.of("martial_cultivator", "spirit_cultivator", "body_cultivator")) {
            Fighter f = qi(cls);
            assertArrayEquals(new int[]{1, 1, 1, 1, 1, 1}, f.attributes(), cls);
            assertEquals(1, f.level());
            assertEquals(0, f.tp());
        }
    }
    @Test void startingResourcesPerClass() {
        assertEquals(new Fighter.Caps(20, 40, 4), qi("martial_cultivator").caps()); // Body 20 = the vanilla 20 health
        assertEquals(new Fighter.Caps(18, 44, 4), qi("spirit_cultivator").caps());  // 18, 44, 3.15 -> 4
        assertEquals(new Fighter.Caps(22, 36, 4), qi("body_cultivator").caps());
        Fighter f = qi("martial_cultivator");
        assertEquals(3, f.fullStat(0));  // Melee 2.5 -> 3
        assertEquals(4, f.fullStat(1));  // Defense 4
        assertEquals(6, f.fullStat(4));  // Qi Power 5.2 -> 6
        assertEquals(16, f.upgradeCost(0));
    }
    @Test void skillsNeedTrainedMind() {
        Fighter f = qi("martial_cultivator");
        f.addTp(10_000);
        var mind = assertThrows(IllegalArgumentException.class, () -> f.learnSkill("myriad_realm:qi_meditation", false));
        assertTrue(mind.getMessage().contains("MND"), mind.getMessage());
        f.setAttribute(4, 10);
        f.learnSkill("myriad_realm:qi_condensation", false);
        assertEquals(41, f.caps().energy()); // 40 +1 % -> 40.4 -> 41
    }
    @Test void exampleTransformationNeedsHalfRelease() {
        Fighter f = qi("martial_cultivator");
        assertEquals(1, f.availableForms().size());
        assertThrows(IllegalArgumentException.class, f::ascend);
        f.setRelease(50);
        assertEquals("myriad_realm:qi_surge", f.ascend().id());
        assertEquals(1, f.formAttribute(0)); // 1 x 1.25 stays 1 until trained
        f.setAttribute(0, 100);
        assertEquals(125, f.formAttribute(0));
    }
}
