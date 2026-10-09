package io.github.artificialturtill.foundation.character;

import io.github.artificialturtill.foundation.content.CharacterClass;
import io.github.artificialturtill.foundation.content.ContentSet;
import io.github.artificialturtill.foundation.content.PowerType;
import io.github.artificialturtill.foundation.content.Race;
import io.github.artificialturtill.foundation.content.SkillDef;
import io.github.artificialturtill.foundation.content.StatDef;
import io.github.artificialturtill.foundation.content.Transformation;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

/**
 * A created character (Dragonblock C+ FighterState, JRMCore jrmc* data): race, power type, class, appearance,
 * attributes, Training Points, Body / energy / Stamina, power release, skills, the active transformation and its
 * mastery, alignment. Server-owned; every rule reads the race / power type / class definitions it was bound to.
 * Methods that the player can trigger throw {@link IllegalArgumentException} with the message shown to the player.
 */
public final class Fighter {
    public static final int MAX_RELEASE = 100, RELEASE_STEP = 5, HURT_PAUSE_TICKS = 600, START_ALIGNMENT = 67;

    private final PowerType power;
    private final Race race;
    private final CharacterClass characterClass;
    private Appearance appearance;
    private final int[] attributes;
    private int tp;
    private final Map<String, Integer> skills = new LinkedHashMap<>();
    private int body, energy, stamina;
    /** jrmcRelease: a new character starts at 0 %. Power types without release always count as 100 %. */
    private int release;
    /** JRMCoreH.a1t3: ticks without Body / energy regeneration after being hurt. */
    private int recoveryDelay;
    /** jrmcAlign: 0-100 % good, new characters 67. */
    private int alignment = START_ALIGNMENT;
    /** Active transformation id ("" = base) and the one the transform key aims for ("" = the first reachable). */
    private String form = "", selectedForm = "";
    private final Map<String, Double> mastery = new LinkedHashMap<>();

    public record Caps(int body, int energy, int stamina) {}

    private Fighter(PowerType power, Race race, CharacterClass characterClass, Appearance appearance, int[] attributes) {
        this.power = power; this.race = race; this.characterClass = characterClass; this.appearance = appearance;
        this.attributes = attributes.clone();
    }

    /** Character creation (JRMCorePacHanS Char accept): validated identity, starting attributes, full resources. */
    public static Fighter create(ContentSet content, String raceId, String powerId, String classId, Appearance look) {
        Race race = content.race(raceId);
        PowerType power = content.powerType(powerId);
        CharacterClass cls = content.characterClass(classId);
        if (race == null) throw new IllegalArgumentException("Unknown race.");
        if (power == null) throw new IllegalArgumentException("Unknown power type.");
        if (cls == null || !cls.powerType().equals(power.id())) throw new IllegalArgumentException("That class does not belong to this power type.");
        if (!race.allows(power.id())) throw new IllegalArgumentException("This race cannot use that power type.");
        Fighter f = new Fighter(power, race, cls, look.validate(race), startingAttributes(race, power));
        f.bindSkills(content);
        f.bindForms(content);
        f.restoreResources();
        return f;
    }
    public static int[] startingAttributes(Race race, PowerType power) {
        return race.starting().getOrDefault(power.id(), power.starting()).clone();
    }

    // ---- identity ---------------------------------------------------------------------------------------------
    public PowerType powerType() { return power; }
    public Race race() { return race; }
    public CharacterClass characterClass() { return characterClass; }
    public Appearance appearance() { return appearance; }
    public void setAppearance(Appearance look) { appearance = look.validate(race); }
    public int[] startingAttributes() { return startingAttributes(race, power); }

