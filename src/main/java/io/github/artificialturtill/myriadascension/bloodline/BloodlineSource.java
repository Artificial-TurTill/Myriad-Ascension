package io.github.artificialturtill.myriadascension.bloodline;

import io.github.artificialturtill.myriadascension.cultivation.realm.CultivationRealm;

public record BloodlineSource(
        BloodlineAcquisitionType acquisitionType,
        CultivationRealm sourceRealm,
        int sourceSubdivision,
        double purity) {

    public BloodlineSource {
        purity = Math.max(0.0D, Math.min(100.0D, purity));
        sourceSubdivision = Math.max(0, sourceSubdivision);
    }
}
