package io.github.artificialturtill.foundation.content;

import io.github.artificialturtill.foundation.lib.arsn.ArsnException;
import io.github.artificialturtill.foundation.lib.arsn.Runtime;
import io.github.artificialturtill.foundation.lib.arsn.UnitNumber;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Reads content files (ARSN) into a {@link ContentSet}. The Foundation schema is put in front of every file, so a
 * content file only holds objects. Every problem is reported with the file name: an ARSN error code for broken
 * syntax / types / constraints, and a plain message for broken cross references (unknown power type, wrong list
 * length, ...). Loading uses class-path resources, not the Minecraft resource manager, so the data layer does not
 * depend on the Minecraft version.
 */
public final class ContentLoader {
    public static final String SCHEMA = "/data/ascension_foundation/arsn/schema.arsn";

    /** One content file: {@code anchor} is a class of the mod that ships the file (its jar is searched). */
    public record Source(Class<?> anchor, String path) {
        public String text() {
            try (InputStream in = anchor.getResourceAsStream(path)) {
                if (in == null) throw new IllegalStateException("Missing content file " + path);
                return new String(in.readAllBytes(), StandardCharsets.UTF_8);
            } catch (IOException e) {
                throw new IllegalStateException("Cannot read content file " + path, e);
            }
        }
    }

    private final Map<String, PowerType> powerTypes = new LinkedHashMap<>();
    private final Map<String, CharacterClass> classes = new LinkedHashMap<>();
    private final Map<String, Race> races = new LinkedHashMap<>();
    private final Map<String, SkillDef> skills = new LinkedHashMap<>();
    private final Map<String, Transformation> transformations = new LinkedHashMap<>();
    private final String schema;

    public ContentLoader() { this(new Source(ContentLoader.class, SCHEMA).text()); }
    public ContentLoader(String schema) { this.schema = schema; }

    public static ContentSet load(List<Source> sources) {
        ContentLoader loader = new ContentLoader();
        for (Source source : sources) loader.add(source.path(), source.text());
        return loader.build();
    }

    /** Parses one file and adds its objects; duplicate ids across files are rejected. */
    public ContentLoader add(String name, String text) {
        Runtime module;
        try {
            // The content header line is optional: the schema already starts with "#! ARSN 0.2".
            module = new Runtime(Map.of()).loads(schema + "\n" + text.replaceFirst("^\\s*#!\\s*ARSN\\s+0\\.2", ""));
        } catch (ArsnException e) {
            throw new IllegalStateException("Invalid content file " + name + ": " + e.getMessage(), e);
        }
        try {
            for (Runtime.ArsnObject o : module.objectsOf("PowerType")) put(powerTypes, powerType(o), PowerType::id, name);
            for (Runtime.ArsnObject o : module.objectsOf("CharacterClass")) put(classes, characterClass(o), CharacterClass::id, name);
            for (Runtime.ArsnObject o : module.objectsOf("Race")) put(races, race(o), Race::id, name);
            for (Runtime.ArsnObject o : module.objectsOf("SkillDef")) put(skills, skill(o), SkillDef::id, name);
            for (Runtime.ArsnObject o : module.objectsOf("Transformation")) put(transformations, transformation(o), Transformation::id, name);
        } catch (RuntimeException e) {
            throw new IllegalStateException("Invalid content file " + name + ": " + e.getMessage(), e);
        }
        return this;
    }

