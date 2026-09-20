package io.github.artificialturtill.myriadascension.affinity;

import io.github.artificialturtill.myriadascension.character.CharacterSex;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.util.RandomSource;

public final class StartingAffinityGenerator {
    public static final int RANDOM_POINT_POOL = 10;
    public static final int STARTER_AFFINITY_CAP = 10;

    private static final AffinityType[] FIVE_ELEMENTS = {
            AffinityType.WOOD,
            AffinityType.FIRE,
            AffinityType.EARTH,
            AffinityType.METAL,
            AffinityType.WATER
    };

    private StartingAffinityGenerator() {
    }

    /**
     * Generates the starting seven-aspect profile.
     *
     * <p>Sex establishes a fixed Yin/Yang baseline first:
     * male = Yang 2 / Yin 1, female = Yin 2 / Yang 1.
     * Ten additional random points are then distributed among the five elements
     * plus the matching polarity. The opposite polarity cannot receive those
     * bonus points. No starting aspect may exceed 10.</p>
     */
    public static AffinityProfile generate(CharacterSex sex, RandomSource random) {
        if (sex == null || sex == CharacterSex.UNSET) {
            throw new IllegalArgumentException("Character sex must be selected before affinity generation.");
        }

        AffinityProfile profile = new AffinityProfile();
        AffinityType favoredPolarity;

        if (sex == CharacterSex.MALE) {
            profile.set(AffinityType.YIN, 1);
            profile.set(AffinityType.YANG, 2);
            favoredPolarity = AffinityType.YANG;
        } else {
            profile.set(AffinityType.YIN, 2);
            profile.set(AffinityType.YANG, 1);
            favoredPolarity = AffinityType.YIN;
        }

        List<AffinityType> eligible = new ArrayList<>(List.of(FIVE_ELEMENTS));
        eligible.add(favoredPolarity);

        int remaining = RANDOM_POINT_POOL;
        while (remaining > 0) {
            AffinityType chosen = eligible.get(random.nextInt(eligible.size()));
            if (profile.get(chosen) >= STARTER_AFFINITY_CAP) {
                eligible.remove(chosen);
                if (eligible.isEmpty()) {
                    break;
                }
                continue;
            }

            profile.set(chosen, profile.get(chosen) + 1);
            remaining--;
        }

        return profile;
    }
}
