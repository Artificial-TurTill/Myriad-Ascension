# Gameplay Rules

This document records the current design decisions supplied by the project owner.
It is authoritative for implementation unless superseded by a later design decision.

## Starting State

A new player begins as a **Mortal**.

- Vanilla base health remains 20 HP unless another system modifies it.
- A Mortal begins with **0 Qi** and no usable cultivation by default.
- Cultivation requires obtaining a method, inheritance, teacher, resource, or another valid route into cultivation.
- The initial character setup asks the player to choose **Male** or **Female**.
- Male establishes a Yang body polarity; Female establishes a Yin body polarity.
- The initial setup also establishes the player's starting moral alignment on the Malicious ↔ Benevolent axis.
- Affiliation is separate from morality and begins unaffiliated until world/faction/cultivation choices establish it.
- Classical elemental affinity is random rather than chosen.
- Early settlements are deliberately important because ordinary wilderness threats are intended to be lethal to Mortals.

The first settlement implementation should build on vanilla villages. Martial clans, sect branches, cities, capitals, and other settlement tiers will be layered on later.

## Affinity

The initial core affinity set is:

- Wood
- Fire
- Earth
- Metal
- Water
- Yin
- Yang

Starting attunement is intentionally limited and uses the following exact genesis rule:

- Male starts at **Yang 2 / Yin 1**.
- Female starts at **Yin 2 / Yang 1**.
- After that fixed baseline, exactly **10 additional random affinity points** are distributed.
- Bonus points may go to Wood, Fire, Earth, Metal, Water, and the character's matching polarity.
- Bonus points may **not** go to Yin for a Male character or Yang for a Female character.
- No starting affinity may exceed **10**.
- Therefore the generated seven-aspect starting profile totals **13 points** including the fixed polarity baseline.

Affinity affects efficiency and ease of development. It is **not** a hard class lock. A poor natural affinity can ultimately be overcome through extraordinary effort, resources, techniques, ascension choices, constitutions, demonic methods, or other systems.

Rare higher-ranked resources can be consumed to influence affinity, but this competes with using those same resources for cultivation or technique training.

## Affiliation, Morality, and Karma

Affiliation and morality are independent systems.

Moral alignment is a continuous Malicious ↔ Benevolent axis.

Affiliation describes the broad political/cultural/cultivation side a character belongs to, such as:

- Righteous
- Unorthodox
- Demonic
- Buddhist
- Imperial
- Unaffiliated

A member of a nominally Righteous affiliation can still become malicious. A Demonic cultivator can become benevolent enough to be regarded as weak or disloyal by their own side.

Technique use, visible aura, Burst, meditation, weapon infusion, and other exposed Qi can reveal a cultivator's actual path to NPCs. Hostile NPCs may attack when they detect an incompatible cultivation signature.

Buddhist cultivation additionally uses Karma. Karma is not identical to moral alignment.

See `docs/design/affiliation-and-morality.md` for the current detailed rules.

## Death and Recovery

Death does **not** reduce the player's permanent cultivation realm.

On death:

- The player respawns through normal Minecraft respawn rules, normally at their bed or valid spawn point.
- Realm, stage, affinity, learned techniques, profession knowledge, and permanent progression are retained.
- The cultivator returns in a damaged state.
- Injuries suppress effective combat and cultivation performance until recovered.
- Recovery requires time and/or resources that would otherwise be useful for cultivation.
- Healing pills and specialist alchemy can accelerate recovery.
- Fighting while seriously damaged can worsen recovery debt and internal injuries.

The design must never silently de-rank a player because they died.

## Cultivation Inheritances

A Mortal needs a valid cultivation inheritance before progressing.

Initial acquisition routes include:

- Master
- Clan
- Sect
- Cultivator Tomb
- Monument
- other inheritance structures

Tombs may grant methods that are poorly suited to the player and methods can have a maximum supported realm.

Monuments are selective and may test specific elemental affinity, innate potential, current attunement, or sex-aligned Yin/Yang potential.

The first test clan is the **Primordialis Testudo Clan**, whose Earth-aligned inheritance is slow but foundation/lifespan focused and supports progression through Transcendent.

See `docs/design/inheritance-and-techniques.md` and `docs/design/primordialis-testudo-clan.md`.

## Calm Cultivation

Calm cultivation is an intentional seated/meditative state.

Benefits can include:

- drawing ambient Qi,
- refilling the Qi reserve,
- circulating Qi through the body,
- increasing cultivation progress,
- improving comprehension,
- training Meditation,
- interacting with spirit veins, arrays, chambers, and cultivation environments.

A cultivation session can accumulate progress before it is safely stabilized.

Being interrupted can cause some of the unstable progress from that session to become internal damage or Qi deviation. Calm, uninterrupted cultivation itself does not randomly cause Qi deviation.

## Cultivation as Sleep

Starting at **Qi Transformation**, uninterrupted cultivation can substitute for sleep.

- Required cultivation duration: **60 real minutes**.
- Tempered Body and Mortals cannot use this rule.
- In multiplayer, the night can advance only when every relevant player is sleeping or has satisfied the cultivation-rest requirement.

## Combat Cultivation

Combat provides progression of a different kind:

- technique mastery,
- battle experience,
- cultivation comprehension,
- practical control of Qi output,
- situational proficiency with movement, defense, weapons, and elemental methods.

Raw realm progress should not become optimally farmable by mindless combat. Combat is primarily a mastery/comprehension accelerator.

## Demonic Cultivation

