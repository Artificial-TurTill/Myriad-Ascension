package io.github.artificialturtill.myriadascension;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

@Mod(MyriadAscension.MOD_ID)
public final class MyriadAscension {
    public static final String MOD_ID = "myriad_ascension";
    public static final Logger LOGGER = LogUtils.getLogger();

    public MyriadAscension(IEventBus modEventBus, ModContainer modContainer) {
        LOGGER.info("Myriad Ascension initializing on NeoForge.");
    }
}
