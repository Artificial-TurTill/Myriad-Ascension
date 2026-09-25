# Programming Session Log — 2026-09-25 — Alpha 14 follow-up

## Session purpose

This log records changes made during the programming session immediately following Alpha 13 runtime verification.

## User feedback / decisions

- Alpha 13 repaired the Cultivator Status screen texture loading successfully.
- The user will handle visual/UI artwork edits personally. Programming work should not redesign UI art unless explicitly requested.
- Requested gameplay/UI behavior: the top-left Cultivator HUD (Health / Qi / Power) must be completely invisible while the player is in the Mortal realm.
- Requested development-process change: programming session logs must be committed to GitHub from this point forward and updated during the work rather than reconstructed only afterward.

## Work started

- Created development branch `alpha.14-dev` from current `main`.
- Began auditing `ClientCultivatorHud` and the cultivation realm state used by the client sync payload.

## Planned implementation

1. Gate the complete cultivation HUD behind a non-Mortal cultivation realm.
2. Preserve the existing HUD texture and visual layout unchanged.
3. Update release/version documentation for the next alpha.
4. Run CI, including PNG validation and Gradle build.
5. Update this session log with implementation and verification results.

## Implementation update

- Added the Mortal visibility gate in `ClientCultivatorHud.render(...)`.
- The HUD now returns before drawing when no synchronized cultivator state exists or when `data.realm().isCultivatorRealm()` is false.
- This means the Myriad Ascension Health / Qi / Power panel is fully absent while Mortal.
- Existing HUD artwork, dimensions, bar rendering and colors were left untouched.
- Removed now-unnecessary null fallbacks from Qi/Power calculations after the visibility guard.
- Added `docs/programming-sessions/README.md` to establish the ongoing logging rule.
- Updated the HUD design documentation so Mortal is explicitly separate from Tempered Body HUD behavior.
- Advanced the development version and GitHub Actions artifact name to `0.1.0-alpha.14`.

## Validation still pending

- GitHub Actions PNG validation.
- Gradle build.
- Runtime confirmation that Mortal shows no Myriad Ascension HUD and Tempered Body still shows it.