    // ---- attributes and statistics ----------------------------------------------------------------------------
    public int attribute(int index) { return attributes[index]; }
    public int[] attributes() { return attributes.clone(); }
    /** The attribute after the transformation multiplier (getPlayerAttribute in a form). */
    public int formAttribute(int index) {
        Transformation t = activeForm();
        if (t == null) return attributes[index];
        return (int) Math.min(Integer.MAX_VALUE, (double) attributes[index] * t.multiplier(index));
    }
    /** Race % + class % of one statistic. */
    public int statBonus(int statIndex) {
        return race.statBonus(power.stats().get(statIndex).key()) + characterClass.statBonus(statIndex);
    }
    /** Skill bonus multiplier of a statistic (energy_pool skills raise the energy pool, like Ki Boost). */
    public float skillBonus(int statIndex) {
        StatDef def = power.stats().get(statIndex);
        return def.role().equals(StatDef.ENERGY_POOL) ? (float) skillEffect(SkillDef.ENERGY_POOL) : 0F;
    }
    /** The statistic from the base attributes. */
    public int stat(int statIndex) {
        StatDef def = power.stats().get(statIndex);
        return StatMath.stat(def, attributes[def.attribute()], statBonus(statIndex), skillBonus(statIndex));
    }
    /** The statistic at 100 % release, with the transformation for released statistics. */
    public int fullStat(int statIndex) {
        StatDef def = power.stats().get(statIndex);
        int attribute = def.released() ? formAttribute(def.attribute()) : attributes[def.attribute()];
        return StatMath.stat(def, attribute, statBonus(statIndex), skillBonus(statIndex));
    }
    /** What the statistic currently gives: released statistics are scaled by the release. */
    public int output(int statIndex) {
        int full = fullStat(statIndex);
        return power.stats().get(statIndex).released() ? StatMath.released(full, release()) : full;
    }
    /** Output of the first statistic with that role, 0 if the power type has none. */
    public int outputOf(String role) { int i = power.statIndex(role); return i < 0 ? 0 : output(i); }
    public int statOf(String role) { int i = power.statIndex(role); return i < 0 ? 0 : stat(i); }

    public Caps caps() {
        return new Caps(power.bodyLife() ? Math.max(1, statOf(StatDef.BODY)) : 0, power.hasEnergy() ? statOf(StatDef.ENERGY_POOL) : 0,
            statOf(StatDef.STAMINA));
    }
    public int level() { return StatMath.level(attributes, startingAttributes()); }
    public int upgradeCost(int multiplierIndex) { return StatMath.upgradeCost(attributes, multiplierIndex, power.attributeLimit()); }

    /** JRMCorePacHanS.handleUpgrade: buys 1/10/100/1000 points of one attribute for TP. */
    public void upgrade(int attribute, int multiplierIndex) {
        if (attribute < 0 || attribute >= attributes.length) throw new IllegalArgumentException("Unknown attribute.");
        if (multiplierIndex < 0 || multiplierIndex > 3) throw new IllegalArgumentException("Unknown upgrade multiplier.");
        int amount = StatMath.upgradeMultiplier(multiplierIndex);
        if ((long) attributes[attribute] + amount > power.attributeLimit())
            throw new IllegalArgumentException("This attribute has reached its limit (" + power.attributeLimit() + ").");
        int cost = upgradeCost(multiplierIndex);
        if (cost <= 0) throw new IllegalArgumentException("Limit reached.");
        if (tp < cost) throw new IllegalArgumentException("Not enough TP: this upgrade costs " + cost + ".");
        Caps old = caps();
        tp -= cost;
        attributes[attribute] += amount;
        grow(old);
    }
    /** Operator command: set an attribute directly. */
    public void setAttribute(int attribute, int value) {
        if (attribute < 0 || attribute >= attributes.length) throw new IllegalArgumentException("Unknown attribute.");
        Caps old = caps();
        attributes[attribute] = Math.clamp(value, 1, power.attributeLimit());
        grow(old);
    }
    /** Grown maxima are added to the current resources (Foundation adaptation: JRMCore added one point's increase). */
    private void grow(Caps old) {
        Caps now = caps();
        setResources(body + Math.max(0, now.body() - old.body()), energy + Math.max(0, now.energy() - old.energy()),
            stamina + Math.max(0, now.stamina() - old.stamina()));
    }

    // ---- Training Points ----------------------------------------------------------------------------------------
    public int tp() { return tp; }
    public void addTp(int amount) {
        if (amount < 0) throw new IllegalArgumentException("TP must be positive.");
        tp = (int) Math.min(Integer.MAX_VALUE, (long) tp + amount);
    }
    public void setTp(int value) { tp = Math.max(0, value); }
    /** JRMCoreH.jrmcExp TP value of a powered hit: 2 x (1 + capacity attribute / 5 x release %). */
    public int hitTpValue() {
        int attr = power.tpAttribute() >= 0 ? attributes[power.tpAttribute()] : 0;
        return (int) Math.min(Integer.MAX_VALUE, 2L * (1L + (long) (attr / 5F * release() * 0.01F)));
    }
    /** A powered hit on a living target: TP with the race chance once the release is at least 5 %. */
    public int rewardHit(Random random) {
        if (power.release() && release < 5) return 0;
        if (random.nextInt(100) >= StatMath.TP_CHANCE) return 0;
        int value = hitTpValue();
        addTp(value);
        return value;
    }

