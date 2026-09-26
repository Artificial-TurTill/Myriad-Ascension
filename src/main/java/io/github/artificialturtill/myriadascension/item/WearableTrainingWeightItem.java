package io.github.artificialturtill.myriadascension.item;

import net.minecraft.core.Holder;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;

/**
 * Zero-protection equipment whose purpose is deliberate training resistance.
 */
public final class WearableTrainingWeightItem extends ArmorItem {
    private final double trainingLoad;

    public WearableTrainingWeightItem(
            Holder<ArmorMaterial> material,
            Type type,
            double trainingLoad,
            Item.Properties properties) {
        super(material, type, properties);
        this.trainingLoad = Math.max(0.0D, trainingLoad);
    }

    public double trainingLoad() {
        return trainingLoad;
    }
}
