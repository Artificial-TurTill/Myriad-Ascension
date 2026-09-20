package io.github.artificialturtill.myriadascension.cultivation.data;

import io.github.artificialturtill.myriadascension.affinity.AffinityProfile;
import io.github.artificialturtill.myriadascension.affinity.BodyPolarity;
import io.github.artificialturtill.myriadascension.alignment.CultivationAlignment;
import io.github.artificialturtill.myriadascension.cultivation.qi.QiRules;
import io.github.artificialturtill.myriadascension.cultivation.realm.CultivationRealm;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;

public final class CultivatorData implements INBTSerializable<CompoundTag> {
    public static final int SCHEMA_VERSION = 1;

    private int schemaVersion = SCHEMA_VERSION;

    private CultivationAlignment alignment = CultivationAlignment.UNDECIDED;
    private BodyPolarity bodyPolarity = BodyPolarity.UNSET;
    private final AffinityProfile affinities = new AffinityProfile();

    private CultivationRealm realm = CultivationRealm.MORTAL;
    private int minorStage;
    private double cultivationProgress;
    private double cultivationComprehension;
    private double battleComprehension;

    private double currentQi;
    private double maximumQi;
    private double circulationPercent;
    private boolean burstMode;

    private double meridianLoad;
    private double bodyInjury;
    private double meridianInjury;
    private double soulInjury;
    private double recoveryDebt;

    private double vesselPurity = 100.0D;
    private double impurityLoad;
    private double demonicQiContamination;

    // Zero means unlearned. Once learned, a skill begins at level 1.
    private int passiveQiRechargingLevel;
    private int meditationLevel;

    public CultivationAlignment alignment() {
        return alignment;
    }

    public void setAlignment(CultivationAlignment alignment) {
        this.alignment = alignment == null ? CultivationAlignment.UNDECIDED : alignment;
    }

    public BodyPolarity bodyPolarity() {
        return bodyPolarity;
    }

    public void setBodyPolarity(BodyPolarity bodyPolarity) {
        this.bodyPolarity = bodyPolarity == null ? BodyPolarity.UNSET : bodyPolarity;
    }

    public AffinityProfile affinities() {
        return affinities;
    }

    public CultivationRealm realm() {
        return realm;
    }

    public void setRealm(CultivationRealm realm) {
        this.realm = realm == null ? CultivationRealm.MORTAL : realm;
        if (this.realm == CultivationRealm.MORTAL) {
            minorStage = 0;
        } else {
            minorStage = Math.max(1, Math.min(minorStage, this.realm.defaultMinorStages()));
        }
    }

    public int minorStage() {
        return minorStage;
    }

    public void setMinorStage(int minorStage) {
        if (realm == CultivationRealm.MORTAL) {
            this.minorStage = 0;
            return;
        }

        this.minorStage = Math.max(1, Math.min(realm.defaultMinorStages(), minorStage));
    }

    public double cultivationProgress() {
        return cultivationProgress;
    }

    public void setCultivationProgress(double cultivationProgress) {
        this.cultivationProgress = Math.max(0.0D, cultivationProgress);
    }

    public double cultivationComprehension() {
        return cultivationComprehension;
    }

    public void addCultivationComprehension(double amount) {
        cultivationComprehension = Math.max(0.0D, cultivationComprehension + amount);
    }

    public double battleComprehension() {
        return battleComprehension;
    }

    public void addBattleComprehension(double amount) {
        battleComprehension = Math.max(0.0D, battleComprehension + amount);
    }

    public double currentQi() {
        return currentQi;
    }

    public void setCurrentQi(double currentQi) {
        this.currentQi = clamp(currentQi, 0.0D, maximumQi);
    }

    public double maximumQi() {
        return maximumQi;
    }

    public void setMaximumQi(double maximumQi) {
        this.maximumQi = Math.max(0.0D, maximumQi);
        this.currentQi = Math.min(currentQi, this.maximumQi);
    }

    public double qiFraction() {
        return maximumQi <= 0.0D ? 0.0D : currentQi / maximumQi;
    }

