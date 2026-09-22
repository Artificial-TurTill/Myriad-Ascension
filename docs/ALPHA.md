# Myriad Ascension — Alpha 0.1.0-alpha.4

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
- no final textures/UI art/audio
- V is a functional placeholder UI
- X is reserved but not implemented
- alpha commands intentionally bypass future gameplay requirements

## Save compatibility

This build writes player-data schema version 9. Future alpha builds will attempt migrations, but backups are recommended because this is pre-release software.
