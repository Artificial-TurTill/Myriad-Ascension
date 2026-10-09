---
tags: [index]
---
# Open Points

## Not verified yet
- Graphical client: checked on a virtual display in 0.1.2 (creation, hairstyle manager, sheet, HUD, C / R / X, Qi
  Surge speed); the Skills and Transformations tabs, G / H and Natural characters were not clicked through yet. A
  real screen / GUI scale other than the default was not tried.
- Multiplayer: public look sync to other players, two characters fighting each other.

## Foundation engine
- **Body renderer**: skin, eyes and body type are chosen and saved but not drawn (hair and height are). Needs
  original models / textures (Dragonblock C+'s JBRA assets are JinGames' and are not copied).
- Hair motion while charging / transforming (JRMCore shape change) and an option to require a barber item.
- Transformation visuals: aura while charging, form colour on hair / aura (data is synced in `PublicLook`).
- Energy attacks (`energy_power` statistic has no use yet) and other DBC concepts listed in
  [[DBC+ Extraction Map]] → "Concepts that can still be extracted".
- Alignment changes only by command; events that move it (killing peaceful mobs, …) are not defined.
- Race field for the TP chance (fixed 25 % now).
- Erase / reset from the menu for players (only `/foundation erase` today).
- German / other translations.

## Myriad Realm
- Transformations: none yet by decision; the first one comes from a designed idea ([[Idea Index]]).
- Qi Surge visuals (aura while it runs) and a flight / swoop that it would also speed up (DBC Turbo did).
- Decide which parked alpha-16 ideas come back (IDEA-001 … IDEA-010).
- Balance: the Qi numbers are the JRMCore Ki defaults as a starting point.
