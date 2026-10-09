package io.github.artificialturtill.foundation.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

/** A saved character exists, but its race / power type / class comes from an add-on that is not installed. */
public final class DormantScreen extends FoundationScreen {
    public DormantScreen() { super(Component.translatable("screen.ascension_foundation.dormant")); }
    @Override protected Tab tab() { return null; }
    @Override protected void initContent() {
        addRenderableWidget(Button.builder(Component.translatable("gui.done"), b -> onClose()).bounds(left + WIDTH / 2 - 40, top + HEIGHT - 26, 80, 20).build());
    }
    @Override protected void drawContent(GuiGraphics g, int mouseX, int mouseY) {
        header(g, title, left + 10, top + 10);
        g.drawWordWrap(font, Component.translatable("screen.ascension_foundation.dormant.text"), left + 10, top + 30, WIDTH - 20, TEXT);
    }
}