Demonic cultivation can use methods forbidden or rejected by righteous traditions.

Planned examples include:

- draining life force from entities,
- stealing/refining foreign Qi,
- deliberately destabilizing another cultivator's circulation,
- inducing Qi deviation and harvesting the released energy.

A victim can be left with **Demonic Qi contamination**. Until cleansed or expelled, this can:

- worsen injuries,
- damage or erode usable Qi,
- obstruct cultivation,
- interfere with circulation,
- create additional risk during recovery.

Foreign Qi from an equal or stronger cultivator is harder to expel directly. Healing, cleansing methods, pills, external assistance, arrays, or superior techniques can therefore become strategically important.

## Qi Recovery

Natural passive recovery is deliberately limited.

- Base natural recovery stops at **10% of maximum Qi**.
- Above that, the player normally needs active gathering/cultivation.
- The passive skill **Passive Qi Recharging** extends the passive ceiling.
- Each skill level adds **4 percentage points** to the passive refill ceiling.
- Skills have levels 1 through 10 once learned.

Therefore, with Passive Qi Recharging at level 10, the current design ceiling is 50% of maximum Qi without active charging.

This value is a balance rule and may later be exposed to configuration.

## Meditation Skill

**Meditation** is an active cultivation skill.

- It is trained by deliberate seated cultivation.
- It improves the efficiency/speed of calm cultivation.
- It begins at level 1 when learned.
- It is separate from Passive Qi Recharging.

## G / H / R Qi Controls

### G — Gather and Circulate

Holding G performs two related actions when the player has a valid cultivation method:

1. draw/recover Qi according to environment, method, realm, and current state;
2. increase the amount of stored Qi actively circulated for immediate use.

A Mortal with no cultivation access and zero Qi cannot simply create useful cultivation power by holding G.

### H — Suppress Usage

H reduces the percentage of Qi being actively circulated/used.

This is the normal way to power down and conserve reserves.

### R — Burst Mode

R is **not** the ordinary increase-output button.

R activates a separate Burst/overdrive state:

- effective power rises,
- movement/travel can become much faster,
- techniques can be forced beyond normal efficient output,
- Qi consumption rises faster than the benefit gained,
- poor technique mastery causes especially severe inefficiency,
- meridian load and injury risk increase.

Burst is intended for emergencies, pursuit, escape, travel, decisive attacks, and other short high-output windows rather than permanent use.

A cultivator with only a small fraction of their reserve remaining may technically force high circulation, but cannot sustain it for long.

## Technique Signatures and Qi Nature

Techniques carry independent path, element, and mechanical-nature traits.

Path traits include:

- Neutral
- Righteous
- Demonic
- Buddhist
- Unorthodox
- Imperial

Imperial can coexist with the other path traits.

Practicing arts can imprint their path nature into the player's Qi. Official affiliation and actual Qi nature are separate systems.

Burst, Qi techniques, weapon infusion, and active cultivation expose the Qi traits actually being circulated.

See `docs/design/inheritance-and-techniques.md`.

## Technique Mastery

Technique strength depends on more than realm.

Relevant factors include:

- realm and stage,
- technique rank,
- mastery level,
- elemental/environmental compatibility,
- current Qi reserve,
- circulation,
- Burst state,
- vessel purity,
- injuries,
- equipment/artifact quality,
- profession support,
- bloodline/constitution,
- comprehension.

Training methods can vary by technique. Examples include:

- repeated use,
- combat,
- cultivation in the correct environment,
- receiving relevant elemental damage,
- surviving hostile environments,
- rare healing-based training for appropriate Wood/Water methods.

## Realm Difference and Combat

A major-realm breakthrough is a qualitative power jump.

- Minor-stage differences matter, but are normally surmountable by skill, techniques, artifacts, preparation, numbers, or matchup advantages.
- A full major-realm difference is much more severe.
- Very large realm gaps should make attacks appear slow, weak, or trivial to the superior cultivator unless an exceptional mechanic bridges the gap.

A low-realm cultivator may wield a vastly superior artifact, but they cannot automatically use its full speed, force, control, durability, or special abilities.

Artifacts and techniques can create an edge; they do not erase all realm scaling.

## Purity and Impurities

Cultivation quality matters.

Impurities may be accumulated through:

- low-grade pills,
- excessive pill use,
- incompatible environments,
- foreign/corrupt Qi,
- demonic contamination,
- unstable breakthroughs,
- poor-quality cultivation methods.

Purity influences efficiency, stability, breakthrough quality, and effective combat performance.

Environmental contamination is path-dependent. For example, prolonged exposure to Nether-aligned demonic/fire/lava energies may be harmful to some righteous methods while being useful to compatible methods.

## Professions

Planned cultivation professions include at minimum:

- Alchemist
- Artifact Refiner / Artificer
- Weapon Smith
- Array Master

Narrow professions can advance faster within a smaller scope. Broad/versatile professions must maintain competence across their required subdisciplines before the profession as a whole can advance.

Professional mastery primarily advances by actually performing the profession.

## Current World Scope

The first playable implementation is deliberately limited to the **Starting World + Planet-level Saint realm**.

Starfield gameplay is deferred because Minecraft does not naturally provide an interplanetary/interstellar framework.

The transition from Planet-level play to Starfield-level play is **travel**, not ascension, and is planned to use mechanisms such as:

- starships,
- interplanetary teleport arrays,
- interstellar teleport arrays.

Other large world-tier transitions remain true ascensions.
