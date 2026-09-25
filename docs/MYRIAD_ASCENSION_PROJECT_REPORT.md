# Myriad Ascension — Master Project Report

**Project:** Myriad Ascension  
**Tagline:** Ten thousand paths. One summit.  
**Minecraft:** 1.21.1  
**Loader:** NeoForge  
**Java:** 21  
**License:** All Rights Reserved  
**Current build phase:** Alpha 0.1.0-alpha.4  
**Current implementation ceiling:** Saint  
**Long-term progression ceiling:** World Creation

---

# 1. Executive Summary

Myriad Ascension is intended to become a large-scale Xianxia / Wuxia / Murim cultivation overhaul for Minecraft 1.21.1.

The mod is not designed as a simple RPG-stat layer placed on top of vanilla Minecraft. The goal is to make cultivation fundamentally change how the player survives, fights, travels, trains, interacts with factions, uses the world, and eventually leaves one world tier for another.

The player begins as an ordinary Mortal with no usable Qi. The early game is deliberately dangerous. Cultivation must be earned through manuals, Masters, clans, sects, ancient inheritances, monuments, tombs, rare resources, or other valid paths.

The central design principle is:

> Starting talent changes difficulty, efficiency, opportunity, and strategy, but should never permanently prevent a sufficiently determined cultivator from reaching the summit.

A player with poor natural affinity may require vastly more time, resources, danger, alternative methods, demonic shortcuts, bloodline refinement, formation support, superior scriptures, ascension sacrifices, or rare treasures. A gifted cultivator advances faster but eventually needs access to higher worlds and higher-grade resources.

The system is inspired by cultivation fiction and uses a Martial Peak-inspired realm structure where requested, while Myriad Ascension remains its own mod with original factions, techniques, content, world systems, mechanics, structures, and implementation.

---

# 2. Core Design Pillars

## 2.1 Cultivation is active, not passive

Cultivation is not intended to be a background XP bar.

Players will actively manage:

- Qi reserve,
- Qi circulation,
- Burst output,
- cultivation sessions,
- technique mastery,
- injuries,
- impurities,
- elemental attunement,
- bloodlines,
- sect/faction relationships,
- concealment,
- equipment,
- formations,
- professions,
- environmental compatibility,
- breakthrough preparation.

## 2.2 Realm advancement changes capability

Higher realms must not simply be larger damage multipliers.

Realm advancement can unlock qualitatively different mechanics such as:

- sensing ambient energy,
- active Qi use,
- external Qi projection,
- advanced movement,
- flight,
- Divine Sense,
- soul-related systems,
- higher-world law/principle interaction,
- ascension.

## 2.3 The world participates in cultivation

Minecraft terrain is intended to become a cultivation ecology.

The world will eventually contain:

- local Qi density,
- spirit veins,
- elemental regions,
- spirit stones,
- rare ores,
- cultivation woods,
- herbs,
- spirit plants,
- natural treasures,
- beasts,
- bloodlines,
- secret inheritances,
- sect structures,
- cultivation chambers,
- formations,
- auctions,
- cities,
- capitals,
- tombs,
- monuments.

## 2.4 No single correct Dao

The player can eventually pursue many viable approaches, including:

- Righteous cultivation,
- Demonic cultivation,
- Buddhist cultivation,
- Unorthodox cultivation,
- Imperial cultivation,
- neutral methods,
- elemental specialization,
- body cultivation,
- bloodline cultivation,
- alchemy,
- arrays/formations,
- weapon forging,
- artifact refinement,
- beast-related paths,
- dangerous forbidden methods.

---

# 3. Player Genesis

A new player begins as a **Mortal**.

Initial state:

- 20 vanilla HP unless another system modifies it,
- 0 usable Qi,
- 0 Qi capacity,
- no cultivation method,
- unaffiliated,
- no special player bloodline by default.

## 3.1 Sex and Yin/Yang polarity

The first-login character screen asks the player to select:

- Male
- Female

This establishes natural polarity:

### Male

- Yang baseline: 2
- Yin baseline: 1

### Female

- Yin baseline: 2
- Yang baseline: 1

## 3.2 Starting affinity distribution

After the fixed Yin/Yang baseline, exactly **10 additional affinity points** are randomly distributed.

Eligible affinities:

- Wood
- Fire
- Earth
- Metal
- Water
- matching polarity

For Male characters:

- bonus points may go to Yang,
- bonus points may not go to Yin.

For Female characters:

- bonus points may go to Yin,
- bonus points may not go to Yang.

No starting affinity may exceed 10.

The complete starting seven-aspect profile therefore totals 13 points.

## 3.3 Innate potential vs trained attunement

The project distinguishes:

- **Innate Affinity** — what the character was born with,
- **Current Attunement** — what the character has developed through cultivation and resources.

This distinction matters for monuments, inheritances, talent tests, and future progression.

A player can improve current attunement without rewriting what they were born with.

## 3.4 Starting morality

The player chooses an initial disposition:

- Benevolent: +1
- Malicious: -1

This is intentionally small.

Extreme virtue or wickedness must come from later actions rather than character creation.

---

# 4. Moral Alignment, Affiliation, Karma, and Politics

These systems are deliberately separate.

## 4.1 Moral alignment

Moral alignment is a continuous scale:

- -100 = maximally malicious
- 0 = neutral
- +100 = maximally benevolent

Affiliation does not automatically determine morality.

Possible combinations include:

- malicious Righteous cultivator,
- benevolent Demonic cultivator,
- ruthless Buddhist,
- honorable Unorthodox cultivator,
- corrupt Imperial official.

## 4.2 Broad affiliations

Current broad affiliation categories:

- Unaffiliated
- Righteous
- Unorthodox
- Demonic
- Buddhist
- Imperial

Players begin Unaffiliated.

Affiliation can later change through:

- joining an organization,
- training under an affiliated Master,
- joining a clan,
- joining a sect,
- joining an Empire,
- adopting a cultivation path strongly associated with an affiliation.

