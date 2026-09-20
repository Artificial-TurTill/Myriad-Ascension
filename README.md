# Myriad Ascension

**Myriad Ascension** is a Minecraft **1.21.2** cultivation and martial-world mod focused on player agency, deep progression, and combat built around active Qi control rather than simple stat inflation.

> **Ten thousand paths. One summit.**

The project draws inspiration from Xianxia, Wuxia, Murim, cultivation fiction, Chinese mythology, Buddhist and Daoist motifs, demonic and righteous cultivation traditions, elemental systems, spirit beasts, bloodlines, sects, formations, alchemy, and ascending world tiers.

The goal is not to reproduce any one existing setting. Myriad Ascension uses original terminology, systems, factions, techniques, progression, and worldbuilding.

## Core Principles

- **No starting talent permanently locks a player out of endgame progression.**
- Natural affinity and constitution change difficulty, efficiency, and available strategies rather than imposing hard class restrictions.
- Cultivation is an active system: the player controls Qi output, circulation, allocation, techniques, weapon infusion, movement, and suppression.
- Stronger realms should feel qualitatively different, not only numerically stronger.
- The world itself contains Qi ecology: spirit veins, elemental regions, spirit stones, beasts, plants, ores, arrays, and cultivation sites.
- Ascension leads to higher worlds/dimensions with stronger enemies, rarer resources, superior techniques, and new progression ceilings.
- Multiple viable paths should exist: righteous, demonic, Buddhist, body cultivation, elemental specialization, bloodline cultivation, formations, alchemy, artifacts, beast-related paths, and more.

## Planned Controls

| Key | Function |
| --- | --- |
| **G** | Gather Qi and raise normal circulation |
| **H** | Suppress/lower normal Qi circulation |
| **R** | Toggle Burst overdrive |
| **X** | Open compact cultivation quick menu |
| **V** | Open full cultivator status screen |

Keybinds will be configurable.

## Core Combat Concept

A cultivator has a stored Qi reserve and an **active output level**. Higher output increases physical enhancement, movement, technique performance, spiritual pressure, weapon infusion, and other effects, but drains Qi faster and increases strain.

The **X quick menu** will control how active Qi is applied, such as:

- Body reinforcement
- Movement
- Weapon infusion
- Defensive reinforcement
- Spiritual perception
- Aura / pressure
- Technique-specific modes

The **V screen** will expose persistent character information such as:

- Cultivation realm and stage
- Stored Qi and Qi capacity
- Active output
- Constitution
- Meridian quality and condition
- Dantian state
- Elemental affinities
- Spiritual roots
- Bloodlines
- Alignment / path information
- Technique mastery
- Spiritual sense
- Body statistics
- Injuries
- Karma / reputation systems where applicable

## World Systems

Planned systems include:

- Five primary elements: **Wood, Fire, Earth, Metal, Water**
- Additional energies such as Lightning, Ice, Wind, Yin, Yang, Blood, Soul, Space, Void, Chaos, Demonic, Life, Death, and others
- Spirit veins and local Qi density
- Spirit stones and cultivation resources
- Elemental biomes and high-density cultivation regions
- Energy-amplifying blocks, ores, woods, herbs, and natural treasures
- Spirit beasts, divine/mythic beasts, bloodlines, beast cores, and oceanic threats
- Formation arrays that gather, redirect, convert, suppress, conceal, defend, or weaponize Qi
- NPC cultivators, sects, structures, teachers, manuals, and crafting traditions
- Higher realms represented by progressively stronger dimensions/worlds

## Progression Philosophy

A weak affinity should make a path harder, not impossible.

Players can overcome poor initial circumstances through systems such as:

- Specialized cultivation methods
- Elemental refinement
- Body cultivation
- Demonic or dangerous shortcuts
- Bloodline awakening
- Rare treasures
- Pills and alchemy
- Formations
- Reconstructed meridians
- Superior scriptures
- Enlightenment
- Spirit beast materials
- Alternative resource conversion
- Higher-world ascension

Exceptional starting talent accelerates early development, while difficult constitutions can gain unique late-game strengths.

## Development Status

**Phase:** Cultivation core implementation

Initial development will focus on the cultivation core before expanding into large content systems.

### First milestone

1. Persistent cultivator data
2. Qi reserve and regeneration
3. Active Qi output
4. G/H/R input controls
5. X quick menu
6. V cultivator screen
7. Basic affinities
8. Weapon Qi infusion
9. One elemental combat path
10. One cultivation breakthrough

## Technical Target

- **Minecraft:** 1.21.2
- **Language:** Java 21
- **Loader:** NeoForge
- **Build tooling:** ModDevGradle
- **NeoForge:** 21.2.1-beta
- **Minecraft:** 1.21.2
- **Base package:** `io.github.artificialturtill.myriadascension`
- **Mod ID:** `myriad_ascension`

## Repository Structure

The intended high-level code architecture is modular:

```text
myriad_ascension
├── cultivation
│   ├── realm
│   ├── qi
│   ├── meridian
│   ├── breakthrough
│   └── ascension
├── combat
│   ├── output
│   ├── infusion
│   ├── techniques
│   ├── pressure
│   └── injuries
├── affinity
├── elements
├── alignment
├── bloodline
├── constitution
├── techniques
├── world
│   ├── qi
│   ├── spiritvein
│   ├── biome
│   └── realms
├── formation
├── alchemy
├── artifacts
├── beasts
├── npc
├── sect
└── client
    ├── hud
    ├── keybind
    └── screen
```

## Design Specifications

- `docs/design/gameplay-rules.md` — authoritative gameplay decisions
- `docs/design/realm-hierarchy.md` — full realm/world progression
- `docs/design/audio.md` — placeholder/final audio policy
- `docs/design/martial-peak-progression-adaptation.md` — realm subdivision/unlock adaptation
- `docs/design/inheritance-and-techniques.md` — manuals, monuments, technique traits, Qi nature
- `docs/design/primordialis-testudo-clan.md` — first test clan
- `docs/design/empire-politics.md` — Empire diplomacy and succession
- `docs/core-systems.md` — technical core-system specification
- `docs/roadmap.md` — implementation roadmap

## License

**All Rights Reserved.** See `LICENSE`.
