package io.github.artificialturtill.foundation.lib.arsn;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** A loaded ARSN v0.2 document. Runtime functions are immutable load-time dependencies. */
public final class Runtime {
  private static final Object UNSET = new Object();
  private final Map<String, RuntimeFunction> functions;
  private final Map<String, Klass> classes = new LinkedHashMap<>();
  private final Map<String, Map<String, Object>> objects = new LinkedHashMap<>();
  private final Set<String> units = new HashSet<>();

  /**
   * Creates a runtime with its complete function environment. Passing null is rejected so
   * function availability is explicit at load time; use {@code Map.of()} if none are needed.
   */
  public Runtime(Map<String, ? extends RuntimeFunction> runtimeFunctions) {
    if (runtimeFunctions == null) throw new NullPointerException("runtimeFunctions is required; use Map.of() when no functions are needed");
    Map<String, RuntimeFunction> copy = new LinkedHashMap<>();
    runtimeFunctions.forEach((name, fn) -> {
      if (name == null || name.isBlank() || fn == null) throw new IllegalArgumentException("runtime function names and values must be non-null");
      copy.put(name, fn);
    });
    this.functions = Map.copyOf(copy);
  }

  public Runtime loads(String source) {
    Parser p = new Parser(source, units);
    while (!p.eof()) {
      if (p.accept("unit")) { units.add(p.ident()); continue; }
      if (p.peek("@")) { Klass k = p.klass(); if (classes.putIfAbsent(k.name, k) != null) throw new ArsnException.Constraint("duplicate class " + k.name); continue; }
      Decl d = p.declaration();
      if (objects.containsKey(d.name)) throw new ArsnException.Constraint("duplicate object " + d.name);
      Klass k = classes.get(d.type); if (k == null) throw new ArsnException.Reference("unknown class " + d.type);
      objects.put(d.name, materialize(k, d.values, d.name));
    }
    validate();
    return this;
  }

  /** Returns a read-only plain-data projection (unit metadata is intentionally lowered). */
  public Map<String, Object> lower() {
    Map<String,Object> result = new LinkedHashMap<>();
    objects.forEach((name, value) -> result.put(name, lowerValue(value)));
    return Collections.unmodifiableMap(result);
  }
  /** Foundation patch ARSN-REF-6: every object of one class in declaration order (the reference API only reads by name). */
  public List<ArsnObject> objectsOf(String className) {
    List<ArsnObject> out = new ArrayList<>();
    objects.values().forEach(o -> { if (((Klass) o.get("__class")).name().equals(className)) out.add(new ArsnObject(this, o)); });
    return Collections.unmodifiableList(out);
  }
  public ArsnObject get(String name) {
    Map<String,Object> value = objects.get(name);
    if (value == null) throw new ArsnException.Reference("unknown object " + name);
    return new ArsnObject(this, value);
  }
  public Object materialize(String className, Map<String, ?> values, String name) {
    Klass k=classes.get(className); if(k==null) throw new ArsnException.Reference("unknown class "+className);
    Map<String,Object> made=materialize(k,new LinkedHashMap<>(values),name);
    if(name!=null) { if(objects.containsKey(name)) throw new ArsnException.Constraint("duplicate object "+name); objects.put(name,made); validate(); }
    return publicValue(made);
  }

