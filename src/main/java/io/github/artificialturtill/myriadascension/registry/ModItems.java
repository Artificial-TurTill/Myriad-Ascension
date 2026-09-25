package io.github.artificialturtill.myriadascension.registry;

import io.github.artificialturtill.myriadascension.MyriadAscension;
import io.github.artificialturtill.myriadascension.artifact.ArtifactArmorItem;
import io.github.artificialturtill.myriadascension.artifact.ArtifactGrade;
import io.github.artificialturtill.myriadascension.artifact.ArtifactShieldItem;
import io.github.artificialturtill.myriadascension.artifact.ArtifactWeaponItem;
import io.github.artificialturtill.myriadascension.artifact.MinorStorageBagItem;
import io.github.artificialturtill.myriadascension.clan.PrimordialisTestudoClan;
import io.github.artificialturtill.myriadascension.item.MartialWorldGuideItem;
import io.github.artificialturtill.myriadascension.item.TechniqueManualItem;
import io.github.artificialturtill.myriadascension.item.medicine.AntidotePillItem;
import io.github.artificialturtill.myriadascension.item.medicine.MedicineItem;
import io.github.artificialturtill.myriadascension.item.medicine.MedicineProfile;
import io.github.artificialturtill.myriadascension.item.medicine.PurificationPillItem;
import io.github.artificialturtill.myriadascension.item.poison.MeridianPoisonItem;
import io.github.artificialturtill.myriadascension.technique.TechniqueCategory;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.component.ItemContainerContents;
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

    public static final Supplier<MedicineItem> MINOR_BODY_MENDING_PILL =
            ITEMS.registerItem(
                    "minor_body_mending_pill",
                    properties -> new MedicineItem(
                            new MedicineProfile(
                                    6.0D,
                                    6.0D,
                                    1.0D,
                                    0.0D,
                                    3.0D,
                                    2.0D,
                                    8.0D,
                                    1.0D),
                            properties),
                    new Item.Properties().stacksTo(16));

    public static final Supplier<MedicineItem> MERIDIAN_SOOTHING_PILL =
            ITEMS.registerItem(
                    "meridian_soothing_pill",
                    properties -> new MedicineItem(
                            new MedicineProfile(
                                    1.0D,
                                    1.0D,
                                    7.0D,
                                    0.0D,
                                    1.0D,
                                    18.0D,
                                    2.0D,
                                    0.75D),
                            properties),
                    new Item.Properties().stacksTo(16));

    public static final Supplier<AntidotePillItem> BASIC_ANTIDOTE_PILL =
            ITEMS.registerItem(
                    "basic_antidote_pill",
                    AntidotePillItem::new,
                    new Item.Properties().stacksTo(16));

    public static final Supplier<PurificationPillItem> MINOR_PURIFICATION_PILL =
            ITEMS.registerItem(
                    "minor_purification_pill",
                    PurificationPillItem::new,
                    new Item.Properties().stacksTo(16));

    public static final Supplier<Item> BASIC_TRAINING_WEIGHT =
            ITEMS.registerItem(
                    "basic_training_weight",
                    Item::new,
                    new Item.Properties().stacksTo(4));

    public static final Supplier<MeridianPoisonItem> CRUDE_MERIDIAN_POISON =
            ITEMS.registerItem(
                    "crude_meridian_poison",
                    MeridianPoisonItem::new,
                    new Item.Properties().stacksTo(8));

    public static final Supplier<ArtifactWeaponItem> TEMPERED_BODY_ARTIFACT_SWORD =
            ITEMS.registerItem(
                    "tempered_body_artifact_sword",
                    properties -> new ArtifactWeaponItem(
                            Tiers.IRON,
                            ArtifactGrade.TEMPERED_BODY,
                            properties),
                    new Item.Properties()
                            .durability(420)
                            .attributes(SwordItem.createAttributes(
                                    Tiers.IRON,
                                    4,
                                    -2.35F)));

    public static final Supplier<ArtifactShieldItem> TEMPERED_BODY_ARTIFACT_SHIELD =
            ITEMS.registerItem(
                    "tempered_body_artifact_shield",
                    properties -> new ArtifactShieldItem(
                            ArtifactGrade.TEMPERED_BODY,
                            properties),
                    new Item.Properties().durability(480));

    public static final Supplier<ArtifactArmorItem> TEMPERED_BODY_ARTIFACT_HELMET =
            registerArtifactArmor(
                    "tempered_body_artifact_helmet",
                    ArmorItem.Type.HELMET);

    public static final Supplier<ArtifactArmorItem> TEMPERED_BODY_ARTIFACT_CHESTPLATE =
            registerArtifactArmor(
                    "tempered_body_artifact_chestplate",
                    ArmorItem.Type.CHESTPLATE);

    public static final Supplier<ArtifactArmorItem> TEMPERED_BODY_ARTIFACT_LEGGINGS =
            registerArtifactArmor(
                    "tempered_body_artifact_leggings",
                    ArmorItem.Type.LEGGINGS);

    public static final Supplier<ArtifactArmorItem> TEMPERED_BODY_ARTIFACT_BOOTS =
            registerArtifactArmor(
                    "tempered_body_artifact_boots",
                    ArmorItem.Type.BOOTS);

    public static final Supplier<MinorStorageBagItem> MINOR_STORAGE_BAG =
            ITEMS.registerItem(
                    "minor_storage_bag",
                    MinorStorageBagItem::new,
                    new Item.Properties()
                            .stacksTo(1)
                            .component(
                                    DataComponents.CONTAINER,
                                    ItemContainerContents.EMPTY));

    public static final Supplier<MartialWorldGuideItem> INSTRUCTION_TO_THE_MARTIAL_WORLD =
            ITEMS.registerItem(
                    "instruction_to_the_martial_world",
                    MartialWorldGuideItem::new,
                    new Item.Properties().stacksTo(1));

    private ModItems() {
    }

    public static void register(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
        modEventBus.addListener(ModItems::addCreativeTabContents);
    }

    public static Supplier<TechniqueManualItem> manualFor(ResourceLocation techniqueId) {
        return TECHNIQUE_MANUALS.get(techniqueId);
    }

    private static Supplier<ArtifactArmorItem> registerArtifactArmor(
            String itemPath,
            ArmorItem.Type type) {
        return ITEMS.registerItem(
                itemPath,
                properties -> new ArtifactArmorItem(
                        ModArmorMaterials.TEMPERED_BODY_ARTIFACT,
                        type,
                        ArtifactGrade.TEMPERED_BODY,
                        properties),
                new Item.Properties().durability(type.getDurability(20)));
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
        if (event.getTabKey() == CreativeModeTabs.FOOD_AND_DRINKS) {
            event.accept(MINOR_BODY_MENDING_PILL.get());
            event.accept(MERIDIAN_SOOTHING_PILL.get());
            event.accept(BASIC_ANTIDOTE_PILL.get());
            event.accept(MINOR_PURIFICATION_PILL.get());
        }

        if (event.getTabKey() == CreativeModeTabs.COMBAT) {
            event.accept(CRUDE_MERIDIAN_POISON.get());
            event.accept(TEMPERED_BODY_ARTIFACT_SWORD.get());
            event.accept(TEMPERED_BODY_ARTIFACT_SHIELD.get());
            event.accept(TEMPERED_BODY_ARTIFACT_HELMET.get());
            event.accept(TEMPERED_BODY_ARTIFACT_CHESTPLATE.get());
            event.accept(TEMPERED_BODY_ARTIFACT_LEGGINGS.get());
            event.accept(TEMPERED_BODY_ARTIFACT_BOOTS.get());
        }

        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(MINOR_STORAGE_BAG.get());
            event.accept(INSTRUCTION_TO_THE_MARTIAL_WORLD.get());
            event.accept(BASIC_TRAINING_WEIGHT.get());
            for (Supplier<TechniqueManualItem> manual : TECHNIQUE_MANUALS.values()) {
                event.accept(manual.get());
            }
        }
    }
}
