package io.github.artificialturtill.foundation.lib.arsn;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

/** Entry points for the dependency-free ARSN v0.2 runtime. */
public final class Arsn {
  private Arsn() { }
  /** Parses a document. The functions map is required, even when it is empty. */
  public static Runtime loads(String source, Map<String, ? extends RuntimeFunction> functions) {
    return new Runtime(functions).loads(source);
  }
  /** Parses a document from a UTF-8 file. The functions map is required. */
  public static Runtime load(Path path, Map<String, ? extends RuntimeFunction> functions) {
    try { return new Runtime(functions).loads(Files.readString(path)); }
    catch (IOException e) { throw new ArsnException.Reference("cannot read " + path + ": " + e.getMessage()); }
  }
}
