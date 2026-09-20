package io.github.artificialturtill.myriadascension.world.beast;

public final class TortoiseRarityRules {
    /**
     * World-generation target, not a hard guarantee:
     * about five rare ancient tortoise candidates per 10,000 x 10,000 block area.
     */
    public static final int TARGET_AREA_WIDTH_BLOCKS = 10_000;
    public static final int TARGET_AREA_LENGTH_BLOCKS = 10_000;
    public static final int TARGET_ANCIENT_TORTOISES_PER_AREA = 5;

    public static final double TARGET_BLOCKS_PER_ANCIENT_TORTOISE =
            (double) TARGET_AREA_WIDTH_BLOCKS
                    * TARGET_AREA_LENGTH_BLOCKS
                    / TARGET_ANCIENT_TORTOISES_PER_AREA;

    private TortoiseRarityRules() {
    }
}
