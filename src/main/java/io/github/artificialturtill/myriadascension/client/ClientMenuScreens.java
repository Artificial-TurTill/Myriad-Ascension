package io.github.artificialturtill.myriadascension.client;

import io.github.artificialturtill.myriadascension.client.screen.MinorStorageBagScreen;
import io.github.artificialturtill.myriadascension.registry.ModMenus;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

public final class ClientMenuScreens {
    private ClientMenuScreens() {
    }

    public static void register(RegisterMenuScreensEvent event) {
        event.register(
                ModMenus.MINOR_STORAGE_BAG.get(),
                MinorStorageBagScreen::new);
    }
}
