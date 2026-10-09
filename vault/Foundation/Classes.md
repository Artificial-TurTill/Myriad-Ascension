---
tags: [foundation, classes]
---
# Classes

JRMCore's class concept (Martial Artist / Spiritualist / Warrior for Ki): each power type has its own classes, chosen
at creation, fixed afterwards.

| Field | Meaning |
| --- | --- |
| `id` | `namespace:path` |
| `power_type` | the power type it belongs to |
| `stat_bonus` | percent per statistic, in the power type's statistic order (JRMCore statIncBonusClass) |
| `recovery_bonus` | percent on Body, energy and Stamina regeneration (JRMCore class recovery bonus) |
| `order` | position in the wizard |

DBC Ki defaults for reference (statistics Melee, Defense, Body, Stamina, Ki Power, Ki Pool):
Martial Artist `[0, 0, 0, 0, 0, 0]`, Spiritualist `[−10, 10, −10, −10, 10, 10]`, Warrior `[10, −10, 10, 10, −10, −10]`;
recovery Spiritualist `[−10, 10, −10]`, Warrior `[10, −10, 10]`.

Current classes: Natural → Unclassed; Qi → Martial / Spirit / Body Cultivator ([[Qi]]).