## 4.3 Broad hostility relationships

Currently defined broad relationships include:

- Righteous ↔ Unorthodox: mortal enemies
- Buddhist ↔ Demonic: mortal enemies
- Righteous ↔ Buddhist: compatible
- Righteous ↔ Imperial: compatible
- Demonic ↔ Unorthodox: neutral by default

Demonic/Unorthodox hostility should develop from actions rather than automatic faction hostility.

Specific sects, clans, cults, gangs, temples, and empires will eventually override or refine these broad defaults.

## 4.4 Buddhist Karma

Buddhist cultivation has a separate Karma system.

Karma is not the same as universal morality.

Actions harmful to Buddhist Karma include:

- killing ordinary villagers,
- killing peaceful/protected animals such as foxes, bears, and pandas,
- aiding Demonic cultivators in ways opposed to Buddhist principles,
- unjustified violence,
- malicious grudges,
- deathmatches.

Buddhists are expected to emphasize:

- support,
- restraint,
- healing,
- purification,
- sealing,
- defense,
- spiritual discipline.

They are not required to be pure healers.

Defeating malicious targets can be compatible with Buddhist progression.

## 4.5 Demonic discipline

Demonic cultivation is high-risk and high-reward.

Its culture can include:

- brutal training,
- lethal failure,
- rank competition,
- life-force expenditure,
- Qi theft,
- destabilization,
- forced advancement,
- dangerous Burst usage,
- self-harm through unstable methods.

Demonic members are not automatically protected simply because they are members of the same faction.

Hierarchy matters.

Examples:

- attack an ordinary junior: unrelated seniors may not care,
- attack a Master: their disciples can defend them,
- attack a cult leader: the entire subordinate cult hierarchy can retaliate,
- challenge a lower Master in front of a higher leader: the leader may allow the competition.

Formal:

- rank challenges,
- humiliation matches,
- deathmatches

can suppress hierarchy protection.

Buddhist sects forbid deathmatches.

## 4.6 Internal punishment

Righteous, Buddhist, and Imperial organizations may tolerate punishment of clearly malicious internal members.

A malicious Righteous member can be punished by another Righteous member without automatically being treated like an innocent same-faction victim.

A Demonic member who becomes too benevolent may be viewed as weak, disloyal, or unworthy.

Unorthodox groups generally normalize more internal violence than structured Righteous/Buddhist organizations.

---

# 5. Cultivation Realm Hierarchy

## Pre-cultivation

- Mortal

## Starting World

1. Tempered Body — 9 Stages
2. Initial Element — 9 Stages
3. Qi Transformation — 9 Stages
4. Separation and Reunion — 9 Stages
5. True Element — 9 Stages
6. Immortal Ascension — 9 Stages
7. Transcendent — 3 Orders

## Planet Level

8. Saint — 3 Orders

This is the current planned implementation ceiling.

## Starfield Level

9. Saint King — 3 Orders
10. Origin Returning — 3 Orders
11. Origin King — 3 Orders

The Planet → Starfield transition is **travel**, not ascension.

Future travel mechanisms may include:

- starships,
- interplanetary teleport arrays,
- interstellar teleport arrays.

## Star Boundary Level

12. Dao Source — 3 Orders
13. Emperor — 3 Orders
14. Pseudo-Great Emperor — special classification
15. Dao Seal — special classification

## Outer Universe Level

16. Half-Step Open Heaven — special classification
17. Open Heaven — 9 Ranks

## Beyond the Outer Universe / Multiverse

18. World Creation

No ordinary minor subdivision is currently assigned to World Creation.

---

# 6. Tempered Body Progression

Tempered Body prepares the physical vessel.

The player does **not** have consciously usable Qi during this realm.

## Stages 1–3

Focus:

- physical strengthening,
- vessel preparation,
- toughness,
- body tempering.

## Stages 4–6

Focus:

- continued body strengthening,
- sensing ambient/world energy,
- becoming aware of aligned environments.

Players may begin using aligned environments and special elemental blocks to influence future/current elemental attunement.

They still cannot properly practice active elemental Qi arts.

## Stages 7–9

Focus:

- continued physical body tempering,
- conscious ambient energy gathering with G,
- difficult Yuan Qi storage in the vessel,
- meridian preparation,
- dantian preparation,
- transition toward Initial Element.

The player can deliberately gather Qi into the vessel, but cannot yet truly circulate or spend it. Initial Element unlocks normal internal circulation and much easier active recharging.

---

# 7. Major Realm Capability Milestones

## Initial Element

- first proper usable internal Qi,
- body reinforcement with Qi,
- active elemental Qi cultivation becomes possible,
- external free Qi projection is still limited.

## Qi Transformation

- external Qi projection becomes possible,
- combat technique scope expands,
- cultivation can substitute for sleep.

### Cultivation sleep rule

At Qi Transformation or higher:

- 60 real minutes of uninterrupted cultivation can satisfy the sleep requirement.

In multiplayer, night can only advance when all relevant players are either:

- sleeping normally,
- or have fulfilled the cultivation-rest condition.

## Separation and Reunion

Planned to emphasize:

- control,
- path discipline,
- internal instability,
- temptation,
- Qi nature,
- dangerous method side effects.

## True Element

- stronger purified Qi state,
- more advanced techniques,
- natural cultivation-powered flight.

## Immortal Ascension

- Knowledge Sea,
- Divine Sense,
- spiritual perception,
- soul-oriented systems.

## Transcendent

- first three-Order realm,
- qualitative leap from the earlier nine-stage structure.

## Saint

- Planet-level cultivation ceiling for the first major development scope.

---

# 8. Cultivation Methods and Inheritances

A Mortal cannot cultivate merely by waiting or gaining generic XP.

A valid inheritance is required.

Possible acquisition sources:

- Master
- Clan
- Sect
- Cultivator Tomb
- Monument
- quest
- ancient inheritance
- loot structure
- auction
- future secret realm content