  private Map<String,Object> materialize(Klass k, Map<String,?> source, String name) {
    Map<String,Object> out=new LinkedHashMap<>(); out.put("__class",k); out.put("__name",name);
    for(String field:source.keySet()) if(!k.fields.containsKey(field)) throw new ArsnException.Constraint("unknown field "+field);
    for(Field f:k.fields.values()) {
      if(f.derived != null) continue;
      Object v=source.containsKey(f.name)?source.get(f.name):f.defaultValue;
      if(v==UNSET && !f.optional) throw new ArsnException.Value("required field "+f.name+" is unset");
      out.put(f.name,v);
    }
    for(Field f:k.fields.values()) if(f.derived != null) out.put(f.name, eval(f.derived,out));
    return out;
  }
  void set(Map<String,Object> object, String field, Object value) {
    Klass k=(Klass)object.get("__class"); Field f=k.fields.get(field);
    if(f==null) throw new ArsnException.Constraint("unknown field "+field);
    if(f.derived!=null) throw new ArsnException.Constraint("derived field cannot be assigned");
    Object before=object.get(field); if(f.constant && before!=UNSET && !Objects.equals(before,value)) throw new ArsnException.Constraint("constant field cannot be changed");
    object.put(field,value);
    try { recompute(); validate(); } catch(RuntimeException e) { object.put(field,before); recompute(); throw e; }
  }
  private Object resolveRefs(Object v, Map<String,Object> self) { if(v instanceof Ref r) return eval(r,self); if(v instanceof List<?> l) { List<Object> out=new ArrayList<>(l.size()); for(Object x:l) out.add(resolveRefs(x,self)); return out; } return v; }
  private void recompute() { for(Map<String,Object> o:objects.values()) for(Field f:((Klass)o.get("__class")).fields.values()) if(f.derived!=null) o.put(f.name,eval(f.derived,o)); }
  private void validate() {
    Map<Field,Set<Object>> unique=new IdentityHashMap<>();
    for(Map<String,Object> o:objects.values()) for(Field f:((Klass)o.get("__class")).fields.values()) {
      Object value=o.get(f.name); if(value instanceof Ref || value instanceof List<?>) { value=resolveRefs(value,o); o.put(f.name,value); } if(value==UNSET) continue;
      if(value==null && !f.nilable) throw new ArsnException.Value("field "+f.name+" is not nilable");
      if(value!=null && !matches(value,f.type)) throw new ArsnException.Type("field "+f.name+" has wrong type");
      if(f.check != null && !truth(eval(f.check,o))) throw new ArsnException.Value("evaluation failed for "+f.name);
      if(f.unique && value!=null && !unique.computeIfAbsent(f,k->new HashSet<>()).add(value)) throw new ArsnException.Constraint("duplicate value for unique field "+f.name);
    }
  }
  private boolean matches(Object v, Type t) {
    if(t.unit!=null) { if(!(v instanceof UnitNumber n) || !t.unit.equals(n.unit())) return false; v=n.value(); }
    else if(v instanceof UnitNumber) return false;
    if(t.alternatives!=null) { Object candidate = v; return t.alternatives.stream().anyMatch(x->matches(candidate,x)); }
    return switch(t.name) { case "any"->true; case "int"->v instanceof Integer || v instanceof Long; case "float"->v instanceof Float || v instanceof Double; case "bool"->v instanceof Boolean; case "string"->v instanceof String; case "list"->v instanceof List<?> l && l.stream().allMatch(x->matches(x,t.item)); default -> v instanceof Map<?,?> m && m.get("__class")==classes.get(t.name); };
  }
  private Object eval(Expr e, Map<String,Object> self) {
    if(e instanceof Literal x) return x.value;
    if(e instanceof Ref x) { if(x.name.equals("self")) return self; Object v=self.getOrDefault(x.name,objects.get(x.name)); if(v==null) throw new ArsnException.Reference("unknown symbol "+x.name); return v; }
    if(e instanceof Call x) { Object[] args=x.args.stream().map(a->eval(a,self)).toArray(); if(x.name.equals("get")) { if(args.length!=2 || !(args[0] instanceof List<?> list) || !(args[1] instanceof Number n)) throw new ArsnException.Type("get requires a list and integer"); int index=n.intValue(); if(index<0 || index>=list.size()) throw new ArsnException.Value("list index is out of range"); return list.get(index); } if(x.name.equals("len")) { if(args.length==1 && args[0] instanceof String s) return s.codePointCount(0, s.length()); if(args.length!=1 || !(args[0] instanceof List<?> list)) throw new ArsnException.Type("len requires a list or string"); return list.size(); } RuntimeFunction f=functions.get(x.name); if(f==null) throw new ArsnException.RuntimeFunction("runtime function not found: "+x.name); return f.apply(args); }
    if(e instanceof Unary x) { Object v=eval(x.value,self); if(x.op.equals("!")) return !truth(v); Number n=number(v); return x.op.equals("-")?neg(n):n; }
    Binary x=(Binary)e; Object a=eval(x.left,self), b=eval(x.right,self);
    return switch(x.op) { case "+"->add(a,b); case "-"->sub(a,b); case "*"->mul(a,b); case "/"->div(a,b); case "=="->Objects.equals(a,b); case "!="->!Objects.equals(a,b); case "&&"->truth(a)&&truth(b); case "||"->truth(a)||truth(b); case "<"->compare(a,b)<0; case ">"->compare(a,b)>0; case "<="->compare(a,b)<=0; case ">="->compare(a,b)>=0; default -> throw new ArsnException.Syntax("unsupported operator "+x.op); };
  }
  private static boolean truth(Object o) { if(!(o instanceof Boolean b)) throw new ArsnException.Type("expected boolean"); return b; }
  private static Number number(Object o) { if(o instanceof UnitNumber n)o=n.value(); if(!(o instanceof Number n))throw new ArsnException.Type("arithmetic requires numbers"); return n; }
  private static Object add(Object a,Object b){return arithmetic(a,b,'+');} private static Object sub(Object a,Object b){return arithmetic(a,b,'-');} private static Object mul(Object a,Object b){return arithmetic(a,b,'*');} private static Object div(Object a,Object b){return arithmetic(a,b,'/');}
  private static Object arithmetic(Object a, Object b, char op) {
    UnitNumber ua = a instanceof UnitNumber n ? n : null;
    UnitNumber ub = b instanceof UnitNumber n ? n : null;
    if (ua != null && ub != null && !ua.unit().equals(ub.unit())) throw new ArsnException.Type("unit mismatch");
    Number an = number(a), bn = number(b);
    double x = an.doubleValue(), y = bn.doubleValue();
    if (op == '/' && y == 0) throw new ArsnException.Value("division by zero");
    double result = switch (op) { case '+' -> x + y; case '-' -> x - y; case '*' -> x * y; default -> x / y; };
    Number value; if (an instanceof Integer && bn instanceof Integer && op != '/') value = (int) result; else value = result;
    String unit = ua != null ? ua.unit() : ub != null ? ub.unit() : null;
    return unit == null ? value : new UnitNumber(value, unit);
  }
  private static Number neg(Number n){if(n instanceof Integer) return -n.intValue(); return n.doubleValue() * -1;}
  private static int compare(Object a,Object b){if(a instanceof Number&&b instanceof Number)return Double.compare(((Number)a).doubleValue(),((Number)b).doubleValue());if(a instanceof Comparable c&&a.getClass()==b.getClass())return c.compareTo(b);throw new ArsnException.Type("values are not comparable");}
  Object publicValue(Object x) { if(x instanceof Map<?,?> m) return new ArsnObject(this,(Map<String,Object>)m); if(x instanceof List<?> l)return l.stream().map(this::publicValue).toList(); return x; }
  private Object lowerValue(Object x) { if(x instanceof UnitNumber n)return n.value(); if(x instanceof Map<?,?> m){Map<String,Object> r=new LinkedHashMap<>();m.forEach((k,v)->{if(!k.toString().startsWith("__"))r.put(k.toString(),lowerValue(v));});return r;}if(x instanceof List<?> l)return l.stream().map(this::lowerValue).toList();return x; }