    // ---- resources ----------------------------------------------------------------------------------------------
    public int body() { return body; }
    public int energy() { return energy; }
    public int stamina() { return stamina; }
    public void setResources(int body, int energy, int stamina) {
        Caps c = caps();
        this.body = Math.clamp(body, 0, c.body());
        this.energy = Math.clamp(energy, 0, c.energy());
        this.stamina = Math.clamp(stamina, 0, c.stamina());
    }
    public void restoreResources() { Caps c = caps(); setResources(c.body(), c.energy(), c.stamina()); }
    public void markHurt() { recoveryDelay = HURT_PAUSE_TICKS; }
    public void tickRecoveryDelay() { if (recoveryDelay > 0) recoveryDelay--; }
    public int recoveryDelay() { return recoveryDelay; }
    /** After death: full resources, release 0, base form (JRMCore respawn). */
    public void resetAfterDeath() { form = ""; restoreResources(); release = 0; recoveryDelay = 0; }

    /**
     * JRMCoreComTickH regeneration step (every 100 ticks). With an empty food bar Body drains 5 % instead. Body and
     * energy only regenerate in the base form and without the hurt pause.
     */
    public void recover(boolean fed) { recover(fed, false); }
    /** {@code energyPaused}: Turbo pauses passive energy regeneration (JRMCoreComTickH, status effect 3). */
    public void recover(boolean fed, boolean energyPaused) {
        Caps c = caps();
        if (!fed) { if (power.bodyLife() && body > 0) body = Math.max(0, body - (int) (c.body() * 0.05D)); return; }
        if (power.bodyLife() && body == 0) return;
        if (recoveryDelay == 0 && form.isEmpty()) {
            double bodyFactor = 1D + skillEffect(SkillDef.RECOVERY);
            body = addCapped(body, (int) (StatMath.recovery(c.body(), characterClass.recoveryBonus(0), 0, release()) * bodyFactor), c.body());
            if (!energyPaused) energy = addCapped(energy, StatMath.recovery(c.energy(), characterClass.recoveryBonus(1), 1, release()), c.energy());
        }
        stamina = addCapped(stamina, StatMath.recovery(c.stamina(), characterClass.recoveryBonus(2), 2, release()), c.stamina());
    }
    private static int addCapped(int value, int amount, int maximum) { return (int) Math.clamp((long) value + amount, 0L, maximum); }

    /** Melee stamina cost (cst = Melee x 0.1); a punch is only powered while Stamina is above it. */
    public int punchCost() { return (int) ((float) statOf(StatDef.MELEE) * 0.1F); }
    public boolean canPowerPunch() { return stamina > punchCost(); }
    public void spendPunch() { stamina = Math.max(0, stamina - punchCost()); }

    // ---- power release ------------------------------------------------------------------------------------------
    public int release() { return power.release() ? release : MAX_RELEASE; }
    public void setRelease(int value) { if (power.release()) release = Math.clamp(value, 0, MAX_RELEASE); }
    public void changeRelease(int direction) {
        if (!power.release()) throw new IllegalArgumentException("This power type has no power release.");
        release = Math.clamp(release + Integer.signum(direction) * RELEASE_STEP, 0, MAX_RELEASE);
    }
    /**
     * One second of the charge key (DBC handleRls + Meditation): the release rises (or falls while lowering), and
     * meditation skills refill energy for Stamina.
     */
    public void chargeSecond(boolean lowering) {
        if (!power.release()) return;
        if (power.bodyLife() && body <= 0) return;
        changeRelease(lowering ? -1 : 1);
        if (lowering) return;
        double meditation = skillEffect(SkillDef.MEDITATION);
        Caps c = caps();
        if (meditation <= 0 || energy >= c.energy()) return;
        int cost = (int) Math.ceil(c.stamina() * 0.01D);
        if (stamina < cost) return;
        int restored = (int) Math.ceil(c.energy() * meditation * 0.01D);
        stamina -= cost;
        energy = addCapped(energy, restored, c.energy());
    }

