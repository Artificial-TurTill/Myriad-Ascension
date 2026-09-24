# Vanilla Minecraft 1.21.1 Reference — Shields

This note records the vanilla implementation details used as a foundation for Myriad Ascension.

Source inspected:

- Minecraft 1.21.1 client JAR supplied from the user's CurseForge launcher installation.
- Minecraft 1.21.1 launcher/version JSON supplied alongside the JAR.

## Vanilla shield asset structure

The 1.21.1 JAR contains:

- `assets/minecraft/models/item/shield.json`
- `assets/minecraft/models/item/shield_blocking.json`
- `assets/minecraft/textures/entity/shield_base.png`
- `assets/minecraft/textures/entity/shield_base_nopattern.png`
- `assets/minecraft/atlases/shield_patterns.json`
- pattern textures under `textures/entity/shield/`

The normal shield model is not a generated 2D item.

It uses:

```json
"parent": "builtin/entity"
```

and defines transforms for:

- third-person right hand
- third-person left hand
- first-person right hand
- first-person left hand
- GUI
- fixed/item-frame
- ground

The model also has a `blocking` override which switches to `shield_blocking.json`.

## Vanilla renderer behavior

Minecraft's `BlockEntityWithoutLevelRenderer` contains a dedicated `ShieldModel`.

The vanilla renderer:

1. recognizes the vanilla shield item,
2. pushes the pose,
3. scales the model by `(1, -1, -1)`,
4. obtains the shield render buffer,
5. renders the ShieldModel handle and plate,
6. optionally applies banner-pattern rendering,
7. pops the pose.

A modded `ShieldItem` does not automatically enter the vanilla renderer's hard-coded vanilla-item branch. Therefore Myriad Ascension supplies a client-side custom BEWLR which reuses Minecraft's own `ShieldModel` geometry.

## Myriad Ascension implementation rule

For specialized vanilla items, do not assume a normal item-model pipeline.

Before implementing or replacing one, inspect:

1. vanilla item model JSON,
2. alternate state model JSONs,
3. relevant entity/model textures,
4. atlases,
5. the item's Java class,
6. its client renderer,
7. any data components/predicates driving model state.

This rule applies especially to:

- shields,
- bows,
- crossbows,
- tridents,
- armor,
- banners,
- decorated pots,
- chests/shulker-like items,
- maps,
- compasses,
- clocks,
- spyglasses,
- fishing rods,
- animated/special entity-backed items.

The goal is to preserve Minecraft-native behavior first, then layer Myriad Ascension mechanics and art on top.
