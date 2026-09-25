package io.github.artificialturtill.myriadascension.registry;

import io.github.artificialturtill.myriadascension.MyriadAscension;
import java.util.EnumMap;
import java.util.List;
import net.minecraft.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModArmorMaterials {
    public static final DeferredRegister<ArmorMaterial> ARMOR_MATERIALS =
            DeferredRegister.create(Registries.ARMOR_MATERIAL, MyriadAscension.MOD_ID);

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> TEMPERED_BODY_ARTIFACT =
            ARMOR_MATERIALS.register(
                    "tempered_body_artifact",
                    () -> new ArmorMaterial(
                            Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                                map.put(ArmorItem.Type.BOOTS, 2);
                                map.put(ArmorItem.Type.LEGGINGS, 5);
                                map.put(ArmorItem.Type.CHESTPLATE, 6);
                                map.put(ArmorItem.Type.HELMET, 2);
                                map.put(ArmorItem.Type.BODY, 5);
                            }),
                            14,
                            SoundEvents.ARMOR_EQUIP_IRON,
                            () -> Ingredient.of(Items.IRON_INGOT),
                            List.of(new ArmorMaterial.Layer(
                                    ResourceLocation.fromNamespaceAndPath(
                                            MyriadAscension.MOD_ID,
                                            "tempered_body_artifact"))),
                            0.0F,
                            0.0F));

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> TRAINING_WEIGHT =
            ARMOR_MATERIALS.register(
                    "training_weight",
                    () -> new ArmorMaterial(
                            Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                                map.put(ArmorItem.Type.BOOTS, 0);
                                map.put(ArmorItem.Type.LEGGINGS, 0);
                                map.put(ArmorItem.Type.CHESTPLATE, 0);
                                map.put(ArmorItem.Type.HELMET, 0);
                                map.put(ArmorItem.Type.BODY, 0);
                            }),
                            0,
                            SoundEvents.ARMOR_EQUIP_CHAIN,
                            () -> Ingredient.of(Items.IRON_INGOT),
                            List.of(new ArmorMaterial.Layer(
                                    ResourceLocation.fromNamespaceAndPath(
                                            MyriadAscension.MOD_ID,
                                            "training_weight"))),
                            0.0F,
                            0.0F));

    private ModArmorMaterials() {
    }

    public static void register(IEventBus modEventBus) {
        ARMOR_MATERIALS.register(modEventBus);
    }
}
