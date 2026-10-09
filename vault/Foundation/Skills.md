---
tags: [foundation, skills]
---
# Skills

The JRMCore skill concept (DBC basic skills): learnable with TP, levels up to a maximum, every level reserves part of
the capacity attribute (DBC Mind); forgetting frees the reservation without refunding TP.

| Field | Meaning |
| --- | --- |
| `id` | `namespace:path` |
| `power_type` | `""` = every power type |
| `max_level` | highest level |
| `tp_cost` | TP per level step; the last entry repeats (`[250]` = 250 for every level) |
| `capacity_cost` | capacity (Mind) per level step; the last entry repeats (DBC `[10, 5]`: 10 for the first, 5 after) |
| `effect` | engine effect per level (below), `""` = concept only |
| `per_level` | effect strength per level |
| `learn` | `sheet` (bought in the Skills tab) or `teacher` (first level must be given: command / add-on code) |
| `order` | position in the Skills tab |

## Engine effects
| Effect | Per level | DBC origin |
| --- | --- | --- |
| `energy_pool` | + per_level × max energy (multiplier in the stat formula) | Ki Boost (KB) 1 % |
| `meditation` | while charging: + per_level % of max energy per second, costs 1 % max Stamina | Meditation (MD) |
| `endurance` | − per_level of damage taken | Endurance (EN) 3 % |
| `recovery` | + per_level × Body regeneration | (Foundation) |

A new mechanic (e.g. flight, dash, sense) is a new engine effect: Foundation code + test, then content can use it.

## DBC skills not extracted yet
Fly, Jump, Dash, Fusion, Ki Sense, Ki Protection, Ki Fist, Defense Penetration, Ki Infuse, Instant Transmission,
Potential Unlock, the racial skill and the transformation skills (Kaioken, God Form, …). See [[DBC+ Extraction Map]].
