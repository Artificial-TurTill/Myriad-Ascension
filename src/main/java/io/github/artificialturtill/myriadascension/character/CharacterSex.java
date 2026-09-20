package io.github.artificialturtill.myriadascension.character;

import io.github.artificialturtill.myriadascension.affinity.BodyPolarity;
import java.util.Locale;

public enum CharacterSex {
    UNSET(BodyPolarity.UNSET),
    MALE(BodyPolarity.YANG),
    FEMALE(BodyPolarity.YIN);

    private final BodyPolarity polarity;

    CharacterSex(BodyPolarity polarity) {
        this.polarity = polarity;
    }

    public BodyPolarity polarity() {
        return polarity;
    }

    public static CharacterSex fromSerializedName(String value) {
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
