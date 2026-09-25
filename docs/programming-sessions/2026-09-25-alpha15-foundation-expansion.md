# Programming Session Log — 2026-09-25 — Alpha 15 foundation expansion

## Session purpose

This session implements the next foundation pass requested after Alpha 14 runtime testing.

## User-reported issues and requirements

- The Affinities page explanatory text is cut off at the bottom/right of the content area.
- Metal affinity/attunement was observed below zero. Normal cultivation-state values must never diminish below their valid floor unless a future explicit sacrifice mechanic performs that loss.
- Add an administrative/testing command framework capable of changing every exposed cultivator statistic/state, statuses, skills, techniques, affinities, realm state and related test data.
- Add a purification item.
- Implement the full early Body Tempering training foundation rather than reducing the realm to one stance/breathing loop.
- The user continues to own visual/UI art direction; programming should fix containment/behavior without redesigning the supplied UI art.

## Source basis

The implementation will follow the attached `Martial Peak as a Minecraft Cultivation World: Complete Mod Foundation Catalogue`, especially its separation of realm/body/energy/affinity/method/training systems and its Tempered Body multi-vector training model.

The source defines early body training around Strength, Endurance, Toughness, Coordination, Stability, Breath Control, Recovery and Vessel Development; training value should depend on real effort and adaptation rather than event count; and Primordialis Testudo should favor stance, controlled breathing, weighted/resisted movement, striking, defensive stress, swimming, difficult movement, combat and recovery.

## Work started

- Created `alpha.15-dev` from current `main`.
- Began auditing cultivator persistent data, sync payloads, status-screen rendering, affinity mutation paths, alpha commands, items and existing training events.

## Planned work

1. Fix Affinities text containment/wrapping.
2. Enforce non-negative affinity/attunement state at data mutation/deserialization boundaries.
3. Add broad alpha/admin state-edit commands.
4. Add a purification consumable with a concrete current effect.
5. Expand Tempered Body into multi-vector physical training with effort/adaptation/fatigue foundations.
6. Update docs and this session log as implementation proceeds.
7. Run PNG validation + Gradle CI and merge only after success.
