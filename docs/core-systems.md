# Core Systems Specification

This document defines the first technical foundation of Myriad Ascension. Gameplay intent is recorded in `docs/design/gameplay-rules.md`.

## 1. Cultivator State

Player cultivation data is stored as a persistent NeoForge entity data attachment.

The initial data schema contains:

- character sex
- body polarity
- cultivation affiliation
- moral alignment
- karma
- Wood / Fire / Earth / Metal / Water / Yin / Yang affinities
- realm and minor stage
- cultivation progress
- cultivation comprehension
- battle comprehension
- current and maximum Qi
- normal circulation percentage
- Burst state
- meridian load
- body, meridian, and soul injury state
- recovery debt
- vessel purity
- impurity load
- Demonic Qi contamination
- Passive Qi Recharging level
- Meditation level
- Qi Concealment level

The model has an explicit schema version so save migrations can be added as systems evolve.

## 2. Mortal State

New data defaults to:

- Realm: Mortal
- Qi: 0 / 0
- circulation: 0%
- Burst: off
- character sex: unset
- body polarity: unset
- affiliation: undecided
- moral alignment: neutral
- karma: 0
- affinities: ungenerated/zero until initial character generation

Mortal is an internal pre-cultivation state rather than one of the major cultivation realms.

## 3. Qi Reserve

Qi is both a resource and the fuel for active reinforcement.

A cultivator tracks:

- current Qi
- maximum Qi
- passive refill ceiling
- active gathering
- normal circulation
- Burst overdrive
- environmental modifiers
- technique efficiency
- meridian load

### Passive recovery

Base natural recovery stops at **10% of maximum Qi**.

Passive Qi Recharging extends that ceiling by **4 percentage points per skill level**, up to level 10.

Current target:

```text
No skill       10%
Level 1        14%
Level 5        30%
Level 10       50%
```

Active gathering/cultivation is required to fill beyond the applicable passive ceiling.

## 4. G / H / R Control Model

### G — Gather and Circulate

Holding G has two roles:

1. gather/refill Qi when the player has a valid cultivation route;
2. increase normal circulation for immediate use.

The amount that can be sustained depends on the actual reserve. High circulation on an almost-empty reserve is possible only briefly.

### H — Suppress

H lowers normal circulation.

This is the efficient way to reduce passive drain and suppress the cultivator's active power.

### R — Burst

R is a distinct overdrive state, not the normal output-up control.

Burst:

- amplifies effective output,
- disproportionately increases Qi drain,
- increases meridian load,
- becomes especially inefficient when technique mastery is poor,
- is intended for short decisive windows, travel, pursuit, escape, or forced techniques.

The exact Burst multiplier and drain curve remain balance constants rather than hard-coded lore.

## 5. Qi Allocation

Normal circulation and allocation are separate.

Example:

```text
Circulation: 60%

Allocation:
Body        20%
Movement    15%
Weapon      20%
Perception   5%
```

The X quick menu will eventually control allocation/modes such as:

- Body
- Movement
- Weapon
- Defense
- Perception
- Aura
- technique-specific states

## 6. Weapon Infusion

Weapon infusion applies cultivated Qi to held weapons.

Performance can depend on:

- realm/stage
- current reserve
- circulation
- Burst state
- weapon allocation
- technique
- elemental affinity
- weapon material/conductivity
- technique mastery
- vessel purity
- injuries

A high-rank artifact can greatly empower a low-rank cultivator without granting the speed, durability, control, or raw body performance of a vastly higher realm.

## 7. Death and Recovery

Realm regression on death is forbidden by design.

On death, the persistent cultivation data is copied to the respawned player and then placed into a recovery state.

Current prototype behavior:

- realm/stage/progress are retained,
- affinities/alignment/skills are retained,
- Burst is disabled,
- normal circulation is reset,
- current Qi is limited toward the base passive baseline,
- body and meridian injuries are imposed,
- recovery debt is imposed.

The numeric injury values are tuning constants and are expected to change during balancing.

## 8. Qi Deviation and Internal Injury

Internal instability can be produced by:

- interrupted cultivation sessions,
- excessive Burst,
- incompatible/foreign Qi,
- hostile techniques,
- demonic interference,
- poor-quality breakthroughs,
- excessive impurities.

Calm uninterrupted cultivation does not randomly produce Qi deviation.

Demonic methods may intentionally destabilize another cultivator, siphon released energy, and leave Demonic Qi contamination.

## 9. Affinity

Initial core affinities:

- Wood
- Fire
- Earth
- Metal
- Water
- Yin
- Yang

