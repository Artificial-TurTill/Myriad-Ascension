# Myriad Ascension — Alpha 0.1.0-alpha.13

This is an early systems alpha for Minecraft 1.21.1 / NeoForge.

It is not content-complete. The purpose of this build is to make the implemented cultivation architecture directly testable before full NPC, world-generation, structure, art, audio, and balance content is added.

## Implemented

- first-login Mortal Genesis
- Male/Female Yin-Yang polarity
- server-generated starting affinities
- Benevolent (+1) / Malicious (-1) starting moral disposition
- persistent cultivation data with versioned migrations
- realm hierarchy with Stage / Order / Rank subdivisions
- G/H/R key controls
- separate normal circulation and Burst state
- server-authoritative control networking
- V Cultivator Status screen
- Primordialis Testudo Longevity Art definition
- known/active cultivation-method persistence
- Mortal -> Tempered Body entry validation
- Primordial Tortoise Earth-Rank bloodline foundation
- organization standing and quest-journal foundations
- Testudo Clan hierarchy/facility rules
- one-book Clan Library authorization system

## Alpha commands

The real NPC/world acquisition loop is not implemented yet. These commands expose the systems for testing and only affect the issuing player.

### /myriadalpha status

Shows current realm, Qi reserve, and active cultivation method.

### /myriadalpha join_testudo

After Mortal Genesis, this:

- joins the Primordialis Testudo Clan as a Junior Disciple,
- grants and activates the Primordialis Testudo Longevity Art,
- enters Tempered Body Stage 1 if the player is still Mortal.

### /myriadalpha tempered stage1
### /myriadalpha tempered stage4
### /myriadalpha tempered stage7
### /myriadalpha tempered stage9

Moves between representative Tempered Body milestones. Stages 7-9 now form a stored Yuan Qi reserve naturally, but that reserve remains unusable until Initial Element.

### /myriadalpha initial_element

Moves the player to Initial Element Stage 1 and supplies a temporary 100-Qi test reserve so G/H/R can be exercised. This is a testing shortcut, not the final breakthrough mechanic.

### /myriadalpha grant_testudo_bloodline

Applies the current Earth-Rank Primordial Tortoise bloodline test source.

### /myriadalpha authorize_fundamentals

Simulates the Testudo Clan Custodian authorizing the Cultivation Fundamentals library book.

## Controls

- B — hold the Primordialis Testudo Foundation Stance
- G — Tempered Body 4-6: focus World Energy perception; Initial Element+: raise conscious Qi circulation
- H — suppress/lower conscious Qi circulation after Initial Element
- R — toggle Burst after active Qi is available
- V — Cultivator Status
- X — reserved for the future quick menu

## Alpha.2 UI changes

- V screen redesigned into a tabbed character sheet
- Overview tab
- Stats & Affinities tab
- Skills tab
- Techniques tab
- core stats now persist with a hard minimum of 1
- technique loadout enforces one active technique per category
- initial categories: Cultivation, Footwork, Weapon, Eyesight
- older saves migrate the active cultivation method into the Cultivation slot

## Alpha.5 UI fix

- V screen no longer uses vanilla menu blur
- world remains sharp behind the character sheet
- custom dark panel remains responsible for readability

## Alpha.6 HUD and technique manuals

- persistent in-game HUD in the top-left
- Health bar
- Qi bar
- active Power percentage bar
- Power percentage is the canonical multiplier for Qi cost/output scaling
- technique-manual item framework
- one distinct manual/scroll item per registered technique
- Primordialis Testudo Longevity Art Manual added
- right-clicking a manual learns its technique server-side
- newly learned techniques auto-equip only when their category slot is empty
- learned-technique knowledge persists in player data
- save schema upgraded to version 10
- manual item currently uses a placeholder paper texture

## Alpha.7 authentic foundation training

The first real cultivation-training loop is the **Primordialis Testudo Foundation Stance**.

Controls and requirements:

- **Hold B** to assume the stance.
- **Hold G** while in the stance to maintain the controlled breathing/gathering rhythm.
- remain grounded and nearly motionless,
- do not sprint, swim, fly, or ride,
- keep both hands empty.

