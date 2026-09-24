package io.github.artificialturtill.myriadascension.artifact;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ShieldItem;

public final class ArtifactShieldItem extends ShieldItem implements ArtifactDescriptor {
    private final ArtifactGrade grade;

    public ArtifactShieldItem(ArtifactGrade grade, Item.Properties properties) {
        super(properties);
        this.grade = grade;
    }

    @Override
    public ArtifactType artifactType() {
        return ArtifactType.SHIELD;
    }

    @Override
    public ArtifactGrade artifactGrade() {
        return grade;
    }
}
