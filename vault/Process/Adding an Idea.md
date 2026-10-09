---
tags: [process, ideas]
---
# Adding an Idea

How a Myriad Realm idea goes from a note to the game. Example: a new transformation.

1. **Write it down**: new note in `Ideas/` from [[Templates/Idea - Transformation]] (or Skill / Race / Class / Power
   Type). Fill in the fantasy, the numbers you have in mind and open questions. Add a row to [[Idea Index]]
   (status `idea`).
2. **Design it into the fields** the engine understands (status `designed`). For a transformation these are the
   fields of [[Transformations]]: which form it comes from, the required skill and level, the multiplier per
   attribute, energy / stamina drain, minimum release, mastery gain / maximum / relief, colour. If the idea needs
   something no field covers (e.g. "drains Body instead of Qi", "only at night"), write it under *Engine needs*:
   that is new Foundation code.
3. **Add it as content** in Myriad Realm:
   - `myriad-realm/src/main/resources/data/myriad_realm/arsn/transformations.arsn` (skills: `skills.arsn`, classes /
     power type: `qi.arsn`, races: a new file `races.arsn` listed in `MyriadRealm.CONTENT`).
   - Names and descriptions in `myriad-realm/src/main/resources/assets/myriad_realm/lang/en_us.json`
     (`form.myriad_realm.<id>` and `form.myriad_realm.<id>.desc`).
4. **Validate**: `./gradlew build`. A typo, a wrong list length or an unknown reference stops the build with the
   file name and reason (see [[Content Files]]). Add a test in `myriad-realm/src/test/...` for the numbers that
   matter.
5. **Release**: version note, link the idea from it, set the idea to `implemented` with `in: x.y.z`.

```arsn
# transformations.arsn
Transformation azure_dragon_body {
  id: "myriad_realm:azure_dragon_body",
  power_type: "myriad_realm:qi",
  previous: "",                      # from the base form
  skill: "myriad_realm:qi_condensation", skill_level: 3,
  multipliers: [1.5, 1.2, 1.3, 1.5, 1.0, 1.0],   # STR DEX CON WIL MND SPI
  energy_drain: 3.0, stamina_drain: 0.5, min_release: 60,
  mastery_gain: 0.02, mastery_max: 100.0, mastery_relief: 0.6,
  color: "#3FA8FF", order: 1
}
```
