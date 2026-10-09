# Programming Session Log — 2026-09-26 — Alpha 16 cautious rebuild

## Reset

The previous Alpha 16 implementation has been fully neutralized by a normal revert commit whose repository tree exactly matches the pre-Alpha-16 Alpha 15 state.

No history was force-rewritten.

Clean base commit:
- 595fa3ea6517d5f2a34ceb0c04eb070aa3051099

This rebuild starts from that clean state on:
- `alpha.16-redo`

## Rebuild discipline

The requested feature set will be rebuilt in isolated phases and validated between phases:

1. **Training weights + dedicated creative tab**
2. **Body Tempering 4-9 + conscious Stage 7-9 gathering + Initial Element recharge/circulation**
3. **Translucent X quick menu and future-setting foundations**
4. **Documentation/versioning/final integration**

Each phase must compile before the next one is treated as trusted.

## User requirements preserved

- Training weights must be deliberately usable and wearable.
- Myriad Ascension items must live in their own creative tab rather than vanilla tabs.
- Tempered Body 4-6 must remain literal physical Body Tempering while perception training is added.
- Tempered Body 7-9 must use G for conscious Qi gathering/storage.
- Storing Qi before Initial Element should feel difficult and exceptional.
- Initial Element unlocks true internal circulation/use and easier active recharge.
- X opens a translucent in-world menu for cultivation methods, technique slots and future settings such as elemental/Yin-Yang scanning and cultivation-level gauging.


## Phase verification

### Phase 1 — training weights and creative tab

PR build 370 passed:
- PNG validation: success
- Java/Gradle build: success
- artifact upload: success

Verified compile-time integration:
- deliberate loose training-weight activation
- zero-protection wearable training weights
- dedicated Myriad Ascension creative tab
- removal of vanilla creative-tab injection

### Phase 2 — Body Tempering and Qi progression

The final Phase 2 head passed PR build 373.

Verified compile-time integration:
- fresh stage-local physical work from Tempered Body Stage 4 onward
- conscious Stage 7-9 G gathering
- no passive Stage 7-9 refill
- no Tempered Body circulation/Burst
- Initial Element+ active recharge and circulation
- Meditation-scaled active recharge

Intermediate builds 371 and 372 failed only because the deliberately split atomic batches temporarily referenced the second batch before it landed. The completed Phase 2 head passed cleanly.

### Phase 3 — translucent X menu

PR build 374 passed the new payload classes and translucent screen in isolation.
PR build 375 passed the fully wired server-authoritative menu.

Verified compile-time integration:
- quick-menu request/action/snapshot payloads
- server-side method and technique cycling
- persisted Resource Scan / Cultivation Gauge switches
- loose-weight quick toggle
- hold-X / hover / release-X interaction surface
- left/right click cycling
- non-pausing transparent world overlay

### Final integration

Alpha version advanced to 0.1.0-alpha.16.
Player-data schema: 14.
Network protocol: 8.

Final PR CI is required after this documentation/version pass before merge.
