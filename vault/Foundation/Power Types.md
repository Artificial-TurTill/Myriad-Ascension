---
tags: [foundation, power-types]
---
# Power Types

JRMCore's `Pwrtyps` concept: one engine, several energies. A power type owns its attributes, statistics, starting
values, attribute limit, classes and whether Body replaces the hearts.

| Field | Meaning |
| --- | --- |
| `id` | `namespace:path`, unique |
| `energy` | `""` = no energy resource, otherwise its name (lang `resource.<ns>.<energy>`) |
| `life` | `vanilla` (hearts stay) or `body` (Body is the only life, hearts hidden) |
| `attributes` | list of `AttributeDef { key, short }` |
| `stats` | list of `StatDef { key, attribute, per_attribute, released, role }` ([[Stats and Attributes]]) |
| `starting` | starting value per attribute |
| `attribute_limit` | maximum per attribute |
| `release` | has a power release (charge key) |
| `capacity_attribute` | attribute reserved by skills (DBC Mind), −1 = none |
| `tp_attribute` | attribute that raises TP per hit (DBC Mind), −1 = none |
| `passive_defense` | % of released Defense that protects while not blocking (JRMCore StatPasDef 20) |
| `turbo` | has DBC Turbo (R key); needs an energy, `release` and a `speed` statistic. Its name comes from the lang key `turbo.<ns>.<path>` (Qi: "Qi Surge") |
| `order` | position in the creation wizard |

## Natural (Foundation, built in)
JRMCore Pwrtyp 0: "the basic Minecraft interface with nothing additional, except the appearance".
STR / DEX / CON starting at 10, limit 100; Melee Dmg 0.3 / STR, Defense 1.0 / DEX, Health 2.0 / CON; one class
"Unclassed"; no energy, no release, hearts stay. Its statistics act as vanilla attribute modifiers measured from the
starting values, so a fresh Natural character is exactly vanilla and training adds health, damage and armor.

A player **without** a character is also Natural in this sense (plain vanilla, no TP), which answers Dragonblock C+
BUG-050 by design.

## Energy power types
Defined by add-ons: **Qi** in [[Qi|Myriad Realm]]. Dragonblock C+'s Ki would be another one (DBC+ could become a
Foundation add-on later). Qi and Ki are separate systems: each owns its numbers.