## 8.1 Cultivator tombs

A tomb may give a cultivation method that:

- is not suited to the player,
- has limited elemental compatibility,
- only supports advancement to a specific realm,
- requires a continuation or method switch later.

The player is not guaranteed that discovered loot matches their build.

## 8.2 Monuments

Monuments may acknowledge only cultivators who meet conditions such as:

- elemental affinity,
- innate potential,
- current attunement,
- Yin/Yang potential,
- bloodline,
- morality,
- Karma,
- cultivation realm,
- special trials.

Yin/Yang monuments evaluate the natural polarity:

- Male → Yang
- Female → Yin

## 8.3 Method ceilings

Cultivation methods can have maximum supported realms.

When reaching a method ceiling, future options may include:

- changing method,
- finding a continuation,
- repairing an incomplete inheritance,
- creating a superior method,
- obtaining a higher inheritance.

---

# 9. Qi System

Qi is both a stored resource and an active combat/training resource.

Tracked concepts include:

- current Qi,
- maximum Qi,
- normal circulation,
- Burst state,
- passive refill ceiling,
- active gathering,
- meridian load,
- technique efficiency,
- injuries,
- environmental modifiers.

## 9.1 G — Gather and Circulate

G performs two connected functions:

1. gather/refill Qi where valid,
2. increase normal Qi circulation for immediate usage.

A Mortal with no cultivation method cannot create usable Qi by holding G.

## 9.2 H — Suppress

H reduces active Qi circulation.

Uses include:

- conserving Qi,
- hiding strength,
- lowering active output,
- reducing exposure.

## 9.3 R — Burst

R toggles a separate Burst/overdrive state.

Burst:

- amplifies output,
- increases movement/travel performance,
- can force techniques harder,
- consumes disproportionately more Qi,
- increases meridian load,
- becomes highly inefficient at poor mastery,
- is dangerous when reserves are low.

Burst is intended for:

- pursuit,
- escape,
- travel,
- emergencies,
- decisive attacks,
- temporarily forcing techniques.

It should not be an efficient permanent state.

## 9.4 Normal passive Qi recovery

Natural passive recovery stops at 10% of maximum Qi.

Passive Qi Recharging extends this cap.

Each level adds 4 percentage points.

Current target:

- no skill: 10%
- Level 1: 14%
- Level 5: 30%
- Level 10: 50%

Above the passive cap, active gathering/cultivation is required.

## 9.5 Meditation

Meditation is an active skill trained through seated cultivation.

It improves calm cultivation speed/efficiency.

---

# 10. Calm Cultivation and Qi Deviation

Calm cultivation is a deliberate seated state.

Possible benefits:

- ambient Qi gathering,
- reserve refill,
- circulation,
- cultivation progress,
- comprehension,
- Meditation skill,
- interaction with cultivation chambers,
- interaction with spirit veins,
- formation bonuses.

Progress made during a session can remain unstable until safely stabilized.

If the player is interrupted during unstable cultivation, some of the session progress can become:

- internal damage,
- Qi deviation,
- meridian problems.

Calm uninterrupted cultivation itself should not randomly generate Qi deviation.

---

## 10.1 Primordialis Testudo Foundation Stance

The first implemented training method is the **Primordialis Testudo Foundation Stance**.

It is intended to feel like martial foundation conditioning rather than a generic XP button.

### Input

- Hold **B** to assume the training stance.
- Hold **G** while maintaining the stance to perform the controlled breathing/gathering rhythm.

### Valid posture

Progress only occurs while the player:

- is grounded,
- remains nearly motionless,
- is not sprinting,
- is not swimming,
- is not fall-flying,
- is not riding another entity,
- has both hands empty.

Breaking posture suspends progress and resets uninterrupted-session continuity.

### Training fatigue

Training accumulates persistent fatigue.

Fatigue:

- lowers efficiency,
- prevents infinite uninterrupted training,
- recovers while the stance is released,
- persists through logout.

Long uninterrupted valid sessions gain a modest continuity efficiency bonus, rewarding discipline without turning the mechanic into pure AFK progression.

### Early progression

With the Primordialis Testudo Longevity Art active:

- a Mortal can use the stance to prepare the vessel and enter Tempered Body Stage 1,
- Tempered Body Stages 1-9 can be trained with the foundation stance,
- Stage 9 stops at full consolidation,
- entering Initial Element requires a separate breakthrough and is not automatically granted.

During Mortal/Tempered Body training, G represents controlled breathing and pre-Qi gathering rather than usable Qi circulation.

# 11. Death and Recovery

Death does not erase permanent cultivation progression.

On death:

- normal Minecraft respawn behavior applies,
- realm is retained,
- stage is retained,
- affinity is retained,
- learned methods are retained,
- knowledge and skills are retained.

The player respawns injured.

Consequences include:

- body injury,
- meridian injury,
- recovery debt,
- suppressed output,
- reduced immediate combat readiness,
- loss of usable Qi toward a baseline.

Recovery can require:

- time,
- medicinal resources,
- healing pills,
- alchemy,
- specialist treatment,
- cultivation,
- safe rest.

Fighting while injured can worsen the condition.

Death never automatically drops the player to a lower cultivation realm.

---

# 12. Purity, Impurities, and Contamination

Cultivation quality matters.

Sources of impurity can include:

- low-grade pills,
- excessive pill use,
- incompatible environments,
- poor cultivation methods,
- foreign Qi,
- Demonic contamination,
- failed or unstable breakthroughs.

Purity can influence:

- cultivation efficiency,
- combat output,
- stability,
- breakthrough quality,
- healing,
- technique efficiency,
- realm-gap performance.

A Righteous cultivator remaining in a highly incompatible Nether environment may accumulate harmful influences, while compatible Demonic/Fire methods may benefit.

---

# 13. Technique System

Techniques are intended to be data-driven and multi-dimensional.

