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

## 5. Character Genesis

Main panel:

`src/main/resources/assets/myriad_ascension/textures/gui/character_genesis.png`

Current source size:

- 512 × 256 px

This texture controls the ornamental/background shell of the first-login character profile screen.

Genesis no longer relies on an invisible/hard-coded panel.

### Shared martial button atlas

`src/main/resources/assets/myriad_ascension/textures/gui/martial_button.png`

Current source size:

- 256 × 64 px

Atlas layout:

- Y 0–19: normal
- Y 20–39: hover/focus
- Y 40–59: selected
- Y 60–63: padding

This atlas is currently used by:

- Character Genesis choices/actions
- Instruction to the Martial World previous/next arrows

It exists specifically to avoid falling back to Minecraft's default grey button texture.

## 6. Editable Item Textures

All current custom item icons are under:

`src/main/resources/assets/myriad_ascension/textures/item/`

Current files:

- `primordialis_testudo_longevity_art_manual.png`
- `minor_body_mending_pill.png`
- `meridian_soothing_pill.png`
- `basic_antidote_pill.png`
- `crude_meridian_poison.png`
- `tempered_body_artifact_sword.png`
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

## 7. 3D Shield Texture

The Tempered Body Artifact Shield deliberately does **not** use a normal `textures/item/*.png` icon.

Minecraft 1.21.1 shields use the built-in entity item renderer and the vanilla `ShieldModel` geometry.

Editable shield surface:

`src/main/resources/assets/myriad_ascension/textures/entity/tempered_body_artifact_shield.png`

Source size:

- 64 × 64 px

The initial placeholder is copied from vanilla 1.21.1's `shield_base_nopattern.png` so the UV layout is guaranteed to match `ShieldModel`.

The item model's `particle` texture deliberately uses `minecraft:block/dark_oak_planks`, matching vanilla shield model behavior. The `particle` slot is atlas-backed and must not point at the custom entity texture. The shield's visible surface is still the Myriad Ascension entity PNG rendered by the custom BEWLR.

Associated models:

- `models/item/tempered_body_artifact_shield.json`
- `models/item/tempered_body_artifact_shield_blocking.json`

Both use:

```json
"parent": "builtin/entity"
```

The normal and blocking transform values intentionally mirror vanilla 1.21.1 shield models.

Do not replace this with `minecraft:item/generated`; doing so collapses the shield back into a flat 2D item.

## 8. Equipped Armor Textures

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

## 9. Item Model Files

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

## 10. UI Text Containment Rules

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

## 11. Editing Workflow

Recommended workflow:

1. Edit the PNG under `src/main/resources/assets/myriad_ascension/textures/`.
2. Save as PNG with transparency preserved where required.
3. Keep the same path and filename.
4. In a development client, reload resources with **F3 + T**, or restart the client if the changed asset is tied to an equipment renderer.
5. No Java change is required when only replacing pixels at the same path.

The placeholders are intentionally simple. They exist so every custom visual has a concrete, editable file from the beginning rather than silently inheriting a vanilla texture.


## UI resource inventory

As of alpha.13 development, the custom UI texture inventory is:

| UI | Editable PNG | Rendering status |
| --- | --- | --- |
| Cultivation HUD | `textures/gui/cultivator_hud.png` | active |
| Minor Storage Bag | `textures/gui/minor_storage_bag.png` | active |
| Cultivator V Screen | `textures/gui/cultivator_status_panel.png` | active; malformed alpha.12 PNG sanitized in alpha.13 |
| V Screen tabs | `textures/gui/cultivator_status_tabs.png` | active; confirmed by alpha.12 runtime screenshot |
| Martial World Guide | `textures/gui/martial_world_guide.png` | active; malformed alpha.12 PNG sanitized in alpha.13 |
| Character Genesis | `textures/gui/character_genesis.png` | active; malformed legacy PNG sanitized in alpha.13 |
| Shared martial buttons | `textures/gui/martial_button.png` | active |

Dynamic text, numeric values, progress bars, item slots, and interaction logic remain code-driven because they must change at runtime. Their ornamental/static visual shells are asset-backed.


## 12. Alpha.12 release-asset rule

The alpha.12 artifact is intentionally built only after the post-alpha.11 UI commits, cultivation corrections, and corrected HUD/storage-bag textures are present on the same release head.

For future alphas, bump the mod version and artifact name only after the intended release changes are already committed. This prevents a versioned artifact from being generated before later same-version UI fixes.


## 13. Alpha.13 PNG validity rule

Minecraft's `NativeImage`/STB loader can reject a file that some image editors still display. Alpha.12 exposed this with the V-screen and Martial World Guide backgrounds: both resources existed at the correct path but contained malformed PNG chunk structure.

Alpha.13 therefore sanitizes the affected V-screen, Martial World Guide and Character Genesis PNGs and adds:

`scripts/validate_pngs.py`

GitHub Actions runs this before Gradle. Every PNG under `src/main/resources/assets/` must have valid chunk boundaries, CRCs, IHDR/IDAT/IEND structure and a complete IDAT zlib stream.

When replacing an editable texture, preserve the expected dimensions/UV layout and save it as a standards-compliant PNG. CI is now the first guard; Minecraft runtime testing remains the final guard.


## 14. Alpha.15 editable item placeholders

Two new gameplay items use mod-local editable PNGs:

- `textures/item/minor_purification_pill.png` — 16x16
- `textures/item/basic_training_weight.png` — 16x16

Their corresponding item models are:

- `models/item/minor_purification_pill.json`
- `models/item/basic_training_weight.json`

The supplied alpha artwork is intentionally simple placeholder art. It can be replaced directly without Java changes as long as the PNG remains structurally valid.


## 15. Alpha.16 training-weight equipment

Alpha.16 adds editable local textures for wearable training resistance gear:

- `textures/item/training_weight_vest.png` — 16x16
- `textures/item/training_weight_leggings.png` — 16x16
- `textures/item/training_ankle_weights.png` — 16x16
- `textures/models/armor/training_weight_layer_1.png` — 64x32
- `textures/models/armor/training_weight_layer_2.png` — 64x32

Corresponding item models:

- `models/item/training_weight_vest.json`
- `models/item/training_weight_leggings.json`
- `models/item/training_ankle_weights.json`

These are intentionally simple editable placeholder assets. The worn material grants zero armor protection; its purpose is visible training resistance.
