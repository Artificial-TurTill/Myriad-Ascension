package io.github.artificialturtill.myriadascension.client;

import io.github.artificialturtill.myriadascension.client.screen.CultivationQuickMenuScreen;
import io.github.artificialturtill.myriadascension.network.QuickMenuRequestPayload;
import io.github.artificialturtill.myriadascension.network.QuickMenuSnapshotPayload;
import net.minecraft.client.Minecraft;
import net.neoforged.neoforge.network.PacketDistributor;

public final class ClientQuickMenuPayloadHandler {
    private static boolean expectingOpen;

    private ClientQuickMenuPayloadHandler() {
    }

    public static void requestOpen() {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || minecraft.getConnection() == null) {
            return;
        }

        expectingOpen = true;
        PacketDistributor.sendToServer(new QuickMenuRequestPayload());
    }

    public static void handleSnapshot(QuickMenuSnapshotPayload payload) {
        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.screen instanceof CultivationQuickMenuScreen screen) {
            screen.updateSnapshot(payload);
            return;
        }

        if (!expectingOpen) {
            return;
        }

        expectingOpen = false;
        minecraft.setScreen(new CultivationQuickMenuScreen(payload));
    }

    public static void cancelExpectedOpen() {
        expectingOpen = false;
    }
}