## 13.1 Path traits

Current path traits:

- Neutral
- Righteous
- Demonic
- Buddhist
- Unorthodox
- Imperial

A technique can carry more than one.

Examples:

- Imperial + Righteous
- Imperial + Buddhist
- Imperial + Demonic
- Neutral + Earth

## 13.2 Elemental traits

Core elements:

- Wood
- Fire
- Earth
- Metal
- Water
- Yin
- Yang

Future/extended energies include concepts such as:

- Lightning
- Ice
- Wind
- Lava
- Poison
- Blood
- Soul
- Life
- Death
- Space
- Void
- Star
- Chaos
- Demonic energy
- other specialized energies

These are not intended to behave like simple Pokémon-style damage types.

They should change cultivation mechanics, environments, techniques, and resource compatibility.

## 13.3 Technique nature tags

Current technical concepts include:

- Harmonizing
- Violent
- Poison
- Blood
- Life Drain
- Gluttony
- Desire
- Soul
- Body Tempering
- Longevity
- Defensive
- Concealment

## 13.4 Technique mastery

Technique performance depends on more than realm.

Factors include:

- cultivation realm,
- stage/order/rank,
- method quality,
- mastery,
- elemental environment,
- current Qi,
- circulation,
- Burst,
- vessel purity,
- injury state,
- equipment,
- artifacts,
- bloodline,
- comprehension.

Possible training routes:

- repeated technique use,
- combat,
- cultivation in correct environment,
- taking relevant elemental damage,
- hostile-environment survival,
- specialized healing practice for some Wood/Water arts.

Combat should improve technique skill and battle comprehension more than raw cultivation progress.

---

# 14. Qi Nature and Exposure

Official faction membership is not the same as the cultivator's actual Qi nature.

A player may officially belong to a Righteous sect while secretly practicing Demonic techniques.

Practicing techniques can imprint their nature into Qi.

Examples:

## Demonic

Can leave:

- corrupting,
- harmful,
- destabilizing,
- life-force-associated traits.

## Buddhist

Can leave:

- harmonizing,
- benevolent,
- purification-compatible traits.

## Righteous

Does not inherently require Buddhist Karma or Demonic corruption.

## Unorthodox

Often includes dangerous or vice-driven methods such as:

- poison,
- violence,
- gluttony,
- desire-based absorption.

A desire-type art may drain opposite-sex Yin/Yang energy.

For NPCs, the stat may be directly reduced.

For players, drained Yin/Yang is recoverable through cultivation/circulation rather than permanent loss of innate potential.

---

# 15. Qi Signature Detection and Concealment

Qi use exposes the cultivator.

Exposure sources include:

- active circulation,
- seated cultivation,
- Qi techniques,
- weapon infusion,
- aura,
- Burst.

Burst is effectively a beacon.

It can function like a siren announcing the nature of the Qi currently being used.

NPCs belonging to hostile factions may attack when they recognize an enemy Qi signature.

## 15.1 Qi Concealment

Qi Concealment is a passive skill, Level 1–10.

At Level 10:

- observers below the user's major realm are guaranteed to be fooled,
- observers in the same major realm are guaranteed to be fooled,
- an observer exactly one major realm higher is fooled unless the user is only 1st minor stage,
- World Creation cultivators cannot be fooled by the skill alone.

Lower levels will later depend on:

- Spiritual Sense,
- realm difference,
- technique mastery,
- exposure strength.

## 15.2 Concealment artifacts

Artifacts can conceal or falsify Qi nature.

Example concept:

An Open Heaven-rank concealment ring can disguise Qi as another style and hide the true nature from sufficiently weak Open Heaven observers.

Artifact concealment strength depends on artifact rank/quality.

---

# 16. Demonic Cultivation Mechanics

Demonic methods may eventually allow:

- life-force drain,
- foreign-Qi theft,
- induced Qi deviation,
- harvesting destabilized energy,
- high-risk forced progression.

A victim may acquire Demonic Qi contamination.

Contamination can:

- worsen injuries,
- damage usable Qi,
- block cultivation,
- interfere with circulation,
- make recovery harder.

Foreign Qi from an equal/higher cultivator is more difficult to expel.

Demonic instability can produce berserk behavior under relevant conditions.

It is not intended to be meaningless random punishment.

Relevant conditions can include:

- poor control,
- fast advancement,
- excessive Burst,
- injuries,
- Qi deviation,
- life-force expenditure,
- forced breakthroughs.

Severe failures can:

- injure the cultivator,
- expend life force,
- cause berserk aggression,
- temporarily boost combat power,
- damage cultivation,
- in extreme cases cause minor-stage regression.

A Demonic player can reduce this risk by training:

- weaker methods first,
- control,
- professions,
- stabilizing resources,
- vessel quality,
- discipline before reckless Burst usage.

---

# 17. Combat Philosophy

## 17.1 Realm gaps matter strongly

A full major realm difference should normally be overwhelming.

Minor-rank differences are more manageable.

Exceptional factors that can bridge some difference include:

- superior artifacts,
- superior techniques,
- formation support,
- preparation,
- numbers,
- matchup advantages,
- bloodlines,
- professional support,
- talent,
- mastery.

## 17.2 Artifacts do not grant the user's body the artifact's realm

A low-rank cultivator carrying a Transcendent weapon may deal a dangerous blow to someone above them.

However, the player still lacks the:

- speed,
- body strength,
- durability,
- reaction time,
- Qi reserves,
- control

of the weapon's intended realm.

Against an overwhelmingly stronger cultivator, even a high-rank weapon can feel slow or poorly utilized.

## 17.3 Planned combat loop

The long-term combat design includes concepts such as:

- probing,
- defense,
- advantage-building,
- poise/openings,
- finishers,
- body reinforcement,
- weapon infusion,
- movement arts,
- pressure,
- Qinggong-style mobility.

---

## 17.1 Minor-realm combat index

Every Stage, Order and Open Heaven Rank has an explicit **Realm Potential**.

