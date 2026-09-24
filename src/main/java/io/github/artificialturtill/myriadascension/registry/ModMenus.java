package io.github.artificialturtill.myriadascension.registry;

import io.github.artificialturtill.myriadascension.MyriadAscension;
import io.github.artificialturtill.myriadascension.menu.MinorStorageBagMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModMenus {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, MyriadAscension.MOD_ID);

    public static final DeferredHolder<MenuType<?>, MenuType<MinorStorageBagMenu>>
            MINOR_STORAGE_BAG = MENUS.register(
                    "minor_storage_bag",
                    () -> IMenuTypeExtension.create(MinorStorageBagMenu::new));

    private ModMenus() {
    }

    public static void register(IEventBus modEventBus) {
        MENUS.register(modEventBus);
    }

}
