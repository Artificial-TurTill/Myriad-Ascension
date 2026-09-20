package io.github.artificialturtill.myriadascension.client;

import io.github.artificialturtill.myriadascension.MyriadAscension;
import io.github.artificialturtill.myriadascension.network.ClientPayloadBridge;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;

@Mod(value = MyriadAscension.MOD_ID, dist = Dist.CLIENT)
public final class MyriadAscensionClient {
    public MyriadAscensionClient(IEventBus modEventBus) {
        ClientPayloadBridge.installGenesisHandlers(
                ClientGenesisPayloadHandler::openGenesis,
                ClientGenesisPayloadHandler::handleGenesisResult);

        modEventBus.addListener(ClientKeyMappings::register);
        NeoForge.EVENT_BUS.addListener(ClientCultivationInput::onClientTick);
    }
}
