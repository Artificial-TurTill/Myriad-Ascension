---
tags: [foundation, combat]
---
# Combat and Resources

## Resources
- **Body** (`life: "body"` only): the only life. Vanilla hearts are hidden and kept full; at Body 0 the player dies.
- **Energy** (Ki / Qi): spent by forms (and later techniques); refilled by regeneration and meditation.
- **Stamina**: spent by powered punches (`Melee × 0.1`), meditation and forms.

## Regeneration (JRMCoreComTickH, every 100 ticks)
`rate = 1 + max × 0.01 × (100 + class recovery%) / 100`
- Body: `rate × (release < 5: 1, < 10: 0.5, < 15: 0.25, else 0)`, × (1 + recovery skills); only in the base form and
  not within 30 s (600 ticks) after a hit.
- Energy: `rate × 1.5 × (50 − release) × 0.02` (at least 1); same conditions as Body.
- Stamina: `rate × 16`, always.
- Empty food bar: Body loses 5 % of its maximum instead.

## Power release
0-100 % in 5 % steps (new characters 0 %, after death 0 %). Hold G: +5 % per second; Shift + G: −5 % per second.
Released statistics scale with it; high release slows Body / energy regeneration.

## Damage (JRMCoreEH + jrmcDam) for Body characters
1. Vanilla armor / enchantment / effect / absorption reductions are switched off (for Body victims and for hits of
   Body attackers).
2. Attacker with a Body character, melee: + released Melee if Stamina > punch cost (then pays it); every hit on a
   living target can give TP ([[Stats and Attributes]]).
3. Victim with a Body character: JRMCore armor rule (each piece `hit × armor / (armor + 25)`, full armor from 30),
   then `damage = (int)((hit − (defense − defense × pen%)) × (1 − endurance))`, at least 1 and at least
   `hit × pen%`; defense = passive share of released Defense (fall damage: 0); mobs pierce 10 %.
4. Body − damage; vanilla damage is 0 (or lethal at Body 0); regeneration pauses for 30 s.
- `/kill` and the void stay vanilla; a totem leaves at least 1 Body.

Natural characters keep vanilla combat; their statistics act through vanilla attributes (`VanillaBridge`).

## Not extracted yet
Blocking, Ki Protection, Ki Fist, energy attacks (Ki techniques), KO / Friendly Fist, fall immunity skills,
difficulty / death penalties, weights and gravity.