    // ---- Turbo (DBC TurboMode; Myriad Realm: Qi Surge) ------------------------------------------------------------
    /** statInc(SPEED, 100) x 0.01 of the power type's speed statistic (race / class bonuses included), 0 without one. */
    public float speed() {
        int index = power.statIndex(StatDef.SPEED);
        if (index < 0) return 0F;
        return StatMath.statInc(power.stats().get(index), 100, statBonus(index), 0F) * 0.01F;
    }
    private int speedAttribute() {
        StatDef def = power.stat(StatDef.SPEED);
        return def == null ? 0 : attributes[def.attribute()];
    }
    /** The run factor at the current release (DashKi "add"); the motion per tick is factor x 0.25 blocks. */
    public float runFactor(boolean turbo) {
        return StatMath.runFactor(speed(), skillEffect(SkillDef.DASH), speedAttribute(), power.attributeLimit(), release(), turbo);
    }
    /** Energy per second while Turbo runs: (int)(1 + run factor). */
    public int turboCost() { return StatMath.turboCost(runFactor(true)); }
    /** The sheet's speed row (JRMCoreH.spdFrm at 100 % release with Turbo), in percent of the normal pace. */
    public int turboSpeedPercent() {
        float prc = 1F + ((float) skillEffect(SkillDef.DASH) + (float) (speedAttribute() / Math.max(1, power.attributeLimit())) + 0.5F);
        return (int) (prc * speed() * 100F);
    }
    /** Why Turbo cannot be switched on now, or null (TurboMode: release above 0 % and energy left). */
    public String turboBlocker() {
        if (!power.turbo()) return "This power type has no Turbo.";
        if (power.bodyLife() && body <= 0) return "Cannot use Turbo now.";
        if (release() <= 0) return "Needs more than 0 % release.";
        if (energy <= 0) return "Needs energy.";
        return null;
    }
    /** DBCreleaseZeroTurnOffTurbo / TurboMode: Turbo stops at 0 % release, without energy or Body. */
    public boolean turboMustStop() { return turboBlocker() != null; }
    /** One second of Turbo: pays the run cost; false (and nothing paid) when the energy is not enough. */
    public boolean spendTurboSecond() {
        int cost = turboCost();
        if (energy < cost) return false;
        energy -= cost;
        return true;
    }

    // ---- skills -------------------------------------------------------------------------------------------------
    public int skill(String id) { return skills.getOrDefault(id, 0); }
    public Map<String, Integer> skills() { return Collections.unmodifiableMap(skills); }
    /** Sum of per_level x level over the learned skills with that effect. */
    public double skillEffect(String effect) {
        double sum = 0;
        for (Map.Entry<String, Integer> e : skills.entrySet()) {
            SkillDef def = skillDefs.get(e.getKey());
            if (def != null && def.effect().equals(effect)) sum += (double) def.perLevel() * e.getValue();
        }
        return sum;
    }
    /** Skill definitions known when the character was bound (content of the running game). */
    private Map<String, SkillDef> skillDefs = Map.of();
    public void bindSkills(ContentSet content) { skillDefs = content.skills(); }
    public int reservedCapacity() {
        int sum = 0;
        for (Map.Entry<String, Integer> e : skills.entrySet()) {
            SkillDef def = skillDefs.get(e.getKey());
            if (def != null) sum += def.reservedCapacity(e.getValue());
        }
        return sum;
    }
    public int capacity() { return power.capacityAttribute() < 0 ? Integer.MAX_VALUE : attributes[power.capacityAttribute()]; }
    /** handleSkill: one level up for TP, if the capacity attribute (DBC Mind) has room. */
    public void learnSkill(String id, boolean teacher) {
        SkillDef def = skillDefs.get(id);
        if (def == null || !def.availableTo(power.id())) throw new IllegalArgumentException("Unknown skill.");
        if (!teacher && def.learn().equals(SkillDef.LEARN_TEACHER) && skill(id) == 0)
            throw new IllegalArgumentException("A teacher has to teach this skill first.");
        if (power.bodyLife() && body <= 0) throw new IllegalArgumentException("Cannot learn while knocked out or dead.");
        int level = skill(id);
        if (level >= def.maxLevel()) throw new IllegalArgumentException("Skill is already at maximum level.");
        int cost = def.tpCost(level);
        if (tp < cost) throw new IllegalArgumentException("Not enough TP: requires " + cost + ".");
        if ((long) reservedCapacity() + def.capacityCost(level) > capacity())
            throw new IllegalArgumentException("Not enough unreserved " + capacityName() + ". Train it first.");
        Caps old = caps();
        tp -= cost;
        skills.put(id, level + 1);
        grow(old);
    }
    /** handleStats3 op 2: deleted without a TP refund; the reservation is freed. */
    public void forgetSkill(String id) {
        if (skill(id) == 0) throw new IllegalArgumentException("That skill is not learned.");
        skills.remove(id);
        setResources(body, energy, stamina);
    }
    /** Operator / teacher: set a level directly (0 removes). */
    public void setSkill(String id, int level) {
        SkillDef def = skillDefs.get(id);
        if (def == null || !def.availableTo(power.id())) throw new IllegalArgumentException("Unknown skill for this power type.");
        if (level < 0 || level > def.maxLevel()) throw new IllegalArgumentException("Level must be 0-" + def.maxLevel() + ".");
        Caps old = caps();
        if (level == 0) skills.remove(id); else skills.put(id, level);
        grow(old);
        setResources(body, energy, stamina);
    }
    private String capacityName() { return power.capacityAttribute() < 0 ? "capacity" : power.attributes().get(power.capacityAttribute()).shortName(); }

