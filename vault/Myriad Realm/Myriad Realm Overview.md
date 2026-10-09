---
tags: [myriad-realm]
---
# Myriad Realm Overview

**Myriad Realm** (`myriad_realm`) is the cultivation add-on: everything it adds to the character system is content
for the [[Foundation Overview|Foundation]], and everything that is only Myriad Realm (cultivation realms,
techniques, sects, world Qi, items …) lives in this mod and talks to the Foundation through its API.

- Requires `ascension_foundation` (same version or newer); without it the game refuses to load.
- Java: `MyriadRealm` registers its content files (`MyriadRealm.CONTENT`).
- Content: [[Qi]] (power type and classes), skills, transformations (`data/myriad_realm/arsn/`).
- Names: `assets/myriad_realm/lang/en_us.json`.
- Tests: `myriad-realm/src/test` (content rules), `myriad-realm/src/gametest` (both mods in a server).

## What exists (0.1.0)
| Kind | Content |
| --- | --- |
| Race | Human (from the Foundation) |
| Power type | Qi |
| Classes | Martial Cultivator, Spirit Cultivator, Body Cultivator |
| Skills | Qi Meditation, Qi Condensation, Endurance |
| Transformations | Qi Surge (example) |

## Next
Ideas are collected in the [[Idea Index]]; the parked alpha-16 designs (realms, Body Tempering, affinities, …) wait
there for a decision.
