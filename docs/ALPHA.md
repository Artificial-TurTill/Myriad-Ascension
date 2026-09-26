# Myriad Ascension — Alpha 0.1.0-alpha.16

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

The two visibly broken 256x256 GUI sources have been re-saved from their recoverable RGBA pixels as standards-compliant PNG files while preserving their intended artwork and dimensions.

The new validator then caught a third malformed asset before release: `character_genesis.png`. It was also re-saved as a standards-compliant 512x256 RGBA PNG. This had not appeared in the supplied screenshots, but would have remained a latent runtime risk without the new CI gate.

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

## Alpha.14 Mortal HUD visibility and development logging

### Mortal HUD visibility

The top-left Myriad Ascension cultivation HUD is now completely hidden while the synchronized player realm is `MORTAL`.

This hides the mod's Health / Qi / Power panel as one unit. Minecraft's normal survival HUD is not modified.

Once the player enters any cultivation realm, beginning with Tempered Body, the cultivation HUD becomes eligible to render again according to the current HUD implementation.

No HUD texture or other UI artwork was redesigned for this change.

### Programming session logs

Programming sessions are now recorded under:

`docs/programming-sessions/`

A session log is created when work begins and updated during implementation with requested behavior, decisions, files changed, validation and build results.

## Alpha.15 Body Tempering foundation, alpha editor, purification and affinity safeguards

### Affinities screen

The Affinities explanatory paragraph now reserves enough vertical space for three wrapped lines so the sentence no longer clips at the bottom of the V-screen content area.

### Monotonic stats and affinities

Ordinary progression APIs are now monotonic:

- core-stat `add(...)` ignores negative additions,
- Current Attunement gains use an increase-only API,
- all affinity values remain clamped at 0 or above,
- deliberate loss is isolated behind explicit sacrifice APIs for future mechanics,
- alpha/admin edit commands remain able to set test values directly.

This separates normal progression from future sacrifice mechanics instead of allowing accidental regression through generic gameplay code.

### Body Tempering state

Player data schema is now version 14 and persists a dedicated Body Tempering state.

Tracked development vectors:

- Strength
- Endurance
- Toughness
- Coordination
- Stability
- Breath Control
- Recovery
- Vessel Development
- World Energy Perception

Tracked activity adaptation:

- Foundation Stance
- Controlled Breathing
- Running
- Swimming
- Climbing
- Weighted Movement
- Striking
- Combat
- Defensive Stress
- Recovery
- World Energy Focus
- Natural Absorption

Training value now depends on activity difficulty, fatigue and adaptation. Repeating the same easy stimulus becomes less efficient instead of granting a flat event-count reward.

### Primordialis Testudo physical training

The early Testudo path now accepts multiple physical training sources rather than making the Foundation Stance the whole realm:

- Foundation Stance develops stability, endurance and breath control.
- Carrying Basic Training Weights increases meaningful stance/movement resistance until the cultivator adapts to the load.
- Sprinting gives a small endurance stimulus.
- Swimming develops endurance, breath control and coordination.
- Climbing develops strength, endurance and coordination.
- Weighted movement develops strength/endurance.
- Dealing meaningful combat damage develops strength, coordination and stability.
- Taking meaningful damage develops toughness and stability with adaptation-based diminishing returns.
- Controlled falls/impacts above the ordinary safe threshold develop toughness and stability, with their own adaptation memory.
- Rest/recovery develops the Recovery vector while Training Fatigue falls.
- Tempered Body 4-6 continues physical training while G adds deliberate World Energy perception training.
- Tempered Body 7-9 continues physical training while deliberate G gathering develops the vessel.

Kills themselves grant no cultivation reward.

Stage progress is now derived from cumulative multi-vector development rather than a flat stance timer. Primordialis Testudo additionally requires breadth and its characteristic stability/toughness/endurance foundation before a stage can complete.

### Basic Training Weight

Added:

`myriad_ascension:basic_training_weight`

Loose Basic Training Weights now contribute load only after the player deliberately secures them by right-clicking the item. Multiple secured weights increase load. As Strength and physical foundation rise, the same load provides less relative challenge.

Wearable training-weight gear now uses normal armor equipment slots and contributes training load while worn.

### Minor Purification Pill

Added:

`myriad_ascension:minor_purification_pill`

Current alpha effect:

- removes up to 18 Impurity Load,
- removes up to 6 Demonic Qi contamination,
- restores up to 4 Vessel Purity,
- is not consumed if there is nothing to purify.

The item has its own editable mod-local placeholder PNG.

### Comprehensive alpha state editor

