# Primordialis Testudo Clan

The **Primordialis Testudo Clan** is the first test cultivation clan.

It is intended to be integrated into vanilla villages before custom cities/capitals are implemented.

## Legend

The clan descends from a cultivator who once saved a tortoise spirit beast.

In return, the beast passed down a complete Earth-aligned inheritance.

The resulting method is intentionally slow, stable, and quality-focused.

## Core Inheritance

The clan's core inheritance is:

**Primordialis Testudo Longevity Art**

### Identity

- Element: Earth
- Path trait: Neutral
- Supported progression: Mortal/Tempered Body through **Transcendent**
- Cultivation speed: slow
- Foundation quality: high
- Lifespan: unusually high
- Defensive/body-tempering emphasis
- Tortoise bloodlines accelerate compatibility/progression

The inheritance can therefore carry a player from their first entry into cultivation through Transcendent without requiring a method switch.

## Design Purpose

This clan is ideal as the first test faction because it stresses systems other than raw speed:

- manuals/inheritances,
- Earth affinity,
- body tempering,
- lifespan,
- foundation quality,
- bloodline synergy,
- Masters and disciples,
- village integration,
- full-method realm ceilings.

## NPC Hierarchy — Prototype

The actual NPC implementation should support at minimum:

- Clan Head
- Elder
- Master
- Senior Disciple
- Junior Disciple
- Mortal Clan Member

The specific cast, names, buildings, and visuals remain asset/lore work for later.

## Village Integration

Initial implementation should use vanilla villages.

A qualifying village can receive a small Primordialis Testudo compound containing some combination of:

- training courtyard,
- clan hall,
- manual/inheritance room,
- meditation chamber,
- Earth-aligned training blocks,
- elder/master NPC,
- disciple NPCs.

The clan should make a village safer than wilderness without making it completely immune to hostile cultivators or beasts.


## Admission by Service

The clan does not hand its inheritance to a random Mortal.

A prospective disciple proves reliability by completing practical service quests for the clan and village.

Initial admission quest pool:

- repair damaged parts of the clan compound,
- act as an errand runner and collect clan weapons from the Blacksmith,
- collect pills and medicine from the Alchemist,
- collect baked goods from the Bakery,
- collect Noodle Soup from the Inn.

These jobs deliberately connect the clan to the settlement economy. They also give a Mortal useful tasks that do not require surviving high-level wilderness combat.

### Service Merit

The implementation uses **Service Merit** as the technical admission progress value.

Current prototype weights:

- repairing the clan compound: 2 merit,
- ordinary delivery/errand: 1 merit.

Current prototype admission threshold: 3 merit.

These numbers are tuning values and may be changed without altering the lore.

Once accepted, the player enters as a **Junior Disciple** and receives access to the Primordialis Testudo Longevity Art.

The final NPC interaction should still involve an Elder/Master formally accepting the player rather than silently changing membership in the background.
