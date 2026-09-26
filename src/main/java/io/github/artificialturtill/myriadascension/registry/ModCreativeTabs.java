package io.github.artificialturtill.myriadascension.registry;

import io.github.artificialturtill.myriadascension.MyriadAscension;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MyriadAscension.MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MYRIAD_ASCENSION =
            TABS.register(
                    "myriad_ascension",
                    () -> CreativeModeTab.builder()
                            .title(Component.translatable("itemGroup.myriad_ascension"))
                            .icon(() -> new ItemStack(ModItems.INSTRUCTION_TO_THE_MARTIAL_WORLD.get()))
                            .displayItems((parameters, output) ->
                                    ModItems.ITEMS.getEntries().forEach(holder -> output.accept(holder.get())))
                            .build());

    private ModCreativeTabs() {
    }

    public static void register(IEventBus modEventBus) {
        TABS.register(modEventBus);
    }
}
