package io.github.artificialturtill.myriadascension.cultivation.qi;

public final class QiExposureRules {
    private QiExposureRules() {
    }

    public static QiExposureStrength baseStrength(QiExposureType type) {
        return switch (type) {
            case SUPPRESSED -> QiExposureStrength.HIDDEN;
            case PASSIVE_AURA -> QiExposureStrength.FAINT;
            case ACTIVE_CIRCULATION -> QiExposureStrength.CLEAR;
            case CALM_CULTIVATION, WEAPON_INFUSION, QI_TECHNIQUE -> QiExposureStrength.CLEAR;
            case BURST -> QiExposureStrength.BEACON;
        };
    }
}
