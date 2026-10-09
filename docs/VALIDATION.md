# Validation

Target: Minecraft 1.21.1, NeoForge 21.1.252, Java 21.

## 0.1.1-dev (2026-10-09)

- `./gradlew build`: **successful**; unit tests **Foundation 20 / 20** (StatMathTest 7 incl. the JRMCoreH.spdFrm run
  factor with DBC Human values, ContentTest 4, FighterTest 9) and **Myriad Realm 6 / 6** (QiContentTest incl. Qi
  Surge numbers, cost, paused regeneration, and no transformations). Zero failures.
- `./gradlew :myriad-realm:runGameTestServer -PgameTests`: **all 6 required GameTests passed** (new: Qi Surge refused
  at 0 % release, costs `(int)(1 + run factor)` Qi per second through the real tick handler, stops at 0 % release).
  Content log: `1 races, 2 power types, 4 classes, 3 skills, 0 transformations`.

- GitHub CI: run [37918728539](https://github.com/Artificial-TurTill/Myriad-Ascension/actions/runs/37918728539) on
  `97087fa` (0.1.1 code `58f737b` + its vault note, pushed together) completed **successfully** (build, unit tests,
  GameTests, artifact upload).

Not run: graphical client (new keys, X menu, Turbo motion, HUD label), multiplayer.

## 0.1.0-dev (2026-10-09)

Run on the build host (Linux, OpenJDK 21.0.12):

- `./gradlew build`: **successful**. Both mods compile; `ascension_foundation-0.1.0-dev.jar` (Foundation classes,
  ARSN runtime + Apache license, `schema.arsn`, `foundation.arsn`, lang) and `myriad_realm-0.1.0-dev.jar` (Myriad
  Realm class, three `.arsn` files, lang; no Foundation classes). Generated `neoforge.mods.toml` checked: versions
  expanded, Myriad Realm requires `ascension_foundation` `[0.1.0-dev,)`.
- Unit tests (NeoForge unit-test environment): **Foundation 19 / 19 passed** (StatMathTest 6, ContentTest 4,
  FighterTest 9), **Myriad Realm 5 / 5 passed** (QiContentTest). Zero failures, errors or skips.
- `./gradlew :myriad-realm:runGameTestServer -PgameTests`: **all 5 required GameTests passed** (content of both mods,
  Qi Body as the only life incl. death at 0, released Melee added to punches with Stamina cost, Natural vanilla
  attributes + height scale + erase, save / load).
- `./gradlew :myriad-realm:runServer` (both mods, dedicated server): started to "Done"; log
  `Ascension Foundation content: 1 races, 2 power types, 4 classes, 3 skills, 1 transformations` and
  `Myriad Realm registered 3 content files`. The only ERROR lines were the host's missing Mojang key server and the
  flat-world preset of the test `server.properties`; none from the mods. The server was stopped by the test script.

Not run:
- No graphical client session: the screens, HUD, key handling and client payload handling compiled but were not
  exercised on screen (the build host has no display).
- No multiplayer session.
- GitHub CI (observed afterwards): runs [37916494971](https://github.com/Artificial-TurTill/Myriad-Ascension/actions/runs/37916494971)
  (`b54bd4f`) and [37916507882](https://github.com/Artificial-TurTill/Myriad-Ascension/actions/runs/37916507882) (`bf527e6`)
  completed **successfully** (build, unit tests, GameTests, artifact upload).

Reproduce:
```sh
./gradlew build
./gradlew :myriad-realm:runGameTestServer -PgameTests
```
