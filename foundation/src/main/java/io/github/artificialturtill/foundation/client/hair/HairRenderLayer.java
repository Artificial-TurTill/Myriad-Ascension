package io.github.artificialturtill.foundation.client.hair;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import io.github.artificialturtill.foundation.api.FoundationApi;
import io.github.artificialturtill.foundation.character.FoundationAttachments;
import io.github.artificialturtill.foundation.character.HairCode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;

/**
 * The custom hairstyle on the player's head (JBRA ModelBipedDBC.renderHairsV2, ported from Dragonblock C+
 * HairRenderLayer): 56 hair groups of up to four chained boxes, tinted with the hair colour. Drawn for every player
 * with a character (from the synced look); the local player shows the hairstyle manager's preview while it is open.
 * Hidden under a helmet ("Hide Hair When Helmet On"). The texture is the Foundation's own.
 */
public final class HairRenderLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(FoundationApi.MOD_ID, "textures/entity/hair.png");
    private static final float[] SIZE_XZ = {1.1F, 1.0F, 0.9F, 0.8F};
    private static final float[] POINT_Y = {0F, 1.5F, 2.5F, 2.5F};
    private static ModelPart root, segment;

    public HairRenderLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> parent) { super(parent); }

    private static void bake() {
        if (root != null) return;
        MeshDefinition mesh = new MeshDefinition();
        mesh.getRoot().addOrReplaceChild("root", CubeListBuilder.create().texOffs(0, 0).addBox(-1F, -1F, -1F, 2, 3, 2), PartPose.ZERO);
        mesh.getRoot().addOrReplaceChild("segment", CubeListBuilder.create().texOffs(0, 0).addBox(-1F, 0F, -1F, 2, 3, 2), PartPose.ZERO);
        ModelPart baked = LayerDefinition.create(mesh, 16, 16).bakeRoot();
        root = baked.getChild("root");
        segment = baked.getChild("segment");
    }

    @Override
    public void render(PoseStack pose, MultiBufferSource buffers, int light, AbstractClientPlayer player, float limbSwing,
                       float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        if (player.isInvisible()) return;
        String code;
        int color;
        if (player == Minecraft.getInstance().player && Minecraft.getInstance().screen instanceof HairState.Preview preview) {
            code = preview.previewHair(); color = preview.previewColor();
        } else {
            var look = player.getData(FoundationAttachments.LOOK);
            if (!look.active()) return;
            code = look.appearance().hair(); color = look.appearance().hairColor();
        }
        if (!HairCode.valid(code) || player.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof ArmorItem) return;
        bake();
        VertexConsumer vc = buffers.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));
        int argb = 0xFF000000 | color;
        pose.pushPose();
        getParentModel().head.translateAndRotate(pose);
        for (int face = 0; face < HairCode.GROUPS; face++) {
            HairCode.Strand strand = HairCode.strand(code, face);
            if (strand != null) renderPart(pose, vc, light, argb, strand, 0, strand.pointX(), strand.pointY(), strand.pointZ(),
                strand.angleX(), strand.angleY(), strand.angleZ());
        }
        pose.popPose();
    }

    /** ModelRendererJBRA.render: segment i, then its child i + 1 inside the same transform. */
    private static void renderPart(PoseStack pose, VertexConsumer vc, int light, int color, HairCode.Strand s, int i,
                                   float px, float py, float pz, float ax, float ay, float az) {
        float lengthY = i == 0 ? 1F : s.length()[i - 1];
        if (i > 0 && lengthY <= 0F) return; // showModel false hides the rest of the chain
        ModelPart box = i == 0 ? root : segment;
        float sizeXZ = SIZE_XZ[i];
        pose.pushPose();
        pose.translate(px / 16F, py / 16F, pz / 16F);
        if (az != 0F) pose.mulPose(Axis.ZP.rotation(az));
        if (ay != 0F) pose.mulPose(Axis.YP.rotation(ay));
        if (ax != 0F) pose.mulPose(Axis.XP.rotation(ax));
        pose.pushPose();
        pose.scale(sizeXZ, lengthY, sizeXZ);
        box.render(pose, vc, light, OverlayTexture.NO_OVERLAY, color);
        pose.popPose();
        if (ax != 0F || ay != 0F || az != 0F) pose.translate(0F, lengthY * 0.15F - 0.15F, 0F);
        if (i < 3) {
            float bend = s.bend()[i];
            renderPart(pose, vc, light, color, s, i + 1, 0F, POINT_Y[i + 1], 0F, s.bendAboutZ() ? 0F : bend, 0F, s.bendAboutZ() ? bend : 0F);
        }
        pose.popPose();
    }
}
