package io.github.artificialturtill.myriadascension.client;

import io.github.artificialturtill.myriadascension.client.renderer.TemperedBodyArtifactShieldRenderer;
import io.github.artificialturtill.myriadascension.registry.ModItems;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

public final class ClientItemExtensions {
    private ClientItemExtensions() {
    }

    public static void register(RegisterClientExtensionsEvent event) {
        TemperedBodyArtifactShieldRenderer shieldRenderer =
                new TemperedBodyArtifactShieldRenderer();

        event.registerItem(
                new IClientItemExtensions() {
                    @Override
                    public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                        return shieldRenderer;
                    }
                },
                ModItems.TEMPERED_BODY_ARTIFACT_SHIELD.get());
    }
}
