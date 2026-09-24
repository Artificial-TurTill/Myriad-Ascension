# Myriad Ascension — Minor-Realm Power Scale and Capability Matrix

> Status: authoritative balancing/design specification.
>
> The values in this document are **relative cultivation power indices**, not literal Minecraft damage, health, or attribute values. Realm potential establishes the order-of-magnitude difference between cultivation levels. Core stats, energy state, purity, injuries, mastery, bloodline, artifacts, environment and other systems modify the effective result.

---

# 1. Why Realm Power and Stats Are Separate

A realm is a qualitative transformation.

A stat is an individual quality inside that transformation.

Example:

- a Tempered Body Stage 9 cultivator with excellent Strength is still not automatically capable of externally projecting Qi;
- a Qi Transformation Stage 1 cultivator with mediocre Strength has access to an entire category of external Qi mechanics that Tempered Body does not possess;
- a Transcendent-grade sword in the hands of a Qi Transformation cultivator does not grant Transcendent movement speed, reactions, body durability or Qi throughput.

Therefore the combat model has two layers:

1. **Realm Potential** — the fundamental cultivation tier.
2. **Personal Multipliers** — how well that individual expresses the tier.

---

# 2. Current Combat Index Formula

The first implemented formula is:

```text
Current Combat Index
=
Realm Potential
× Physical Factor
× Energy Factor
× Soul Factor
× Foundation Factor
× Condition Factor
× Battle Factor
```

Future combat passes will additionally apply technique, artifact, affinity/environment and formation modifiers at the action level.

The V screen displays the implemented factors separately.

---

# 3. Core Stat Contribution

All core stats have a hard minimum of **1**.

They do not need to contain the astronomical realm scaling themselves. Realm Potential represents the qualitative cultivation leap.

## Strength

Contributes to the **Physical Factor**.

Affects:

- unarmed force
- melee weapon acceleration
- physical technique damage
- lifting/carrying
- knockback
- high-mass artifact handling
- jump/launch force
- grappling

## Vitality

Contributes to the **Physical Factor** and survivability.

Affects:

- physical durability
- health scaling
- recovery
- body injury resistance
- poison tolerance
- breakthrough backlash survival
- lifespan preservation

## Agility

Contributes to the **Physical Factor**.

Affects:

- acceleration
- reaction speed
- attack-speed ceiling
- dodging
- footwork
- aerial maneuvering
- ability to exploit high-rank weapons

## Meridian Quality

Contributes to the **Foundation Factor**.

Affects:

- safe circulation
- Qi throughput
- Power %
- Burst safety
- technique throughput
- internal-injury resistance

## Dantian Quality

Contributes to the **Foundation Factor**.

Affects:

- energy reserve
- energy density
- conversion efficiency
- breakthrough stability
- later Dao Seal foundation

## Spiritual Sense

Contributes to the **Soul Factor**.

Its functionality evolves:

- ordinary cultivation perception
- World Energy sense
- refined detection
- Divine Sense
- Shi/Domain awareness
- Principle perception
- world perception

## Soul Strength

Contributes to the **Soul Factor**.

Affects:

- Knowledge Sea stability
- Divine Sense range/force
- soul attacks
- soul defense
- Shi
- Domain support
- Principle comprehension
- Dao Seal stability

---

# 4. Implemented Personal Multipliers

## Physical Factor

Weighted from:

- Strength: 40%
- Vitality: 35%
- Agility: 25%

Current formula intentionally uses square-root scaling so stats matter strongly without allowing one stat grind to erase major-realm gaps.

## Energy Factor

Before Initial Element:

- combat Energy Factor = 1.0
- naturally stored Yuan Qi at Tempered Body 7–9 is not consciously usable combat output

Initial Element and above:

Depends on:

- current reserve fraction
- active Power %
- Burst

A nearly empty reserve or low output substantially lowers immediate combat expression.

## Soul Factor

Weighted from:

