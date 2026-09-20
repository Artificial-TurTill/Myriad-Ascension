package io.github.artificialturtill.myriadascension.alignment;

import java.util.Locale;

public enum CultivationAffiliation {
    UNDECIDED,
    UNAFFILIATED,
    RIGHTEOUS,
    UNORTHODOX,
    DEMONIC,
    BUDDHIST,
    IMPERIAL;

    public static CultivationAffiliation fromSerializedName(String value) {
        if (value == null || value.isBlank()) {
            return UNDECIDED;
        }

        try {
            return valueOf(value.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ignored) {
            return UNDECIDED;
        }
    }
}
