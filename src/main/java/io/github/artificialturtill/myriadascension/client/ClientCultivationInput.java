package io.github.artificialturtill.myriadascension.client;

import io.github.artificialturtill.myriadascension.network.QiControlAction;
import io.github.artificialturtill.myriadascension.network.QiControlPayload;
import net.minecraft.client.Minecraft;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

public final class ClientCultivationInput {
    private static final int HELD_CONTROL_PULSE_TICKS = 4;
    private static int tickCounter;

    private ClientCultivationInput() {
    }

    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || minecraft.getConnection() == null) {
            return;
        }

        tickCounter++;

        // If both are held, treat them as opposing inputs and do not send either pulse.
        if (tickCounter % HELD_CONTROL_PULSE_TICKS == 0) {
            boolean gather = ClientKeyMappings.GATHER.isDown();
            boolean suppress = ClientKeyMappings.SUPPRESS.isDown();

            if (gather && !suppress) {
                PacketDistributor.sendToServer(new QiControlPayload(QiControlAction.GATHER_AND_CIRCULATE));
            } else if (suppress && !gather) {
                PacketDistributor.sendToServer(new QiControlPayload(QiControlAction.SUPPRESS_CIRCULATION));
            }
        }

        while (ClientKeyMappings.BURST.consumeClick()) {
            PacketDistributor.sendToServer(new QiControlPayload(QiControlAction.TOGGLE_BURST));
        }

        // Reserve the inputs now. Their screens are implemented in later milestones.
        while (ClientKeyMappings.QUICK_MENU.consumeClick()) {
            // X quick menu: intentionally not opened until the menu implementation is ready.
        }

        while (ClientKeyMappings.STATUS.consumeClick()) {
            ClientCultivatorScreens.openStatus();
        }
    }
}