Added the `/myriadalpha edit ...` command tree for development/testing.

It can directly change or manage:

- realm and stage,
- sex and affiliation,
- moral alignment and Karma,
- all core stats,
- innate affinities,
- current affinity/attunement values,
- core skills,
- current/max Qi, circulation and Burst,
- cultivation/battle progress and comprehension,
- injuries, impurity, purity, contamination, meridian load, fatigue and recovery debt,
- bloodline purity/conflict,
- Body Tempering vectors and activity adaptation,
- learned/equipped techniques,
- learned/active cultivation methods,
- organization membership, rank and service merit.

Examples:

`/myriadalpha edit stat strength 5`

`/myriadalpha edit affinity metal 3`

`/myriadalpha edit skill meditation 4`

`/myriadalpha edit condition impurities 0`

`/myriadalpha edit technique equip weapon myriad_ascension:test_blade_art`

`/myriadalpha edit training status`



## Alpha.16 — weights, conscious gathering, and the X quick menu

### Dedicated creative inventory tab

All Myriad Ascension items are now exposed through a dedicated **Myriad Ascension** creative-mode tab. The mod no longer injects its items into vanilla Food, Combat, or Tools tabs.

### Deliberate and wearable training weights

- Right-click `basic_training_weight` to secure/release loose inventory weights.
- Loose weights add load only while secured.
- `training_weight_vest` adds 25 training load while worn.
- `training_weight_leggings` adds 18 training load while worn.
- `training_ankle_weights` adds 12 training load while worn.
- Wearable weights grant zero armor protection; they are resistance equipment, not defensive armor.

### Body Tempering remains bodily

From Tempered Body Stage 4 onward, progression tracks fresh physical work performed in the current stage in addition to cumulative physical development.

Stages 4-6 therefore require:
- continued active bodily tempering,
- fresh physical work in the current stage,
- World Energy perception development.

Stages 7-9 also retain fresh physical work requirements while vessel development becomes increasingly important.

### Stage 7-9 conscious Qi gathering

The old passive late-Tempered-Body refill has been removed.

At Tempered Body Stages 7-9:
- the vessel has Yuan Qi storage capacity,
- holding **G** consciously gathers Qi into that vessel,
- gathering is deliberately slow and affected by environment and fatigue,
- successful storage develops the vessel,
- circulation, Burst, and actual Qi spending remain locked.

Accumulating Qi before Initial Element is therefore a feat rather than passive background regeneration.

### Initial Element true circulation

At Initial Element and above, **G**:
- actively recharges the reserve,
- can recharge from an empty reserve,
- raises internal circulation,
- recharges substantially faster than Tempered Body storage,
- benefits from Meditation level in the current prototype tuning.

### X cultivation quick menu

Holding **X** requests and opens a translucent 3x3 in-world quick menu. The world remains visible and the game is not paused.

Current controls:
- Cultivation Method
- Cultivation Technique
- Weapon Technique
- Footwork Technique
- Eyesight Technique
- Element / Yin-Yang Scan toggle
- Cultivation Gauge toggle
- Loose Training Weights toggle
- central MORE/future-controls cell

Interaction:
- hover a cell and release X to activate/cycle it,
- left click cycles forward/toggles,
- right click cycles backward,
- method and technique changes are server-authoritative.

The Resource Scan and Cultivation Gauge switches are persistent foundations for future detection/rendering systems; Alpha.16 does not yet highlight resources or reveal target cultivation levels.

### Data/network

- player-data schema: **14**
- network protocol: **8**

## Known limitations

- no generated clan compound yet
- no cultivation NPC runtime yet
- service quests exist as data/state but not full NPC interactions
- no physical manual/library items yet
- no seated calm-cultivation runtime yet
- environmental Current Attunement growth is designed and documented but still lacks a full runtime loop
- no final Tempered Body stat/progression tuning; Stage 7-9 conscious-gathering capacities/rates are provisional alpha values
- no passive Qi-regeneration runtime yet
- no final Qi-drain/Burst curves
- no weapon-infusion runtime yet
- resource scanning and cultivation-gauge toggles persist, but their actual detection/rendering systems are not implemented yet
- no final spirit-beast/world-Qi/formation/alchemy/profession content
- final art/audio is not supplied yet; current textures and UI art are editable local placeholder PNGs validated structurally by CI
- V is functional and texture-backed; alpha.13 repairs the invalid panel PNG while final artwork and spacing can still be iterated
- alpha commands intentionally bypass future gameplay requirements

## Save compatibility

This build writes player-data schema version 14. Future alpha builds will attempt migrations, but backups are recommended because this is pre-release software.
