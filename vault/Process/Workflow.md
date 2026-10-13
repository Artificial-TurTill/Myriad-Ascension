---
tags: [process]
---
# Workflow

The same process as Dragonblock C+, extended by the idea pipeline and the rambling notes. Unlike Dragonblock C+ there
is little to no material to port: Myriad Realm is mostly **developed from scratch**, so design notes (rambling →
idea → designed) come before code.

- **Every user report** gets a note in `Bugs/` (template [[Templates/Bug]]) with the version it was reported in and a
  row in [[Bug Index]]. When it is fixed, set `status: fixed` and `fixed_in`, and link it from the version note.
- **Every idea** (a transformation, skill, race, class, power type or system for Myriad Realm) gets a note in
  `Ideas/` from the matching idea template and a row in [[Idea Index]]. Status: `idea` → `designed` → `implemented`
  (`in` = the version) or `parked` / `rejected`. See [[Adding an Idea]] for how a designed idea becomes game content.
- **Every rambling session** (unsorted thoughts of the user) is written down in `Myriad Realm/Rambling/` (template
  [[Templates/Rambling]], row in [[Rambling]]) with the user's points, the current state of the code for context and
  the open design questions. Rambling is **not an order**: nothing is implemented until the user asks for it; then it
  becomes an idea in [[Idea Index]] that links back to the rambling note.
- **Every release** gets a version note (template [[Templates/Version]]) and a row in [[Version History]], and both
  jars are named with the version (`ascension_foundation-x.y.z.jar`, `myriad_realm-x.y.z.jar`).
- **Downloads**: every GitHub build artifact (`myriad-ascension-<version>`) holds both jars and
  `Myriad-Ascension-Vault.zip` (this vault, open the unzipped folder in Obsidian). The coding session also hands the
  two jars and the vault zip over directly with each release.
- **Layer rule**: anything that is the character system itself belongs to the **Foundation**; anything that is
  Myriad Realm (Qi, cultivation, its lore and content) belongs to **Myriad Realm** and plugs into the Foundation
  (content files or the `FoundationApi`). The Foundation never depends on Myriad Realm.
- **Content first**: races, power types, classes, skills and transformations are data ([[Content Files]]); new Java
  is only written when an idea needs a mechanic the engine does not have yet (then it becomes an engine feature
  with a test, and the content uses it).
- The coding process is logged in `docs/WORK_LOG.md` of the repository; validation results in `docs/VALIDATION.md`.
  The vault links and summarises them.
- Before a change of a concept taken from Dragonblock C+ / JRMCore, check it against the DBC+ source
  (`Artificial-TurTill/Dragonblock-C-`, branch with the port); deviations are written down as "Adaptation" in the
  version note and in [[DBC+ Extraction Map]].
- Every change is validated before it is pushed: `./gradlew build` (compiles both mods and runs the unit tests,
  including the content validation of every ARSN file).

## Versions and channels
- `x.y.z-dev`: development builds (both mods share the version for now; they may diverge later).
- Network protocol: `foundation_protocol` in `gradle.properties`; raise it whenever a payload changes (client and
  server must match).
- Save schema: `CharacterProfile.SCHEMA`; raise it when the saved character format changes and keep loading older
  saves.
