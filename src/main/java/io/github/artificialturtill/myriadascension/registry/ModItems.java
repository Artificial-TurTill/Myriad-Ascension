package io.github.artificialturtill.myriadascension.registry;

import io.github.artificialturtill.myriadascension.MyriadAscension;
import io.github.artificialturtill.myriadascension.clan.PrimordialisTestudoClan;
import io.github.artificialturtill.myriadascension.item.TechniqueManualItem;
import io.github.artificialturtill.myriadascension.technique.TechniqueCategory;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModItems {
    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(MyriadAscension.MOD_ID);

    private static final Map<ResourceLocation, Supplier<TechniqueManualItem>> TECHNIQUE_MANUALS =
            new LinkedHashMap<>();

    public static final Supplier<TechniqueManualItem> PRIMORDIALIS_TESTUDO_LONGEVITY_ART_MANUAL =
            registerTechniqueManual(
                    "primordialis_testudo_longevity_art_manual",
                    PrimordialisTestudoClan.PRIMORDIAL_TESTUDO_LONGEVITY_ART.id(),
                    TechniqueCategory.CULTIVATION,
                    PrimordialisTestudoClan.PRIMORDIAL_TESTUDO_LONGEVITY_ART.displayName());

    private ModItems() {
    }

    public static void register(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
        modEventBus.addListener(ModItems::addCreativeTabContents);
    }

    public static Supplier<TechniqueManualItem> manualFor(ResourceLocation techniqueId) {
        return TECHNIQUE_MANUALS.get(techniqueId);
    }

    private static Supplier<TechniqueManualItem> registerTechniqueManual(
            String itemPath,
            ResourceLocation techniqueId,
            TechniqueCategory category,
            String displayName) {

        Supplier<TechniqueManualItem> manual = ITEMS.registerItem(
                itemPath,
                properties -> new TechniqueManualItem(
                        techniqueId,
                        category,
                        displayName,
                        properties),
                new Item.Properties().stacksTo(1));

        Supplier<TechniqueManualItem> existing = TECHNIQUE_MANUALS.putIfAbsent(
                techniqueId,
                manual);

        if (existing != null) {
            throw new IllegalStateException(
                    "Technique already has a manual registered: " + techniqueId);
        }

        return manual;
    }

    private static void addCreativeTabContents(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            for (Supplier<TechniqueManualItem> manual : TECHNIQUE_MANUALS.values()) {
                event.accept(manual.get());
            }
        }
    }
}
