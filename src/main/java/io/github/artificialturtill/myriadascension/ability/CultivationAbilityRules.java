package io.github.artificialturtill.myriadascension.ability;

import io.github.artificialturtill.myriadascension.cultivation.realm.CultivationRealm;
import java.util.ArrayList;
import java.util.List;

public final class CultivationAbilityRules {
    private CultivationAbilityRules() {
    }

    public static List<CultivationAbilityState> forRealm(CultivationRealm realm, int stage) {
        List<CultivationAbilityState> abilities = new ArrayList<>();

        add(abilities, "Body Tempering",
                atLeast(realm, stage, CultivationRealm.TEMPERED_BODY, 1),
                "Cultivation-grade physical conditioning.");
        add(abilities, "World Energy Sense",
                atLeast(realm, stage, CultivationRealm.TEMPERED_BODY, 4),
                "Perceive ambient World Energy.");
        add(abilities, "Energy Identification",
                atLeast(realm, stage, CultivationRealm.TEMPERED_BODY, 6),
                "Distinguish broad elemental/energy characteristics.");
        add(abilities, "Natural Yuan Qi Formation",
                atLeast(realm, stage, CultivationRealm.TEMPERED_BODY, 7),
                "Naturally absorb World Energy into Yuan Qi; still sealed from active use.");
        add(abilities, "Conscious Yuan Qi Circulation",
                atLeast(realm, stage, CultivationRealm.INITIAL_ELEMENT, 1),
                "Actively circulate Yuan Qi and choose Power output.");
        add(abilities, "Internal Qi Techniques",
                atLeast(realm, stage, CultivationRealm.INITIAL_ELEMENT, 1),
                "Reinforce body, movement, contact strikes and held weapons.");
        add(abilities, "Stable Weapon Qi Conduction",
                atLeast(realm, stage, CultivationRealm.INITIAL_ELEMENT, 4),
                "Maintain Yuan Qi through a held weapon with reduced waste.");
        add(abilities, "High-Output Internal Arts",
                atLeast(realm, stage, CultivationRealm.INITIAL_ELEMENT, 7),
                "Sustain stronger internal techniques and Burst output.");
        add(abilities, "External Qi Projection",
                atLeast(realm, stage, CultivationRealm.QI_TRANSFORMATION, 1),
                "Release Yuan Qi beyond the body.");
        add(abilities, "Sustained External Barriers",
                atLeast(realm, stage, CultivationRealm.QI_TRANSFORMATION, 4),
                "Maintain projected defenses and aura techniques.");
        add(abilities, "Advanced Qi Projection",
                atLeast(realm, stage, CultivationRealm.QI_TRANSFORMATION, 7),
                "Use layered, sustained and high-output projected techniques.");
        add(abilities, "Parallel Circulation",
                atLeast(realm, stage, CultivationRealm.SEPARATION_AND_REUNION, 1),
                "Coordinate multiple internal/external circulation routes.");
        add(abilities, "Integrated Technique Flow",
                atLeast(realm, stage, CultivationRealm.SEPARATION_AND_REUNION, 4),
                "Combine movement, weapon and cultivation techniques efficiently.");
        add(abilities, "Precision Yuan Qi Control",
                atLeast(realm, stage, CultivationRealm.SEPARATION_AND_REUNION, 7),
                "Minimize waste and maintain several technique demands at once.");
        add(abilities, "True Qi",
                atLeast(realm, stage, CultivationRealm.TRUE_ELEMENT, 1),
                "Purified energy supporting higher Secret Techniques.");
        add(abilities, "Cultivation Flight",
                atLeast(realm, stage, CultivationRealm.TRUE_ELEMENT, 1),
                "Fly using True Qi; early flight is costly and unstable.");
        add(abilities, "Stable Flight",
                atLeast(realm, stage, CultivationRealm.TRUE_ELEMENT, 4),
                "Sustain efficient directional flight.");
        add(abilities, "High-Speed True Qi Flight",
                atLeast(realm, stage, CultivationRealm.TRUE_ELEMENT, 7),
                "High-output travel and aerial combat.");
        add(abilities, "Knowledge Sea",
                atLeast(realm, stage, CultivationRealm.IMMORTAL_ASCENSION, 1),
                "Forms the spiritual sea supporting the Divine Soul.");
        add(abilities, "Divine Sense",
                atLeast(realm, stage, CultivationRealm.IMMORTAL_ASCENSION, 1),
                "Nonvisual spiritual perception.");
        add(abilities, "Remote Artifact Control",
                atLeast(realm, stage, CultivationRealm.IMMORTAL_ASCENSION, 4),
                "Control suitable artifacts with Divine Sense.");
        add(abilities, "Divine Soul Combat",
                atLeast(realm, stage, CultivationRealm.IMMORTAL_ASCENSION, 7),
                "Perform and resist direct soul-oriented techniques.");
        add(abilities, "Transcendent Physique",
                atLeast(realm, stage, CultivationRealm.TRANSCENDENT, 1),
                "Body and lifespan surpass ordinary lower-world limits.");
        add(abilities, "Deep Body-Soul Integration",
                atLeast(realm, stage, CultivationRealm.TRANSCENDENT, 2),
                "True Qi, body and soul function as a stronger unified foundation.");
        add(abilities, "Peak Lower-World Foundation",
                atLeast(realm, stage, CultivationRealm.TRANSCENDENT, 3),
                "Foundation prepared for Saint Qi transformation.");
        add(abilities, "Saint Qi",
                atLeast(realm, stage, CultivationRealm.SAINT, 1),
                "True Qi evolves into Saint Qi.");
        add(abilities, "Starry-Sky Resistance",
                atLeast(realm, stage, CultivationRealm.SAINT, 2),
                "Body increasingly resists extreme Starfield conditions.");
        add(abilities, "Unprotected Starry-Sky Survival",
                atLeast(realm, stage, CultivationRealm.SAINT, 3),
                "Most cultivators can survive the Starry Sky without a defensive artifact.");
        add(abilities, "Dense Saint Qi",
                atLeast(realm, stage, CultivationRealm.SAINT_KING, 1),
                "Saint Qi becomes much denser and more stable.");
        add(abilities, "Starfield Travel Resilience",
                atLeast(realm, stage, CultivationRealm.SAINT_KING, 2),
                "Greatly improved tolerance of spatial and interstellar travel.");
        add(abilities, "Shi Foundation",
                atLeast(realm, stage, CultivationRealm.SAINT_KING, 3),
                "Will, Divine Soul and Saint Qi are prepared to manifest Shi.");
        add(abilities, "Shi",
                atLeast(realm, stage, CultivationRealm.ORIGIN_RETURNING, 1),
                "Suppress lower cultivators through Will + Saint Qi + Divine Soul.");
        add(abilities, "Expanded Shi",
                atLeast(realm, stage, CultivationRealm.ORIGIN_RETURNING, 2),
                "Greater Shi radius, pressure and resistance.");
        add(abilities, "Grand Accomplishment Shi",
                atLeast(realm, stage, CultivationRealm.ORIGIN_RETURNING, 3),
                "Shi reaches the threshold required for Domain formation.");
        add(abilities, "Domain",
                atLeast(realm, stage, CultivationRealm.ORIGIN_KING, 1),
                "Establish authority over a surrounding region.");
        add(abilities, "Mature Domain",
                atLeast(realm, stage, CultivationRealm.ORIGIN_KING, 2),
                "Stronger environmental manipulation and Domain integrity.");
        add(abilities, "Grand Accomplishment Domain",
                atLeast(realm, stage, CultivationRealm.ORIGIN_KING, 3),
                "Domain reaches the threshold for Dao Source.");
        add(abilities, "Source Qi",
                atLeast(realm, stage, CultivationRealm.DAO_SOURCE, 1),
                "Saint Qi is transformed into Source Qi.");
        add(abilities, "World Principle Sense",
                atLeast(realm, stage, CultivationRealm.DAO_SOURCE, 1),
                "Sense and begin borrowing World Principles.");
        add(abilities, "Principle Transfer",
                atLeast(realm, stage, CultivationRealm.DAO_SOURCE, 2),
                "Transfer Principle Strength into techniques reliably.");
        add(abilities, "Deep Principle Control",
                atLeast(realm, stage, CultivationRealm.DAO_SOURCE, 3),
                "High-grade control of the cultivator's principal Dao.");
        add(abilities, "Emperor Qi",
                atLeast(realm, stage, CultivationRealm.EMPEROR, 1),
                "Source Qi begins evolving into Emperor Qi.");
        add(abilities, "Emperor Pressure",
                atLeast(realm, stage, CultivationRealm.EMPEROR, 1),
                "Presence itself can suppress much weaker beings.");
        add(abilities, "Direct Principle Control",
                atLeast(realm, stage, CultivationRealm.EMPEROR, 2),
                "Manipulate relevant Principles directly.");
        add(abilities, "Mature Emperor Foundation",
                atLeast(realm, stage, CultivationRealm.EMPEROR, 3),
                "Peak normal Emperor foundation.");
        add(abilities, "Peak Emperor",
                realm.ordinal() >= CultivationRealm.PSEUDO_GREAT_EMPEROR.ordinal(),
                "Power beyond ordinary 3rd Order Emperor.");
        add(abilities, "Dao Seal",
                realm.ordinal() >= CultivationRealm.DAO_SEAL.ordinal(),
                "Condense lifetime cultivation around a Grand Dao.");
        add(abilities, "Embryonic World Force",
                realm.ordinal() >= CultivationRealm.HALF_STEP_OPEN_HEAVEN.ordinal(),
                "Elemental condensation begins generating embryonic World Force.");
        add(abilities, "Small Universe & World Force",
                realm.ordinal() >= CultivationRealm.OPEN_HEAVEN.ordinal(),
                "Primary power comes from an internal Small Universe.");
        add(abilities, "Living Small Universe",
                realm == CultivationRealm.WORLD_CREATION
                        || (realm == CultivationRealm.OPEN_HEAVEN && stage >= 7),
                "Small Universe becomes a complete Universe World capable of supporting life.");
        add(abilities, "Universe Creation",
                realm == CultivationRealm.WORLD_CREATION,
                "Create true Universe Worlds and life through perfected Dao authority.");

        return List.copyOf(abilities);
    }

    public static List<CultivationAbilityState> relevantForDisplay(CultivationRealm realm, int stage) {
        List<CultivationAbilityState> all = forRealm(realm, stage);
        List<CultivationAbilityState> result = new ArrayList<>();

        for (CultivationAbilityState state : all) {
            if (state.unlocked()) {
                result.add(state);
            }
        }

        int lockedAdded = 0;
        for (CultivationAbilityState state : all) {
            if (!state.unlocked() && lockedAdded < 4) {
                result.add(state);
                lockedAdded++;
            }
        }

        return List.copyOf(result);
    }

    private static void add(
            List<CultivationAbilityState> target,
            String name,
            boolean unlocked,
            String description) {
        target.add(new CultivationAbilityState(name, unlocked, description));
    }

    private static boolean atLeast(
            CultivationRealm currentRealm,
            int currentStage,
            CultivationRealm requiredRealm,
            int requiredStage) {

        if (currentRealm.ordinal() > requiredRealm.ordinal()) {
            return true;
        }
        if (currentRealm != requiredRealm) {
            return false;
        }
        return currentStage >= requiredStage;
    }
}
