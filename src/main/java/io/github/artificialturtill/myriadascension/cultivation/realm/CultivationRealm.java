package io.github.artificialturtill.myriadascension.cultivation.realm;

import java.util.Locale;

public enum CultivationRealm {
    MORTAL("Mortal", WorldScale.STARTING_WORLD, RealmSubdivisionType.NONE, 1),

    TEMPERED_BODY("Tempered Body", WorldScale.STARTING_WORLD, RealmSubdivisionType.STAGE, 9),
    INITIAL_ELEMENT("Initial Element", WorldScale.STARTING_WORLD, RealmSubdivisionType.STAGE, 9),
    QI_TRANSFORMATION("Qi Transformation", WorldScale.STARTING_WORLD, RealmSubdivisionType.STAGE, 9),
    SEPARATION_AND_REUNION("Separation and Reunion", WorldScale.STARTING_WORLD, RealmSubdivisionType.STAGE, 9),
    TRUE_ELEMENT("True Element", WorldScale.STARTING_WORLD, RealmSubdivisionType.STAGE, 9),
    IMMORTAL_ASCENSION("Immortal Ascension", WorldScale.STARTING_WORLD, RealmSubdivisionType.STAGE, 9),
    TRANSCENDENT("Transcendent", WorldScale.STARTING_WORLD, RealmSubdivisionType.ORDER, 3),

    SAINT("Saint", WorldScale.PLANET, RealmSubdivisionType.ORDER, 3),

    SAINT_KING("Saint King", WorldScale.STARFIELD, RealmSubdivisionType.ORDER, 3),
    ORIGIN_RETURNING("Origin Returning", WorldScale.STARFIELD, RealmSubdivisionType.ORDER, 3),
    ORIGIN_KING("Origin King", WorldScale.STARFIELD, RealmSubdivisionType.ORDER, 3),

    DAO_SOURCE("Dao Source", WorldScale.STAR_BOUNDARY, RealmSubdivisionType.ORDER, 3),
    EMPEROR("Emperor", WorldScale.STAR_BOUNDARY, RealmSubdivisionType.ORDER, 3),
    PSEUDO_GREAT_EMPEROR("Pseudo-Great Emperor", WorldScale.STAR_BOUNDARY, RealmSubdivisionType.NONE, 1),
    DAO_SEAL("Dao Seal", WorldScale.STAR_BOUNDARY, RealmSubdivisionType.NONE, 1),

    HALF_STEP_OPEN_HEAVEN("Half-Step Open Heaven", WorldScale.OUTER_UNIVERSE, RealmSubdivisionType.NONE, 1),
    OPEN_HEAVEN("Open Heaven", WorldScale.OUTER_UNIVERSE, RealmSubdivisionType.RANK, 9),

    WORLD_CREATION("World Creation", WorldScale.MULTIVERSE, RealmSubdivisionType.NONE, 1);

    public static final CultivationRealm IMPLEMENTATION_CEILING = SAINT;

    private final String displayName;
    private final WorldScale worldScale;
    private final RealmSubdivisionType subdivisionType;
    private final int maximumSubdivision;

    CultivationRealm(
            String displayName,
            WorldScale worldScale,
            RealmSubdivisionType subdivisionType,
            int maximumSubdivision) {
        this.displayName = displayName;
        this.worldScale = worldScale;
        this.subdivisionType = subdivisionType;
        this.maximumSubdivision = maximumSubdivision;
    }

    public String displayName() {
        return displayName;
    }

    public WorldScale worldScale() {
        return worldScale;
    }

    public RealmSubdivisionType subdivisionType() {
        return subdivisionType;
    }

    public int maximumSubdivision() {
        return maximumSubdivision;
    }

    /**
     * Legacy accessor retained while the rest of the prototype migrates from
     * a universal "minor stage" model.
     */
    public int defaultMinorStages() {
        return maximumSubdivision;
    }

    public boolean hasSubdivisions() {
        return subdivisionType != RealmSubdivisionType.NONE;
    }

    public boolean isCultivatorRealm() {
        return this != MORTAL;
    }

    public boolean isCurrentlyImplemented() {
        return ordinal() <= IMPLEMENTATION_CEILING.ordinal();
    }

    public CultivationRealm nextMajorRealm() {
        int next = ordinal() + 1;
        return next < values().length ? values()[next] : this;
    }

    public static CultivationRealm fromSerializedName(String value) {
        if (value == null || value.isBlank()) {
            return MORTAL;
        }

        try {
            return valueOf(value.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ignored) {
            return MORTAL;
        }
    }
}
