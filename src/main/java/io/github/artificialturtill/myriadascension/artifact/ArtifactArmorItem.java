package io.github.artificialturtill.myriadascension.artifact;

import net.minecraft.core.Holder;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;

public final class ArtifactArmorItem extends ArmorItem implements ArtifactDescriptor {
    private final ArtifactGrade grade;

    public ArtifactArmorItem(
            Holder<ArmorMaterial> material,
            Type type,
            ArtifactGrade grade,
            Item.Properties properties) {
        super(material, type, properties);
        this.grade = grade;
    }

    @Override
    public ArtifactType artifactType() {
        return ArtifactType.ARMOR;
    }

    @Override
    public ArtifactGrade artifactGrade() {
        return grade;
    }
}
