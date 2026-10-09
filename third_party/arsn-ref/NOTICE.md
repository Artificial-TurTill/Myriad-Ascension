# ARSN reference implementation (arsn-ref 0.2.0)

ARSN (Advanced Rich Structure Notation) by DukeOfFlames, Java reference implementation, Apache License 2.0
(see LICENSE). Vendored into `foundation/src/main/java/io/github/artificialturtill/foundation/lib/arsn`, taken from
the Dragonblock C+ copy (which already carries the fixes ARSN-REF-1..5).

Modifications:
- The package `io.arsn` is relocated to `io.github.artificialturtill.foundation.lib.arsn`.
- ARSN-REF-1..5 bug fixes as in Dragonblock C+ (`tools/arsn_patches.py` there).
- ARSN-REF-6 (Foundation): `Runtime.objectsOf(className)` lists every object of a class, so content files can be
  read without knowing object names in advance.
- ARSN-REF-7 (Foundation): negative number literals in values (`stat_bonus: [0, -10, 10]`); the reference parser only
  accepted them inside `[ ... ]` expressions.
