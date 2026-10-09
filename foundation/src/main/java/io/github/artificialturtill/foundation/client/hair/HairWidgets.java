package io.github.artificialturtill.foundation.client.hair;

import java.util.function.DoubleConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

/** The hairstyle manager's small controls, drawn with fills (JRMCore's button textures are not used). */
final class HairWidgets {
    /** JRMCoreGuiButtonsA2: a 10 x 10 icon button ("<", ">", "o", "x", "1", "2"). */
    static final class SmallButton extends AbstractButton {
        private final Runnable action;
        SmallButton(int x, int y, String label, Runnable action) { super(x, y, 10, 10, Component.literal(label)); this.action = action; }
        @Override public void onPress() { action.run(); }
        @Override protected void renderWidget(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
            int bg = !active ? 0xFF3A3A3A : isHovered() ? 0xFF8A7440 : 0xFF5A4A28;
            g.fill(getX(), getY(), getX() + width, getY() + height, 0xFF000000);
            g.fill(getX() + 1, getY() + 1, getX() + width - 1, getY() + height - 1, bg);
            var font = Minecraft.getInstance().font;
            String s = getMessage().getString();
            g.drawString(font, s, getX() + (width - font.width(s)) / 2 + 1, getY() + 1, 0xFFEDE6D6, false);
        }
        @Override protected void updateWidgetNarration(NarrationElementOutput output) { defaultButtonNarrationText(output); }
    }

    /** JRMCoreGuiSlider02: a vertical 5 x 35 slider, value 0..1 from the top (grey while it repeats to the page). */
    static final class VSlider extends AbstractWidget {
        private double value; private final boolean grey; private final DoubleConsumer change;
        VSlider(int x, int y, double value, boolean grey, DoubleConsumer change) {
            super(x, y, 5, 35, Component.empty());
            this.value = Math.clamp(value, 0, 1); this.grey = grey; this.change = change;
        }
        @Override protected void renderWidget(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
            g.fill(getX(), getY(), getX() + width, getY() + height, 0xFF000000);
            g.fill(getX() + 1, getY() + 1, getX() + width - 1, getY() + height - 1, grey ? 0xFF8A8A8A : 0xFF3C3428);
            int knob = getY() + (int) (value * (height - 2));
            g.fill(getX(), knob, getX() + width, knob + 2, isHovered() ? 0xFFFFE6A0 : 0xFFD8B66A);
        }
        private void set(double mouseY) { value = Math.clamp((mouseY - getY()) / height, 0, 1); change.accept(value); }
        @Override public void onClick(double mouseX, double mouseY) { set(mouseY); }
        @Override protected void onDrag(double mouseX, double mouseY, double dragX, double dragY) { set(mouseY); }
        @Override protected void updateWidgetNarration(NarrationElementOutput output) { }
    }

    /** A colour swatch button. */
    static final class Swatch extends AbstractButton {
        private final java.util.function.IntSupplier color; private final Runnable action;
        Swatch(int x, int y, int w, java.util.function.IntSupplier color, Runnable action) { super(x, y, w, 10, Component.empty()); this.color = color; this.action = action; }
        @Override public void onPress() { action.run(); }
        @Override protected void renderWidget(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
            g.fill(getX() - 1, getY() - 1, getX() + width + 1, getY() + height + 1, isHovered() ? 0xFFFFFFA0 : 0xFF000000);
            g.fill(getX(), getY(), getX() + width, getY() + height, 0xFF000000 | color.getAsInt());
        }
        @Override protected void updateWidgetNarration(NarrationElementOutput output) { }
    }
    private HairWidgets() {}
}
