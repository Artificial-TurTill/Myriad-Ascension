package io.github.artificialturtill.myriadascension.cultivation.data;

import io.github.artificialturtill.myriadascension.affinity.AffinityProfile;
import io.github.artificialturtill.myriadascension.affinity.AffinityType;
import io.github.artificialturtill.myriadascension.affinity.BodyPolarity;
import io.github.artificialturtill.myriadascension.affinity.StartingAffinityGenerator;
import io.github.artificialturtill.myriadascension.alignment.CultivationAffiliation;
import io.github.artificialturtill.myriadascension.alignment.MoralAlignment;
import io.github.artificialturtill.myriadascension.character.CharacterSex;
import io.github.artificialturtill.myriadascension.bloodline.BloodlineState;
import io.github.artificialturtill.myriadascension.cultivation.qi.QiNatureProfile;
import io.github.artificialturtill.myriadascension.cultivation.qi.QiRules;
import io.github.artificialturtill.myriadascension.cultivation.realm.CultivationRealm;
import io.github.artificialturtill.myriadascension.inheritance.CultivationMethodState;
import io.github.artificialturtill.myriadascension.organization.OrganizationStandingState;
import io.github.artificialturtill.myriadascension.library.LibraryAuthorizationState;
import io.github.artificialturtill.myriadascension.quest.QuestJournalState;
import io.github.artificialturtill.myriadascension.stats.CultivatorStats;
import io.github.artificialturtill.myriadascension.technique.TechniqueCategory;
import io.github.artificialturtill.myriadascension.technique.TechniqueKnowledgeState;
import io.github.artificialturtill.myriadascension.technique.TechniqueLoadoutState;
import io.github.artificialturtill.myriadascension.training.BodyTemperingState;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.neoforged.neoforge.common.util.INBTSerializable;

public final class CultivatorData implements INBTSerializable<CompoundTag> {
    public static final int SCHEMA_VERSION = 13;

    private int schemaVersion = SCHEMA_VERSION;

    private CharacterSex characterSex = CharacterSex.UNSET;
    private BodyPolarity bodyPolarity = BodyPolarity.UNSET;
    private CultivationAffiliation affiliation = CultivationAffiliation.UNDECIDED;
    private double moralAlignment = MoralAlignment.NEUTRAL;
    private double karma;
    private final AffinityProfile innateAffinities = new AffinityProfile();
    private final AffinityProfile affinities = new AffinityProfile();
    private final QiNatureProfile qiNature = new QiNatureProfile();
    private final CultivationMethodState cultivationMethods = new CultivationMethodState();
    private final OrganizationStandingState organizationStandings = new OrganizationStandingState();
    private final QuestJournalState questJournal = new QuestJournalState();
    private final BloodlineState bloodline = new BloodlineState();
    private final LibraryAuthorizationState libraryAuthorizations = new LibraryAuthorizationState();
    private final CultivatorStats stats = new CultivatorStats();
    private final TechniqueLoadoutState techniqueLoadout = new TechniqueLoadoutState();
    private final TechniqueKnowledgeState techniqueKnowledge = new TechniqueKnowledgeState();
    private final BodyTemperingState bodyTempering = new BodyTemperingState();

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
    private int qiConcealmentLevel;

    // Training fatigue persists so relogging cannot be used as instant recovery.
    private double trainingFatigue;

    // Whether loose Basic Training Weight items are deliberately secured.
    private boolean looseTrainingWeightsEnabled;

    // Runtime-only training state.
    private boolean trainingRequested;
    private int trainingSessionTicks;
    private long lastTrainingBreathPulseTick = Long.MIN_VALUE;

    // Runtime-only anti-spam state. These values intentionally do not persist to disk.
    private long lastCirculationControlTick = Long.MIN_VALUE;
    private long lastBurstToggleTick = Long.MIN_VALUE;

    public CharacterSex characterSex() {
        return characterSex;
    }

    public void setCharacterSex(CharacterSex characterSex) {
        this.characterSex = characterSex == null ? CharacterSex.UNSET : characterSex;
        this.bodyPolarity = this.characterSex.polarity();
    }

    public BodyPolarity bodyPolarity() {
        return bodyPolarity;
    }

    public CultivationAffiliation affiliation() {
        return affiliation;
    }

    public void setAffiliation(CultivationAffiliation affiliation) {
        this.affiliation = affiliation == null ? CultivationAffiliation.UNDECIDED : affiliation;
    }

    public double moralAlignment() {
        return moralAlignment;
    }

    public void setMoralAlignment(double moralAlignment) {
        this.moralAlignment = MoralAlignment.clamp(moralAlignment);
    }

    public void adjustMoralAlignment(double amount) {
        setMoralAlignment(moralAlignment + amount);
    }

    public double karma() {
        return karma;
    }