The current combat index combines:

- Realm Potential
- Physical Factor
- Energy Factor
- Soul Factor
- Foundation Factor
- Condition Factor
- Battle Factor

The V screen exposes these components rather than hiding the result behind one unexplained number.

See `docs/design/minor-realm-power-and-capabilities.md`.

## 17.2 Starter medicines, poisons, and artifacts

The first tangible martial-world item set now includes:

- Minor Body-Mending Pill
- Meridian-Soothing Pill
- Basic Antidote Pill
- Crude Meridian Poison
- Tempered Body artifact weapon/shield/armor
- Minor Storage Bag with 9 slots
- Instruction to the Martial World

The Instruction manual is granted after Mortal Genesis and provides the in-game beginner guide.

See `docs/design/martial-tools-consumables.md`.

# 18. Movement and Travel

Long-term movement systems include:

- reinforced running,
- dashes,
- Qinggong,
- air steps,
- wall running,
- water running,
- flying,
- sword riding,
- high-realm travel.

R/Burst is especially useful for travel because it can force higher movement output at inefficient Qi cost.

True Element is the current milestone planned for natural cultivation-powered flight.

---

# 19. Weapon Qi Infusion

Weapons remain important because they can act as Qi conduits.

Weapon infusion may depend on:

- circulation,
- element,
- technique,
- weapon material,
- conductivity,
- mastery,
- artifact grade,
- injuries,
- vessel purity.

Example elemental effects:

- Fire → ignition/burning
- Metal → cutting/penetration
- Lightning → penetrating/shock effects
- Water → fluid attack/defense
- Demonic → vitality damage/corruptive wounds

The system is intended to keep vanilla and modded weapons useful rather than replacing them with a separate isolated combat inventory.

---

# 20. X Quick Menu

The X key is reserved for a compact cultivation quick menu.

Planned functions include allocation and mode selection.

Potential allocation categories:

- Body
- Movement
- Weapon
- Defense
- Perception
- Aura
- technique-specific states

Example:

- total circulation: 60%
- Body: 20%
- Movement: 15%
- Weapon: 20%
- Perception: 5%

The quick menu will also control features such as weapon infusion.

---

## 20.1 In-game cultivation HUD

After the player enters a cultivation realm, Myriad Ascension displays a compact top-left HUD inspired structurally by Dragon Block C.

While the player remains Mortal, the Myriad Ascension cultivation HUD is completely hidden and only Minecraft's normal survival HUD is shown.

It shows:

- Health
- Qi
- active Power percentage

Active Power is the current circulation percentage. It determines the fraction of full technique/output strength being used and proportionally scales Qi consumption for systems that consume Qi.

The HUD is separate from the V character sheet and remains visible during normal play.

## 20.2 Technique manuals and scrolls

Every registered technique must have its own physical Manual or Scroll item.

A technique item contains exactly one technique identity and category.

Using a manual:

- is validated on the server,
- permanently records the technique as learned,
- does not consume the manual,
- automatically equips it only if its technique-category slot is empty.

The first implemented example is the **Primordialis Testudo Longevity Art Manual**.

This item framework is also intended for clan libraries, Masters, sect rewards, tomb loot, monuments, auctions, and other inheritance sources.

# 21. V Cultivator Status Screen

The V screen is intended to become the full character/cultivation profile.

Current/future information includes:

- realm,
- stage/order/rank,
- cultivation method,
- Qi,
- circulation,
- Burst,
- sex,
- Yin/Yang polarity,
- moral alignment,
- affiliation,
- Karma,
- affinities,
- innate vs current attunement,
- bloodline,
- purity,
- impurities,
- Demonic contamination,
- injuries,
- recovery debt,
- skills,
- comprehension,
- technique mastery,
- profession progression,
- specific sect/faction reputation,
- constitution,
- Spiritual Sense.

The alpha originally contained a single crowded placeholder V screen.

## 21.1 Tabbed V-screen layout

The V interface is now designed as a compact character-sheet UI inspired by the navigation structure of Dragon Block C while retaining Myriad Ascension's own Xianxia visual identity.

Initial pages:

- **Overview** — realm, method, bloodline, affiliation, morality, Qi, Burst, injuries/condition.
- **Stats & Affinities** — core cultivator attributes and elemental/Yin-Yang affinities.
- **Skills** — learned passive/active skills and their levels.
- **Techniques** — equipped technique categories.

The interface should show only one focused page at a time rather than rendering all character data simultaneously.

### Core stats

Initial persistent core stats:

- Strength
- Vitality
- Agility
- Spiritual Sense
- Meridian Quality
- Dantian Quality
- Soul Strength

Every core stat has a hard minimum of **1**.

Save loading, migration, setters, and future penalties must never reduce a core stat below 1.

Affinities remain a separate system and may have values such as 0 according to the affinity-generation/training rules.

### Technique slots

The initial technique loadout contains one active slot for each category:

- Cultivation
- Footwork
- Weapon
- Eyesight

Only **one technique per category** may be active at a time.

Equipping another technique in the same category replaces the currently equipped technique rather than allowing two techniques from that category to operate simultaneously.

Additional technique categories may be added later when explicitly designed.

---

# 22. Bloodlines

Bloodlines are intended to be major long-term lineage commitments.

Players do not randomly start with special bloodlines.

## 22.1 Player acquisition

Possible sources:

- specific quests,
- ancient cultivator tombs,
- monuments,
- auctions,
- spirit pacts,
- spirit-beast refinement.

NPCs can inherit bloodlines naturally through family lineage.

## 22.2 Primary lineage commitment

A cultivator has one primary lineage.

A stronger compatible lineage can overwrite a weaker version of the same family.

Example:

- stronger Tortoise lineage can overwrite weaker Tortoise lineage.

An incompatible family cannot simply replace it.

Example:

- Tiger bloodline applied over established Tortoise bloodline → bloodline conflict/damage.

