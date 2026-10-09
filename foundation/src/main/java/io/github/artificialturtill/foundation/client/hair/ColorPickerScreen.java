package io.github.artificialturtill.foundation.client.hair;

import com.mojang.blaze3d.platform.NativeImage;
import io.github.artificialturtill.foundation.api.FoundationApi;
import io.github.artificialturtill.foundation.client.FoundationScreen;
import java.awt.Color;
import java.util.function.IntConsumer;
import java.util.function.Supplier;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

/**
 * The HSB colour picker (JRMCoreGuiScreen GUI 1): a 128 x 128 hue / saturation field at the chosen brightness (baked
 * into a dynamic texture) and the brightness slider. Every pick is applied at once; Done goes back.
 */
public final class ColorPickerScreen extends FoundationScreen implements HairState.Preview {
    private static final ResourceLocation FIELD = ResourceLocation.fromNamespaceAndPath(FoundationApi.MOD_ID, "hair_color_field");
    private static DynamicTexture texture;
    private static float bakedBrightness = -1F;
    private float brightness;
    private int color;
    private final IntConsumer set;
    private final Supplier<Screen> back;

    public ColorPickerScreen(int initial, IntConsumer set, Supplier<Screen> back) {
        super(Component.translatable("hair.ascension_foundation.color"));
        this.color = initial & 0xFFFFFF; this.set = set; this.back = back;
        brightness = Color.RGBtoHSB(color >> 16 & 255, color >> 8 & 255, color & 255, null)[2];
    }
    @Override protected Tab tab() { return null; }
    @Override public String previewHair() { return HairState.code; }
    @Override public int previewColor() { return HairState.color; }

    @Override protected void initContent() {
        addRenderableWidget(new AbstractWidget(left + 140, top + 20, 14, 128, Component.empty()) {
            @Override protected void renderWidget(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
                for (int i = 0; i < height; i++) {
                    int v = (int) (255F * i / (height - 1));
                    g.fill(getX(), getY() + i, getX() + width, getY() + i + 1, 0xFF000000 | v << 16 | v << 8 | v);
                }
                int knob = getY() + (int) (brightness * (height - 2));
                g.fill(getX() - 2, knob, getX() + width + 2, knob + 2, 0xFFFF4040);
            }
            private void set(double y) { brightness = (float) Math.clamp((y - getY()) / height, 0, 1); }
            @Override public void onClick(double mouseX, double mouseY) { set(mouseY); }
            @Override protected void onDrag(double mouseX, double mouseY, double dx, double dy) { set(mouseY); }
            @Override protected void updateWidgetNarration(NarrationElementOutput output) { }
        });
        addRenderableWidget(Button.builder(Component.translatable("gui.done"), b -> minecraft.setScreen(back.get()))
            .bounds(left + WIDTH - 66, top + HEIGHT - 24, 60, 18).build());
    }
    @Override public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int x = (int) mouseX - left - 6, y = (int) mouseY - top - 20;
        if (x >= 0 && x < 128 && y >= 0 && y < 128) {
            color = Color.getHSBColor(x * 2F / 255F, y * 2F / 255F, brightness).getRGB() & 0xFFFFFF;
            set.accept(color);
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }
    @Override protected void drawContent(GuiGraphics g, int mouseX, int mouseY) {
        header(g, title, left + 8, top + 6);
        if (texture == null) {
            texture = new DynamicTexture(128, 128, false);
            minecraft.getTextureManager().register(FIELD, texture);
        }
        if (bakedBrightness != brightness) {
            NativeImage image = texture.getPixels();
            for (int x = 0; x < 128; x++) for (int y = 0; y < 128; y++) {
                int rgb = Color.getHSBColor(x * 2F / 255F, y * 2F / 255F, brightness).getRGB();
                image.setPixelRGBA(x, y, 0xFF000000 | (rgb & 0xFF) << 16 | (rgb & 0xFF00) | (rgb >> 16 & 0xFF)); // NativeImage is ABGR
            }
            texture.upload();
            bakedBrightness = brightness;
        }
        g.blit(FIELD, left + 6, top + 20, 0, 0, 128, 128, 128, 128);
        g.fill(left + 6, top + 154, left + 154, top + 166, 0xFF000000 | color);
        text(g, String.format("#%06X", color), left + 6, top + 170, TEXT);
        g.enableScissor(left + 160, top + 18, left + WIDTH - 4, top + HEIGHT - 28);
        if (minecraft.player != null) HairState.drawHead(g, left + 228, top + 100, 60F, 25F, 0F, minecraft.player);
        g.disableScissor();
    }
}
