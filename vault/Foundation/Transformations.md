---
tags: [foundation, transformations]
---
# Transformations

DBC forms as data. A transformation multiplies attributes for the released statistics while it drains energy and
Stamina; time in the form raises mastery, which lowers the drain.

| Field | Meaning |
| --- | --- |
| `id` | `namespace:path` |
| `power_type` | the power type it belongs to |
| `race` | `""` = every race, otherwise only that race (racial forms) |
| `previous` | `""` = from the base form, otherwise the form it ascends from (chains like SSJ → SSJ2) |
| `skill` / `skill_level` | required skill and level (`""` = none) |
| `multipliers` | one per attribute of the power type (Qi: STR DEX CON WIL MND SPI) |
| `energy_drain` | % of max energy per second |
| `stamina_drain` | % of max Stamina per second |
| `min_release` | minimum power release %; below it the form drops |
| `mastery_gain` | mastery per second in the form |
| `mastery_max` | mastery cap |
| `mastery_relief` | at full mastery the drains are × (1 − relief) |
| `color` | `"#RRGGBB"` (HUD / future aura and hair tint) |
| `order` | position in the list; also the order R tries forms in |

## Rules
- **R (ascend)**: the form selected in the Transformations tab if it is the next step from the current form,
  otherwise the first reachable form (in `order`). Requirements: previous form, skill level, release ≥ min_release,
  energy > 0.
- **Z (descend)**: back to the base form.
- Every second: drain `ceil(max × drain% × (1 − relief × mastery / max))`; not enough energy / Stamina or release
  below the minimum → back to base. Mastery + `mastery_gain`.
- Released statistics (Melee, Defense, energy power) use `attribute × multiplier`; pools (Body, energy, Stamina) do
  not change (as in DBC).
- Body and energy only regenerate in the base form (JRMCoreComTickH).
- Death resets to the base form.

## Not extracted yet
DBC's racial form tables, Kaioken / Ultra Instinct / God forms (state2 stacking), strain, the transformation meter
and hair / size changes. A second stacking layer (like Kaioken on top of a form) would be an engine extension.
