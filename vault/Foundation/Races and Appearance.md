---
tags: [foundation, races, appearance]
---
# Races and Appearance

## Races
JRMCore races, reduced to **Human** only for now (Saiyan, Half-Saiyan, Namekian, Arcosian and Majin were Dragonblock
C+ content and are not part of the Foundation). New races are content ([[Templates/Idea - Race]]).

| Field | Meaning |
| --- | --- |
| `id` | `namespace:path` |
| `genders` | `["male", "female"]` or one of them |
| `power_types` | allowed power types, `[]` = all |
| `stat_bonus` | list of `StatBonus { stat, percent }` by statistic key (e.g. `melee`), for every power type with that key |
| `starting` | optional `StartingOverride { power_type, values }` (DBC races had their own starting attributes) |
| `appearance` | an `AppearanceOptions` object (below) |
| `order` | position in the wizard |

Human: both genders, every power type, no bonuses (JRMCore Human has none), power type starting values.

## Appearance
The JRMCore creation page "Appearance" (jrmcDNS / jrmcDNSH), as a data model (`Appearance`):
gender, skin colour, hair style (−1 = bald), hair colour, eye colour, body type, height (%).

`AppearanceOptions` per race: `skin_colors`, `hair_styles` (count, 0 = no hair), `hair_colors`, `eye_colors`
(all `"#RRGGBB"` presets), `body_types` (count), `min_height` / `max_height` (%).

Human: 6 skin tones, 12 hair styles, 7 hair colours, 5 eye colours, 3 body types, height 90-110 %.

- Chosen in the creation wizard, validated by the server against the race's options, saved with the character and
  synced to every client (`PublicLook`).
- **Height** already acts in game: the 1.21 `scale` attribute (90 % … 110 %).
- **Not drawn yet**: skin, hair, eyes and body type have no renderer; the player keeps the normal skin. A body / hair
  renderer is the largest open point ([[Open Points]]). Dragonblock C+'s JBRA models and textures are JinGames assets
  and are not copied; Myriad Realm needs its own.
