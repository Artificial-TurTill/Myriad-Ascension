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
gender, skin colour, the **hairstyle code** (`""` = bald), hair colour (any colour), eye colour, body type, height (%).

`AppearanceOptions` per race: `skin_colors`, `hair` (the race has hair), `hair_colors` (quick presets), `eye_colors`
(all `"#RRGGBB"` presets), `body_types` (count), `min_height` / `max_height` (%).

Human: 6 skin tones, hair, 7 quick hair colours, 5 eye colours, 3 body types, height 90-110 %.

## Hairstyle manager (from Dragonblock C+ / JRMCore)
The custom hair of JRMCore (`jrmcDNSH`, JBRA renderHairsV2), ported from DBC+ `LegacyHair` / `HairEditor` /
`HairRenderLayer` into `HairCode` and `client/hair`:
- **Code**: 786 digits = 56 hair groups × 14 digits (length, X / Y / Z rotation, bend, bend point, shape change) +
  version "G". Groups: 0-3 front, 4-13 right, 14-23 left, 24-39 back, 40-55 top. Values 18-82 (50 = neutral).
  JRMCore / DBC+ codes can be pasted (version 1 codes are upgraded).
- **Barber Menu** ("Hairstyle"): custom hair or bald, hair colour (quick colours with the arrows, any colour with the
  HSB colour picker), Revert, Cancel, Done. Opened from the creation wizard (Hair row), the character sheet
  (Hairstyle button, bottom right) and the X menu (Hairstyle cell).
- **Editor** (5 pages: Front, Right, Left, Back, Top): per group Tier 1 sliders (X / Y / Z rotation, length) and
  Tier 2 sliders (bend, bend point, shape change), show / hide, tier switch, reset group; repeat mode copies the
  first group of a page to all; yaw / pitch for the head preview; Share (copy code), Paste, Reset (to the preset).
- **Presets**: the six JRMCore presets plus the player's own (Save as Preset / Delete Preset), stored in
  `config/ascension_foundation-hairpresets.txt` on the client.
- **Rendering**: every player with a character shows the hairstyle (synced in `PublicLook`), tinted with the hair
  colour, hidden under a helmet; the local player sees the editor's preview live. Texture: the Foundation's own
  `textures/entity/hair.png` (generated, not a JBRA texture).
- Changing the hairstyle is allowed at any time (adaptation: DBC needed Barber Scissors after the free creation
  edit; the Foundation has no items).
- Not ported yet: the hair animation while transforming / charging (hair standing up, the shape change value), the
  classic JBRA hair models 0-11 (JinGames models).

- Chosen in the creation wizard, validated by the server against the race's options, saved with the character and
  synced to every client (`PublicLook`).
- **Height** already acts in game: the 1.21 `scale` attribute (90 % … 110 %).
- **Hair is drawn** (hairstyle manager above). **Not drawn yet**: skin, eyes and body type; the player keeps the
  normal skin. A body renderer is an open point ([[Open Points]]); DBC+'s JBRA body models and textures are JinGames
  assets and are not copied.