- Spiritual Sense: 55%
- Soul Strength: 45%

Before Immortal Ascension the modifier is deliberately mild.

At Immortal Ascension+, the Soul Factor becomes substantially more important because soul combat and Divine Sense become real combat systems.

## Foundation Factor

Depends on:

- Meridian Quality
- Dantian Quality
- Vessel Purity
- Impurity Load

Excellent raw stats cannot fully compensate for a contaminated or poorly built foundation.

## Condition Factor

Penalized by:

- Body Injury
- Meridian Injury
- Soul Injury
- Recovery Debt
- Meridian Load

A wounded superior-realm cultivator can therefore temporarily fight far below normal capability.

## Battle Factor

Currently based on Battle Comprehension.

It represents:

- timing
- combat judgment
- adaptation
- practical application of strength

Technique-specific mastery will later be a separate action modifier.

---

# 5. External Power Factors

These are intentionally not collapsed into core realm power.

## Technique Mastery

Affects only the relevant technique.

High mastery improves:

- efficiency
- speed
- precision
- output
- reduced Qi waste
- reduced strain

## Artifact

An artifact contributes its own potential.

Usable contribution is limited by:

- Strength
- Agility
- realm
- energy output
- Spiritual Sense
- artifact control
- compatibility

An artifact can let a lower cultivator injure someone they normally could not, but does not grant the wielder the artifact's own body or reaction level.

## Bloodline

May modify:

- physique
- affinity
- regeneration
- resistance
- lifespan
- unique abilities
- breakthrough stability

## Physique

Separate from bloodline.

Can fundamentally alter:

- stat growth
- training
- elemental compatibility
- body techniques
- breakthrough behavior

## Affinity

Changes:

- energy efficiency
- technique compatibility
- refinement
- elemental resistance
- environmental gain
- Principle/Dao comprehension

It is not an absolute permission lock.

## Environment

Can strengthen or weaken:

- elemental techniques
- absorption
- recovery
- contamination
- formation output

## Formation

Can generate a large prepared-area advantage.

## Medicine / Pills

Can temporarily or permanently influence:

- recovery
- energy
- breakthroughs
- injury
- purity

Poor medicine can add impurities.

---

# 6. Realm-Gap Interpretation

Approximate comparison of Current Combat Index:

| Attacker / Defender Ratio | Interpretation |
| --- | --- |
| 0.80–1.25 | broadly competitive |
| 0.50–0.80 | meaningful disadvantage |
| 0.25–0.50 | severe disadvantage |
| 0.10–0.25 | overwhelming disadvantage |
| 0.03–0.10 | ordinary attacks rarely matter |
| <0.03 | effective suppression / near-irrelevance without special means |
| 1.25–2.0 | clear advantage |
| 2–4 | dominant advantage |
| 4–10 | overwhelming |
| >10 | ordinary direct combat is generally hopeless |

Special mechanics such as poison, sealing, soul attacks, formations, sacrifice and absurdly superior artifacts can bypass ordinary direct-combat assumptions.

---

# 7. Mortal

| State | Realm Potential | Can | Cannot |
| --- | ---: | --- | --- |
| Mortal | 1.0 | use mundane weapons/tools, physical training, professions | sense World Energy, store/use Qi, use cultivation techniques, fly, project energy |

A Mortal can still become physically skilled, wealthy, politically important or professionally competent.

They remain fundamentally mortal until a cultivation method begins transforming the vessel.

---

# 8. Tempered Body — 9 Stages

## Stage 1 — Realm Potential 1.25

Can:

- begin true body-tempering training
- exceed ordinary mortal conditioning
- develop early cultivation-grade Strength/Vitality/Agility

Cannot:

- sense World Energy
- generate Yuan Qi
- use Power %
- use Qi techniques

## Stage 2 — 1.50

Can:

- tolerate higher physical training loads
- develop stronger tendons/bones/musculature
- recover from ordinary exertion more effectively

Cannot:

