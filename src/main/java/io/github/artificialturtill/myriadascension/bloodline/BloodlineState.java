package io.github.artificialturtill.myriadascension.bloodline;

import java.util.Locale;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

public final class BloodlineState {
    private String lineageId = "";
    private String lineageFamilyId = "";
    private String lineageDisplayName = "";

    private String gradeId = "";
    private String gradeDisplayName = "";
    private String effectiveThroughRealm = "";
    private int gradePowerTier;

    private double purity;
    private double conflictDamage;

    public boolean hasBloodline() {
        return !lineageId.isBlank();
    }

    public String lineageId() {
        return lineageId;
    }

    public String lineageFamilyId() {
        return lineageFamilyId;
    }

    public String lineageDisplayName() {
        return lineageDisplayName;
    }

    public String gradeId() {
        return gradeId;
    }

    public String gradeDisplayName() {
        return gradeDisplayName;
    }

    public String effectiveThroughRealm() {
        return effectiveThroughRealm;
    }

    public int gradePowerTier() {
        return gradePowerTier;
    }

    public double purity() {
        return purity;
    }

    public double conflictDamage() {
        return conflictDamage;
    }

    public void addConflictDamage(double amount) {
        conflictDamage = Math.max(0.0D, conflictDamage + Math.max(0.0D, amount));
    }

    public boolean isSameFamily(BloodlineLineage lineage) {
        return lineage != null
                && !lineageFamilyId.isBlank()
                && lineageFamilyId.equals(lineage.familyId().toString());
    }

    public void install(BloodlineLineage lineage, BloodlineGrade grade, double purity) {
        lineageId = lineage.id().toString();
        lineageFamilyId = lineage.familyId().toString();
        lineageDisplayName = lineage.displayName();

        gradeId = grade.id().toString();
        gradeDisplayName = grade.displayName();
        effectiveThroughRealm = grade.effectiveThroughRealm().name();
        gradePowerTier = grade.sourcePowerTier();

        this.purity = clamp(purity, 0.0D, 100.0D);
        conflictDamage = 0.0D;
    }

    public void overwrite(BloodlineLineage lineage, BloodlineGrade grade, double purity) {
        install(lineage, grade, purity);
    }

    public boolean refinePurity(double incomingPurity) {
        double clamped = clamp(incomingPurity, 0.0D, 100.0D);
        if (clamped <= purity) {
            return false;
        }

        purity = clamped;
        conflictDamage = Math.max(0.0D, conflictDamage - (clamped - purity) * 0.25D);
        return true;
    }

    public void copyFrom(BloodlineState other) {
        lineageId = other.lineageId;
        lineageFamilyId = other.lineageFamilyId;
        lineageDisplayName = other.lineageDisplayName;
        gradeId = other.gradeId;
        gradeDisplayName = other.gradeDisplayName;
        effectiveThroughRealm = other.effectiveThroughRealm;
        gradePowerTier = other.gradePowerTier;
        purity = other.purity;
        conflictDamage = other.conflictDamage;
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.putString("LineageId", lineageId);
        tag.putString("LineageFamilyId", lineageFamilyId);
        tag.putString("LineageDisplayName", lineageDisplayName);
        tag.putString("GradeId", gradeId);
        tag.putString("GradeDisplayName", gradeDisplayName);
        tag.putString("EffectiveThroughRealm", effectiveThroughRealm);
        tag.putInt("GradePowerTier", gradePowerTier);
        tag.putDouble("Purity", purity);
        tag.putDouble("ConflictDamage", conflictDamage);
        return tag;
    }

    public void load(CompoundTag tag) {
        lineageId = tag.getString("LineageId");
        lineageFamilyId = tag.getString("LineageFamilyId");
        lineageDisplayName = tag.getString("LineageDisplayName");
        gradeId = tag.getString("GradeId");
        gradeDisplayName = tag.getString("GradeDisplayName");
        effectiveThroughRealm = tag.getString("EffectiveThroughRealm").toUpperCase(Locale.ROOT);
        gradePowerTier = Math.max(0, tag.getInt("GradePowerTier"));
        purity = clamp(tag.getDouble("Purity"), 0.0D, 100.0D);
        conflictDamage = Math.max(0.0D, tag.getDouble("ConflictDamage"));
    }

    private static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }
}
