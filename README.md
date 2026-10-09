# Myriad Ascension

Two mods for **Minecraft 1.21.1 / NeoForge**:

| Mod | Id | Jar | What it is |
| --- | --- | --- | --- |
| **Ascension Foundation** | `ascension_foundation` | `ascension_foundation-<version>.jar` | The character engine extracted from Dragonblock C+ (its JRMCore layer): races and appearance, power types, attributes and statistics, classes, Training Points, power release, skills, transformations with mastery, Body-as-life combat, creation wizard, character sheet, HUD. |
| **Myriad Realm** | `myriad_realm` | `myriad_realm-<version>.jar` | The cultivation add-on. **Requires the Foundation.** Adds Qi (its own power type, every attribute starts at 1, Qi Surge), its classes and skills. Transformations come later. |

Install both jars. Current version: **0.1.2-dev** (network protocol 3, save schema 2).

## In game
- **V**: character menu. Without a character it opens the creation wizard (Appearance → Power Type → Summary).
  Choose **Human** and **Natural** (plain vanilla) or **Qi** (Myriad Realm).
| Key | Function |
| --- | --- |
| V | Character menu (creation wizard if you have no character) |
| C (hold) | Raise power release (with Qi Meditation also refills Qi) |
| Shift + C | Lower power release |
| G | Transform (no transformations exist yet) |
| H | Back to base form |
| R | Qi Surge (Turbo) on / off: run faster for Qi per second |
| X (hold) | X menu: hover a cell, release X (left / right click cycles) |

Hairstyle: the JRMCore / Dragonblock C+ hairstyle manager (Barber Menu, five-page editor, presets, colour picker) from
the creation wizard, the character sheet (Hairstyle button) or the X menu.

- Character sheet: spend TP on attributes ("+", UC multiplier x1/x10/x100/x1000), Skills tab, Transformations tab.
- Operators: `/foundation content | info | tp | attribute | skill | mastery | release | alignment | restore | erase`.

## Adding Myriad Realm content
Races, power types, classes, skills and transformations are ARSN data files, not Java:
`myriad-realm/src/main/resources/data/myriad_realm/arsn/*.arsn` (names in `assets/myriad_realm/lang/en_us.json`).
The build validates them. Step by step: `vault/Process/Adding an Idea.md`.

## Development
- Documentation lives in the **Obsidian vault** `vault/` (open the folder in Obsidian): versions, bugs, ideas,
  Foundation and Myriad Realm reference notes, workflow. Coding log: `docs/WORK_LOG.md`; checks:
  `docs/VALIDATION.md`.
- Build and unit tests: `./gradlew build` (jars in `foundation/build/libs` and `myriad-realm/build/libs`).
- In-game tests: `./gradlew :myriad-realm:runGameTestServer -PgameTests`.
- Run: `./gradlew :myriad-realm:runClient` (both mods).
- The earlier single-mod alpha (0.1.0-alpha.16) is archived in `archive/alpha-16/` and in git history (`bcdb5d1`).

## Technical target
Minecraft 1.21.1, NeoForge 21.1.252, Java 21, ModDevGradle 2.0.147, Parchment 2024.11.17.
Packages: `io.github.artificialturtill.foundation`, `io.github.artificialturtill.myriadrealm`.

## License
**All Rights Reserved.** See `LICENSE`. The vendored ARSN reference implementation is Apache 2.0
(`third_party/arsn-ref`).
