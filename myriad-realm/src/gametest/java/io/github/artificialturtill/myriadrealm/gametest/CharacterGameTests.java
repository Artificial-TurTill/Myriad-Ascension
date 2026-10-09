package io.github.artificialturtill.myriadrealm.gametest;

import io.github.artificialturtill.foundation.api.FoundationApi;
import io.github.artificialturtill.foundation.character.Appearance;
import io.github.artificialturtill.foundation.character.CharacterProfile;
import io.github.artificialturtill.foundation.character.CharacterService;
import io.github.artificialturtill.foundation.character.Fighter;
import io.github.artificialturtill.foundation.character.FoundationAttachments;
import io.github.artificialturtill.foundation.character.VanillaBridge;
import io.github.artificialturtill.myriadrealm.MyriadRealm;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/** Both mods in a real server: content loaded by the Foundation, Qi characters from Myriad Realm, real damage events. */
@GameTestHolder(MyriadRealm.MOD_ID)
@PrefixGameTestTemplate(false)
public final class CharacterGameTests {
    private static Appearance look() { return Appearance.defaults(FoundationApi.content().race(FoundationApi.HUMAN)); }
    private static Fighter create(Player player, String power, String cls) {
        CharacterProfile profile = player.getData(FoundationAttachments.CHARACTER);
        profile.create(FoundationApi.content(), FoundationApi.HUMAN, power, cls, look());
        VanillaBridge.apply(player);
        return profile.fighter();
    }

    @GameTest(template = "empty", timeoutTicks = 40)
    public static void contentIsLoadedFromBothMods(GameTestHelper test) {
        var content = FoundationApi.content();
        test.assertTrue(content.powerType(FoundationApi.NATURAL) != null && content.powerType(MyriadRealm.QI) != null, "Natural and Qi must be loaded");
        test.assertTrue(content.classesOf(MyriadRealm.QI).size() == 3, "Qi must have three classes");
        test.assertTrue(content.raceList().size() == 1, "Only Humans for now");
        test.succeed();
    }

    @GameTest(template = "empty", timeoutTicks = 40)
    public static void qiBodyIsTheOnlyLife(GameTestHelper test) {
        Player player = test.makeMockPlayer(GameType.SURVIVAL);
        Fighter f = create(player, MyriadRealm.QI, "myriad_realm:martial_cultivator");
        test.assertTrue(f.body() == 20 && f.caps().energy() == 40, "A fresh Qi character has Body 20 and Qi 40, got " + f.body() + " / " + f.caps().energy());
        player.hurt(test.getLevel().damageSources().generic(), 5F);
        test.assertTrue(f.body() == 15, "Generic 5 damage must take 5 Body (Defense is 0 at release 0), Body " + f.body());
        test.assertTrue(player.getHealth() == player.getMaxHealth(), "Vanilla health must not move");
        test.assertTrue(f.recoveryDelay() > 0, "A hit must pause regeneration");
        player.invulnerableTime = 0;
        player.hurt(test.getLevel().damageSources().generic(), 100F);
        test.assertTrue(f.body() == 0 && !player.isAlive(), "Body 0 must kill");
        test.succeed();
    }

    @GameTest(template = "empty", timeoutTicks = 40)
    public static void releasedMeleeAddsToPunches(GameTestHelper test) {
        Player player = test.makeMockPlayer(GameType.SURVIVAL);
        Fighter f = create(player, MyriadRealm.QI, "myriad_realm:body_cultivator");
        f.setAttribute(0, 4);   // Melee 2.5 x 4 +10 % = 11
        f.setAttribute(2, 10);  // enough Stamina for powered punches
        f.restoreResources();
        f.setRelease(100);
        Cow cow = test.spawn(EntityType.COW, new BlockPos(1, 2, 1));
        cow.getAttribute(Attributes.MAX_HEALTH).setBaseValue(100D); // a cow's 10 health would cap the measured hit
        cow.setHealth(100F);
        float before = cow.getHealth();
        int stamina = f.stamina();
        player.attack(cow);
        test.assertTrue(before - cow.getHealth() >= 11F, "Released Melee must be added to the punch, dealt " + (before - cow.getHealth()));
        test.assertTrue(f.stamina() == stamina - f.punchCost(), "A powered punch costs Stamina");
        test.succeed();
    }

    @GameTest(template = "empty", timeoutTicks = 40)
    public static void naturalTrainingUsesVanillaAttributesAndHeightScales(GameTestHelper test) {
        Player player = test.makeMockPlayer(GameType.SURVIVAL);
        Fighter f = create(player, FoundationApi.NATURAL, FoundationApi.UNCLASSED);
        test.assertTrue(player.getMaxHealth() == 20F, "A fresh Natural character is vanilla");
        f.setAttribute(2, 20);
        VanillaBridge.apply(player);
        test.assertTrue(player.getMaxHealth() == 40F, "CON 20 -> Health 40, got " + player.getMaxHealth());
        f.setAppearance(new Appearance(0, look().skinColor(), 0, look().hairColor(), look().eyeColor(), 0, 110));
        VanillaBridge.apply(player);
        test.assertTrue(Math.abs(player.getAttributeValue(Attributes.SCALE) - 1.1) < 1e-6, "Height 110 % must scale the player");
        player.getData(FoundationAttachments.CHARACTER).erase();
        VanillaBridge.apply(player);
        test.assertTrue(player.getMaxHealth() == 20F && player.getAttributeValue(Attributes.SCALE) == 1.0, "Erase must restore vanilla");
        test.succeed();
    }

    @GameTest(template = "empty", timeoutTicks = 40)
    public static void characterSurvivesSaveAndLoad(GameTestHelper test) {
        Player player = test.makeMockPlayer(GameType.SURVIVAL);
        Fighter f = create(player, MyriadRealm.QI, "myriad_realm:spirit_cultivator");
        f.addTp(123);
        CompoundTag saved = player.getData(FoundationAttachments.CHARACTER).save();
        CharacterProfile loaded = CharacterProfile.load(saved);
        test.assertTrue(loaded.hasCharacter() && loaded.fighter().tp() == 123 && loaded.fighter().characterClass().id().equals("myriad_realm:spirit_cultivator"),
            "The character must survive save and load");
        test.assertTrue(CharacterService.fighter(player) == f, "The service must see the attached character");
        test.succeed();
    }
}
