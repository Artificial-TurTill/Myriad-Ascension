# Myriad Ascension — Starter Medicines, Poisons, Artifacts, and Martial Tools

## Purpose

This document defines the first tangible martial-world item layer.

The initial items are not intended to represent the final power ceiling. They exist to establish the permanent systems:

- cultivation-aware medicine,
- cultivation-aware poison,
- artifact type/grade metadata,
- real storage artifacts,
- in-game instruction/manual UX.

---

# 1. Medicines

Medicines can affect both ordinary Minecraft health and cultivation condition.

## Minor Body-Mending Pill

Current effects:

- heals ordinary health,
- reduces Body Injury,
- slightly reduces Meridian Injury,
- reduces Recovery Debt,
- reduces Training Fatigue,
- adds medicinal impurities.

Design purpose:

A cheap pill should help immediately without being a free perfect recovery tool.

Repeated dependence on low-grade medicine can degrade long-term foundation quality through Impurity Load.

## Meridian-Soothing Pill

Current effects:

- small ordinary healing,
- strongly reduces Meridian Injury,
- strongly reduces Meridian Load,
- slight Body Injury reduction,
- small fatigue/recovery benefit,
- adds medicinal impurities.

Design purpose:

This becomes the early answer to overusing Burst, techniques, or unstable circulation.

## Basic Antidote Pill

Current effects:

- removes ordinary Minecraft Poison,
- repairs a small amount of Body/Meridian Injury,
- adds a very small impurity cost.

Future higher poisons may require specialized antidotes rather than this generic starting pill.

---

# 2. Poison

## Crude Meridian Poison

Use:

- right-click a living target while holding the poison.

Current effects:

- ordinary Poison effect,
- Body Injury,
- Meridian Injury,
- Meridian Load.

Design purpose:

Poison can weaken a stronger cultivator by attacking the systems that let them express realm power.

It does not simply ignore realm strength.

Future poison systems should account for:

- Vitality,
- poison resistance,
- cultivation realm,
- bloodline,
- physique,
- antidotes,
- poison grade,
- elemental nature,
- alchemist skill.

---

# 3. Artifact Framework

Artifacts have two independent identifiers:

- Artifact Type
- Artifact Grade

## Types

Initial types:

- Weapon
- Shield
- Armor
- Storage
- Utility

## Grades

Initial framework includes:

- Mortal
- Tempered Body
- Initial Element
- Qi Transformation
- True Element
- Immortal Ascension
- Transcendent
- Saint
- Saint King
- Origin
- Dao Source
- Emperor
- Open Heaven

These grades are technical balancing categories, not a claim that every future artifact system must use exactly one artifact family per cultivation realm.

---

# 4. Starter Artifact Equipment

The first test equipment is **Tempered Body-grade**.

Implemented:

- Tempered Body Artifact Sword
- Tempered Body Artifact Shield
- Tempered Body Artifact Helmet
- Tempered Body Artifact Chestplate
- Tempered Body Artifact Leggings
- Tempered Body Artifact Boots

For the first alpha implementation, the vanilla-facing attributes use approximately iron-class behavior plus custom durability.

The real cultivation artifact contribution will later be applied by the cultivation combat system.

A high-grade artifact never grants the wielder:

- equivalent body speed,
- equivalent reaction time,
- equivalent durability,
- equivalent Qi capacity,
- equivalent spiritual control.

It only contributes the power that the wielder can actually control and physically exploit.

---

# 5. Minor Storage Bag

The Minor Storage Bag is the first storage artifact.

Capacity:

- exactly 9 inventory slots.

Rules:

- right-click opens the storage menu,
- contents are stored on the ItemStack itself,
- dropping or moving the bag preserves contents,
- storage bags cannot be inserted into themselves,
- bag contents are independent from the player's ordinary inventory.

Future storage grades can increase:

- slots,
- weight tolerance,
- preservation,
- living-space capability,
- artifact restrictions.

---

# 6. Instruction to the Martial World

Every player receives one copy after completing Mortal Genesis.

Right-click opens an in-world instruction interface.

Current chapters:

1. Controls
2. The Cultivation Path
3. Training
4. Power
5. Techniques
6. Medicines & Poisons
7. Artifacts
8. The Martial World

The instruction manual is intended to remain the canonical beginner-facing guide as the mod expands.

It should be updated whenever core controls or progression concepts change.

---

# 7. Acquisition Philosophy

Starting alpha access uses Creative tabs and the Genesis guide grant so systems can be tested.

Final acquisition will shift toward:

- village craftsmen,
- alchemists,
- clan stores,
- quests,
- sect contribution shops,
- auctions,
- tombs,
- monuments,
- beast materials,
- crafting/refinement professions,
- world loot.

Artifacts and medicines should eventually be scarce enough that professions and organizations matter.
