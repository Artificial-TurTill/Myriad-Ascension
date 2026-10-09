package io.github.artificialturtill.foundation.lib.arsn;

/** Base exception for invalid ARSN documents and values. */
public class ArsnException extends RuntimeException {
  private final String code;
  public ArsnException(String code, String message) { super(code + ": " + message); this.code = code; }
  public String code() { return code; }
  public static final class Syntax extends ArsnException { public Syntax(String m) { super("ARSN-1000", m); } }
  public static final class Reference extends ArsnException { public Reference(String m) { super("ARSN-1001", m); } }
  public static final class Constraint extends ArsnException { public Constraint(String m) { super("ARSN-1002", m); } }
  public static final class Value extends ArsnException { public Value(String m) { super("ARSN-1003", m); } }
  public static final class Type extends ArsnException { public Type(String m) { super("ARSN-1004", m); } }
  public static final class RuntimeFunction extends ArsnException { public RuntimeFunction(String m) { super("ARSN-1010", m); } }
}