    public void setKarma(double karma) {
        this.karma = karma;
    }

    public void adjustKarma(double amount) {
        this.karma += amount;
    }

    public AffinityProfile innateAffinities() {
        return innateAffinities;
    }

    public AffinityProfile affinities() {
        return affinities;
    }

    /**
     * Normal progression may only improve current attunement. Deliberate loss
     * belongs to an explicit sacrifice mechanic and must call sacrificeAffinity.
     */
    public void increaseAffinity(AffinityType type, int amount) {
        if (type == null || amount <= 0) {
            return;
        }
        affinities.set(type, affinities.get(type) + amount);
    }

    public boolean sacrificeAffinity(AffinityType type, int amount) {
        if (type == null || amount <= 0) {
            return false;
        }
        int before = affinities.get(type);
        int after = Math.max(0, before - amount);
        affinities.set(type, after);
        return after != before;
    }

    /** Alpha/admin setter. Gameplay systems should use increaseAffinity instead. */
    public void setAffinityForTesting(AffinityType type, int value) {
        if (type != null) {
            affinities.set(type, value);
        }
    }

    public QiNatureProfile qiNature() {
        return qiNature;
    }

    public CultivationMethodState cultivationMethods() {
        return cultivationMethods;
    }

    public OrganizationStandingState organizationStandings() {
        return organizationStandings;
    }

    public QuestJournalState questJournal() {
        return questJournal;
    }

    public BloodlineState bloodline() {
        return bloodline;
    }

    public LibraryAuthorizationState libraryAuthorizations() {
        return libraryAuthorizations;
    }

    public CultivatorStats stats() {
        return stats;
    }

    public TechniqueLoadoutState techniqueLoadout() {
        return techniqueLoadout;
    }

    public TechniqueKnowledgeState techniqueKnowledge() {
        return techniqueKnowledge;
    }

    public BodyTemperingState bodyTempering() {
        return bodyTempering;
    }

