---
tags: [foundation, dbcplus, extraction]
---
# DBC+ Extraction Map

Source: Dragonblock C+ **0.8.0.2-dev-WP** (`Artificial-TurTill/Dragonblock-C-`, port branch, commit `0e43f12`),
itself a port of Dragon Block C 1.4.85 + JRMCore 1.3.51 + JBRA 1.6.52. What went into the Foundation, how it changed,
and what stayed behind.

## Taken (generalised)
| DBC+ | Foundation | Change |
| --- | --- | --- |
| `FighterState` | `Fighter` | race / power type / class are content ids instead of fixed indices; Ki-specific fields removed |
| `CharacterData` (attachment, import archive) | `CharacterProfile` | no 1.7.10 import; dormant state for missing add-ons |
| `LegacyStats.stat / statInc / upgradeCost / level` | `StatMath` | per `StatDef`, any attribute count, generalised level |
| `LegacyBaseStats` (starting attributes, resource maxima) | power type `starting`, race `starting`, roles `body` / `energy_pool` / `stamina` | data |
| `LegacyGameplay.recovery` | `StatMath.recovery` | class bonus from data |
| `LegacyDamage.bodyDamage / passive`, `CharacterCombat` | `StatMath.bodyDamage`, `CharacterCombat` | without Ki Fist / Protection / blocking / KO (open points) |
| Race / class tables (6 races, 3 classes) | ARSN `Race` / `CharacterClass` | only Human; Ki classes become Qi classes in Myriad Realm |
| `BasicSkill` (TP + Mind tables) | ARSN `SkillDef` + engine effects | 4 effects so far |
| `TransformSkill`, `LegacyForms`, `TransformService` (forms, mastery) | ARSN `Transformation` + `Fighter.ascend / descend / tickFormSecond` | generic chains, drain %, mastery relief |
| Power types `Natural` / `Ki` (JRMCore Pwrtyps) | `PowerType` (Natural built in) | Ki is not shipped; Qi is Myriad Realm's own |
| Release (charge key, handleRls) | `Fighter.changeRelease / chargeSecond` | Meditation as an effect |
| Turbo (`AbilityService.toggleTurbo`, `MovementService` DashKi run, `DashClient`) | `CharacterService.toggleTurbo`, `Fighter.runFactor / turboCost`, `TurboClient` | power-type flag + `speed` statistic; only Turbo runs (not sprint); no flight |
| TP per hit (jrmcExp) | `Fighter.rewardHit` | chance 25 % for every race |
| Alignment (jrmcAlign 67 %) | `Fighter.alignment` | changes only by command / add-on so far |
| `CreationScreen` (GUI 0 / 2 / 3) | `CreationScreen` | procedural drawing, data-driven rows |
| `CharacterScreen` (GUI 10), `SkillsScreen`, `LegacyHud` | `CharacterSheetScreen`, `SkillsScreen`, `FormsScreen`, `HudLayer` | procedural drawing |
| `LegacyBody` design data | `Appearance` + `AppearanceOptions` | presets per race; no body renderer yet |
| `LegacyHair`, `HairEditor` (Barber Menu, editor, presets, colour picker), `HairRenderLayer` (custom hair), `HairUpdate` | `HairCode`, `client/hair/*`, `HairUpdatePayload` | procedural GUI, own hair texture, no transformation hair motion, no classic models |
| `CharacterService`, `CharacterNetwork`, snapshots | `CharacterService`, `network/*` | owner gets the saved tag and recomputes with the same code |
| ARSN data layer (`lib/arsn`, `ArsnData`) | `lib/arsn`, `ContentLoader` | content files instead of rule tables |
| `/dbcplus` | `/foundation` | |

## Stripped (stays in Dragonblock C+)
All DBC items, blocks, wearables, vanity, extra inventory, plants, trees, music; worlds and dimensions (Namek,
Otherworld, Space Pod…); NPCs, masters, sagas, dragons and wishes; Ki techniques and attacks, Instant Transmission,
fusion, Majin absorption, auras and Ki particles; races Saiyan, Half-Saiyan, Namekian, Arcosian, Majin and their
racial forms; JBRA body models, the classic hair models 0-11 and every JinGames texture and sound; training mini-games, Shadow Dummy,
gravity device; difficulty, death / Otherworld system; the 1.7.10 character import.

## Concepts that can still be extracted (engine features)
Flight / dash / jump skills, Ki sense, blocking, Ki Protection / Ki Fist, energy attacks, KO and Friendly Fist,
stacking forms (Kaioken), transformation meter, strain, groups, settings menu, death penalties. Each would come as a
generic Foundation feature with a skill effect or content field, then Myriad Realm uses it.

## Adaptations (deviations)
1. Level = 1 + (Σ − starting Σ) / 5 instead of Σ / 5 − 11 (identical for 60-point starts).
2. Attribute upgrades add the grown maximum to the current resources.
3. TP chance 25 % for every race (JRMCore: Human / Namekian 25, others 20); races may get a field later.
4. Natural statistics as vanilla attribute modifiers measured from the starting values (JRMCore's Natural had no
   such effect; the Foundation makes training meaningful while a fresh character stays vanilla).
5. GUIs drawn with fills instead of JRMCore textures.
6. Height as the 1.21 `scale` attribute (no JRMCore counterpart).
7. Release changes are allowed in any form (DBC blocked lowering in Kaioken and for Humans / Namekians in a form).
8. Turbo runs only with Turbo on; DBC's DashKi also ran while sprinting (sprinting stays vanilla here).
9. Turbo's speed bonus of the race is data (Human has none; DBC Human had +10 % speed).
10. The X quick menu is not from DBC: it is alpha-16's cultivation quick menu, rebuilt as a generic Foundation menu.
11. The hairstyle can be changed at any time (DBC: Barber Scissors after the free creation edit).
12. Hair colour is free in creation and later (DBC Saiyans were forced to black; no such race here).