    // ---- transformations ----------------------------------------------------------------------------------------
    private Map<String, Transformation> formDefs = Map.of();
    private List<Transformation> forms = List.of();
    public void bindForms(ContentSet content) {
        formDefs = content.transformations();
        forms = content.transformationsOf(power.id(), race.id());
        if (!form.isEmpty() && !formDefs.containsKey(form)) form = "";
        if (!selectedForm.isEmpty() && !formDefs.containsKey(selectedForm)) selectedForm = "";
    }
    public List<Transformation> availableForms() { return forms; }
    public String form() { return form; }
    public Transformation activeForm() { return form.isEmpty() ? null : formDefs.get(form); }
    public boolean inBaseForm() { return form.isEmpty(); }
    public String selectedForm() { return selectedForm; }
    public void selectForm(String id) {
        if (!id.isEmpty() && forms.stream().noneMatch(t -> t.id().equals(id))) throw new IllegalArgumentException("Unknown transformation.");
        selectedForm = id;
    }
    public double mastery(String id) { return mastery.getOrDefault(id, 0D); }
    public void setMastery(String id, double value) {
        Transformation t = formDefs.get(id);
        if (t == null) throw new IllegalArgumentException("Unknown transformation.");
        mastery.put(id, Math.clamp(value, 0D, t.masteryMax()));
    }
    /** Why a form cannot be entered now, or null if it can (from the current form). */
    public String blocker(Transformation t) {
        if (!t.previous().equals(form)) return t.previous().isEmpty() ? "Return to the base form first." : "Needs the previous form.";
        if (!t.skill().isEmpty() && skill(t.skill()) < Math.max(1, t.skillLevel())) return "Needs the required skill.";
        if (release() < t.minRelease()) return "Needs at least " + t.minRelease() + " % release.";
        if (power.hasEnergy() && energy <= 0) return "No energy left.";
        if (power.bodyLife() && body <= 0) return "Cannot transform now.";
        return null;
    }
    /** DBC ascend: the selected form if it is the next step, otherwise the first form reachable from here. */
    public Transformation ascend() {
        Transformation target = null;
        if (!selectedForm.isEmpty()) {
            Transformation t = formDefs.get(selectedForm);
            if (t != null && t.previous().equals(form)) target = t;
        }
        if (target == null) for (Transformation t : forms) if (t.previous().equals(form) && blocker(t) == null) { target = t; break; }
        if (target == null) {
            for (Transformation t : forms) if (t.previous().equals(form)) throw new IllegalArgumentException(blocker(t));
            throw new IllegalArgumentException("No transformation is available.");
        }
        String reason = blocker(target);
        if (reason != null) throw new IllegalArgumentException(reason);
        form = target.id();
        return target;
    }
    /** DBC descend: back to the base form. */
    public boolean descend() {
        if (form.isEmpty()) return false;
        form = "";
        setResources(body, energy, stamina);
        return true;
    }
    /**
     * One second in a form: energy / stamina drain (reduced by mastery), mastery gain; drops to the base form when
     * the energy runs out or the release falls below the form's minimum. Returns true when it dropped.
     */
    public boolean tickFormSecond() {
        Transformation t = activeForm();
        if (t == null) return false;
        Caps c = caps();
        double factor = t.drainFactor(mastery(t.id()));
        int energyCost = (int) Math.ceil(c.energy() * t.energyDrain() * 0.01D * factor);
        int staminaCost = (int) Math.ceil(c.stamina() * t.staminaDrain() * 0.01D * factor);
        if (release() < t.minRelease() || power.hasEnergy() && energy < energyCost || stamina < staminaCost) { descend(); return true; }
        energy -= energyCost;
        stamina -= staminaCost;
        mastery.put(t.id(), Math.min(t.masteryMax(), mastery(t.id()) + t.masteryGain()));
        return false;
    }

