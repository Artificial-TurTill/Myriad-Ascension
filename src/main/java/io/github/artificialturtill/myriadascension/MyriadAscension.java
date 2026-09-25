package io.github.artificialturtill.myriadascension;

import com.mojang.logging.LogUtils;
import io.github.artificialturtill.myriadascension.alpha.AlphaCommands;
import io.github.artificialturtill.myriadascension.cultivation.data.ModAttachments;
import io.github.artificialturtill.myriadascension.cultivation.data.PlayerCultivationEvents;
import io.github.artificialturtill.myriadascension.network.ModNetworking;
import io.github.artificialturtill.myriadascension.registry.ModArmorMaterials;
import io.github.artificialturtill.myriadascension.registry.ModItems;
import io.github.artificialturtill.myriadascension.registry.ModMenus;
import io.github.artificialturtill.myriadascension.training.PlayerTrainingEvents;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;

@Mod(MyriadAscension.MOD_ID)
public final class MyriadAscension {
    public static final String MOD_ID = "myriad_ascension";
    public static final Logger LOGGER = LogUtils.getLogger();

    public MyriadAscension(IEventBus modEventBus, ModContainer modContainer) {
        ModAttachments.register(modEventBus);
        ModArmorMaterials.register(modEventBus);
        ModItems.register(modEventBus);
        ModMenus.register(modEventBus);
        modEventBus.addListener(ModNetworking::registerPayloads);
        NeoForge.EVENT_BUS.addListener(PlayerCultivationEvents::onPlayerLoggedIn);
        NeoForge.EVENT_BUS.addListener(PlayerCultivationEvents::onPlayerClone);
        NeoForge.EVENT_BUS.addListener(PlayerTrainingEvents::onPlayerTick);
        NeoForge.EVENT_BUS.addListener(PlayerTrainingEvents::onLivingIncomingDamage);
        NeoForge.EVENT_BUS.addListener(PlayerTrainingEvents::onLivingFall);
        NeoForge.EVENT_BUS.addListener(AlphaCommands::register);

        LOGGER.info("Myriad Ascension initializing on NeoForge.");
    }
}
