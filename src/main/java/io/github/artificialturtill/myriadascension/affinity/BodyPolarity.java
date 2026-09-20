package io.github.artificialturtill.myriadascension.affinity;

import java.util.Locale;

public enum BodyPolarity {
    UNSET,
    YIN,
    YANG;

    public static BodyPolarity fromSerializedName(String value) {
        if (value == null || value.isBlank()) {
            return UNSET;
        }

        try {
            return valueOf(value.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ignored) {
            return UNSET;
        }
    }
}