Training effects:

- builds Cultivation Progress,
- builds persistent Training Fatigue,
- fatigue reduces training efficiency,
- uninterrupted posture gradually improves session efficiency,
- releasing the stance allows fatigue to recover,
- relogging does not erase fatigue.

Progression:

- a Mortal with the active Primordialis Testudo Longevity Art can train into Tempered Body Stage 1,
- the stance can continue training through Tempered Body Stages 1-9,
- fully completing Stage 9 does **not** automatically enter Initial Element,
- Initial Element remains a future breakthrough mechanic.

Historical alpha.7 note: this original all-stage B+G breathing loop is superseded by alpha.12. Tempered Body is now stage-aware: Stages 1-3 are physical, Stages 4-6 use G only as a perception/focus aid, and Stages 7-9 gather Yuan Qi passively.

Player data schema is now version 11.

## Alpha.8 power, V-screen, medicines and artifacts

### V screen

The V interface is now divided into focused tabs:

- Overview
- Stats
- Affinities
- Skills
- Techniques
- Abilities
- Conditions

The Abilities page supports mouse-wheel scrolling and shows both unlocked capabilities and the next locked milestones.

The Stats page shows:

- Realm Potential
- Current Combat Index
- Physical Factor
- Energy Factor
- Soul Factor
- Foundation Factor
- Condition Factor
- Battle Factor

### Power scaling

Every minor Stage / Order / Rank now has an explicit Realm Potential value.

The current combat index combines realm potential with:

- physical stats
- energy reserve/output
- soul stats
- foundation/purity
- injuries/condition
- battle comprehension

See `docs/design/minor-realm-power-and-capabilities.md`.

### Medicines and poison

Added:

- Minor Body-Mending Pill
- Meridian-Soothing Pill
- Basic Antidote Pill
- Crude Meridian Poison

Low-grade medicines can leave impurities.

### Artifacts

Added initial artifact framework and:

- Tempered Body Artifact Sword
- Tempered Body Artifact Shield
- Tempered Body Artifact Helmet
- Tempered Body Artifact Chestplate
- Tempered Body Artifact Leggings
- Tempered Body Artifact Boots
- Minor Storage Bag (9 internal slots)

### Instruction manual

Added **Instruction to the Martial World**.

Every new cultivator receives one after Mortal Genesis.

Right-clicking it opens an in-game guide covering:

- controls
- cultivation
- training
- power
- techniques
- medicine/poison
- artifacts
- martial-world progression

### Network

Network protocol is now version 7.

## Alpha.9 editable texture and UI repair

This release fixes the asset and layout problems found in alpha.8.

### Editable local PNG assets

Custom items no longer borrow their visible texture from `minecraft:item/...`.

All current item textures now live under:

`src/main/resources/assets/myriad_ascension/textures/item/`

This includes manuals, medicines, poison, artifacts, the storage bag, and the Instruction manual.

The Tempered Body Artifact Shield now uses:

`textures/item/tempered_body_artifact_shield.png`

instead of the previous iron-ingot placeholder.

### Equipped artifact armor

Worn artifact armor no longer uses the vanilla iron armor material for its visual layers.

Editable armor textures now live at:

- `textures/models/armor/tempered_body_artifact_layer_1.png`
- `textures/models/armor/tempered_body_artifact_layer_2.png`

### Editable GUI PNGs

The following interfaces are now backed by mod-local PNG files:

- `textures/gui/cultivator_status_panel.png`
- `textures/gui/cultivator_status_tabs.png`
- `textures/gui/cultivator_hud.png`
- `textures/gui/minor_storage_bag.png`
- `textures/gui/martial_world_guide.png`

The V-screen tabs no longer use Minecraft's default grey Button texture.

### V-screen text containment

The V screen now enforces:

- fixed content clipping,
- width-aware text wrapping,
- independent left/right column widths,
- wrapping for long cultivation-method, bloodline, technique and ability names,
- wrapped ability descriptions,
- scrolling for the Abilities page.

Long values can no longer draw into neighboring fields.

### Asset map

