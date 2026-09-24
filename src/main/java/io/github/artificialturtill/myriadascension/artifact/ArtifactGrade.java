package io.github.artificialturtill.myriadascension.artifact;

import io.github.artificialturtill.myriadascension.cultivation.realm.CultivationRealm;

public enum ArtifactGrade {
    MORTAL("Mortal", CultivationRealm.MORTAL, 1.0D),
    TEMPERED_BODY("Tempered Body", CultivationRealm.TEMPERED_BODY, 1.5D),
    INITIAL_ELEMENT("Initial Element", CultivationRealm.INITIAL_ELEMENT, 3.0D),
    QI_TRANSFORMATION("Qi Transformation", CultivationRealm.QI_TRANSFORMATION, 8.0D),
    TRUE_ELEMENT("True Element", CultivationRealm.TRUE_ELEMENT, 30.0D),
    IMMORTAL_ASCENSION("Immortal Ascension", CultivationRealm.IMMORTAL_ASCENSION, 100.0D),
    TRANSCENDENT("Transcendent", CultivationRealm.TRANSCENDENT, 500.0D),
    SAINT("Saint", CultivationRealm.SAINT, 2_000.0D),
    SAINT_KING("Saint King", CultivationRealm.SAINT_KING, 10_000.0D),
    ORIGIN("Origin", CultivationRealm.ORIGIN_KING, 100_000.0D),
    DAO_SOURCE("Dao Source", CultivationRealm.DAO_SOURCE, 1_000_000.0D),
    EMPEROR("Emperor", CultivationRealm.EMPEROR, 10_000_000.0D),
    OPEN_HEAVEN("Open Heaven", CultivationRealm.OPEN_HEAVEN, 1.0E9D);

    private final String displayName;
    private final CultivationRealm equivalentRealm;
    private final double artifactPotential;

    ArtifactGrade(String displayName, CultivationRealm equivalentRealm, double artifactPotential) {
        this.displayName = displayName;
        this.equivalentRealm = equivalentRealm;
        this.artifactPotential = artifactPotential;
    }

    public String displayName() {
        return displayName;
    }

    public CultivationRealm equivalentRealm() {
        return equivalentRealm;
    }

    public double artifactPotential() {
        return artifactPotential;
    }
}