- sense or manipulate World Energy
- use Qi

## Stage 3 — 1.80

Can:

- complete the first basic physical vessel
- survive training that would seriously injure an ordinary Mortal

Cannot:

- perceive World Energy
- store usable cultivation energy

## Stage 4 — 2.20

Can:

- faintly sense ambient World Energy
- receive vague environmental feedback

Cannot:

- deliberately absorb World Energy
- identify precise elements reliably
- use Qi

## Stage 5 — 2.60

Can:

- detect stronger/weaker energy direction
- recognize broad compatibility
- search for richer cultivation sites

Cannot:

- intentionally absorb
- use internal Qi techniques

## Stage 6 — 3.10

Can:

- broadly identify elemental/energy character
- better sense spirit stones, veins and cultivator energy

Cannot:

- deliberately accelerate absorption
- use stored Qi in techniques

## Stage 7 — 3.70

Can:

- naturally absorb World Energy
- begin producing **Yuan Qi**
- form a functional early dantian reservoir
- circulate Yuan Qi naturally through prepared meridians

Cannot:

- deliberately increase normal absorption with G
- consciously spend Yuan Qi
- activate Power %
- use Qi techniques

## Stage 8 — 4.30

Can:

- naturally maintain a larger Yuan Qi reserve
- develop stronger meridian/dantian preparation
- benefit strongly from rich environments and gathering formations

Cannot:

- consciously cast or reinforce with Yuan Qi

## Stage 9 — 5.00

Can:

- possess a mature Tempered Body vessel
- fully consolidate Yuan Qi storage
- prepare for Initial Element breakthrough

Cannot:

- actively use Yuan Qi until Initial Element
- project energy

---

# 9. Initial Element — 9 Stages

This realm unlocks **conscious Yuan Qi use**, but not free external projection.

## Stage 1 — 6.0

Can:

- consciously circulate Yuan Qi
- use Power %
- reinforce the body internally
- power contact-based cultivation techniques

Cannot:

- release detached Qi attacks
- create remote barriers

## Stage 2 — 7.5

Can:

- maintain circulation longer
- reinforce movement and strikes more consistently

Cannot:

- freely project Qi away from body/held object

## Stage 3 — 9.0

Can:

- reliably use basic internal martial skills
- sustain low-output reinforcement during normal combat

Cannot:

- use true external Qi techniques

## Stage 4 — 11.0

Can:

- conduct Qi more stably through held weapons
- improve internal footwork techniques

Cannot:

- fire detached sword-Qi/projectiles as a normal ability

## Stage 5 — 13.5

Can:

- sustain weapon conduction
- combine body reinforcement and movement more efficiently

## Stage 6 — 15.5

Can:

- use increasingly complex internal techniques
- hold moderate Power % for longer

## Stage 7 — 17.0

Can:

- use high-output internal arts
- exploit Burst more effectively

Still cannot:

- freely project Yuan Qi outside the body

## Stage 8 — 18.5

Can:

- approach peak Yuan Qi control for internal use
- maintain multiple internal demands with reduced waste

## Stage 9 — 20.0

Can:

- reach peak Initial Element Yuan Qi control
- prepare for World Baptism / Qi Transformation

Cannot:

- cross the external-projection boundary without breakthrough

---

# 10. Qi Transformation — 9 Stages

This realm unlocks **external Yuan Qi projection**.

## Stage 1 — 25

Can:

- project Yuan Qi outside the body
- use short ranged Qi attacks
- form brief external defenses

## Stage 2 — 30

Can:

- improve projection range and stability

## Stage 3 — 36

Can:

- reliably use basic projected martial techniques

## Stage 4 — 44

Can:

- sustain external barriers
- maintain projected weapon aura
- use persistent aura techniques

## Stage 5 — 54

Can:

- maintain multiple external effects for short periods

## Stage 6 — 65

Can:

- use sustained mid-range techniques with improved efficiency

## Stage 7 — 77

Can:

- use high-output layered projection
- maintain stronger pressure/aura

## Stage 8 — 89

Can:

- combine projection with movement/weapon combat efficiently

## Stage 9 — 100

Can:

- reach peak Yuan Qi projection
- prepare for Separation and Reunion

Cannot:

- use True Qi
- perform natural cultivation flight available to True Element

---

# 11. Separation and Reunion — 9 Stages

No new named energy; the defining advantages are Yuan Qi quantity, control and coordination.

## Stage 1 — 120

Can:

- establish parallel circulation routes
- coordinate internal/external demand

## Stage 2 — 140

Can:

- reduce interference between simultaneous techniques

## Stage 3 — 165

Can:

- sustain multiple circulation channels under combat load

## Stage 4 — 190

Can:

- integrate cultivation, footwork and weapon techniques more efficiently

## Stage 5 — 220

Can:

- substantially reduce Yuan Qi waste

## Stage 6 — 245

Can:

- maintain high control under changing combat conditions

## Stage 7 — 265

Can:

- achieve precision Yuan Qi control
- rapidly reallocate Power between combat needs

## Stage 8 — 285

Can:

- sustain multiple technique demands at high efficiency

## Stage 9 — 300

Can:

- reach peak Yuan Qi control/quantity before energy purification

Cannot:

- use True Qi until the next breakthrough

---

# 12. True Element — 9 Stages

Yuan Qi evolves into **True Qi**.

## Stage 1 — 400

Can:

- begin using True Qi
- perform cultivation-powered flight
- use higher Secret Techniques

Flight:

- costly
- relatively unstable
- poor efficiency

## Stage 2 — 500

Can:

- improve True Qi conversion/control
- sustain flight longer

## Stage 3 — 620

Can:

- reliably fight while airborne for short periods

## Stage 4 — 760

Can:

- use stable directional flight
- use stronger True-Qi barriers and weapon infusion

## Stage 5 — 920

Can:

- sustain aerial combat
- use mid-grade True-Qi Secret Techniques efficiently

## Stage 6 — 1,080

Can:

- perform long-duration flight at moderate output

## Stage 7 — 1,240

Can:

- perform high-speed True Qi flight
- use complex high-output Secret Techniques

## Stage 8 — 1,380

Can:

- coordinate high-speed aerial movement and combat

## Stage 9 — 1,500

Can:

- reach peak True Element True-Qi expression
- prepare soul/consciousness for Immortal Ascension

Cannot:

- use a true Knowledge Sea/Divine Sense before Immortal Ascension

---

# 13. Immortal Ascension — 9 Stages

Unlocks **Knowledge Sea** and **Divine Sense**.

## Stage 1 — 1,800

Can:

- form Knowledge Sea
- awaken Divine Sense
- perceive beyond normal sight

## Stage 2 — 2,200

Can:

- expand Divine Sense range
- improve spiritual identification

## Stage 3 — 2,700

Can:

- reliably use Divine Sense in ordinary combat awareness

## Stage 4 — 3,300

Can:

- remotely control suitable artifacts
- inspect spiritual signatures more deeply

## Stage 5 — 4,000

Can:

- maintain multiple Divine Sense tasks
- improve soul defenses

## Stage 6 — 4,700

Can:

- exert stronger spiritual pressure and artifact control

## Stage 7 — 5,500

Can:

- perform direct Divine Soul combat techniques
- use serious soul attacks/defenses

## Stage 8 — 6,500

Can:

- maintain sophisticated soul/perception techniques

## Stage 9 — 7,500

Can:

- reach peak lower-realm Divine Sense before Transcendent

Cannot:

- use Shi, Domain or World Principles

---

# 14. Transcendent — 3 Orders

## 1st Order — 15,000

Can:

- possess a Transcendent physique
- clearly surpass ordinary mortal biological limits
- gain major lifespan increase

## 2nd Order — 28,000

Can:

- deepen True Qi/body/soul integration
- wield Transcendent-level power more efficiently

## 3rd Order — 50,000

Can:

- reach peak lower-world Transcendent foundation
- prepare True Qi for Saint Qi transformation

Cannot:

- use Saint Qi until Saint breakthrough

---

# 15. Saint — 3 Orders

## 1st Order — 100,000

Can:

- transform/use **Saint Qi**
- possess Saint-grade body/energy

## 2nd Order — 180,000

Can:

- resist increasingly severe Starfield conditions
- sustain much denser Saint Qi

## 3rd Order — 300,000

Can:

- normally survive the Starry Sky without ordinary defensive artifacts
- become viable for true Planet → Starfield travel

---

# 16. Saint King — 3 Orders

## 1st Order — 700,000

Can:

- greatly densify Saint Qi
- gain major Starfield combat endurance

## 2nd Order — 1,200,000

Can:

- tolerate stronger spatial/interstellar travel conditions

## 3rd Order — 2,000,000

Can:

- prepare Will + Divine Soul + Saint Qi for Shi

Cannot:

- manifest true Shi until Origin Returning

---

# 17. Origin Returning — 3 Orders

## 1st Order — 5,000,000

Can:

- manifest **Shi**
- suppress lower cultivators through Will + Saint Qi + Divine Soul

## 2nd Order — 10,000,000

Can:

- expand Shi radius
- increase pressure and resistance against hostile Shi

## 3rd Order — 20,000,000

Can:

- reach Grand Accomplishment Shi
- prepare Shi to evolve into Domain

---

# 18. Origin King — 3 Orders

## 1st Order — 50,000,000

Can:

- form **Domain**
- directly influence the environment within it

## 2nd Order — 100,000,000

Can:

- mature Domain authority
- improve Domain radius/integrity

## 3rd Order — 200,000,000

Can:

- reach Grand Accomplishment Domain
- prepare for Principle comprehension

Risk:

- severe Domain damage can injure the cultivator's foundation

---

# 19. Dao Source — 3 Orders

Requires complete Saint Qi → **Source Qi** conversion.

## 1st Order — 600,000,000

Can:

- use Source Qi
- sense World Principles
- begin borrowing Principle Strength

## 2nd Order — 1,500,000,000

Can:

- reliably transfer Principle Strength into techniques
- deepen Primary Dao specialization

## 3rd Order — 3,000,000,000

Can:

- exercise deep Principle control
- prepare Source Qi/Principles for Emperor

Cannot:

- use true Emperor Pressure/Emperor Qi until Emperor

---

# 20. Emperor — 3 Orders

## 1st Order — 20,000,000,000

Can:

- begin Source Qi → **Emperor Qi** conversion
- emit Emperor Pressure
- exercise strong Principle control

## 2nd Order — 50,000,000,000

Can:

- use mature Emperor Qi
- directly manipulate relevant Principles with great authority

## 3rd Order — 100,000,000,000

Can:

- reach mature normal Emperor foundation
- stand at the peak of ordinary Emperor cultivation

Full Emperor potential requires mature Emperor-Qi conversion.

---

# 21. Pseudo-Great Emperor

**Realm Potential: 500,000,000,000**

Can:

- exceed ordinary 3rd Order Emperor
- exercise peak Emperor foundation/Principle control

Cannot:

- claim Dao Seal abilities without actually condensing one

---

# 22. Dao Seal

**Realm Potential: 3,000,000,000,000**

Can:

- condense lifetime cultivation around a Grand Dao
- possess true Dao Seal foundation
- begin Open Heaven elemental-resource path

The Dao Seal records the quality of the cultivator's lifetime foundation.

It is not just a promotion flag.

---

# 23. Half-Step Open Heaven

**Realm Potential: 20,000,000,000,000**

Can:

- refine Yin/Yang/Five-Element powers into Dao Seal
- generate embryonic World Force after required elemental cycle progress
- prepare Small Universe formation

Cannot:

- use a complete Small Universe or true Open Heaven World Force

Resource quality and the weakest condensed resource determine future Open Heaven outcome.

---

# 24. Open Heaven — 9 Ranks

## 1st Rank — 100,000,000,000,000

Can:

- form Small Universe
- generate true World Force
- use Low-Rank Open Heaven power

## 2nd Rank — 300,000,000,000,000

Can:

- greatly expand Small Universe background
- use denser World Force

## 3rd Rank — 1,000,000,000,000,000

Can:

- reach peak Low-Rank Open Heaven

## 4th Rank — 10,000,000,000,000,000

Major qualitative leap into Middle Rank.

Can:

- possess vastly greater Small Universe background/World Force

## 5th Rank — 100,000,000,000,000,000

Can:

- deepen Middle-Rank world foundation

## 6th Rank — 1,000,000,000,000,000,000

Can:

- reach peak Middle Rank
- approach the enormous High-Rank threshold

## 7th Rank — 100,000,000,000,000,000,000

Major qualitative leap into High Rank.

Can:

- complete Small Universe World Principles
- possess a true internal Universe World
- support living creatures/populations

## 8th Rank — 10,000,000,000,000,000,000,000

Can:

- possess vastly expanded World Force/background
- exercise extreme High-Rank world authority

## 9th Rank — 1,000,000,000,000,000,000,000,000

Can:

- reach peak Open Heaven
- possess the greatest normal Small Universe/World Force state before World Creation

Cannot:

- create true external Universe Worlds/life through World Creation authority without the final qualitative breakthrough

---

# 25. World Creation

**Reference Potential: 1e30+**

The numeric index stops being a useful ordinary comparison.

Can:

- integrate/master myriad Grand Daos at an extreme level
- exercise Creation Authority
- create true Universe Worlds
- create life
- externalize/replace the old Small Universe paradigm
- establish world laws

World Creation is not Open Heaven Rank 10.

---

# 26. Artifact Contribution Rules

Artifact grade is independent of user realm.

A weapon/shield/armor artifact has:

- artifact type
- artifact grade
- artifact potential
- future refinement quality
- future element/path traits

The user can only express a portion of artifact potential.

Planned usable-artifact factor:

```text
usable artifact power
=
artifact potential
× control compatibility
× physical handling
× energy throughput
× spiritual-control compatibility
```

Therefore a Qi Transformation cultivator can hold a Transcendent weapon and potentially injure someone otherwise beyond them, but:

- cannot swing with Transcendent speed
- cannot survive Transcendent recoil/output
- cannot feed it Transcendent-grade energy
- cannot react like a Transcendent
- cannot automatically penetrate arbitrarily higher cultivation

---

# 27. Medicines and Poison in Power Scaling

Medicine does not directly grant realm power by default.

It restores lost expression by repairing:

- health
- injuries
- meridian load
- fatigue
- recovery debt

Low-grade pills can add impurity, which reduces Foundation Factor.

Poison can lower effective power by:

- ordinary damage
- Body Injury
- Meridian Injury
- Meridian Load
- soul effects at higher grades
- energy contamination

This gives poison a legitimate way to threaten someone stronger without pretending poison simply ignores cultivation.

---

# 28. V-Screen Interpretation

The Stats page should show:

- seven core stats
- Realm Potential
- Current Combat Index
- Physical Factor
- Energy Factor
- Soul Factor
- Foundation Factor
- Condition Factor
- Battle Factor

The Abilities page should show:

- already unlocked qualitative abilities
- the next several locked capabilities
- descriptions of what each capability actually permits

The Conditions page should show:

- Vessel Purity
- Impurities
- Demonic Qi
- Body Injury
- Meridian Injury
- Soul Injury
- Recovery Debt
- Bloodline Conflict
- resulting Condition Factor

This lets the player understand both:

> "What realm am I?"

and:

> "Why am I currently weaker or stronger than another cultivator of the same realm?"