See `docs/ASSET_GUIDE.md` for exact paths, source dimensions, atlas regions, and the editing workflow.

## Alpha.10 vanilla-style 3D shield

The Tempered Body Artifact Shield now follows Minecraft 1.21.1's actual shield rendering architecture.

Changes:

- replaced the flat `minecraft:item/generated` model with `builtin/entity`,
- copied vanilla shield display transforms for normal use,
- added a separate blocking model with vanilla blocking transforms,
- added a client-only `BlockEntityWithoutLevelRenderer`,
- reuses Minecraft's own `ShieldModel` geometry,
- uses the same `(1, -1, -1)` model-space flip as the vanilla renderer,
- retains enchantment glint through `ItemRenderer.getFoilBufferDirect`,
- replaced the obsolete 16×16 item icon with a 64×64 entity shield texture,
- the initial 64×64 texture uses the vanilla `shield_base_nopattern.png` UV layout as the editable foundation.

Editable texture:

`assets/myriad_ascension/textures/entity/tempered_body_artifact_shield.png`

See:

- `docs/ASSET_GUIDE.md`
- `docs/research/vanilla-1.21.1-shields.md`

## Alpha.11 runtime crash repair and complete UI asset pass

### Alpha.10 startup crash

Alpha.10 could compile successfully but crash during Minecraft client initialization.

Root cause:

- the custom artifact shield renderer was constructed during `RegisterClientExtensionsEvent`,
- its constructor immediately called `EntityModelSet.bakeLayer(ModelLayers.SHIELD)`,
- at that lifecycle point Minecraft had not yet populated the shield model layer,
- the client aborted with `IllegalArgumentException: No model for layer minecraft:shield#main`.

Alpha.11 removes that lifecycle dependency.

The shield renderer now:

- initializes lazily only when Minecraft actually requests the renderer,
- builds the vanilla shield geometry directly with `ShieldModel.createLayer().bakeRoot()`,
- no longer calls `EntityModelSet.bakeLayer(ModelLayers.SHIELD)` during startup.

### UI asset audit

Confirmed working from user testing:

- `textures/gui/minor_storage_bag.png`
- `textures/gui/cultivator_hud.png`

Already asset-backed in code:

- `textures/gui/cultivator_status_panel.png`
- `textures/gui/cultivator_status_tabs.png`
- `textures/gui/martial_world_guide.png`

Newly converted in alpha.11:

- `textures/gui/character_genesis.png`
- `textures/gui/martial_button.png`

Character Genesis no longer uses an invisible/hard-coded shell with vanilla buttons.

The Martial World Guide navigation arrows no longer use vanilla buttons.

See `docs/ASSET_GUIDE.md` for the full editable UI inventory.

## Alpha.12 cultivation correction, guide expansion, and release-asset consolidation

Alpha.12 incorporates the corrected project history and fixes the mismatch between the early Testudo prototype and the established Tempered Body design.

### Stage-aware Testudo training

- Mortal / Tempered Body 1-3: B is physical foundation training; G is not required.
- Tempered Body 4-6: B remains the stance and G acts only as a short World Energy perception/focus pulse.
- Tempered Body 7-9: the stance can continue physical consolidation, while Yuan Qi forms naturally without G.
- Initial Element+: G resumes its long-term role as conscious Qi circulation/mobilization.
- Burst and conscious circulation are explicitly blocked before Initial Element.

### Tempered Body Yuan Qi

Tempered Body Stages 7-9 now possess a real stored Yuan Qi reserve.

Current provisional alpha test capacities:

- Stage 7: 25
- Stage 8: 50
- Stage 9: 75

The reserve fills passively and is forced to 0% active circulation during Tempered Body. The values are test-balance constants, not final lore values.

### Instruction to the Martial World

The in-game guide now documents:

- corrected Tempered Body stage behavior,
- Testudo stage-specific training,
- repeated-use and combat training,
- correct-environment training,
- elemental-damage training,
- hostile-environment training,
- rare Wood/Water healing-based training,
- Innate Affinity versus Current Attunement,
- aligned training, elemental resources, techniques, environment and spirit veins as attunement routes.

