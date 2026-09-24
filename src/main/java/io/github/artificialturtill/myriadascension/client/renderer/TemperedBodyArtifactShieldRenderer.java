package io.github.artificialturtill.myriadascension.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.artificialturtill.myriadascension.MyriadAscension;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ShieldModel;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public final class TemperedBodyArtifactShieldRenderer
        extends BlockEntityWithoutLevelRenderer {

    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(
                    MyriadAscension.MOD_ID,
                    "textures/entity/tempered_body_artifact_shield.png");

    private final ShieldModel shieldModel;

    public TemperedBodyArtifactShieldRenderer() {
        super(
                Minecraft.getInstance().getBlockEntityRenderDispatcher(),
                Minecraft.getInstance().getEntityModels());

        this.shieldModel = new ShieldModel(
                ShieldModel.createLayer().bakeRoot());
    }

    @Override
    public void renderByItem(
            ItemStack stack,
            ItemDisplayContext displayContext,
            PoseStack poseStack,
            MultiBufferSource bufferSource,
            int packedLight,
            int packedOverlay) {

        poseStack.pushPose();

        // Vanilla BlockEntityWithoutLevelRenderer applies this exact axis flip
        // before rendering the ShieldModel.
        poseStack.scale(1.0F, -1.0F, -1.0F);

        VertexConsumer consumer = ItemRenderer.getFoilBufferDirect(
                bufferSource,
                RenderType.entityCutoutNoCull(TEXTURE),
                true,
                stack.hasFoil());

        shieldModel.renderToBuffer(
                poseStack,
                consumer,
                packedLight,
                packedOverlay,
                0xFFFFFFFF);

        poseStack.popPose();
    }
}
