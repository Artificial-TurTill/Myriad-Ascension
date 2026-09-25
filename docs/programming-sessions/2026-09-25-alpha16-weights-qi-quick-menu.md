# Programming Session Log — 2026-09-25 — Alpha 16 weights, Qi gathering and quick menu

## Session purpose

Expand the current Alpha 15 foundation in four linked areas:

- make training weights active/useable and add wearable training weights,
- move all Myriad Ascension items into a dedicated creative-mode tab instead of vanilla tabs,
- correct Tempered Body 4-9 / Initial Element Qi behavior,
- implement the X quick menu as a translucent in-world control surface based on the supplied DragonBlock C-style screenshot.

## User-directed behavior

### Body Tempering

- Stages 4-6 remain literal Body Tempering: physical training must continue to matter rather than being replaced by perception work.
- Stages 7-9 consciously gather Qi with G.
- Qi accumulation at Tempered Body 7-9 must be difficult and notable; the cultivator is forming/storing Qi in the vessel but cannot yet freely circulate it internally.
- Initial Element unlocks true internal Qi circulation/use and makes active recharging substantially easier.

### Training weights

- The existing Basic Training Weight must become an active/useable training tool rather than merely granting load because it sits in inventory.
- Add wearable training-weight equipment.
- Training load should contribute only while the weight is deliberately used/worn.

### Creative inventory

- Add a dedicated Myriad Ascension creative tab.
- Current mod items should appear there and should no longer be injected into vanilla Food/Combat/Tools tabs.

### X quick menu

Visual reference: supplied screenshot of a translucent DragonBlock C-style menu floating over the world.

The menu should:
- remain see-through and keep the world visible,
- be opened by X,
- provide direct control over cultivation methods and technique slots,
- reserve/implement settings that can expand into active elemental/Yin-Yang resource scanning and entity cultivation-level gauging,
- avoid copying the reference artwork directly; use the screenshot as interaction/layout inspiration.

## Work started

- Created `alpha.16-dev` from the current `main` head.
- Beginning audit of keybindings/networking, training/Qi logic, item/creative registration and technique/method state.

## Validation plan

- PNG asset validation.
- Gradle/Java build.
- Merge only after CI succeeds.
- Runtime verification remains required for screen interaction feel, key behavior and balancing.
