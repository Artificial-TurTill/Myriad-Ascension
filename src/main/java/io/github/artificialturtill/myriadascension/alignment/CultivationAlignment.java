package io.github.artificialturtill.myriadascension.alignment;

import java.util.Locale;

public enum CultivationAlignment {
    UNDECIDED,
    UNALIGNED,
    RIGHTEOUS,
    DEMONIC,
    BUDDHIST;

    public static CultivationAlignment fromSerializedName(String value) {
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