    public void completeInitialSetup(CharacterSex sex, double startingMoralAlignment, RandomSource random) {
        if (hasCompletedInitialSetup()) {
            throw new IllegalStateException("Initial cultivation setup has already been completed.");
        }
        if (sex == null || sex == CharacterSex.UNSET) {
            throw new IllegalArgumentException("Character sex must be Male or Female.");
        }

        if (startingMoralAlignment != -1.0D && startingMoralAlignment != 1.0D) {
            throw new IllegalArgumentException("Starting moral alignment must be exactly -1 or +1.");
        }

        setCharacterSex(sex);
        setMoralAlignment(startingMoralAlignment);
        setAffiliation(CultivationAffiliation.UNAFFILIATED);

        AffinityProfile generated = StartingAffinityGenerator.generate(sex, random);
        innateAffinities.copyFrom(generated);
        affinities.copyFrom(generated);
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

    public void setCultivationComprehension(double value) {
        cultivationComprehension = Math.max(0.0D, value);
    }

    public void addCultivationComprehension(double amount) {
        if (amount > 0.0D) {
            setCultivationComprehension(cultivationComprehension + amount);
        }
    }

    public double battleComprehension() {
        return battleComprehension;
    }

    public void setBattleComprehension(double value) {
        battleComprehension = Math.max(0.0D, value);
    }

    public void addBattleComprehension(double amount) {
        if (amount > 0.0D) {
            setBattleComprehension(battleComprehension + amount);
        }
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

    public void setBodyInjury(double bodyInjury) {
        this.bodyInjury = Math.max(0.0D, bodyInjury);
    }

    public double meridianInjury() {
        return meridianInjury;
    }

    public void setMeridianInjury(double meridianInjury) {
        this.meridianInjury = Math.max(0.0D, meridianInjury);
    }

    public double soulInjury() {
        return soulInjury;
    }

    public void setSoulInjury(double soulInjury) {
        this.soulInjury = Math.max(0.0D, soulInjury);
    }

    public double recoveryDebt() {
        return recoveryDebt;
    }

    public void setRecoveryDebt(double recoveryDebt) {
        this.recoveryDebt = Math.max(0.0D, recoveryDebt);
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

    public int qiConcealmentLevel() {
        return qiConcealmentLevel;
    }

    public void setQiConcealmentLevel(int level) {
        qiConcealmentLevel = QiRules.clampSkillLevel(level);
    }

    public double passiveRechargeCeiling() {
        return QiRules.passiveRechargeCeiling(passiveQiRechargingLevel);
    }

    public double trainingFatigue() {
        return trainingFatigue;
    }

    public void setTrainingFatigue(double trainingFatigue) {
        this.trainingFatigue = clamp(trainingFatigue, 0.0D, 100.0D);
    }

    public boolean looseTrainingWeightsEnabled() {
        return looseTrainingWeightsEnabled;
    }

    public void setLooseTrainingWeightsEnabled(boolean enabled) {
        looseTrainingWeightsEnabled = enabled;
    }

    public boolean trainingRequested() {
        return trainingRequested;
    }

    public void setTrainingRequested(boolean trainingRequested) {
        this.trainingRequested = trainingRequested;
        if (!trainingRequested) {
            resetTrainingSession();
        }
    }

    public int trainingSessionTicks() {
        return trainingSessionTicks;
    }

    public void incrementTrainingSessionTicks() {
        trainingSessionTicks++;
    }

    public void resetTrainingSession() {
        trainingSessionTicks = 0;
    }

    public long lastTrainingBreathPulseTick() {
        return lastTrainingBreathPulseTick;
    }

    public void markTrainingBreathPulse(long gameTime) {
        lastTrainingBreathPulseTick = gameTime;
    }

    public boolean hasCompletedInitialSetup() {
        return characterSex != CharacterSex.UNSET
                && bodyPolarity != BodyPolarity.UNSET
                && affiliation != CultivationAffiliation.UNDECIDED
                && affinities.total() > 0;
    }

    public boolean tryAcceptCirculationControl(long gameTime) {
        if (!hasElapsed(gameTime, lastCirculationControlTick, QiRules.MIN_CIRCULATION_CONTROL_INTERVAL_TICKS)) {
            return false;
        }
        lastCirculationControlTick = gameTime;
        return true;
    }

    public boolean tryAcceptBurstToggle(long gameTime) {
        if (!hasElapsed(gameTime, lastBurstToggleTick, QiRules.MIN_BURST_TOGGLE_INTERVAL_TICKS)) {
            return false;
        }
        lastBurstToggleTick = gameTime;
        return true;
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
        characterSex = other.characterSex;
        bodyPolarity = other.bodyPolarity;
        affiliation = other.affiliation;
        moralAlignment = other.moralAlignment;
        karma = other.karma;
        innateAffinities.copyFrom(other.innateAffinities);
        affinities.copyFrom(other.affinities);
        qiNature.copyFrom(other.qiNature);
        cultivationMethods.copyFrom(other.cultivationMethods);
        organizationStandings.copyFrom(other.organizationStandings);
        questJournal.copyFrom(other.questJournal);
        bloodline.copyFrom(other.bloodline);
        libraryAuthorizations.copyFrom(other.libraryAuthorizations);
        stats.copyFrom(other.stats);
        techniqueLoadout.copyFrom(other.techniqueLoadout);
        techniqueKnowledge.copyFrom(other.techniqueKnowledge);
        bodyTempering.copyFrom(other.bodyTempering);

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
        qiConcealmentLevel = other.qiConcealmentLevel;
        trainingFatigue = other.trainingFatigue;
        looseTrainingWeightsEnabled = other.looseTrainingWeightsEnabled;

        trainingRequested = false;
        trainingSessionTicks = 0;
        lastTrainingBreathPulseTick = Long.MIN_VALUE;
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();

        tag.putInt("SchemaVersion", SCHEMA_VERSION);
        tag.putString("CharacterSex", characterSex.name());
        tag.putString("BodyPolarity", bodyPolarity.name());
        tag.putString("Affiliation", affiliation.name());
        tag.putDouble("MoralAlignment", moralAlignment);
        tag.putDouble("Karma", karma);
        tag.put("InnateAffinities", innateAffinities.save());
        tag.put("Affinities", affinities.save());
        tag.put("QiNature", qiNature.save());
        tag.put("CultivationMethods", cultivationMethods.save());
        tag.put("OrganizationStandings", organizationStandings.save());
        tag.put("QuestJournal", questJournal.save());
        tag.put("Bloodline", bloodline.save());
        tag.put("LibraryAuthorizations", libraryAuthorizations.save());
        tag.put("Stats", stats.save());
        tag.put("TechniqueLoadout", techniqueLoadout.save());
        tag.put("TechniqueKnowledge", techniqueKnowledge.save());
        tag.put("BodyTempering", bodyTempering.save());

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
        tag.putInt("QiConcealmentLevel", qiConcealmentLevel);
        tag.putDouble("TrainingFatigue", trainingFatigue);
        tag.putBoolean("LooseTrainingWeightsEnabled", looseTrainingWeightsEnabled);

        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
        int loadedSchemaVersion = Math.max(1, tag.getInt("SchemaVersion"));
        schemaVersion = SCHEMA_VERSION;

        if (loadedSchemaVersion >= 2) {
            characterSex = CharacterSex.fromSerializedName(tag.getString("CharacterSex"));
            bodyPolarity = BodyPolarity.fromSerializedName(tag.getString("BodyPolarity"));
            affiliation = CultivationAffiliation.fromSerializedName(tag.getString("Affiliation"));
            moralAlignment = MoralAlignment.clamp(tag.getDouble("MoralAlignment"));
            karma = tag.getDouble("Karma");
        } else {
            migrateLegacyAlignment(tag.getString("Alignment"));
            bodyPolarity = BodyPolarity.fromSerializedName(tag.getString("BodyPolarity"));
            characterSex = switch (bodyPolarity) {
                case YANG -> CharacterSex.MALE;
                case YIN -> CharacterSex.FEMALE;
                case UNSET -> CharacterSex.UNSET;
            };
        }

        affinities.load(tag.getCompound("Affinities"));

        if (loadedSchemaVersion >= 3 && tag.contains("InnateAffinities")) {
            innateAffinities.load(tag.getCompound("InnateAffinities"));
        } else {
            // Schema v1/v2 had only one profile. Treat the saved values as both
            // innate potential and current attunement during migration.
            innateAffinities.copyFrom(affinities);
        }

        if (loadedSchemaVersion >= 3 && tag.contains("QiNature")) {
            qiNature.load(tag.getCompound("QiNature"));
        }

        if (loadedSchemaVersion >= 4 && tag.contains("CultivationMethods")) {
            cultivationMethods.load(tag.getCompound("CultivationMethods"));
        }

        if (loadedSchemaVersion >= 5 && tag.contains("OrganizationStandings")) {
            organizationStandings.load(tag.getCompound("OrganizationStandings"));
        }

        if (loadedSchemaVersion >= 6 && tag.contains("QuestJournal")) {
            questJournal.load(tag.getCompound("QuestJournal"));
        }

        if (loadedSchemaVersion >= 7 && tag.contains("Bloodline")) {
            bloodline.load(tag.getCompound("Bloodline"));
        }

        if (loadedSchemaVersion >= 8 && tag.contains("LibraryAuthorizations")) {
            libraryAuthorizations.load(tag.getCompound("LibraryAuthorizations"));
        }

        if (loadedSchemaVersion >= 9 && tag.contains("Stats")) {
            stats.load(tag.getCompound("Stats"));
        }

        if (loadedSchemaVersion >= 9 && tag.contains("TechniqueLoadout")) {
            techniqueLoadout.load(tag.getCompound("TechniqueLoadout"));
        } else if (!cultivationMethods.activeMethodId().isBlank()) {
            techniqueLoadout.equipSerialized(
                    TechniqueCategory.CULTIVATION,
                    cultivationMethods.activeMethodId());
        }

        if (loadedSchemaVersion >= 10 && tag.contains("TechniqueKnowledge")) {
            techniqueKnowledge.load(tag.getCompound("TechniqueKnowledge"));
        }

        if (loadedSchemaVersion >= 12 && tag.contains("BodyTempering")) {
            bodyTempering.load(tag.getCompound("BodyTempering"));
        }

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
        qiConcealmentLevel = QiRules.clampSkillLevel(tag.getInt("QiConcealmentLevel"));

        trainingFatigue = loadedSchemaVersion >= 11
                ? clamp(tag.getDouble("TrainingFatigue"), 0.0D, 100.0D)
                : 0.0D;

        looseTrainingWeightsEnabled = loadedSchemaVersion >= 13
                && tag.getBoolean("LooseTrainingWeightsEnabled");

        trainingRequested = false;
        trainingSessionTicks = 0;
        lastTrainingBreathPulseTick = Long.MIN_VALUE;
    }

    private void migrateLegacyAlignment(String legacyAlignment) {
        String value = legacyAlignment == null ? "" : legacyAlignment.toUpperCase(java.util.Locale.ROOT);
        switch (value) {
            case "RIGHTEOUS" -> {
                affiliation = CultivationAffiliation.RIGHTEOUS;
                moralAlignment = 25.0D;
            }
            case "DEMONIC" -> {
                affiliation = CultivationAffiliation.DEMONIC;
                moralAlignment = MoralAlignment.NEUTRAL;
            }
            case "BUDDHIST" -> {
                affiliation = CultivationAffiliation.BUDDHIST;
                moralAlignment = 25.0D;
            }
            case "UNALIGNED" -> {
                affiliation = CultivationAffiliation.UNAFFILIATED;
                moralAlignment = MoralAlignment.NEUTRAL;
            }
            default -> {
                affiliation = CultivationAffiliation.UNDECIDED;
                moralAlignment = MoralAlignment.NEUTRAL;
            }
        }
        karma = 0.0D;
    }

    private static boolean hasElapsed(long now, long previous, int requiredTicks) {
        return previous == Long.MIN_VALUE || now < previous || now - previous >= requiredTicks;
    }

    private static double clamp(double value, double minimum, double maximum) {
        return Math.max(minimum, Math.min(maximum, value));
    }
}
