package io.github.artificialturtill.foundation.client;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

/**
 * Base of every Foundation screen (Dragonblock C+ DbcScreen): a centred panel drawn with plain fills (no texture
 * files), an optional tab row above it (Character, Skills, Transformations) and the server's last refusal at the
 * bottom. The panel is drawn in renderBackground after the vanilla blur, so it stays sharp (DBC+ BUG-001).
 */
public abstract class FoundationScreen extends Screen {
    public static final int WIDTH = 300, HEIGHT = 196;
    public static final int PANEL = 0xE0141018, BORDER = 0xFFB08A3E, INNER = 0x40B08A3E, TEXT = 0xFFEDE6D6, DIM = 0xFFA39A88,
        ACCENT = 0xFFFFD27F, GOOD = 0xFF7FE07F, BAD = 0xFFFF7F7F, BLUE = 0xFF7FC8FF;
    protected int left, top;

    public enum Tab { CHARACTER, SKILLS, FORMS }

    protected FoundationScreen(Component title) { super(title); }

    /** V: the creation wizard without a character, otherwise the character sheet. */
    public static Screen open() {
        if (ClientCharacter.fighter() == null) return ClientCharacter.dormant() ? new DormantScreen() : CreationScreen.open();
        return new CharacterSheetScreen();
    }

    protected abstract Tab tab();
    protected abstract void initContent();
    protected abstract void drawContent(GuiGraphics g, int mouseX, int mouseY);
    /** A new snapshot arrived: rebuild (screens without a character close or switch). */
    public void characterChanged() {
        if (tab() != null && ClientCharacter.fighter() == null) { minecraft.setScreen(open()); return; }
        rebuildWidgets();
    }

    @Override protected void init() {
        left = (width - WIDTH) / 2;
        top = (height - HEIGHT) / 2 + 6;
        if (tab() != null) {
            int x = left;
            for (Tab t : Tab.values()) {
                Component label = Component.translatable("screen.ascension_foundation.tab." + t.name().toLowerCase(java.util.Locale.ROOT));
                int w = font.width(label) + 12;
                Button button = addRenderableWidget(Button.builder(label, b -> switchTo(t)).bounds(x, top - 18, w, 16).build());
                button.active = t != tab();
                x += w + 2;
            }
        }
        initContent();
    }
    private void switchTo(Tab t) {
        minecraft.setScreen(switch (t) { case CHARACTER -> new CharacterSheetScreen(); case SKILLS -> new SkillsScreen(); case FORMS -> new FormsScreen(); });
    }

    @Override public void renderBackground(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        super.renderBackground(g, mouseX, mouseY, partialTick);
        g.fill(left - 1, top - 1, left + WIDTH + 1, top + HEIGHT + 1, BORDER);
        g.fill(left, top, left + WIDTH, top + HEIGHT, PANEL);
    }
    @Override public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        super.render(g, mouseX, mouseY, partialTick);
        drawContent(g, mouseX, mouseY);
        String message = ClientCharacter.message();
        if (!message.isEmpty()) g.drawCenteredString(font, message, width / 2, top + HEIGHT + 4, BAD);
    }
    @Override public boolean isPauseScreen() { return false; }

    // ---- drawing helpers ------------------------------------------------------------------------------------------
    protected void text(GuiGraphics g, Component text, int x, int y, int color) { g.drawString(font, text, x, y, color, false); }
    protected void text(GuiGraphics g, String text, int x, int y, int color) { g.drawString(font, text, x, y, color, false); }
    protected void header(GuiGraphics g, Component text, int x, int y) {
        text(g, text, x, y, ACCENT);
        g.fill(x, y + 9, x + Math.max(40, font.width(text)), y + 10, INNER);
    }
    /** "Label: value" with the value in its own colour; returns the width. */
    protected int pair(GuiGraphics g, Component label, String value, int x, int y, int valueColor) {
        String l = label.getString() + ": ";
        text(g, l, x, y, DIM);
        text(g, value, x + font.width(l), y, valueColor);
        return font.width(l) + font.width(value);
    }
    /** Tooltip (wrapped at 200 px) while the mouse is inside the box. */
    protected void hover(Component tip, int x, int y, int w, int h, int mouseX, int mouseY) {
        if (tip.getString().isEmpty() || mouseX < x || mouseX >= x + w || mouseY < y || mouseY >= y + h) return;
        List<FormattedCharSequence> lines = new ArrayList<>();
        for (String part : tip.getString().split("\n")) lines.addAll(font.split(Component.literal(part), 200));
        setTooltipForNextRenderPass(lines);
    }
    protected void swatch(GuiGraphics g, int x, int y, int w, int color) {
        g.fill(x - 1, y - 1, x + w + 1, y + 9, 0xFF000000);
        g.fill(x, y, x + w, y + 8, 0xFF000000 | color);
    }
    protected void bar(GuiGraphics g, int x, int y, int w, int value, int max, int color) {
        g.fill(x, y, x + w, y + 4, 0xFF2A2A2A);
        int filled = max <= 0 ? 0 : (int) ((long) w * Math.clamp(value, 0, max) / max);
        g.fill(x, y, x + filled, y + 4, color);
    }
}
