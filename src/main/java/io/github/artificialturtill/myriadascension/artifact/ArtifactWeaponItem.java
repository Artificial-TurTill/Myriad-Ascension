package io.github.artificialturtill.myriadascension.artifact;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;

public final class ArtifactWeaponItem extends SwordItem implements ArtifactDescriptor {
    private final ArtifactGrade grade;

    public ArtifactWeaponItem(Tier tier, ArtifactGrade grade, Item.Properties properties) {
        super(tier, properties);
        this.grade = grade;
    }

    @Override
    public ArtifactType artifactType() {
        return ArtifactType.WEAPON;
    }

    @Override
    public ArtifactGrade artifactGrade() {
        return grade;
    }
}