    // ---- alignment ----------------------------------------------------------------------------------------------
    public int alignment() { return alignment; }
    public void setAlignment(int value) { alignment = Math.clamp(value, 0, 100); }

    // ---- save ---------------------------------------------------------------------------------------------------
    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.putString("race", race.id());
        tag.putString("powerType", power.id());
        tag.putString("class", characterClass.id());
        tag.put("appearance", appearance.save());
        tag.putIntArray("attributes", attributes.clone());
        tag.putInt("tp", tp);
        CompoundTag skillTag = new CompoundTag();
        skills.forEach(skillTag::putInt);
        tag.put("skills", skillTag);
        tag.putInt("body", body); tag.putInt("energy", energy); tag.putInt("stamina", stamina);
        tag.putInt("release", release);
        tag.putInt("recoveryDelay", recoveryDelay);
        tag.putInt("alignment", alignment);
        tag.putString("form", form);
        tag.putString("selectedForm", selectedForm);
        CompoundTag masteryTag = new CompoundTag();
        mastery.forEach(masteryTag::putDouble);
        tag.put("mastery", masteryTag);
        return tag;
    }

    /** Loads against the running content; returns null when the race, power type or class no longer exists. */
    public static Fighter load(ContentSet content, CompoundTag tag) {
        Race race = content.race(tag.getString("race"));
        PowerType power = content.powerType(tag.getString("powerType"));
        CharacterClass cls = content.characterClass(tag.getString("class"));
        if (race == null || power == null || cls == null || !cls.powerType().equals(power.id())) return null;
        int[] stored = tag.getIntArray("attributes");
        int[] attributes = startingAttributes(race, power);
        for (int i = 0; i < Math.min(stored.length, attributes.length); i++) attributes[i] = Math.clamp(stored[i], 1, power.attributeLimit());
        Appearance look = Appearance.load(tag.getCompound("appearance"));
        try { look.validate(race); } catch (IllegalArgumentException e) { look = Appearance.defaults(race); }
        Fighter f = new Fighter(power, race, cls, look, attributes);
        f.tp = Math.max(0, tag.getInt("tp"));
        CompoundTag skillTag = tag.getCompound("skills");
        for (String id : skillTag.getAllKeys()) {
            SkillDef def = content.skill(id);
            // Skills of removed add-ons are dropped; levels are clamped to the current maximum.
            if (def != null && def.availableTo(power.id())) f.skills.put(id, Math.clamp(skillTag.getInt(id), 0, def.maxLevel()));
        }
        f.skills.values().removeIf(level -> level == 0);
        f.bindSkills(content);
        f.release = Math.clamp(tag.getInt("release"), 0, MAX_RELEASE) / RELEASE_STEP * RELEASE_STEP;
        f.recoveryDelay = Math.clamp(tag.getInt("recoveryDelay"), 0, HURT_PAUSE_TICKS);
        f.alignment = tag.contains("alignment", Tag.TAG_ANY_NUMERIC) ? Math.clamp(tag.getInt("alignment"), 0, 100) : START_ALIGNMENT;
        f.form = tag.getString("form");
        f.selectedForm = tag.getString("selectedForm");
        CompoundTag masteryTag = tag.getCompound("mastery");
        for (String id : masteryTag.getAllKeys()) {
            Transformation t = content.transformation(id);
            if (t != null) f.mastery.put(id, Math.clamp(masteryTag.getDouble(id), 0D, t.masteryMax()));
        }
        f.bindForms(content);
        f.setResources(tag.getInt("body"), tag.getInt("energy"), tag.getInt("stamina"));
        return f;
    }
}
