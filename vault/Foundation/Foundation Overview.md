---
tags: [foundation, architecture]
---
# Foundation Overview

**Ascension Foundation** (`ascension_foundation`) is the character engine of Dragonblock C+ (the JRMCore layer of
the original Dragon Block C, see [[DBC+ Extraction Map]]) as its own mod. Content mods plug into it, the first is
[[Myriad Realm Overview|Myriad Realm]].

## Why a separate engine (the JRMCore idea)
JinRyuu split the original into JRMCore (engine: character, stats, damage, GUI), JBRA (rendering) and DBC (content
for the Ki power type), so one engine could carry several games (DBC, Naruto C, SAO C). The Foundation keeps that
split as two jars:
- **Foundation** = engine: character data, maths, damage, regeneration, TP, creation wizard, sheet, HUD, commands.
  It knows *concepts* (power type, class, race, skill, transformation), never a specific setting.
- **Add-on** (Myriad Realm) = content and setting rules, registered through the API. The Foundation never imports
  add-on code.

## Layers inside the Foundation (package `io.github.artificialturtill.foundation`)
| Package | Role | DBC+ counterpart |
| --- | --- | --- |
| `content` | Records (PowerType, CharacterClass, Race, SkillDef, Transformation, …) and the ARSN loader | the constant tables in `character/Legacy*` |
| `lib.arsn` | Vendored ARSN runtime (Apache 2.0) | `lib/arsn` |
| `api` | `FoundationApi`: add-on registration, loaded content | – (DBC+ is one mod) |
| `character` | `Fighter` (character state), `StatMath`, `CharacterProfile` (attachment), `CharacterService`, `CharacterCombat`, `VanillaBridge` | `FighterState`, `LegacyStats`, `LegacyBaseStats`, `LegacyGameplay`, `LegacyDamage`, `CharacterData`, `CharacterService`, `CharacterCombat` |
| `network` | Owner snapshot, public look, actions, creation, results | `network/*` |
| `client` | Creation wizard, sheet, Skills, Transformations, HUD, keys | `client/CreationScreen`, `CharacterScreen`, `SkillsScreen`, `LegacyHud` |
| `command` | `/foundation` | `/dbcplus` |

## Add-on API
```java
@Mod("my_addon")
public final class MyAddon {
    public MyAddon(IEventBus bus) {
        FoundationApi.addContent(MyAddon.class, "/data/my_addon/arsn/content.arsn");
    }
}
```
- `neoforge.mods.toml` of the add-on: dependency on `ascension_foundation`, `type="required"`, `ordering="AFTER"`.
- The Foundation loads its own `foundation.arsn` and then every registered file during common setup, checks all
  cross references and stops the game with the file name and reason when something is wrong ([[Content Files]]).
- At runtime: `FoundationApi.content()` (all definitions), `CharacterService.fighter(player)` (a player's
  character or null), `Fighter` methods for resources, attributes, skills, forms.

## The player's states
- **No character** (new player): Natural in the JRMCore sense, plain vanilla, no TP. Pressing V opens the creation
  wizard.
- **Character** (created with Accept): race + power type + class + appearance, saved with the player, kept through
  death (`copyOnDeath`).
- **Dormant**: a saved character whose race / power type / class is not installed (add-on removed). It is kept
  untouched and returns when the add-on is installed again.

## Keys
| Key | Function |
| --- | --- |
| V | Character menu (creation wizard without a character) |
| G (hold) | Charge: power release +5 % per second; with a meditation skill also refills energy |
| Shift + G (hold) | Lower the power release 5 % per second |
| R | Transform (ascend to the selected or first available form) |
| Z | Descend to the base form |

All keys can be changed in the controls menu (category "Ascension Foundation").

## Commands (operators)
`/foundation content | info <player> | erase <player> | tp <player> <amount> | attribute <player> <index> <value> |
skill <player> <id> <level> | mastery <player> <form> <value> | release <player> <0-100> | alignment <player> <0-100>
| restore <player>`
