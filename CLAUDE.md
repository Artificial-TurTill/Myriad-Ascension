# Myriad Ascension: instructions for coding sessions

Two mods in one Gradle build: `foundation/` (Ascension Foundation, the engine extracted from Dragonblock C+) and
`myriad-realm/` (Myriad Realm, the cultivation add-on that requires the Foundation).

## Rules
- **Layering**: the Foundation never depends on Myriad Realm. Character-system concepts go into the Foundation as
  generic, data-driven features; Myriad Realm adds content (ARSN files) and Myriad-only systems.
- **Content is data**: races, power types, classes, skills, transformations live in `data/<mod>/arsn/*.arsn`.
  Only write Java when an idea needs a mechanic the engine lacks; add it as a generic feature + test.
- **Vault process** (`vault/`, Obsidian), same as Dragonblock C+:
  - user bug report → `vault/Bugs/BUG-0xx.md` from `Templates/Bug.md` + row in `Bugs/Bug Index.md`;
  - user idea → `vault/Ideas/IDEA-0xx <Name>.md` from the matching `Templates/Idea - *.md` + row in `Ideas/Idea Index.md`;
  - every release → `vault/Versions/<version>.md` from `Templates/Version.md` + row in `Versions/Version History.md`,
    update `Home.md` (current version) and `Open Points.md`;
  - reference notes in `vault/Foundation/` and `vault/Myriad Realm/` must match the code (fields, formulas, keys).
- `docs/WORK_LOG.md`: append what was done, decided and validated in each session. `docs/VALIDATION.md`: what was
  run and the results; never claim an unrun check.
- Validate before pushing: `./gradlew build` (unit tests incl. content validation) and, for gameplay changes,
  `./gradlew :myriad-realm:runGameTestServer -PgameTests`. Screen / HUD / input changes are checked in the real client
  on a virtual display with `tools/client-check` (see its README).
- Every release hands the user both jars and the vault zip (`Myriad-Ascension-Vault-<version>.zip`) directly.
- Raise `foundation_protocol` (gradle.properties) when a payload changes, `CharacterProfile.SCHEMA` when the save
  format changes (keep loading old saves).
- Concepts taken from Dragonblock C+ / JRMCore are checked against the DBC+ source; deviations go to
  `vault/Foundation/DBC+ Extraction Map.md` (Adaptations) and the version note.
- No Dragonblock C+ / JinGames assets (textures, models, sounds) in this repository.
