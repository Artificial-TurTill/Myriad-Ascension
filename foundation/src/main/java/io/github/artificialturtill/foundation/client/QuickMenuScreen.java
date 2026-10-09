package io.github.artificialturtill.foundation.client;

import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

/**
 * The X menu (from alpha-16's cultivation quick menu): a translucent 3 x 3 grid over the unblurred world. Hold X,
 * hover a cell and release X to use it; left click = next / use, right click = previous. The centre cell is "More"
 * (room for future controls). The game keeps running.
 */
public final class QuickMenuScreen extends Screen {
    private static final int COLUMNS = 3, ROWS = 3, GAP = 2, CELL_HEIGHT = 56, CENTER = 4;
    private static final int CELL = 0x66F5F5F5, CELL_HOVER = 0x99FFFFFF, CELL_OFF = 0x40B0B0B0, BORDER = 0x88707070;
    private int hovered = -1;

    public QuickMenuScreen() { super(Component.translatable("screen.ascension_foundation.quick_menu")); }
    @Override public boolean isPauseScreen() { return false; }
    @Override public void renderBackground(GuiGraphics g, int mouseX, int mouseY, float partialTick) { }

    /** Cell index -> entry (the centre cell is "More"). */
    private static QuickMenu.Entry entry(int cell) {
        if (cell == CENTER || cell < 0) return null;
        int index = cell < CENTER ? cell : cell - 1;
        List<QuickMenu.Entry> entries = QuickMenu.entries();
        return index < entries.size() ? entries.get(index) : null;
    }

    @Override public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        if (FoundationClient.QUICK_MENU.matches(keyCode, scanCode)) {
            int cell = hovered;
            onClose(); // first, so an entry that opens a screen is not closed again
            activate(cell, 1);
            return true;
        }
        return super.keyReleased(keyCode, scanCode, modifiers);
    }
    @Override public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int cell = cellAt(mouseX, mouseY);
        if (cell < 0) return super.mouseClicked(mouseX, mouseY, button);
        activate(cell, button == GLFW.GLFW_MOUSE_BUTTON_RIGHT ? -1 : 1);
        return true;
    }
    private void activate(int cell, int direction) {
        QuickMenu.Entry e = entry(cell);
        if (e != null && e.available().get()) e.activate().accept(direction);
    }

    @Override public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        hovered = cellAt(mouseX, mouseY);
        g.drawCenteredString(font, Component.translatable("screen.ascension_foundation.quick_menu.hint"), width / 2, top() - 30, 0xFFE7E7E7);
        g.drawCenteredString(font, Component.translatable("screen.ascension_foundation.quick_menu.clicks"), width / 2, top() - 17, 0xFFBDBDBD);
        for (int cell = 0; cell < COLUMNS * ROWS; cell++) {
            int x = cellX(cell), y = cellY(cell), w = cellWidth();
            QuickMenu.Entry e = entry(cell);
            boolean usable = cell == CENTER || e != null && e.available().get();
            g.fill(x, y, x + w, y + CELL_HEIGHT, !usable ? CELL_OFF : cell == hovered ? CELL_HOVER : CELL);
            g.renderOutline(x, y, w, CELL_HEIGHT, BORDER);
            if (cell == CENTER) {
                g.drawCenteredString(font, Component.translatable("screen.ascension_foundation.quick_menu.more"), x + w / 2, y + 13, QuickMenu.NEUTRAL);
                g.drawCenteredString(font, Component.translatable("screen.ascension_foundation.quick_menu.future"), x + w / 2, y + 32, QuickMenu.MUTED);
            } else if (e != null) {
                fitted(g, e.title().get(), x + w / 2, y + 13, w - 8, QuickMenu.NEUTRAL);
                fitted(g, e.value().get(), x + w / 2, y + 32, w - 8, usable ? e.color().get() : QuickMenu.MUTED);
            }
        }
    }
    /** drawCenteredString without the shadow, shortened with "..." to fit. */
    private void fitted(GuiGraphics g, Component text, int centerX, int y, int maxWidth, int color) {
        String s = text.getString();
        if (font.width(s) > maxWidth) {
            while (!s.isEmpty() && font.width(s + "...") > maxWidth) s = s.substring(0, s.length() - 1);
            s += "...";
        }
        g.drawString(font, s, centerX - font.width(s) / 2, y, color, false);
    }

    private int panelWidth() { return Math.min(560, Math.max(330, width - 80)); }
    private int cellWidth() { return (panelWidth() - GAP * (COLUMNS - 1)) / COLUMNS; }
    private int top() { return Math.max(48, (height - (ROWS * CELL_HEIGHT + GAP * (ROWS - 1))) / 2); }
    private int cellX(int cell) { return (width - panelWidth()) / 2 + (cell % COLUMNS) * (cellWidth() + GAP); }
    private int cellY(int cell) { return top() + (cell / COLUMNS) * (CELL_HEIGHT + GAP); }
    private int cellAt(double mouseX, double mouseY) {
        for (int cell = 0; cell < COLUMNS * ROWS; cell++)
            if (mouseX >= cellX(cell) && mouseX < cellX(cell) + cellWidth() && mouseY >= cellY(cell) && mouseY < cellY(cell) + CELL_HEIGHT) return cell;
        return -1;
    }
}
