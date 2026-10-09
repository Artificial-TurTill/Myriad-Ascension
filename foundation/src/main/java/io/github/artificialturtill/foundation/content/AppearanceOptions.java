package io.github.artificialturtill.foundation.content;

import java.util.List;

/**
 * What the creation wizard offers for a race (JRMCore GUI 0 "Appearance"): colour presets, whether the race has hair
 * (edited with the hairstyle manager; hair colours are quick presets, any colour can be picked), body types, heights.
 */
public record AppearanceOptions(List<Integer> skinColors, boolean hair, List<Integer> hairColors, List<Integer> eyeColors,
                                int bodyTypes, int minHeight, int maxHeight) {
}
