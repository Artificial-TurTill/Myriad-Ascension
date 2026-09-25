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

## Implementation update

### Training weights

- Replaced the passive inventory-only Basic Training Weight behavior with an explicit secured/released state.
- Right-clicking a Basic Training Weight toggles whether loose inventory weights contribute training load.
- Added the same loose-weight toggle to the X quick menu.
- Added zero-protection wearable resistance gear:
  - Training Weight Vest: 25 load
  - Training Weight Leggings: 18 load
  - Training Ankle Weights: 12 load
- Worn pieces contribute load automatically through normal armor equipment slots.
- Added editable item icons and local worn armor-layer PNGs.

### Creative inventory

- Added a dedicated Myriad Ascension creative-mode tab.
- Removed all Myriad Ascension item injection into vanilla Food, Combat and Tools tabs.
- The dedicated tab enumerates all registered mod items so later items automatically appear there.

### Body Tempering 4-9

- Added persistent stage-local physical and energy work to Body Tempering state.
- Every new Tempered Body stage resets stage-local work.
- Stages 4-6 now require both cumulative physical foundation and fresh physical work in the current stage in addition to World Energy perception.
- Stages 7-9 retain fresh physical work requirements.
- Renamed the former Natural Gathering band to Conscious Gathering.

### Qi progression correction

- Removed passive Yuan Qi refill from Tempered Body 7-9.
- G at Tempered Body 7-9 now consciously stores Qi in the vessel at deliberately low rates.
- Successful stored Qi trains Vessel Development.
- Circulation and Burst are forcibly unavailable during Tempered Body.
- Initial Element+ G now both actively recharges the reserve and raises internal circulation.
- Initial Element active recharge is substantially faster and currently scales with Meditation level.
- Network version will advance because of new quick-menu payloads.

### X quick menu

Implemented a transparent 3x3 in-world quick menu based on the interaction/layout concept in the supplied DragonBlock C screenshot without copying its artwork.

Cells:
- Cultivation Method
- Cultivation Technique
- Weapon Technique
- Footwork Technique
- MORE / future controls
- Eyesight Technique
- Element / Yin-Yang Scan
- Cultivation Gauge
- Loose Training Weights

Interaction:
- X requests server-authoritative menu state.
- Holding X and releasing it over a tile activates/cycles the tile.
- Left click cycles forward/toggles.
- Right click cycles backward.
- No menu background blur is rendered.
- Methods and equipped techniques are changed on the server.
- Resource Scan and Cultivation Gauge are persisted switches for future scanner implementations; they do not yet reveal blocks/entities.

### Persistent state

Player-data schema advanced to 13 for:
- loose training-weight activation,
- future resource-scanning toggle,
- future cultivation-gauge toggle,
- stage-local Body Tempering work persisted inside BodyTempering state.

### Development commands

Extended `/myriadalpha edit` with:
- `training stage_work <physical> <energy>`
- `utility loose_weights <true|false>`
- `utility resource_scan <true|false>`
- `utility cultivation_gauge <true|false>`

## Validation pending

- PNG structural validation.
- Gradle/Java compile.
- Runtime testing of creative tab placement, wearable armor rendering, weight activation, G behavior and X-menu hold/release interaction.

## First Alpha.16 CI result

Pull-request run 365 validated all 29 packaged PNG files successfully, then failed Java compilation on one networking contract omission:

- `QuickMenuSnapshotPayload` implemented `CustomPacketPayload` but lacked its required `type()` override.

The payload now returns its registered `TYPE` exactly like the other custom payloads. No PNG or resource failure was involved. A new CI build is being used to expose any further compile-time integration errors before merge.
