package io.github.artificialturtill.myriadascension.client.screen;

import io.github.artificialturtill.myriadascension.MyriadAscension;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public final class CultivatorTabButton extends Button {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(
                    MyriadAscension.MOD_ID,
                    "textures/gui/cultivator_status_tabs.png");

    private boolean selected;

    public CultivatorTabButton(
            int x,
            int y,
            int width,
            int height,
            Component message,
            OnPress onPress) {
        super(x, y, width, height, message, onPress, DEFAULT_NARRATION);
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        this.active = !selected;
    }

    @Override
    protected void renderWidget(
            GuiGraphics graphics,
            int mouseX,
            int mouseY,
            float partialTick) {

        int sourceY = selected ? 40 : (isHoveredOrFocused() ? 20 : 0);

        graphics.blit(
                TEXTURE,
                getX(),
                getY(),
                getWidth(),
                getHeight(),
                0.0F,
                (float) sourceY,
                256,
                20,
                256,
                64);

        graphics.drawCenteredString(
                Minecraft.getInstance().font,
                getMessage(),
                getX() + getWidth() / 2,
                getY() + (getHeight() - 8) / 2,
                selected ? 0xFFFFD978 : 0xFFF2EEE2);
    }
}
