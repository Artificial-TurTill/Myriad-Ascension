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

## Implementation update

### Affinity/UI repair

- Expanded the Affinities-page explanatory text allowance from two wrapped lines to three and moved it upward so the sentence is not clipped.
- Confirmed the persistent `AffinityProfile` already clamps every affinity to a minimum of 0.
- Added explicit increase-only and sacrifice APIs around Current Attunement so ordinary progression and deliberate future loss are separate concepts.
- Changed ordinary core-stat `add(...)` to ignore negative additions. Explicit sacrifice/admin setters remain separate.

### Body Tempering implementation

Added persistent Body Tempering development vectors:

- Strength
- Endurance
- Toughness
- Coordination
- Stability
- Breath Control
- Recovery
- Vessel Development
- World Energy Perception

Added persistent adaptation per training activity and upgraded player data to schema version 12.

Implemented runtime stimuli for:

- Foundation Stance
- Controlled Breathing
- ordinary running
- swimming
- climbing
- inventory-carried training weights / weighted movement
- meaningful striking/combat damage
- meaningful incoming damage / defensive tempering
- recovery/rest
- Tempered Body 4-6 World Energy focus
- Tempered Body 7-9 vessel development through natural absorption

Stage progress is now derived from multi-vector development. Primordialis Testudo specifically requires breadth plus Stability/Toughness/Endurance rather than allowing one repeated exercise to carry the entire realm.

### Items

Added `minor_purification_pill`:

- reduces Impurity Load,
- reduces Demonic Qi contamination,
- restores Vessel Purity,
- is not consumed when there is nothing to purify.

Added `basic_training_weight` with inventory-based load for the current alpha foundation.

Both have editable local 16x16 placeholder PNGs.

### Alpha/admin editing

Added `/myriadalpha edit` with direct edit/test access to realm/stage, stats, affinities, skills, Qi, conditions, progress/comprehension, bloodline condition, Body Tempering vectors/adaptation, techniques, cultivation methods and organization standing.

### Documentation/version

- Bumped version/artifact to `0.1.0-alpha.15`.
- Updated Alpha and Asset Guide documentation.

## Validation still pending

- PNG structural validation.
- Java/Gradle compilation.
- Runtime verification of Affinities wrapping, Body Tempering activity detection, item behavior and alpha editor commands.

## CI verification update

Pull request #28 triggered GitHub Actions build run 357.

Results on the implementation head:

- PNG structural validation: **passed**
- Gradle/Java build: **passed**
- Alpha artifact upload: **passed**

The compile pass confirms the NeoForge 1.21.1 event hooks used for combat/defensive Body Tempering, the new persistent schema, item registrations and command tree are syntactically valid.

Runtime verification remains required for:
- Affinities paragraph wrapping on the user's display,
- movement/swimming/climbing/combat training feel and balance,
- inventory-carried training-weight load behavior,
- Minor Purification Pill effects,
- the full `/myriadalpha edit` command tree.

## Controlled impact follow-up

After the first successful compile, the Body Tempering activity set was completed with an explicit controlled falling/impact path:

- added a dedicated `FALLING_IMPACT` adaptation channel,
- added a `LivingFallEvent` hook,
- falls beyond the ordinary safe threshold can develop Toughness and Stability,
- fall stimulus scales with excess distance and then diminishes through adaptation,
- this remains subject to Training Fatigue and the same multi-vector stage requirements.

The follow-up compile/CI run also passed before release finalization.

## Post-merge command parser audit

Before handing Alpha 15 to runtime testing, the new editor command examples were checked against Brigadier parsing. Namespaced Minecraft resource IDs contain a colon (for example `myriad_ascension:test_blade_art`), which is not accepted by Brigadier's unquoted `word()` parser.

The technique/method ID arguments were changed to a greedy string argument and still validated through `ResourceLocation.tryParse(...)`. This allows ordinary namespaced IDs exactly as documented while still rejecting invalid resource locations.

A replacement Alpha 15 artifact will be built from the corrected release head.