    /** Checks every cross reference and returns the immutable set. */
    public ContentSet build() {
        List<String> errors = new ArrayList<>();
        for (PowerType p : powerTypes.values()) {
            for (StatDef s : p.stats()) if (s.attribute() >= p.attributeCount())
                errors.add("power type " + p.id() + ": statistic " + s.key() + " uses attribute " + s.attribute() + " (only " + p.attributeCount() + ")");
            if (p.capacityAttribute() >= p.attributeCount()) errors.add("power type " + p.id() + ": capacity_attribute out of range");
            if (p.tpAttribute() >= p.attributeCount()) errors.add("power type " + p.id() + ": tp_attribute out of range");
            if (p.bodyLife() && p.stat(StatDef.BODY) == null) errors.add("power type " + p.id() + ": life \"body\" needs a statistic with role body");
            if (p.hasEnergy() && p.stat(StatDef.ENERGY_POOL) == null) errors.add("power type " + p.id() + ": an energy needs a statistic with role energy_pool");
            for (int v : p.starting()) if (v < 1 || v > p.attributeLimit()) errors.add("power type " + p.id() + ": starting attributes must be 1.." + p.attributeLimit());
            if (classes.values().stream().noneMatch(c -> c.powerType().equals(p.id()))) errors.add("power type " + p.id() + " has no class");
            if (p.turbo() && (!p.hasEnergy() || !p.release() || p.stat(StatDef.SPEED) == null))
                errors.add("power type " + p.id() + ": turbo needs an energy, a power release and a statistic with role speed");
        }
        for (CharacterClass c : classes.values()) {
            PowerType p = powerTypes.get(c.powerType());
            if (p == null) errors.add("class " + c.id() + ": unknown power type " + c.powerType());
            else if (c.statBonus().length != p.stats().size()) errors.add("class " + c.id() + ": stat_bonus needs " + p.stats().size() + " entries");
        }
        for (Race r : races.values()) {
            for (String p : r.powerTypes()) if (!powerTypes.containsKey(p)) errors.add("race " + r.id() + ": unknown power type " + p);
            r.starting().forEach((p, values) -> {
                PowerType type = powerTypes.get(p);
                if (type == null) errors.add("race " + r.id() + ": starting values for unknown power type " + p);
                else if (values.length != type.attributeCount()) errors.add("race " + r.id() + ": starting values for " + p + " need " + type.attributeCount() + " entries");
            });
            for (String g : r.genders()) if (!g.equals("male") && !g.equals("female")) errors.add("race " + r.id() + ": unknown gender " + g);
        }
        for (SkillDef s : skills.values()) {
            if (!s.powerType().isEmpty() && !powerTypes.containsKey(s.powerType())) errors.add("skill " + s.id() + ": unknown power type " + s.powerType());
            if (!s.learn().equals(SkillDef.LEARN_SHEET) && !s.learn().equals(SkillDef.LEARN_TEACHER)) errors.add("skill " + s.id() + ": learn must be sheet or teacher");
            if (!List.of("", SkillDef.ENERGY_POOL, SkillDef.MEDITATION, SkillDef.ENDURANCE, SkillDef.RECOVERY, SkillDef.DASH).contains(s.effect()))
                errors.add("skill " + s.id() + ": unknown effect " + s.effect());
        }
        for (Transformation t : transformations.values()) {
            PowerType p = powerTypes.get(t.powerType());
            if (p == null) { errors.add("transformation " + t.id() + ": unknown power type " + t.powerType()); continue; }
            if (t.multipliers().length != p.attributeCount()) errors.add("transformation " + t.id() + ": multipliers need " + p.attributeCount() + " entries");
            if (!t.race().isEmpty() && !races.containsKey(t.race())) errors.add("transformation " + t.id() + ": unknown race " + t.race());
            if (!t.previous().isEmpty() && !transformations.containsKey(t.previous())) errors.add("transformation " + t.id() + ": unknown previous form " + t.previous());
            if (!t.skill().isEmpty() && !skills.containsKey(t.skill())) errors.add("transformation " + t.id() + ": unknown skill " + t.skill());
        }
        if (!errors.isEmpty()) throw new IllegalStateException("Invalid Foundation content:\n - " + String.join("\n - ", errors));
        return new ContentSet(Map.copyOf(powerTypes), Map.copyOf(classes), Map.copyOf(races), Map.copyOf(skills), Map.copyOf(transformations));
    }

    private static <T> void put(Map<String, T> map, T value, java.util.function.Function<T, String> id, String file) {
        String key = id.apply(value);
        if (!key.matches("[a-z0-9_.-]+:[a-z0-9_./-]+")) throw new IllegalArgumentException("id \"" + key + "\" must be namespace:path in lower case");
        if (map.putIfAbsent(key, value) != null) throw new IllegalArgumentException("duplicate id " + key + " (" + file + ")");
    }

