package io.github.artificialturtill.foundation;

import io.github.artificialturtill.foundation.content.ContentLoader;
import io.github.artificialturtill.foundation.content.ContentSet;

/** The built-in Foundation content plus a small Ki-like test power type with a skill and two chained forms. */
final class TestContent {
    static final String TEST = """
        AttributeDef t_str { key: "strength", short: "STR" }
        AttributeDef t_dex { key: "dexterity", short: "DEX" }
        AttributeDef t_con { key: "constitution", short: "CON" }
        AttributeDef t_mnd { key: "mind", short: "MND" }
        AttributeDef t_spi { key: "spirit", short: "SPI" }
        StatDef t_melee { key: "melee", attribute: 0, per_attribute: 2.5, released: true, role: "melee" }
        StatDef t_defense { key: "defense", attribute: 1, per_attribute: 4.0, released: true, role: "defense" }
        StatDef t_body { key: "body", attribute: 2, per_attribute: 20.0, role: "body" }
        StatDef t_stamina { key: "stamina", attribute: 2, per_attribute: 3.5, role: "stamina" }
        StatDef t_pool { key: "pool", attribute: 4, per_attribute: 40.0, role: "energy_pool" }
        PowerType test {
          id: "test:energy", energy: "energy", life: "body",
          attributes: [t_str, t_dex, t_con, t_mnd, t_spi], stats: [t_melee, t_defense, t_body, t_stamina, t_pool],
          starting: [10, 10, 10, 10, 10], attribute_limit: 10000, release: true, capacity_attribute: 3, tp_attribute: 3
        }
        CharacterClass fighter { id: "test:fighter", power_type: "test:energy", stat_bonus: [10, -10, 10, 10, -10] }
        SkillDef boost { id: "test:boost", power_type: "test:energy", max_level: 10, tp_cost: [100], capacity_cost: [10, 5],
          effect: "energy_pool", per_level: 0.01 }
        SkillDef secret { id: "test:secret", max_level: 1, tp_cost: [0], learn: "teacher" }
        Transformation first { id: "test:first", power_type: "test:energy", multipliers: [2.0, 1.5, 1.0, 1.0, 1.0],
          energy_drain: 10.0, min_release: 50, mastery_gain: 1.0, mastery_max: 10.0, mastery_relief: 0.5, color: "#FFCC00" }
        Transformation second { id: "test:second", power_type: "test:energy", previous: "test:first", skill: "test:boost",
          skill_level: 2, multipliers: [3.0, 2.0, 1.0, 1.0, 1.0], energy_drain: 20.0, order: 1 }
        """;

    static ContentSet load() {
        ContentLoader loader = new ContentLoader();
        loader.add("foundation.arsn", new ContentLoader.Source(TestContent.class, "/data/ascension_foundation/arsn/foundation.arsn").text());
        loader.add("test.arsn", TEST);
        return loader.build();
    }
    private TestContent() {}
}
