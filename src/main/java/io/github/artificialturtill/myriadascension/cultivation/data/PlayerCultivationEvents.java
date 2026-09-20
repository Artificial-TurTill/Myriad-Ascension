package io.github.artificialturtill.myriadascension.cultivation.data;

import net.neoforged.neoforge.event.entity.player.PlayerEvent;

public final class PlayerCultivationEvents {
    private PlayerCultivationEvents() {
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