## 22.3 Grade and purity

Grade and purity are different.

The source beast's power determines the grade ceiling.

Purity depends on how the bloodline is obtained.

Current quality concept:

1. natural last-breath transfer — highest purity potential
2. spirit pact — very high purity
3. inherited family bloodline — high but lineage-dependent
4. killing/refining the beast — significantly reduced purity
5. quest/tomb/monument/auction — defined by the reward

A bloodline cannot automatically evolve beyond its source grade.

It must be replaced/refined by a stronger compatible source.

---

# 23. Primordial Tortoise Bloodline

The first defined bloodline family is the Primordial Tortoise lineage.

## 23.1 Earth Rank

Current early-world grade:

- Earth Rank

Its power keeps pace through Saint.

Above Saint:

- the bloodline remains,
- but no longer scales competitively without a stronger compatible lineage.

## 23.2 Benefits

The bloodline changes the cultivator physically internally, not visibly.

Planned benefit hooks:

- lifespan,
- defense,
- regeneration,
- Earth compatibility,
- resistance,
- shell-art compatibility,
- breakthrough stability,
- improved Primordialis Testudo cultivation efficiency.

## 23.3 Rarity

Ancient Tortoise spirit beasts are extremely rare.

Initial target:

- approximately 5 candidates per 10,000 × 10,000 block area.

This is statistical, not guaranteed spacing.

Players should not expect multiple ancient tortoises within an ordinary 1,000-block exploration radius.

## 23.4 Future Primordial Tortoise Legacy

A future ancient inheritance is tied to an era when spirit beasts were the dominant and most powerful species.

This legacy reaches significance at:

- Open Heaven, Rank 9.

The eventual inheritance allows a Primordial Tortoise path to remain valuable even at the peak of Open Heaven.

This is separate from the early Earth-Rank bloodline.

---

# 24. Primordialis Testudo Clan

The first test clan is the **Primordialis Testudo Clan**.

## 24.1 Clan legend

The clan originates from a cultivator who saved a tortoise spirit beast.

In gratitude, the beast passed down a complete Earth-aligned inheritance.

The clan's style emphasizes:

- slow cultivation,
- strong foundations,
- longevity,
- defense,
- quality over speed.

## 24.2 Core inheritance

**Primordialis Testudo Longevity Art**

Traits:

- Earth aligned
- Neutral path
- slow cultivation
- high foundation quality
- longevity
- body tempering
- defensive emphasis
- supported through Transcendent
- improved by compatible Tortoise bloodlines

The art is usable even by someone with poor Earth affinity.

Poor compatibility should reduce efficiency rather than permanently block the path.

## 24.3 Admission

The clan does not immediately hand the inheritance to strangers.

Prospective members prove reliability through service quests.

Current admission quest concepts:

- repair clan buildings,
- collect weapons from the Blacksmith,
- collect pills/medicine from the Alchemist,
- collect baked goods from a Bakery,
- collect Noodle Soup from the Inn.

Current prototype merit:

- repair quest: 2 Service Merit
- ordinary errand: 1 Service Merit
- admission threshold: 3 Service Merit

On acceptance:

- player becomes Junior Disciple,
- player receives access to the Primordialis Testudo Longevity Art.

These values are balance placeholders.

## 24.4 Clan hierarchy

Current institutional hierarchy:

1. Clan Leader — exactly 1
2. Elders — exactly 6
3. Custodian — exactly 1
4. Masters
5. Senior Disciples
6. Junior Disciples
7. Servants — Mortal workers

The Clan Leader is the sole head.

The six Elders:

- advise the leader,
- receive commands,
- help administer the clan.

The Custodian:

- is a senior office,
- oversees the library.

Masters:

- train/supervise disciples.

A sufficiently high-ranking cultivator may eventually become a direct disciple of the Clan Leader.

Current provisional minimum consideration rank:

- Senior Disciple.

Additional merit/talent/quest requirements remain to be designed.

## 24.5 Clan facilities

Planned clan compound spaces:

- Clan Leader Hall
- Elder council space
- Hall of Punishment
- Clan Library
- Training Court
- Meditation Chamber
- Inheritance Room
- Disciple Quarters
- Servant Quarters

---

# 25. Clan Library

The Clan Library is a controlled knowledge system.

Book categories:

- History
- Facts
- Fundamentals
- Techniques

Technique manuals must be sparse.

The player is not intended to walk into the library and freely select any technique.

## 25.1 Possession does not equal permission

A player can physically carry multiple books.

Only authorized books are usable.

Stealing books does not automatically unlock their contents.

## 25.2 One verified take-out book

A clan member can have exactly one verified take-out book from the library at a time.

If the Custodian verifies a different book:

- previous authorization is replaced,
- the new book becomes usable,
- other carried clan books remain inaccessible.

## 25.3 Verification authority

Normal authority:

- Custodian

Higher authority:

- Elder
- Clan Leader

## 25.4 Additional restrictions

Even a verified book can later require:

- rank,
- realm,
- affinity,
- cultivation method compatibility,
- Master approval,
- merit,
- quest completion,
- prerequisite techniques.

The intent is to prevent technique hoarding and free choice of rare manuals.

---

# 26. Hall of Punishment

The Hall of Punishment is confirmed as a clan facility.

Detailed offenses and punishments are **not yet defined**.

Likely future purposes include disciplinary consequences for:

- theft,
- betrayal,
- harming members,
- forbidden technique use,
- disobedience,
- property damage,
- unauthorized manual access.

These are examples of possible implementation categories, not yet locked rules.

---

# 27. Professions

Planned professions include:

- Alchemist
- Artifact Refiner / Artificer
- Weapon Smith
- Array Master

The design allows both broad and specialized professions.

Example:

A specialist Blacksmith may excel at:

- weapons,
- armor,

but not be able to refine unrelated artifacts such as rings.

A broad Artificer has more versatility but must advance multiple subskills before the profession as a whole advances.