### Corrected user textures

The user-supplied corrected files are now the packaged sources for:

- `textures/gui/cultivator_hud.png`
- `textures/gui/minor_storage_bag.png`

### UI release consolidation

Alpha.11's version commit occurred before several later UI-asset commits on main. Alpha.12 deliberately packages the complete post-alpha.11 UI state under one new version/artifact so the V-screen panel, V-screen tabs, Martial World Guide shell, Character Genesis shell, shared martial buttons, HUD and storage bag are all built from the same release head.

The following remain explicitly wired to local editable assets:

- `textures/gui/cultivator_status_panel.png`
- `textures/gui/cultivator_status_tabs.png`
- `textures/gui/martial_world_guide.png`

These three require runtime visual verification in the alpha.12 artifact because compile-time asset references alone do not prove the client's final rendered appearance.

## Alpha.13 runtime texture repair and asset validation

Alpha.13 is driven by the user's alpha.12 runtime screenshots and logs rather than by compile-time assumptions.

### V-screen and Martial World Guide diagnosis

Alpha.12 successfully resolved both GUI ResourceLocations and reached the intended draw calls. The failure was inside Minecraft's image decoder:

- `cultivator_status_panel.png` was found, but `NativeImage` rejected it with `PNG not supported: unknown PNG chunk type`.
- `martial_world_guide.png` failed for the same reason.
- the magenta/black result was therefore Minecraft's missing-texture fallback after image decoding failed, not a wrong namespace, filename, or Java screen path.
- `cultivator_status_tabs.png` rendered in the runtime screenshot and did not produce the same load failure.

The two broken 256x256 GUI sources have been losslessly re-saved from their recoverable RGBA pixels as standards-compliant PNG files while preserving their intended artwork and dimensions.

### PNG validation gate

Alpha.13 adds `scripts/validate_pngs.py` and runs it in GitHub Actions before Gradle.

The validator checks every packaged PNG for:

- the PNG signature,
- legal chunk structure and boundaries,
- IHDR/IDAT/IEND presence,
- CRC integrity,
- a complete zlib-compressed IDAT stream,
- no trailing bytes after IEND.

A malformed custom texture now fails CI before an alpha JAR can be published.

### Shield particle-atlas warning

Alpha.12 also logged the artifact shield's model particle texture as missing from the block atlas.

The shield's actual rendered surface remains:

`textures/entity/tempered_body_artifact_shield.png`

through the custom shield renderer.

Only the item model's `particle` entry now follows vanilla shield behavior and points to `minecraft:block/dark_oak_planks`, which belongs to the correct atlas. This removes the unrelated block-atlas lookup without changing the custom shield surface.

### Historical alpha.10 crash log

The supplied crash report documents the already-known alpha.10 startup failure caused by attempting to bake `minecraft:shield#main` during `RegisterClientExtensionsEvent`. That failure remains fixed by the alpha.11 lazy renderer/direct `ShieldModel.createLayer().bakeRoot()` implementation and is not the cause of the alpha.12 GUI checkerboards.

## Known limitations

- no generated clan compound yet
- no cultivation NPC runtime yet
- service quests exist as data/state but not full NPC interactions
- no physical manual/library items yet
- no seated calm-cultivation runtime yet
- environmental Current Attunement growth is designed and documented but still lacks a full runtime loop
- no final Tempered Body stat/progression tuning; Stage 7-9 Yuan Qi capacities/rates are provisional alpha values
- no passive Qi-regeneration runtime yet
- no final Qi-drain/Burst curves
- no weapon-infusion runtime yet
- no final spirit-beast/world-Qi/formation/alchemy/profession content
- final art/audio is not supplied yet; current textures and UI art are editable local placeholder PNGs validated structurally by CI
- V is functional and texture-backed; alpha.13 repairs the invalid panel PNG while final artwork and spacing can still be iterated
- X is reserved but not implemented
- alpha commands intentionally bypass future gameplay requirements

## Save compatibility

This build writes player-data schema version 11. Future alpha builds will attempt migrations, but backups are recommended because this is pre-release software.
