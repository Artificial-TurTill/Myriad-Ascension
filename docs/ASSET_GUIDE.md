# Myriad Ascension — Editable Asset Guide

This file is the canonical map for textures that are intended to be edited directly.

All custom visual assets must live under:

`src/main/resources/assets/myriad_ascension/textures/`

Custom item models may use vanilla **model parents** such as `minecraft:item/generated` or `minecraft:item/handheld`, but their `layer0` texture must point to the `myriad_ascension` namespace unless there is an explicit design reason not to.

---

## 1. Cultivator V Screen

### Main panel

`src/main/resources/assets/myriad_ascension/textures/gui/cultivator_status_panel.png`

Current source size:

- 256 × 256 px

Controls:

- outer panel background,
- gold frame,
- left navigation background,
- vertical navigation/content divider,
- ornamental panel details.

The Java screen scales this PNG to the current panel size.

The navigation divider in the source image is positioned at 25% of the texture width. The Java layout also reserves 25% of the panel for navigation so the divider stays aligned.

### Tab button atlas

`src/main/resources/assets/myriad_ascension/textures/gui/cultivator_status_tabs.png`

Current source size:

- 256 × 64 px

Atlas layout:

- Y 0–19: normal tab
- Y 20–39: hovered/focused tab
- Y 40–59: selected tab
- Y 60–63: unused padding

Each state is scaled to the current tab-button dimensions.

The tab text itself remains rendered by Minecraft's font so localization can change without repainting the PNG.

---

## 2. In-Game Cultivation HUD

`src/main/resources/assets/myriad_ascension/textures/gui/cultivator_hud.png`

Current source size:

- 148 × 45 px

Controls:

- top-left HUD frame,
- three bar backgrounds,
- border styling.

Health/Qi/Power fill amounts are dynamic and remain code-rendered because their widths change every frame.

---

## 3. Minor Storage Bag GUI

`src/main/resources/assets/myriad_ascension/textures/gui/minor_storage_bag.png`

Source size:

- 176 × 166 px

This is rendered at native size.

It contains the backgrounds for:

- 9 artifact-storage slots,
- player inventory,
- hotbar,
- bag frame.

If this texture's dimensions change, the screen/menu coordinates must also be changed.

---

## 4. Instruction to the Martial World GUI

`src/main/resources/assets/myriad_ascension/textures/gui/martial_world_guide.png`

Source size:

- 256 × 256 px

The screen scales it to the current guide window.

Text remains dynamic and is width-wrapped by Java.

---

## 5. Editable Item Textures

All current custom item icons are under:

`src/main/resources/assets/myriad_ascension/textures/item/`

Current files:

- `primordialis_testudo_longevity_art_manual.png`
- `minor_body_mending_pill.png`
- `meridian_soothing_pill.png`
- `basic_antidote_pill.png`
- `crude_meridian_poison.png`
- `tempered_body_artifact_sword.png`
- `tempered_body_artifact_shield.png`
- `tempered_body_artifact_helmet.png`
- `tempered_body_artifact_chestplate.png`
- `tempered_body_artifact_leggings.png`
- `tempered_body_artifact_boots.png`
- `minor_storage_bag.png`
- `instruction_to_the_martial_world.png`

Current placeholder icons are 16 × 16 px.

They can be replaced by higher-resolution PNGs if desired; Minecraft item textures support higher resolutions as long as the image remains a valid resource texture.

The Tempered Body Artifact Shield now has its own shield-shaped local texture. It no longer uses an iron-ingot texture.

---

## 6. Equipped Armor Textures

The artifact armor no longer uses `ArmorMaterials.IRON` for its visual layer.

It uses a Myriad Ascension armor material with the following editable files:

`src/main/resources/assets/myriad_ascension/textures/models/armor/tempered_body_artifact_layer_1.png`

`src/main/resources/assets/myriad_ascension/textures/models/armor/tempered_body_artifact_layer_2.png`

Current source size:

- 64 × 32 px each

Layer 1 is used for the outer armor pieces.

Layer 2 is used for the leggings layer.

These are deliberately simple original placeholders. Replace them with final artwork while preserving the normal Minecraft 1.21.1 armor UV layout.

---

## 7. Item Model Files

Model JSONs are located under:

`src/main/resources/assets/myriad_ascension/models/item/`

The texture field should follow this pattern:

```json
{
  "parent": "minecraft:item/generated",
  "textures": {
    "layer0": "myriad_ascension:item/example_item"
  }
}
```

For handheld weapons, the model parent may be:

```json
"parent": "minecraft:item/handheld"
```

Using a Minecraft parent is normal and does **not** mean the texture is vanilla.

The important part is that `layer0` points to `myriad_ascension:item/...`.

---

## 8. UI Text Containment Rules

The V screen now enforces:

- a clipped content region,
- width-aware wrapping,
- separate left/right column widths,
- wrapped cultivation-method and bloodline names,
- wrapped technique names,
- wrapped ability descriptions,
- scrollable ability entries.

Future UI text should use the same wrapped rendering helpers instead of direct unrestricted `drawString` calls.

---

## 9. Editing Workflow

Recommended workflow:

1. Edit the PNG under `src/main/resources/assets/myriad_ascension/textures/`.
2. Save as PNG with transparency preserved where required.
3. Keep the same path and filename.
4. In a development client, reload resources with **F3 + T**, or restart the client if the changed asset is tied to an equipment renderer.
5. No Java change is required when only replacing pixels at the same path.

The placeholders are intentionally simple. They exist so every custom visual has a concrete, editable file from the beginning rather than silently inheriting a vanilla texture.
