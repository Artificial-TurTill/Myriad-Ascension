# Work log

## 2026-10-09: Foundation extracted from Dragonblock C+, Myriad Realm add-on (0.1.0-dev)

Request: take the Dragonblock C+ mod (uploaded `dragonblockcplus-0.8.0.2-dev-WP.jar`; source on the port branch of
`Artificial-TurTill/Dragonblock-C-`, commit `0e43f12`) and extract the whole stat system and its infrastructure plus
the concepts of skills, races and transformations, so Myriad Realm ideas can be added to it. Strip every DBC item,
races back to Human only. Keep the main structure, race and appearance, stats, the Natural / Ki character and the
class concepts; in Myriad Realm the energy is Qi, a separate system, and a Qi character starts with 1 in every
stat. Myriad Realm is an add-on of the Foundation. Set up the same Obsidian vault and development process as
Dragonblock C+.

Decisions asked from the user:
- Existing alpha-16 code: **fresh start, archive alpha-16**.
- Packaging: **two separate jar mods**, the Foundation a required dependency of Myriad Realm.

Done:
- Read the DBC+ source (FighterState, CharacterData, LegacyStats / LegacyBaseStats / LegacyGameplay / LegacyDamage,
  CharacterService, CharacterCombat, CharacterAttachments, CreationScreen, CharacterScreen, BasicSkill,
  TransformSkill, ARSN layer) and its vault / work log / validation conventions. The jar only contains classes; the
  source branch was used for all reading.
- Archived alpha-16: `src/` and the old build removed (history keeps it, last commit `bcdb5d1`), documents moved to
  `archive/alpha-16/`.
- New Gradle multi-project (`foundation`, `myriad-realm`), ModDevGradle 2.0.147, NeoForge 21.1.252 (the DBC+ version;
  alpha-16 used .251). Myriad Realm depends on the Foundation project for compiling and declares it as a required
  `ordering="AFTER"` dependency; the Foundation jar contains no Myriad Realm code and vice versa.
- Foundation: ARSN runtime vendored from DBC+ (+ ARSN-REF-6 `objectsOf`, ARSN-REF-7 negative literals), schema and
  content loader with cross-reference checks, records for power types / classes / races / skills / transformations,
  `StatMath` (JRMCore maths generalised), `Fighter`, `CharacterProfile` (with dormant state), attachments (character,
  runtime, synced public look), service (actions, creation, tick, sync), combat (Body as life), `VanillaBridge`
  (Natural attribute modifiers, height scale), network (protocol 1), commands, client (creation wizard, sheet,
  Skills, Transformations, HUD, keys V / G / R / Z, hidden hearts for Body characters).
- Built-in content: Human (appearance presets), Natural (STR / DEX / CON, Melee Dmg 0.3, Defense 1.0, Health 2.0),
  class Unclassed.
- Myriad Realm: Qi (six attributes starting at 1, JRMCore Ki per-point values, Body life, release, Mind capacity),
  Martial / Spirit / Body Cultivator, Qi Meditation / Qi Condensation / Endurance, example form Qi Surge.
- Tests: 19 Foundation unit tests, 5 Myriad Realm unit tests, 5 GameTests (Myriad Realm GameTest source set with an
  empty 3x3x3 structure template).
- Vault, README, CLAUDE.md, this log, VALIDATION.md, CI for two jars.

Problems met:
- The ARSN reference parser had no negative literals and no hex numbers: added ARSN-REF-7, colours became
  `"#RRGGBB"` strings.
- Five unit-test expectations were wrong (class bonus on the pool, level count, ARSN error code 1003 for a failed
  constraint); the code was right, the tests were corrected.
- A GameTest measured a punch on a 10-health cow; the fixture now raises the cow's health.
- Maven Central answered 429 once while downloading JUnit; the retry passed.

Validation: see `docs/VALIDATION.md` (0.1.0).

## 2026-10-09: 0.1.1 key layout, Qi Surge (Turbo), X menu, no transformations yet

- CI of 0.1.0 checked: both runs green (see VALIDATION.md).
- User request: keys V menu, C (hold) raise release, Shift+C lower, G transform, H base form, R Qi Surge (Turbo),
  X the X menu (still exists); transformations are not introduced yet.