Affinity modifies efficiency and development difficulty rather than acting as a permanent class restriction.

The player does not freely choose classical elemental affinity at spawn.

The first-spawn generator is now defined:

- Male: Yang 2 / Yin 1
- Female: Yin 2 / Yang 1
- 10 additional points distributed randomly among the five elements plus matching polarity
- opposite polarity excluded from bonus distribution
- starter cap of 10 per affinity

## 10. Purity and Impurities

The data model reserves:

- vessel purity
- impurity load
- Demonic Qi contamination

These will later affect:

- cultivation efficiency,
- technique performance,
- stability,
- breakthrough quality,
- healing,
- realm-gap performance.

## 11. Realm Model

The complete realm hierarchy is represented in code now, but the implementation ceiling is currently **Saint**.

The realm model also records world scale and whether the transition to that scale is:

- none,
- ascension,
- physical/interstellar travel.

Planet -> Starfield is explicitly modeled as travel, not ascension.

## 12. Persistence

NeoForge data attachments are used for persistent entity cultivation data. For Minecraft 1.21.2–1.21.3, NeoForge documents data attachments as the supported mechanism for attaching persistent custom data to entities.

Death copying is handled manually so the respawn process can preserve permanent progression while applying recovery consequences.

## 13. Networking Rule

Combat and progression are server-authoritative.

The client may request actions, but the server will validate:

- Qi availability
- circulation changes
- Burst state
- technique prerequisites
- cooldowns
- equipment
- realm requirements
- target validity
- damage
- persistent progression

Client synchronization is the next layer and will use NeoForge payload networking.

The client is responsible for presentation, input, UI, and safe prediction only.


## 14. Character Genesis

Character creation is server-authoritative.

Flow:

1. Server detects an incomplete player profile on login.
2. Client is instructed to open the genesis screen.
3. Player chooses Male/Female and Benevolent/Malicious.
4. Client submits only those choices.
5. Server validates that setup has not already been completed.
6. Server assigns moral alignment exactly +1 or -1, sets Unaffiliated, derives Yin/Yang polarity, and rolls affinities.
7. Server returns the generated affinity result to the client.

The client cannot choose or reroll its own affinities.

## 15. Qi Signature Exposure and Concealment

Qi use has an exposure strength.

Current categories:

- suppressed
- passive aura
- active circulation
- calm cultivation
- weapon infusion
- Qi technique
- Burst

Burst is a beacon-level exposure of the active Qi signature.

Qi Concealment is stored as a level 1-10 passive skill once learned. Level-10 hard guarantees currently encoded:

- same/lower major realm observer: concealed
- exactly one major realm higher: concealed unless user is only minor stage 1
- World Creation observer: always detects

Lower-level resolution remains contested until Spiritual Sense/perception rules are implemented.

Concealment artifacts are modeled separately so artifacts can mask or falsify affiliation signatures according to artifact rank.


## 16. Bloodlines

CultivatorData schema v7 persists one primary bloodline lineage.

Bloodline state stores:

- lineage/family identity
- grade identity
- grade effectiveness ceiling
- grade power tier
- purity
- bloodline-conflict damage

### Rules

- bloodline grade and purity are separate,
- same-family stronger grade can overwrite a weaker one,
- same-grade compatible sources can improve purity,
- incompatible families cannot replace the existing lineage and instead cause conflict damage,
- Earth-Rank Primordial Tortoise bloodline keeps pace through Saint,
- the player model does not receive forced visible mutations from this lineage,
- exact stat modifiers remain data/balance work.

### Acquisition

Player acquisition is restricted to explicit progression sources such as quests, ancient tombs, monuments, auctions, spirit pacts, or beast-derived refinement.

NPCs may inherit bloodlines from family lineage at birth.

Ancient Tortoise placement is intentionally sparse, targeting roughly five candidates per 10,000 × 10,000 block area rather than normal mob density.

See `docs/design/bloodlines.md`.


## 17. Clan Library Authorization

CultivatorData schema v8 persists library authorization independently from physical inventory.

The initial Primordialis Testudo implementation allows exactly one authorized take-out book per player for that clan library.

Rules:

- physical possession does not grant usability,
- verification is server-authoritative,
- a newly authorized book replaces the prior authorization,
- rank restrictions still apply,
- technique manuals can layer additional eligibility requirements,
- Custodian is the normal overseer; Elders and Clan Leader may also authorize,
- unauthorized books remain unusable even if stolen/carried.

This prevents inventory theft or mass manual hoarding from bypassing the clan's knowledge hierarchy.
