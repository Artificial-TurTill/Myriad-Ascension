package io.github.artificialturtill.foundation;

import com.mojang.logging.LogUtils;
import io.github.artificialturtill.foundation.api.FoundationApi;
import io.github.artificialturtill.foundation.character.CharacterCombat;
import io.github.artificialturtill.foundation.character.CharacterService;
import io.github.artificialturtill.foundation.character.FoundationAttachments;
import io.github.artificialturtill.foundation.command.FoundationCommands;
import io.github.artificialturtill.foundation.network.FoundationNetwork;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;

/**
 * Ascension Foundation: the character engine extracted from Dragonblock C+ (JRMCore layer). It owns the character
 * data, the statistics maths, the damage pipeline, the creation wizard and the character sheet; content (races, power
 * types, classes, skills, transformations) comes from ARSN files of the Foundation and of add-ons.
 */
@Mod(FoundationApi.MOD_ID)
public final class FoundationMod {
    public static final Logger LOGGER = LogUtils.getLogger();

    public FoundationMod(IEventBus bus, ModContainer container) {
        FoundationAttachments.TYPES.register(bus);
        bus.addListener(FoundationNetwork::register);
        // Content: after every mod constructor ran (add-ons register their files there), on the main thread.
        bus.addListener((FMLCommonSetupEvent event) -> event.enqueueWork(() -> {
            FoundationApi.loadAll();
            var c = FoundationApi.content();
            LOGGER.info("Ascension Foundation content: {} races, {} power types, {} classes, {} skills, {} transformations",
                c.races().size(), c.powerTypes().size(), c.classes().size(), c.skills().size(), c.transformations().size());
        }));
        NeoForge.EVENT_BUS.addListener(FoundationCommands::register);
        NeoForge.EVENT_BUS.addListener(CharacterService::login);
        NeoForge.EVENT_BUS.addListener(CharacterService::respawn);
        NeoForge.EVENT_BUS.addListener(CharacterService::changeDimension);
        NeoForge.EVENT_BUS.addListener(CharacterService::tick);
        NeoForge.EVENT_BUS.addListener(CharacterCombat::incoming);
        NeoForge.EVENT_BUS.addListener(EventPriority.LOWEST, CharacterCombat::beforeDamage);
        NeoForge.EVENT_BUS.addListener(CharacterCombat::totem);
    }
}
