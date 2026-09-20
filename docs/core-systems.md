# Core Systems Specification

This document defines the first mechanical foundation of Myriad Ascension.

## 1. Cultivator State

Persistent player state should be logically separated from temporary combat state.

### Persistent state

Examples:

- Realm
- Minor stage
- Cultivation progress
- Maximum Qi
- Base Qi regeneration
- Affinities
- Constitution
- Meridian quality
- Meridian damage
- Dantian quality/state
- Bloodlines
- Alignment/path data
- Known techniques
- Technique mastery
- Spiritual sense
- Permanent modifiers

### Temporary state

Examples:

- Current Qi
- Active output
- Current Qi allocation
- Current technique
- Casting state
- Cooldowns
- Combat strain
- Active buffs/debuffs
- Poise
- Aura state
- Weapon infusion state

The server should remain authoritative over combat-relevant state.

## 2. Qi Reserve

Every cultivator has:

- Current Qi
- Maximum Qi
- Base regeneration
- Environmental regeneration modifier
- Technique efficiency modifiers
- Active drain

Qi is not merely mana. It also fuels passive physical enhancement when the cultivator raises active output.

## 3. Active Output

The player can circulate only a portion of their accessible cultivation power.

Example:

```text
Current Qi:     8,450 / 10,000
Active Output:  35%
Meridian Load:  18%
```

Higher output can improve:

- Movement speed
- Jump performance
- Physical attack scaling
- Knockback resistance
- Defensive reinforcement
- Technique scaling
- Spiritual pressure
- Weapon infusion
- Perception

Higher output also increases:

- Qi drain
- Meridian load
- Visibility of aura
- Risk from unstable techniques

Output scaling should avoid making maximum output the optimal permanent state.

## 4. Input Model

### G — Gather / Charge

Holding G gathers and circulates Qi into an immediately usable state.

Potential uses:

- Recover combat-ready Qi
- Prepare techniques
- Stabilize circulation
- Accelerate recovery while stationary
- Interact with cultivation locations

### R — Raise Output

Raises active output in configurable increments.

### H — Lower Output

Lowers active output in configurable increments.

At very low output, the player behaves close to vanilla Minecraft and consumes little or no passive Qi.

### X — Quick Menu

Opens a compact menu for combat/cultivation modes.

Initial categories:

- Body
- Movement
- Weapon
- Defense
- Perception
- Aura

The exact UI may be radial, cross-shaped, or compact list-based.

### V — Cultivator Screen

Opens the complete status interface.

## 5. Qi Allocation

Active output and allocation are separate concepts.

Example:

```text
Active Output: 60%

Allocation:
Body        20%
Movement    15%
Weapon      20%
Perception   5%
```

The allocation total cannot exceed active output.

This allows players to specialize dynamically without changing builds.

## 6. Weapon Infusion

Weapon infusion applies cultivated Qi to held weapons.

Possible effects depend on:

- Active output
- Weapon allocation
- Technique
- Element affinity
- Weapon material
- Weapon Qi conductivity
- Realm
- Mastery

Weapon infusion should support vanilla weapons and provide extension points for modded weapons.

## 7. Meridian Load and Internal Injury

Rapid or excessive power use creates strain.

Possible results:

- Increased Qi cost
- Lower regeneration
- Technique instability
- Reduced maximum output
- Internal injuries
- Temporary meridian damage
- Qi deviation under extreme conditions

This acts as an anti-spam mechanic and supports alchemy/healing systems later.

## 8. Affinity

Affinities are efficiency/scaling factors, not class locks.

Initial primary affinities:

- Wood
- Fire
- Earth
- Metal
- Water

Secondary/special affinities will be added later.

Low affinity should increase difficulty, resource cost, and training time but never make ultimate progression mathematically impossible.

## 9. Environmental Qi

Chunks/regions may eventually expose local Qi composition.

Example:

```text
Spiritual Qi: 120
Water Qi:     370
Wood Qi:       90
Yin Qi:        40
Demonic Qi:     5
```

This system will later feed:

- Cultivation speed
- Spirit veins
- Arrays
- Plants
- Ores
- Beasts
- Biomes
- Sect placement
- Natural treasures

## 10. Networking Rule

Combat and progression must be server-authoritative.

The client may request actions, but the server validates:

- Qi availability
- Output changes
- Technique prerequisites
- Cooldowns
- Equipment
- Realm requirements
- Target validity
- Damage
- Persistent progression

The client is responsible for presentation, input, prediction where safe, and UI.