    public double circulationPercent() {
        return circulationPercent;
    }

    public void setCirculationPercent(double circulationPercent) {
        this.circulationPercent = clamp(circulationPercent, 0.0D, 100.0D);
    }

    public void increaseCirculation(double percentagePoints) {
        setCirculationPercent(circulationPercent + Math.max(0.0D, percentagePoints));
    }

    public void decreaseCirculation(double percentagePoints) {
        setCirculationPercent(circulationPercent - Math.max(0.0D, percentagePoints));
    }

    public boolean burstMode() {
        return burstMode;
    }

    public void setBurstMode(boolean burstMode) {
        this.burstMode = burstMode;
    }

    public double meridianLoad() {
        return meridianLoad;
    }

    public void setMeridianLoad(double meridianLoad) {
        this.meridianLoad = clamp(meridianLoad, 0.0D, 100.0D);
    }

    public double bodyInjury() {
        return bodyInjury;
    }

    public double meridianInjury() {
        return meridianInjury;
    }

    public double soulInjury() {
        return soulInjury;
    }

    public double recoveryDebt() {
        return recoveryDebt;
    }

    public double vesselPurity() {
        return vesselPurity;
    }

    public void setVesselPurity(double vesselPurity) {
        this.vesselPurity = clamp(vesselPurity, 0.0D, 100.0D);
    }

    public double impurityLoad() {
        return impurityLoad;
    }

    public void setImpurityLoad(double impurityLoad) {
        this.impurityLoad = Math.max(0.0D, impurityLoad);
    }

    public double demonicQiContamination() {
        return demonicQiContamination;
    }

    public void setDemonicQiContamination(double demonicQiContamination) {
        this.demonicQiContamination = Math.max(0.0D, demonicQiContamination);
    }

    public int passiveQiRechargingLevel() {
        return passiveQiRechargingLevel;
    }

    public void setPassiveQiRechargingLevel(int level) {
        passiveQiRechargingLevel = QiRules.clampSkillLevel(level);
    }

    public int meditationLevel() {
        return meditationLevel;
    }

    public void setMeditationLevel(int level) {
        meditationLevel = QiRules.clampSkillLevel(level);
    }

    public double passiveRechargeCeiling() {
        return QiRules.passiveRechargeCeiling(passiveQiRechargingLevel);
    }

    public boolean hasCompletedInitialSetup() {
        return alignment != CultivationAlignment.UNDECIDED && bodyPolarity != BodyPolarity.UNSET;
    }

    public void applyDeathRecoveryState() {
        burstMode = false;
        circulationPercent = 0.0D;
        meridianLoad = Math.max(meridianLoad, 35.0D);
        bodyInjury = Math.max(bodyInjury, 50.0D);
        meridianInjury = Math.max(meridianInjury, 25.0D);
        recoveryDebt = Math.max(recoveryDebt, 1.0D);

        // Death never de-ranks the player. Qi is reduced to the normal passive baseline.
        currentQi = Math.min(currentQi, maximumQi * QiRules.BASE_PASSIVE_RECHARGE_CEILING);
    }

