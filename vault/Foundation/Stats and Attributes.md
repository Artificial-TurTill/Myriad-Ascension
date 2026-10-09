---
tags: [foundation, stats]
---
# Stats and Attributes

The JRMCore 1.3.51 character sheet maths (Dragonblock C+ `LegacyStats` / `LegacyBaseStats` / `LegacyGameplay`),
generalised in `StatMath` so every [[Power Types|power type]] can use it. Numbers keep the original float / double
order and rounding, tested against DBC+ values (`StatMathTest`).

## Attributes
Trainable values defined by the power type (Natural: STR, DEX, CON; Qi: STR, DEX, CON, WIL, MND, SPI). Starting
values come from the power type (`starting`) or a race override; the limit per attribute is `attribute_limit`.

## Statistics
Each statistic grows from one attribute:

`stat = ROUND_UP( per_attribute × attribute × (1 + (race% + class%) / 100) + per_attribute × attribute × skillBonus )`

- race% from the race's `stat_bonus` by statistic key, class% from the class's `stat_bonus` by position.
- skillBonus: only the energy pool, from skills with effect `energy_pool` (DBC Ki Boost).
- **Released** statistics (`released: true`, DBC Melee / Defense / Ki Power) are scaled by the power release:
  `output = (int)(stat × release / 100)`, and use the transformed attributes ([[Transformations]]).
- The sheet tooltip shows the increase per attribute point (JRMCoreH.statInc, rounded down to 0.1).

## Roles (what the engine does with a statistic)
| Role | Effect |
| --- | --- |
| `melee` | Body characters: released Melee is added to every powered punch. Natural: vanilla attack damage modifier. |
| `defense` | Body characters: passive share (`passive_defense` %, default 20 %) reduces hits. Natural: armor modifier (/10). |
| `body` | Maximum Body (the only life for `life: "body"`). |
| `stamina` | Maximum Stamina (punch cost, meditation cost, form drain). |
| `energy_pool` | Maximum energy (Ki / Qi). |
| `energy_power` | Shown on the sheet; reserved for energy attacks (open point). |
| `health` | Natural: vanilla max health modifier. |
| `speed` | Turbo run speed: statInc of 100 points × 0.01, its attribute is DBC's DEX ([[Combat and Resources]]); shown as a % on the sheet. |
| `""` | Display only. |

## Level
JRMCore: `sum / 5 − 11` (level 1 at its 60 starting points). Foundation (adaptation):
`level = 1 + (sum − starting sum) / 5`, identical for 60-point starts and also level 1 for Qi's 6-point start.

## Upgrading (Training Points)
JRMCore `attrCst`: weighted = (int)(Σ attributes × 0.75); one point costs `max(16, weighted × 0.5 − max(0, 140 −
weighted))`; the UC multiplier buys 1 / 10 / 100 / 1000 points at the summed price. An attribute cannot pass the
power type's `attribute_limit`. Adaptation: after an upgrade the grown maximum of Body / energy / Stamina is added to
the current value (JRMCore added one point's increase, capped at the old maximum).

## TP
Earned per landed hit on a living target (not armor stands, not in creative): chance 25 % (the JRMCore Human value),
and for power types with release only from 5 % release; value `2 × (1 + tpAttribute / 5 × release%)` (DBC: Mind).
