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
0-100 % in 5 % steps (new characters 0 %, after death 0 %). Hold C: +5 % per second; Shift + C: −5 % per second
(both twice as fast while Turbo is on). Released statistics scale with it; high release slows Body / energy
regeneration.

## Turbo (DBC TurboMode; Myriad Realm: Qi Surge)
For power types with `turbo: true` (needs an energy, a release and a `speed` statistic).
- R toggles it. Switching on needs release above 0 % and energy; it switches itself off at 0 % release, without
  energy or Body, on death, dimension change and logout.
- While on (on foot: not flying, gliding, riding, swimming; not in creative): the horizontal motion is replaced by
  `run factor × 0.25` blocks per tick in the input direction (DBC DashKi), costing `(int)(1 + run factor)` energy per
  second. Run factor (JRMCoreH.spdFrm): `(0.5 + 0.5 + (dash + DEX / attribute_limit + 0.5) × release%) × speed`,
  speed = statInc(speed statistic, 100) × 0.01 (race / class % included), DEX / limit is an integer division.
- Charging steps twice as fast; passive energy regeneration pauses (Body and Stamina still regenerate).
- No FOV change and head bobbing capped at the sprint pace while running.
- The sheet's speed row shows the Turbo pace at 100 % release in percent (`(1 + dash + DEX / limit + 0.5) × speed`).
- Adaptation: DBC also ran with sprint; here sprinting stays vanilla, only Turbo runs.

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
