package io.github.artificialturtill.myriadascension.quest;

import java.util.List;
import net.minecraft.resources.ResourceLocation;

public record ServiceQuestDefinition(
        ResourceLocation id,
        String displayName,
        ServiceQuestType type,
        ServiceProviderType source,
        ServiceProviderType destination,
        int serviceMerit,
        List<String> requiredItemTags) {

    public ServiceQuestDefinition {
        requiredItemTags = requiredItemTags == null ? List.of() : List.copyOf(requiredItemTags);

        if (serviceMerit <= 0) {
            throw new IllegalArgumentException("serviceMerit must be positive");
        }
    }
}