- Read DBC+ Turbo (`AbilityService.toggleTurbo`, `MovementService` DashKi, `DashClient`, docs/TURBO_SWOOP_0_6_4.md)
  and alpha-16's `CultivationQuickMenuScreen` (git `bcdb5d1`).
- Foundation: Turbo as a power-type feature (`turbo: true` + a `speed` statistic): toggle with DBC conditions,
  JRMCoreH.spdFrm run factor, `(int)(1 + factor)` energy per second, charge twice as fast, energy regeneration
  paused, client run motion without FOV change; skill effect `dash`. Generic X menu (`QuickMenu` registry +
  `QuickMenuScreen`), six built-in cells. Key layout changed. Protocol 2 (PublicLook carries Turbo). Version 0.1.1.
- Myriad Realm: `qi_speed` statistic, class speed bonuses, `turbo: true`, name "Qi Surge"; example transformation
  removed, `transformations.arsn` left empty with instructions.
- Decision: sprinting stays vanilla (DBC also ran with sprint); recorded as an adaptation.
- Tests: +1 Foundation unit test, Myriad Realm example-form test replaced by "no transformations" + Qi Surge test,
  +1 GameTest. One expectation in the new spdFrm test was wrong (3.25, not 2.2); corrected.
- Vault: 0.1.1 note, keys / Turbo / X menu in Foundation Overview, Combat and Resources, Qi, Transformations, Power
  Types, Stats, Skills, Extraction Map, Open Points, Idea Index.

## 2026-10-09: downloads

- User: the jar downloads and the vault download were missing. Handed over `ascension_foundation-0.1.1-dev.jar`,
  `myriad_realm-0.1.1-dev.jar` (built from `c3bd611`, same code as `58f737b`) and `Myriad-Ascension-Vault-0.1.1.zip`
  directly in the session.
- CI now also zips the vault into the build artifact next to both jars; Workflow note updated.

## 2026-10-09: 0.1.2 hairstyle manager restored, first real-client check

- User: "The hairstyle manager was not needed to be removed." Ported the JRMCore custom hair from Dragonblock C+
  (`LegacyHair`, `HairEditor`, `HairRenderLayer`, `HairUpdate`/`HairService`) into the Foundation: `HairCode`,
  `client/hair/*` (state + presets, Barber Menu, five-page editor, HSB colour picker, render layer),
  `HairUpdatePayload`. No JinGames assets: widgets drawn with fills, own generated 16 x 16 hair texture.
- Appearance stores the hairstyle code and a free hair colour; content field `hair_styles` -> `hair`; save schema 2
  (old saves migrate), protocol 3, version 0.1.2.
- The DBC+ BUG-049 cause (empty preset list when the editor is opened without the Barber Menu) is avoided by loading
  the presets before every use. DBC+ itself was not changed.
- Session was interrupted once by a worker restart and the user's usage limit; work resumed from the working tree.
- Discovered that the real client runs here under Xvfb with Mesa software OpenGL; wrote `tools/client-check`
  (python-xlib input + screenshots) and checked creation, hairstyle manager, sheet, HUD, C / R / X and Qi Surge speed
  in the running game (details in VALIDATION.md). Four layout problems were found that way and fixed.
- Tests: +4 unit tests (30), GameTests unchanged (6), all green.

## 2026-10-13: rambling note on stats, release and realm advancement

- User rambling (German): release-based percentage scaling of stats (from 5 %), only CON effective at 0 %, mobility
  only through Qi Surge (fine for the start), Body Tempering limited to STR / DEX / CON plus MND, minor and major realm
  advancement (Body Tempering -> Initial Element unlocks WIL and SPI), Martial Peak inspiration; Myriad Realm is
  mostly self-developed (little to port).
- Recorded only, no code change: new `vault/Myriad Realm/Rambling/` (index, note with the user's points, the current
  code state per stat, open design questions, possible building blocks), template `Templates/Rambling.md`, Workflow
  and CLAUDE.md rule "rambling is not an order", links from Home, Myriad Realm Overview, IDEA-001 and IDEA-002.