  public static final class ArsnObject { private final Runtime runtime; private final Map<String,Object> values; private ArsnObject(Runtime r,Map<String,Object>v){runtime=r;values=v;} public Object get(String name){if(!values.containsKey(name))throw new ArsnException.Reference("unknown field "+name);return runtime.publicValue(values.get(name));} public void set(String name,Object value){runtime.set(values,name,value);} public String name(){return (String)values.get("__name");} public Map<String,Object> toMap(){return (Map<String,Object>)runtime.lowerValue(values);} }
  private record Klass(String name,Map<String,Field> fields){} private record Field(String name,Type type,boolean optional,boolean constant,boolean unique,boolean nilable,Object defaultValue,Expr derived,Expr check){} private record Type(String name,String unit,Type item,List<Type> alternatives){} private sealed interface Expr permits Literal,Ref,Call,Unary,Binary{} private record Literal(Object value)implements Expr{} private record Ref(String name)implements Expr{} private record Call(String name,List<Expr> args)implements Expr{} private record Unary(String op,Expr value)implements Expr{} private record Binary(String op,Expr left,Expr right)implements Expr{} private record Decl(String type,String name,Map<String,Object> values){}

  private static final class Parser {
    private final List<String> t=new ArrayList<>(); private int i; private final Set<String> units;
    Parser(String s,Set<String> units){this.units=units; String input=s.replaceFirst("^\\s*#!\\s*ARSN\\s+0\\.2", ""); Matcher m=Pattern.compile("\\s*(?:#(?![!]).*?$|([A-Za-z_][A-Za-z0-9_]*|\\d+(?:\\.\\d+)?|\\.\\d+|\\\"(?:\\\\.|[^\\\"])*\\\"|&&|\\|\\||==|!=|<=|>=|[@${}()\\[\\],:?!+*/<>|.-]))",Pattern.MULTILINE).matcher(input);int end=0;while(m.find()){end=m.end();if(m.group(1)!=null)t.add(m.group(1));}if(end<input.length()&&!input.substring(end).trim().isEmpty())throw new ArsnException.Syntax("invalid input");t.add("<eof>");}
    boolean eof(){return peek("<eof>");} boolean peek(String s){return t.get(i).equals(s);} boolean accept(String s){if(peek(s)){i++;return true;}return false;} void need(String s){if(!accept(s))throw new ArsnException.Syntax("expected '"+s+"'");} String ident(){String s=t.get(i++);if(!s.matches("[A-Za-z_][A-Za-z0-9_]*"))throw new ArsnException.Syntax("expected identifier");return s;}
    Klass klass(){need("@");String name=ident();need("{");Map<String,Field> fs=new LinkedHashMap<>();while(!accept("}")){String n=ident();boolean optional=accept("?"),constant=!optional&&accept("!"),unique=false,nilable=false;while(peek("unique")||peek("nil")){if(accept("unique"))unique=true;else{accept("nil");nilable=true;}}boolean derived=accept("derive");Type type=parseType();Expr check=null;if(accept("[")){check=expr();need("]");}Object def=UNSET;Expr deriv=null;if(derived){need(":");deriv=expr();}else if(accept(":"))def=value();if(fs.putIfAbsent(n,new Field(n,type,optional,constant,unique,nilable,def,deriv,check))!=null)throw new ArsnException.Constraint("duplicate field "+n);accept(",");}return new Klass(name,fs);}
    Type parseType(){List<Type>a=new ArrayList<>();a.add(typeAtom());while(accept("|"))a.add(typeAtom());Type r=a.size()==1?a.get(0):new Type("union",null,null,a);if(i<t.size()&&units.contains(t.get(i)))r=new Type(r.name,ident(),r.item,r.alternatives);return r;} Type typeAtom(){if(accept("list")){need("(");Type x=peek(")")?new Type("any",null,null,null):parseType();need(")");return new Type("list",null,x,null);}return new Type(ident(),null,null,null);}
    Decl declaration(){String type=ident(),name=ident();need("{");Map<String,Object>v=new LinkedHashMap<>();while(!accept("}")){String k=ident();need(":");v.put(k,value());accept(",");}return new Decl(type,name,v);}
    Object value(){if(accept("[")){List<Object> r=new ArrayList<>();if(!accept("]")){do{r.add(value());}while(accept(","));need("]");}return r;}if(accept("true"))return true;if(accept("false"))return false;if(accept("nil"))return null;if(accept("-")){Object v=value();if(v instanceof Number n)return neg(n);if(v instanceof UnitNumber u)return new UnitNumber(neg(u.value()),u.unit());throw new ArsnException.Syntax("expected a number after -");}String x=t.get(i++);if(x.startsWith("\""))return x.substring(1,x.length()-1).replace("\\n","\n").replace("\\\"","\"");if(x.matches("(?:\\d+(?:\\.\\d+)?|\\.\\d+)")){Number n; if(x.contains(".")) n=Double.valueOf(x); else n=Integer.valueOf(x);if(i<t.size()&&units.contains(t.get(i)))return new UnitNumber(n,ident());return n;}i--;return new Ref(ident());}
    Expr expr(){return binary(0);} Expr binary(int min){Expr left=unary();String[]ops={"||","&&","==","!=","<",">","<=",">=","+","-","*","/"};int[]prec={1,2,3,3,4,4,4,4,5,5,6,6};for(;;){int n=-1;for(int j=0;j<ops.length;j++)if(peek(ops[j])){n=j;break;}if(n<0||prec[n]<min)return left;String op=t.get(i++);left=new Binary(op,left,binary(prec[n]+1));}}
    Expr unary(){if(accept("!")||accept("-")||accept("+")){String op=t.get(i-1);return new Unary(op,unary());}if(accept("$")){String name=ident();return call(name);}if(peek("get")||peek("len")){String name=ident();return call(name);}if(accept("(")){Expr x=expr();need(")");return x;}Object x=value();return x instanceof Ref r?r:new Literal(x);} Expr call(String name){need("(");List<Expr>a=new ArrayList<>();if(!accept(")")){do{a.add(expr());}while(accept(","));need(")");}return new Call(name,a);}
  }
}
