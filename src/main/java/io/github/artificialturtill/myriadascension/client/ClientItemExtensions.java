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
        event.registerItem(
                new IClientItemExtensions() {
                    private BlockEntityWithoutLevelRenderer renderer;

                    @Override
                    public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                        if (renderer == null) {
                            renderer = new TemperedBodyArtifactShieldRenderer();
                        }
                        return renderer;
                    }
                },
                ModItems.TEMPERED_BODY_ARTIFACT_SHIELD.get());
    }
}
