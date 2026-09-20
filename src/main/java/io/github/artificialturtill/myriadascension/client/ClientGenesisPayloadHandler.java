package io.github.artificialturtill.myriadascension.client;

import io.github.artificialturtill.myriadascension.client.screen.CharacterGenesisScreen;
import io.github.artificialturtill.myriadascension.network.GenesisResultPayload;
import net.minecraft.client.Minecraft;

public final class ClientGenesisPayloadHandler {
    private ClientGenesisPayloadHandler() {
    }

    public static void openGenesis() {
        Minecraft minecraft = Minecraft.getInstance();
        if (!(minecraft.gui.screen() instanceof CharacterGenesisScreen)) {
            minecraft.gui.setScreen(new CharacterGenesisScreen());
        }
    }

    public static void handleGenesisResult(GenesisResultPayload payload) {
        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.gui.screen() instanceof CharacterGenesisScreen screen) {
            screen.applyResult(payload);
            return;
        }

        CharacterGenesisScreen screen = new CharacterGenesisScreen();
        minecraft.gui.setScreen(screen);
        screen.applyResult(payload);
    }
}
