package io.github.artificialturtill.foundation.content;

import java.util.List;

/** What the creation wizard offers for a race (JRMCore GUI 0 "Appearance"): colour presets, styles and heights. */
public record AppearanceOptions(List<Integer> skinColors, int hairStyles, List<Integer> hairColors, List<Integer> eyeColors,
                                int bodyTypes, int minHeight, int maxHeight) {
    public boolean canHaveHair() { return hairStyles > 0 && !hairColors.isEmpty(); }
}