    // ---- ARSN object -> record -------------------------------------------------------------------------------
    private static PowerType powerType(Runtime.ArsnObject o) {
        List<AttributeDef> attributes = new ArrayList<>();
        for (Map<String, Object> a : maps(o, "attributes")) attributes.add(new AttributeDef((String) a.get("key"), (String) a.get("short")));
        List<StatDef> stats = new ArrayList<>();
        for (Map<String, Object> s : maps(o, "stats"))
            stats.add(new StatDef((String) s.get("key"), num(s.get("attribute")).intValue(), num(s.get("per_attribute")).floatValue(),
                (Boolean) s.get("released"), (String) s.get("role")));
        return new PowerType(str(o, "id"), str(o, "energy"), str(o, "life").equals("body"), List.copyOf(attributes), List.copyOf(stats),
            ints(o.get("starting")), i(o, "attribute_limit"), (Boolean) o.get("release"), i(o, "capacity_attribute"), i(o, "tp_attribute"),
            i(o, "passive_defense"), (Boolean) o.get("turbo"), i(o, "order"));
    }
    private static CharacterClass characterClass(Runtime.ArsnObject o) {
        return new CharacterClass(str(o, "id"), str(o, "power_type"), ints(o.get("stat_bonus")), ints(o.get("recovery_bonus")), i(o, "order"));
    }
    private static Race race(Runtime.ArsnObject o) {
        Map<String, Integer> bonus = new LinkedHashMap<>();
        for (Map<String, Object> b : maps(o, "stat_bonus")) bonus.merge((String) b.get("stat"), num(b.get("percent")).intValue(), Integer::sum);
        Map<String, int[]> starting = new LinkedHashMap<>();
        for (Map<String, Object> s : maps(o, "starting")) starting.put((String) s.get("power_type"), ints(s.get("values")));
        Object lookValue = o.get("appearance");
        @SuppressWarnings("unchecked") Map<String, Object> look = lookValue instanceof Runtime.ArsnObject a ? a.toMap() : (Map<String, Object>) lookValue;
        AppearanceOptions appearance = new AppearanceOptions(colors(look.get("skin_colors")), num(look.get("hair_styles")).intValue(),
            colors(look.get("hair_colors")), colors(look.get("eye_colors")), num(look.get("body_types")).intValue(),
            num(look.get("min_height")).intValue(), num(look.get("max_height")).intValue());
        @SuppressWarnings("unchecked") List<String> genders = List.copyOf((List<String>) o.get("genders"));
        @SuppressWarnings("unchecked") List<String> powers = List.copyOf((List<String>) o.get("power_types"));
        return new Race(str(o, "id"), genders, powers, Map.copyOf(bonus), Map.copyOf(starting), appearance, i(o, "order"));
    }
    private static SkillDef skill(Runtime.ArsnObject o) {
        int[] tp = ints(o.get("tp_cost")), capacity = ints(o.get("capacity_cost"));
        if (capacity.length == 0) capacity = new int[]{0};
        for (int v : tp) if (v < 0) throw new IllegalArgumentException("skill " + str(o, "id") + ": negative TP cost");
        return new SkillDef(str(o, "id"), str(o, "power_type"), i(o, "max_level"), tp, capacity, str(o, "effect"),
            num(o.get("per_level")).floatValue(), str(o, "learn"), i(o, "order"));
    }
    private static Transformation transformation(Runtime.ArsnObject o) {
        List<?> list = (List<?>) o.get("multipliers");
        float[] multipliers = new float[list.size()];
        for (int k = 0; k < multipliers.length; k++) multipliers[k] = num(list.get(k)).floatValue();
        return new Transformation(str(o, "id"), str(o, "power_type"), str(o, "race"), str(o, "previous"), str(o, "skill"),
            i(o, "skill_level"), multipliers, num(o.get("energy_drain")).floatValue(), num(o.get("stamina_drain")).floatValue(),
            i(o, "min_release"), num(o.get("mastery_gain")).floatValue(), num(o.get("mastery_max")).floatValue(),
            num(o.get("mastery_relief")).floatValue(), color(str(o, "color")), i(o, "order"));
    }

    // ---- field helpers (unit-carrying numbers lose their unit, like ARSN lowering) ----------------------------
    private static Number num(Object v) { return v instanceof UnitNumber n ? n.value() : (Number) v; }
    private static int i(Runtime.ArsnObject o, String field) { return num(o.get(field)).intValue(); }
    private static String str(Runtime.ArsnObject o, String field) { return (String) o.get(field); }
    private static int[] ints(Object list) {
        List<?> l = (List<?>) list;
        int[] out = new int[l.size()];
        for (int k = 0; k < out.length; k++) out[k] = num(l.get(k)).intValue();
        return out;
    }
    @SuppressWarnings("unchecked")
    private static List<Map<String, Object>> maps(Runtime.ArsnObject o, String field) {
        List<Map<String, Object>> out = new ArrayList<>();
        for (Object x : (List<?>) o.get(field)) out.add(x instanceof Runtime.ArsnObject a ? a.toMap() : (Map<String, Object>) x);
        return out;
    }
    private static List<Integer> colors(Object list) {
        List<Integer> out = new ArrayList<>();
        for (Object x : (List<?>) list) out.add(color((String) x));
        return List.copyOf(out);
    }
    /** "#RRGGBB" -> 0xRRGGBB. */
    public static int color(String text) {
        if (text == null || !text.matches("#[0-9A-Fa-f]{6}")) throw new IllegalArgumentException("colour \"" + text + "\" must be #RRGGBB");
        return Integer.parseInt(text.substring(1), 16);
    }
}
