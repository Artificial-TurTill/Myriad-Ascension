package io.github.artificialturtill.myriadascension.client;

import io.github.artificialturtill.myriadascension.client.screen.CultivatorStatusScreen;
import io.github.artificialturtill.myriadascension.network.CultivatorSyncPayload;
import net.minecraft.client.Minecraft;

public final class ClientCultivatorScreens {
    private ClientCultivatorScreens() {
    }

    public static void openStatus() {
        CultivatorSyncPayload snapshot = ClientCultivatorState.snapshot();
        if (snapshot == null) {
            return;
        }

        Minecraft.getInstance().setScreen(new CultivatorStatusScreen(snapshot));
    }
}