Profession growth should primarily come from actually performing professional work.

Professions also interact with combat/cultivation:

- healing pills,
- weapons,
- artifacts,
- concealment items,
- formations,
- breakthrough support.

---

# 28. World Qi Ecology

Every region/chunk may eventually have an invisible environmental Qi profile.

Examples:

- Spiritual Qi
- Water Qi
- Wood Qi
- Fire Qi
- Yin Qi
- Demonic Qi
- Void/Space Qi

This affects:

- cultivation speed,
- technique training,
- beasts,
- plants,
- resources,
- settlement value,
- spirit veins.

## 28.1 Spirit veins

Spirit veins are underground or regional sources of cultivation energy.

They can generate:

- spirit stones,
- rare herbs,
- beasts,
- sect locations,
- high-value terrain,
- treasure zones.

Players may eventually manipulate or redirect veins.

## 28.2 Cultivation paradises

Formation arrays can create artificial cultivation zones.

A Spirit Gathering Array can use:

- spirit stones,
- flags,
- formation core,
- geometry/layout,

to increase local Qi density.

Large sect arrays may transform mountains or whole regions.

---

# 29. Formations / Arrays

Planned array types include:

- Spirit Gathering
- Killing
- Illusion
- Concealment
- Barrier
- Teleportation
- Element Conversion
- Weather
- Soul
- Sealing

Arrays are intended to become a major profession and world-control system.

---

# 30. Dimension and World-Tier Design

## Overworld

General lower-world environment.

Expected common energies:

- balanced Spiritual Qi,
- Wood,
- Water,
- Earth.

## Nether

Expected to emphasize:

- Fire,
- Lava,
- Demonic/infernal energies,
- dangerous Demonic beasts.

It can be beneficial for compatible cultivation and harmful/impure for incompatible Righteous cultivation.

## End

Expected to emphasize:

- Void,
- Space,
- Soul,
- Eldritch/Outer energy,
- Chaos.

Endermen and End ecology may become cultivation-relevant.

## Higher worlds

Later progression includes custom higher-world dimensions with:

- stronger ambient Qi,
- new resources,
- new techniques,
- higher-rank enemies,
- stronger sects,
- higher-tier crafting,
- materials unavailable in lower worlds.

---

# 31. Ascension

Most major world-tier transitions are true ascensions.

Planned ascension loop:

1. reach lower-world peak,
2. prepare ascension,
3. undergo tribulation/ascension challenge,
4. survive,
5. enter higher world.

Ascension may allow the cultivator to sacrifice accumulated effort/resources to improve fundamentals such as:

- affinity,
- constitution,
- other rebirth-like choices.

Planet → Starfield remains a travel transition rather than ascension.

---

# 32. Spirit Beasts

The world will eventually contain:

- ordinary spirit beasts,
- powerful ancient beasts,
- divine/mythic beasts,
- ocean beasts,
- bloodline-bearing beasts.

Spirit beasts can provide:

- bloodlines,
- beast cores,
- crafting materials,
- spirit pacts,
- inheritance,
- techniques,
- rare resources.

Oceans are intended to become especially dangerous and associated with strong Water Qi and powerful ocean beasts.

---

# 33. Settlements, NPCs, Clans, Sects, Cities

Early implementation starts from vanilla villages.

Possible settlement progression:

- small village,
- village with martial clan,
- larger clan settlement,
- sect branch,
- city,
- capital.

Players may be fortunate enough to spawn near:

- a martial clan,
- a larger city,
- eventually a capital/sect.

Small villages/clans should be most common.

Villages are intended to be strategically important because Mortals are vulnerable.

---

# 34. Early-Game Danger

The Mortal wilderness should be dangerous.

At night, enemies may be capable of killing a normal 20-HP Mortal extremely quickly.

The intended feeling is that ordinary hostile mobs are far beyond vanilla difficulty, comparable to heavily armed/equipped threats.

Daytime should be safer, not harmless.

This pushes early players toward:

- villages,
- clans,
- settlements,
- safe routes,
- cultivation opportunities.

Established cultivators should eventually outgrow ordinary lower-world threats.

---

# 35. Quest and Economy Foundations

The first quest architecture is service-based.

Initial service-provider roles:

- Clan
- Blacksmith
- Alchemist
- Bakery
- Inn

The long-term plan is to make these actual Myriad Ascension roles/buildings rather than permanently relying on vanilla villager professions.

Quest systems are intended to support:

- admission,
- reputation,
- faction progress,
- errands,
- repairs,
- deliveries,
- professional work,
- inheritance access.

---

# 36. Empire System

Empires are political entities, not simply another religious/path affiliation.

## 36.1 Imperial techniques

Imperial can stack with:

- Righteous
- Buddhist
- Demonic
- Unorthodox
- Neutral

Imperial technique bonuses apply when:

- the user belongs to the relevant Empire,
- the target is a declared enemy,
- the technique has the Imperial trait.

## 36.2 Political screen

Future Emperor controls include:

- allies,
- enemies,
- war state,
- state religion,
- internal appointments.

## 36.3 State religion

The first development scope remains Asian-themed.

Christianity/Westernized religious content is deferred until designed separately.

## 36.4 Player becoming Emperor

A player cannot seize the throne merely because the ruler dies to random Minecraft hazards.

Deaths such as:

- falling,
- lava,
- fire,
- drowning,
- suffocation,
- ordinary environmental death

do not open the conquest route.

For player usurpation:

1. Emperor must die in qualifying political/enemy battle,
2. Crown Prince must also fall in qualifying political/enemy battle,
3. player must become the strongest eligible cultivator in the Empire.

---

# 37. UI and Art Direction

The cultivation UI should use an elaborate Xianxia visual identity.

Vanilla Minecraft interfaces should remain visually vanilla unless replaced by a specific Myriad Ascension screen.

Planned custom visual surfaces:

- Mortal Genesis,
- V Cultivator Status,
- X Quick Menu,
- technique screens,
- profession screens,
- sect/clan interfaces,
- political screen,
- quest journal.

Current alpha UIs are functional placeholders.

---

# 38. Audio Policy

Final audio will be supplied by the project owner.

The developer should not generate substitute sound effects.

Placeholder audio can use the owner-provided OGG noise file duplicated/renamed into required sound slots until final assets are supplied.

---

# 39. Technical Architecture

The project targets:

- Minecraft 1.21.1
- NeoForge
- Java 21
- server-authoritative gameplay

Persistent cultivation data uses NeoForge player/entity attachments.

The client handles:

- input,
- display,
- UI,
- safe presentation/prediction.

The server validates:

- Qi availability,
- circulation,
- Burst,
- techniques,
- prerequisites,
- cooldowns,
- realm requirements,
- equipment,
- target validity,
- progression,
- persistent mutations.

Client data must never be trusted for authoritative combat/progression decisions.

---

# 40. Current Persistent Player Data

The current architecture has already reserved or implemented data for:

- sex,
- body polarity,
- affiliation,
- moral alignment,
- Karma,
- innate affinities,
- current affinities,
- realm,
- minor subdivision,
- cultivation progress,
- cultivation comprehension,
- battle comprehension,
- current/max Qi,
- circulation,
- Burst,
- meridian load,
- body injury,
- meridian injury,
- soul injury,
- recovery debt,
- vessel purity,
- impurities,
- Demonic contamination,
- Passive Qi Recharging,
- Meditation,
- Qi Concealment,
- Qi nature,
- known cultivation methods,
- active cultivation method,
- organization standing,
- quest journal,
- bloodline,
- clan library authorization.

Current save schema:

- version 8

---

# 41. Alpha 0.1.0-alpha.4

The first alpha exists as a systems test build.

Implemented/testable foundations include:

- Mortal Genesis,
- random starting affinity generation,
- Male/Female polarity,
- +1/-1 starting morality,
- persistent player data,
- realm/subdivision model,
- G/H/R input,
- circulation,
- Burst,
- server-authoritative networking,
- V screen,
- Primordialis Testudo Longevity Art,
- cultivation-method persistence,
- Mortal → Tempered Body gate,
- Primordial Tortoise bloodline foundation,
- organization standing,
- quest journal,
- Testudo Clan hierarchy/facility definitions,
- Clan Library authorization.

Temporary alpha commands exist to test incomplete gameplay without waiting for NPC/worldgen.

See:

- `docs/ALPHA.md`

---

# 42. Confirmed but Not Yet Fully Implemented

Major confirmed systems still requiring runtime implementation include:

- X quick menu,
- Qi allocation,
- proper passive Qi regeneration,
- actual active cultivation/refill,
- seated meditation,
- Qi deviation runtime,
- Burst drain curve,
- meridian-load runtime,
- body reinforcement,
- weapon infusion,
- movement reinforcement,
- technique execution,
- technique mastery progression,
- Spiritual Sense,
- True Element flight,
- professions,
- spirit veins,
- ambient Qi,
- spirit stones,
- cultivation resources,
- formations,
- alchemy,
- artifact refinement,
- village clan structures,
- cultivation NPCs,
- Testudo service quests,
- Blacksmith/Alchemist/Bakery/Inn buildings,
- actual manual items,
- library shelves,
- clan punishment runtime,
- spirit beasts,
- rare ancient tortoises,
- spirit pacts,
- bloodline extraction,
- auctions,
- tombs,
- monuments,
- cities,
- capitals,
- empires,
- politics,
- higher worlds,
- ascension,
- Starfield travel.

---

# 43. Areas Intentionally Left Open

The following are not fully locked yet and can be expanded later.

## Primordialis Testudo Clan

Still undefined:

- exact cultivation realm ranges for Clan Leader/Elders/Custodian/Masters/disciples,
- Hall of Punishment offense table,
- punishment severity,
- exact first technique manuals,
- final structure layout,
- NPC names,
- final visual design.

## Bloodlines

Still undefined:

- complete grade ladder above Earth Rank,
- exact stat numbers,
- Tortoise beast behavior,
- exact Open Heaven legacy content.

## Cultivation balance

Still undefined:

- exact stage XP/progress formulas,
- Qi capacities,
- body-stat scaling,
- breakthrough numeric requirements,
- Burst multipliers,
- technique coefficients.

## World generation

Still undefined:

- exact structure spacing,
- biome placement,
- spirit-vein density,
- city/capital generation,
- sect distribution.

These should remain adjustable rather than being invented prematurely.

---

# 44. Development Priority Going Forward

The next coherent playable vertical slice is:

1. spawn as Mortal,
2. complete Mortal Genesis,
3. find a village,
4. discover the Primordialis Testudo Clan,
5. complete errands/service quests,
6. become Junior Disciple,
7. receive authorization for the inheritance,
8. learn Primordialis Testudo Longevity Art,
9. enter Tempered Body Stage 1,
10. train through Tempered Body,
11. reach Initial Element,
12. begin active Qi gameplay,
13. test G/H/R,
14. learn restricted clan techniques,
15. continue toward Saint.

This vertical slice should become the reference implementation for later clans, sects, professions, inheritances, and faction systems.

---

# 45. Project Direction

Myriad Ascension is intended to become a world-scale cultivation sandbox rather than a linear class mod.

The long-term player story should be able to grow from:

> a powerless Mortal hiding in a village at night

to:

> a cultivator who commands techniques, factions, bloodlines, professions, formations, worlds, empires, and eventually reaches World Creation.

The intended path is not fixed.

Players should arrive at the summit through different combinations of:

- talent,
- suffering,
- discipline,
- risk,
- resources,
- teachers,
- inheritance,
- politics,
- battle,
- professions,
- bloodlines,
- enlightenment,
- ascension,
- and personal choice.

This document is the current master consolidation of the project's agreed design and should be updated when later decisions supersede any section.
