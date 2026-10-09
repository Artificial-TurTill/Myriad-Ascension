---
tags: [foundation, arsn, content]
---
# Content Files

Races, power types, classes, skills and transformations are written in **ARSN** (Advanced Rich Structure Notation,
by DukeOfFlames, the data layer Dragonblock C+ introduced in 0.7.9): typed classes, constraints, defaults and
comments, checked when the game loads.

## Where
| File | Mod | Holds |
| --- | --- | --- |
| `foundation/src/main/resources/data/ascension_foundation/arsn/schema.arsn` | Foundation | the classes (fields, types, defaults, constraints) |
| `…/ascension_foundation/arsn/foundation.arsn` | Foundation | Natural, Unclassed, Human |
| `myriad-realm/src/main/resources/data/myriad_realm/arsn/qi.arsn` | Myriad Realm | Qi power type and classes |
| `…/myriad_realm/arsn/skills.arsn` | Myriad Realm | Qi skills |
| `…/myriad_realm/arsn/transformations.arsn` | Myriad Realm | Qi transformations (none yet) |

New Myriad Realm files are added to `MyriadRealm.CONTENT` (Java list of file names).

## How it is read
- The Foundation puts `schema.arsn` in front of every content file, so a content file only holds objects.
- One file = one ARSN module: object names (`qi_melee`, `human_look`) only need to be unique inside the file;
  `id` values (`myriad_realm:qi`) must be unique across all files.
- Nested values (attributes, statistics, appearance) are separate named objects referenced by name.
- Colours are strings `"#RRGGBB"`; negative numbers are allowed (`[-10, 10]`, Foundation patch ARSN-REF-7).
- `#` starts a comment.

## Errors
Loading stops the game (and `./gradlew build` fails in the tests) with the file and the reason:
- ARSN codes for syntax / type / constraint problems: `Invalid content file qi.arsn: ARSN-1000: …`
  (1000 syntax, 1001 reference, 1002 constraint, 1003 value, 1004 type, 1005 collision …).
- Cross-reference checks after all files: unknown power type / race / skill / previous form, `stat_bonus` or
  `multipliers` with the wrong length, starting values outside 1…limit, a power type without a class, a Body power
  type without a `body` statistic, an energy without an `energy_pool` statistic, unknown skill effect, ids that are
  not `namespace:path`.

## Translations
Names live in the mod's `lang/en_us.json`: `race.<ns>.<path>`, `power_type.…`, `class.…`, `skill.…`, `form.…` (each
with `.desc` for the tooltip), `attribute.<ns>.<key>`, `stat.<ns>.<key>`, `resource.<ns>.<energy>` (ns = namespace
of the power type). A missing name falls back to the id in title case.

## The vendored runtime
`foundation/src/main/java/.../lib/arsn` (arsn-ref 0.2.0, Apache 2.0, notice in `third_party/arsn-ref/NOTICE.md`):
the Dragonblock C+ copy with ARSN-REF-1..5 plus two Foundation patches: ARSN-REF-6 `Runtime.objectsOf(className)`,
ARSN-REF-7 negative number literals.
