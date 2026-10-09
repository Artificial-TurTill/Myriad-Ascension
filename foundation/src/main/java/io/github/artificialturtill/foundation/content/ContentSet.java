package io.github.artificialturtill.foundation.content;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/** Every loaded race, power type, class, skill and transformation, by id (sorted by their {@code order}). */
public record ContentSet(Map<String, PowerType> powerTypes, Map<String, CharacterClass> classes, Map<String, Race> races,
                         Map<String, SkillDef> skills, Map<String, Transformation> transformations) {
    public static final ContentSet EMPTY = new ContentSet(Map.of(), Map.of(), Map.of(), Map.of(), Map.of());

    public PowerType powerType(String id) { return powerTypes.get(id); }
    public CharacterClass characterClass(String id) { return classes.get(id); }
    public Race race(String id) { return races.get(id); }
    public SkillDef skill(String id) { return skills.get(id); }
    public Transformation transformation(String id) { return transformations.get(id); }

    public List<PowerType> powerTypeList() { return sorted(powerTypes.values(), Comparator.comparingInt(PowerType::order)); }
    public List<Race> raceList() { return sorted(races.values(), Comparator.comparingInt(Race::order)); }
    public List<CharacterClass> classesOf(String powerType) {
        return sorted(classes.values().stream().filter(c -> c.powerType().equals(powerType)).toList(), Comparator.comparingInt(CharacterClass::order));
    }
    public List<SkillDef> skillsOf(String powerType) {
        return sorted(skills.values().stream().filter(s -> s.availableTo(powerType)).toList(), Comparator.comparingInt(SkillDef::order));
    }
    /** Transformations of a power type that the race may use, in order. */
    public List<Transformation> transformationsOf(String powerType, String race) {
        return sorted(transformations.values().stream().filter(t -> t.powerType().equals(powerType)
            && (t.race().isEmpty() || t.race().equals(race))).toList(), Comparator.comparingInt(Transformation::order));
    }
    private static <T> List<T> sorted(java.util.Collection<T> values, Comparator<T> order) {
        List<T> list = new ArrayList<>(values);
        list.sort(order);
        return List.copyOf(list);
    }
}
