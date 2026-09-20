package io.github.artificialturtill.myriadascension.cultivation.data;

import io.github.artificialturtill.myriadascension.network.OpenGenesisPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;

public final class PlayerCultivationEvents {
    private PlayerCultivationEvents() {
    }

    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        CultivatorData data = player.getData(ModAttachments.CULTIVATOR_DATA);
        if (!data.hasCompletedInitialSetup()) {
            PacketDistributor.sendToPlayer(player, OpenGenesisPayload.INSTANCE);
        }
    }

    public static void onPlayerClone(PlayerEvent.Clone event) {
        if (!event.isWasDeath()) {
            return;
        }

        CultivatorData original = event.getOriginal().getData(ModAttachments.CULTIVATOR_DATA);
        CultivatorData respawned = new CultivatorData();
        respawned.copyFrom(original);
        respawned.applyDeathRecoveryState();

        event.getEntity().setData(ModAttachments.CULTIVATOR_DATA, respawned);
    }
}
