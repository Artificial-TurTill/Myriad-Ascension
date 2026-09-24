# Myriad Ascension — Alpha 0.1.0-alpha.11

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

Moves between representative Tempered Body milestones. Tempered Body deliberately keeps usable Qi at zero.

### /myriadalpha initial_element

Moves the player to Initial Element Stage 1 and supplies a temporary 100-Qi test reserve so G/H/R can be exercised. This is a testing shortcut, not the final breakthrough mechanic.

### /myriadalpha grant_testudo_bloodline

Applies the current Earth-Rank Primordial Tortoise bloodline test source.

### /myriadalpha authorize_fundamentals

Simulates the Testudo Clan Custodian authorizing the Cultivation Fundamentals library book.

## Controls

- G — gather/raise normal Qi circulation
- H — suppress/lower normal Qi circulation
- R — toggle Burst
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

During Tempered Body, G is used as a breathing/training rhythm and still does not create usable Qi.

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

## Known limitations

- no generated clan compound yet
- no cultivation NPC runtime yet
- service quests exist as data/state but not full NPC interactions
- no physical manual/library items yet
- no seated calm-cultivation runtime yet
- no final Tempered Body stat/progression tuning
- no passive Qi-regeneration runtime yet
- no final Qi-drain/Burst curves
- no weapon-infusion runtime yet
- no final spirit-beast/world-Qi/formation/alchemy/profession content
- final art/audio is not supplied yet; current textures and UI art are editable local placeholder PNGs
- V is functional and texture-backed; final artwork and spacing can still be iterated
- X is reserved but not implemented
- alpha commands intentionally bypass future gameplay requirements

## Save compatibility

This build writes player-data schema version 11. Future alpha builds will attempt migrations, but backups are recommended because this is pre-release software.