    public void copyFrom(CultivatorData other) {
        schemaVersion = other.schemaVersion;
        alignment = other.alignment;
        bodyPolarity = other.bodyPolarity;
        affinities.copyFrom(other.affinities);

        realm = other.realm;
        minorStage = other.minorStage;
        cultivationProgress = other.cultivationProgress;
        cultivationComprehension = other.cultivationComprehension;
        battleComprehension = other.battleComprehension;

        currentQi = other.currentQi;
        maximumQi = other.maximumQi;
        circulationPercent = other.circulationPercent;
        burstMode = other.burstMode;

        meridianLoad = other.meridianLoad;
        bodyInjury = other.bodyInjury;
        meridianInjury = other.meridianInjury;
        soulInjury = other.soulInjury;
        recoveryDebt = other.recoveryDebt;

        vesselPurity = other.vesselPurity;
        impurityLoad = other.impurityLoad;
        demonicQiContamination = other.demonicQiContamination;

        passiveQiRechargingLevel = other.passiveQiRechargingLevel;
        meditationLevel = other.meditationLevel;
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();

        tag.putInt("SchemaVersion", schemaVersion);
        tag.putString("Alignment", alignment.name());
        tag.putString("BodyPolarity", bodyPolarity.name());
        tag.put("Affinities", affinities.save());

        tag.putString("Realm", realm.name());
        tag.putInt("MinorStage", minorStage);
        tag.putDouble("CultivationProgress", cultivationProgress);
        tag.putDouble("CultivationComprehension", cultivationComprehension);
        tag.putDouble("BattleComprehension", battleComprehension);

        tag.putDouble("CurrentQi", currentQi);
        tag.putDouble("MaximumQi", maximumQi);
        tag.putDouble("CirculationPercent", circulationPercent);
        tag.putBoolean("BurstMode", burstMode);

        tag.putDouble("MeridianLoad", meridianLoad);
        tag.putDouble("BodyInjury", bodyInjury);
        tag.putDouble("MeridianInjury", meridianInjury);
        tag.putDouble("SoulInjury", soulInjury);
        tag.putDouble("RecoveryDebt", recoveryDebt);

        tag.putDouble("VesselPurity", vesselPurity);
        tag.putDouble("ImpurityLoad", impurityLoad);
        tag.putDouble("DemonicQiContamination", demonicQiContamination);

        tag.putInt("PassiveQiRechargingLevel", passiveQiRechargingLevel);
        tag.putInt("MeditationLevel", meditationLevel);

        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
        schemaVersion = Math.max(1, tag.getInt("SchemaVersion"));

        alignment = CultivationAlignment.fromSerializedName(tag.getString("Alignment"));
        bodyPolarity = BodyPolarity.fromSerializedName(tag.getString("BodyPolarity"));
        affinities.load(tag.getCompound("Affinities"));

        realm = CultivationRealm.fromSerializedName(tag.getString("Realm"));
        minorStage = tag.getInt("MinorStage");
        if (realm == CultivationRealm.MORTAL) {
            minorStage = 0;
        } else {
            minorStage = Math.max(1, Math.min(realm.defaultMinorStages(), minorStage));
        }

        cultivationProgress = Math.max(0.0D, tag.getDouble("CultivationProgress"));
        cultivationComprehension = Math.max(0.0D, tag.getDouble("CultivationComprehension"));
        battleComprehension = Math.max(0.0D, tag.getDouble("BattleComprehension"));

        maximumQi = Math.max(0.0D, tag.getDouble("MaximumQi"));
        currentQi = clamp(tag.getDouble("CurrentQi"), 0.0D, maximumQi);
        circulationPercent = clamp(tag.getDouble("CirculationPercent"), 0.0D, 100.0D);
        burstMode = tag.getBoolean("BurstMode");

        meridianLoad = clamp(tag.getDouble("MeridianLoad"), 0.0D, 100.0D);
        bodyInjury = Math.max(0.0D, tag.getDouble("BodyInjury"));
        meridianInjury = Math.max(0.0D, tag.getDouble("MeridianInjury"));
        soulInjury = Math.max(0.0D, tag.getDouble("SoulInjury"));
        recoveryDebt = Math.max(0.0D, tag.getDouble("RecoveryDebt"));

        vesselPurity = tag.contains("VesselPurity")
                ? clamp(tag.getDouble("VesselPurity"), 0.0D, 100.0D)
                : 100.0D;
        impurityLoad = Math.max(0.0D, tag.getDouble("ImpurityLoad"));
        demonicQiContamination = Math.max(0.0D, tag.getDouble("DemonicQiContamination"));

        passiveQiRechargingLevel = QiRules.clampSkillLevel(tag.getInt("PassiveQiRechargingLevel"));
        meditationLevel = QiRules.clampSkillLevel(tag.getInt("MeditationLevel"));
    }

    private static double clamp(double value, double minimum, double maximum) {
        return Math.max(minimum, Math.min(maximum, value));
    }
}
