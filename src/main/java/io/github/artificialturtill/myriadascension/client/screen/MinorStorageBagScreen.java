package io.github.artificialturtill.myriadascension.client.screen;

import io.github.artificialturtill.myriadascension.menu.MinorStorageBagMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public final class MinorStorageBagScreen
        extends AbstractContainerScreen<MinorStorageBagMenu> {

    public MinorStorageBagScreen(
            MinorStorageBagMenu menu,
            Inventory inventory,
            Component title) {
        super(menu, inventory, title);
        imageWidth = 176;
        imageHeight = 166;
        inventoryLabelY = 72;
    }

    @Override
    protected void renderBg(
            GuiGraphics graphics,
            float partialTick,
            int mouseX,
            int mouseY) {

        int left = leftPos;
        int top = topPos;

        graphics.fill(
                left,
                top,
                left + imageWidth,
                top + imageHeight,
                0xEE17130E);

        graphics.fill(
                left,
                top,
                left + imageWidth,
                top + 2,
                0xFF8D6D2F);

        graphics.fill(
                left,
                top + imageHeight - 2,
                left + imageWidth,
                top + imageHeight,
                0xFF8D6D2F);

        graphics.fill(
                left + 57,
                top + 12,
                left + 119,
                top + 74,
                0xAA2A241B);
    }

    @Override
    public void render(
            GuiGraphics graphics,
            int mouseX,
            int mouseY,
            float partialTick) {
        renderBackground(graphics, mouseX, mouseY, partialTick);
        super.render(graphics, mouseX, mouseY, partialTick);
        renderTooltip(graphics, mouseX, mouseY);
    }
}
