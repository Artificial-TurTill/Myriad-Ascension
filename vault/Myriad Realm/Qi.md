---
tags: [myriad-realm, qi, power-type]
---
# Qi

Myriad Realm's energy. It uses the JRMCore Ki structure as its skeleton, but it is **its own system**: own id
(`myriad_realm:qi`), own names, own numbers, and it is changed in `qi.arsn` without touching Dragonblock C+.

**Every Qi character starts with 1 in every attribute**: a cultivator begins as an ordinary mortal body.

## Attributes
| | Attribute | Raises |
| --- | --- | --- |
| STR | Strength | Melee |
| DEX | Dexterity | Defense |
| CON | Constitution | Body, Stamina |
| WIL | Willpower | Qi Power |
| MND | Mind | skill capacity, TP per hit |
| SPI | Spirit | Max Qi |

Limit 10,000 per attribute (the DBC training limit).

## Statistics (per attribute point, JRMCore Ki defaults as a starting balance)
| Statistic | Per point | Released | At start (Martial) |
| --- | --- | --- | --- |
| Melee | 2.5 × STR | yes | 3 |
| Defense | 4.0 × DEX | yes | 4 |
| Body | 20 × CON | | 20 (= the vanilla 20 health) |
| Stamina | 3.5 × CON | | 4 |
| Qi Power | 5.2 × WIL | yes | 6 |
| Max Qi | 40 × SPI | | 40 |

Life is Body (hearts hidden), power release 0-100 %, passive Defense 20 %.

## Classes (the DBC class concept)
| Class | Melee | Defense | Body | Stamina | Qi Power | Max Qi | Recovery (Body / Qi / Stamina) |
| --- | --- | --- | --- | --- | --- | --- | --- |
| Martial Cultivator | 0 | 0 | 0 | 0 | 0 | 0 | 0 / 0 / 0 |
| Spirit Cultivator | −10 | +10 | −10 | −10 | +10 | +10 | −10 / +10 / −10 |
| Body Cultivator | +10 | −10 | +10 | +10 | −10 | −10 | +10 / −10 / +10 |

Starting resources: Martial 20 / 40 / 4, Spirit 18 / 44 / 4, Body 22 / 36 / 4 (Body / Qi / Stamina).

## Skills
| Skill | Max | TP per level | Mind (first / later) | Effect |
| --- | --- | --- | --- | --- |
| Qi Meditation | 10 | 250 | 10 / 5 | while charging: +0.5 % max Qi per second per level |
| Qi Condensation | 10 | 800 | 10 / 5 | +1 % max Qi per level |
| Endurance | 10 | 150 | 10 / 5 | −3 % damage taken per level |

With MND 1 no skill can be learned yet: Mind has to be trained to 10 first (deliberate: mortals cannot cultivate
techniques until the mind is ready). Every upgrade starts at 16 TP.

## Transformations
| Form | From | Multipliers | Drain | Min release |
| --- | --- | --- | --- | --- |
| Qi Surge (example) | base | STR, DEX, WIL × 1.25 | 2 % Qi / s | 50 % |
