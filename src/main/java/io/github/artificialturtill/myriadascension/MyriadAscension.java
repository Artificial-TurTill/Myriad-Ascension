package io.github.artificialturtill.myriadascension;

import com.mojang.logging.LogUtils;
import io.github.artificialturtill.myriadascension.cultivation.data.ModAttachments;
import io.github.artificialturtill.myriadascension.cultivation.data.PlayerCultivationEvents;
import io.github.artificialturtill.myriadascension.network.ModNetworking;
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
        modEventBus.addListener(ModNetworking::registerPayloads);
        NeoForge.EVENT_BUS.addListener(PlayerCultivationEvents::onPlayerClone);

        LOGGER.info("Myriad Ascension initializing on NeoForge.");
    }
}
