---
tags: [index]
---
# Open Points

## Not verified yet
- Graphical client: creation wizard, character sheet, Skills / Transformations tabs, HUD and keys were compiled but
  not clicked through in a real client (the build host has no display). Server-side behaviour is covered by
  GameTests.
- Multiplayer: public look sync to other players, two characters fighting each other.

## Foundation engine
- **Appearance renderer**: skin, hair style / colour, eyes and body type are chosen and saved but not drawn; only the
  height (scale) is visible. Needs original models / textures (Dragonblock C+'s JBRA assets are JinGames' and are not
  copied).
- Transformation visuals: aura while charging, form colour on hair / aura (data is synced in `PublicLook`).
- Energy attacks (`energy_power` statistic has no use yet) and other DBC concepts listed in
  [[DBC+ Extraction Map]] → "Concepts that can still be extracted".
- Alignment changes only by command; events that move it (killing peaceful mobs, …) are not defined.
- Race field for the TP chance (fixed 25 % now).
- Erase / reset from the menu for players (only `/foundation erase` today).
- German / other translations.

## Myriad Realm
- Qi Surge is an example: replace with the first designed transformation ([[Idea Index]]).
- Decide which parked alpha-16 ideas come back (IDEA-001 … IDEA-010).
- Balance: the Qi numbers are the JRMCore Ki defaults as a starting point.
