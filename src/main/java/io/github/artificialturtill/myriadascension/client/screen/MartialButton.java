package io.github.artificialturtill.myriadascension.client.screen;

import io.github.artificialturtill.myriadascension.MyriadAscension;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public final class MartialButton extends Button {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(
                    MyriadAscension.MOD_ID,
                    "textures/gui/martial_button.png");

    private boolean selected;

    public MartialButton(
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

        int textColor;
        if (!active) {
            textColor = 0xFF8E8A80;
        } else if (selected) {
            textColor = 0xFFFFD978;
        } else {
            textColor = 0xFFF2EEE2;
        }

        graphics.drawCenteredString(
                Minecraft.getInstance().font,
                getMessage(),
                getX() + getWidth() / 2,
                getY() + (getHeight() - 8) / 2,
                textColor);
    }
}
