package io.github.artificialturtill.myriadascension.client.screen;

import com.mojang.blaze3d.platform.InputConstants;
import io.github.artificialturtill.myriadascension.client.ClientQuickMenuPayloadHandler;
import io.github.artificialturtill.myriadascension.network.QuickMenuAction;
import io.github.artificialturtill.myriadascension.network.QuickMenuActionPayload;
import io.github.artificialturtill.myriadascension.network.QuickMenuSnapshotPayload;
import io.github.artificialturtill.myriadascension.technique.TechniqueCategory;
import java.util.Locale;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.network.PacketDistributor;
import org.lwjgl.glfw.GLFW;

public final class CultivationQuickMenuScreen extends Screen {
    private static final int COLUMNS = 3;
    private static final int ROWS = 3;
    private static final int GAP = 2;
    private static final int CELL_HEIGHT = 62;
    private static final int PANEL_MAX_WIDTH = 600;

    private static final int CELL = 0x66F5F5F5;
    private static final int CELL_HOVER = 0x99FFFFFF;
    private static final int BORDER = 0x88707070;
    private static final int TEXT = 0xFF242424;
    private static final int MUTED = 0xFF5A5A5A;
    private static final int ON = 0xFF168A24;
    private static final int OFF = 0xFF9D2020;

    private QuickMenuSnapshotPayload snapshot;
    private int hoveredCell = -1;
    private boolean sawQuickKeyHeld;

    public CultivationQuickMenuScreen(QuickMenuSnapshotPayload snapshot) {
        super(Component.translatable("screen.myriad_ascension.quick_menu.title"));
        this.snapshot = snapshot;
    }

    public void updateSnapshot(QuickMenuSnapshotPayload snapshot) {
        if (snapshot != null) {
            this.snapshot = snapshot;
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void renderBackground(
            GuiGraphics graphics,
            int mouseX,
            int mouseY,
            float partialTick) {
        // Deliberately transparent: keep the world visible exactly like an in-world control overlay.
    }

    @Override
    public void tick() {
        super.tick();

        Minecraft minecraft = Minecraft.getInstance();
        long window = minecraft.getWindow().getWindow();
        boolean quickKeyDown = InputConstants.isKeyDown(window, GLFW.GLFW_KEY_X);

        if (quickKeyDown) {
            sawQuickKeyHeld = true;
            return;
        }

        if (sawQuickKeyHeld) {
            sawQuickKeyHeld = false;
            if (hoveredCell >= 0) {
                activateCell(hoveredCell, 1);
            }
            onClose();
        }
    }

    @Override
    public void onClose() {
        ClientQuickMenuPayloadHandler.cancelExpectedOpen();
        super.onClose();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int cell = cellAt(mouseX, mouseY);
        if (cell >= 0) {
            int direction = button == GLFW.GLFW_MOUSE_BUTTON_RIGHT ? -1 : 1;
            activateCell(cell, direction);
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void render(
            GuiGraphics graphics,
            int mouseX,
            int mouseY,
            float partialTick) {

        hoveredCell = cellAt(mouseX, mouseY);

        graphics.drawCenteredString(
                font,
                Component.literal("Hover over and release X"),
                width / 2,
                panelTop() - 31,
                0xFFE7E7E7);

        graphics.drawCenteredString(
                font,
                Component.literal("Left click: next   Right click: previous"),
                width / 2,
                panelTop() - 18,
                0xFFBDBDBD);

        for (int index = 0; index < COLUMNS * ROWS; index++) {
            int x = cellX(index);
            int y = cellY(index);
            int w = cellWidth();

            graphics.fill(x, y, x + w, y + CELL_HEIGHT, index == hoveredCell ? CELL_HOVER : CELL);
            graphics.fill(x, y, x + w, y + 1, BORDER);
            graphics.fill(x, y + CELL_HEIGHT - 1, x + w, y + CELL_HEIGHT, BORDER);
            graphics.fill(x, y, x + 1, y + CELL_HEIGHT, BORDER);
            graphics.fill(x + w - 1, y, x + w, y + CELL_HEIGHT, BORDER);

            renderCell(graphics, index, x, y, w);
        }
    }

    private void renderCell(GuiGraphics graphics, int index, int x, int y, int w) {
        String title = switch (index) {
            case 0 -> "Cultivation Method";
            case 1 -> "Cultivation Technique";
            case 2 -> "Weapon Technique";
            case 3 -> "Footwork Technique";
            case 4 -> "MORE";
            case 5 -> "Eyesight Technique";
            case 6 -> "Element / Yin-Yang Scan";
            case 7 -> "Cultivation Gauge";
            case 8 -> "Loose Training Weights";
            default -> "";
        };

        graphics.drawCenteredString(
                font,
                Component.literal(title),
                x + w / 2,
                y + 13,
                TEXT);

        if (index == 4) {
            graphics.drawCenteredString(
                    font,
                    Component.literal("Future cultivation controls"),
                    x + w / 2,
                    y + 34,
                    MUTED);
            return;
        }

        String value;
        int color = TEXT;

        switch (index) {
            case 0 -> value = prettyId(snapshot.activeMethodId());
            case 1 -> value = prettyId(snapshot.cultivationTechniqueId());
            case 2 -> value = prettyId(snapshot.weaponTechniqueId());
            case 3 -> value = prettyId(snapshot.footworkTechniqueId());
            case 5 -> value = prettyId(snapshot.eyesightTechniqueId());
            case 6 -> {
                value = snapshot.resourceScanningEnabled() ? "On" : "Off";
                color = snapshot.resourceScanningEnabled() ? ON : OFF;
            }
            case 7 -> {
                value = snapshot.cultivationGaugeEnabled() ? "On" : "Off";
                color = snapshot.cultivationGaugeEnabled() ? ON : OFF;
            }
            case 8 -> {
                value = snapshot.looseTrainingWeightsEnabled() ? "On" : "Off";
                color = snapshot.looseTrainingWeightsEnabled() ? ON : OFF;
            }
            default -> value = "";
        }

        drawFittedCentered(graphics, value, x + w / 2, y + 34, w - 12, color);
    }

    private void activateCell(int index, int direction) {
        QuickMenuAction action;
        TechniqueCategory category = null;

        switch (index) {
            case 0 -> action = QuickMenuAction.CYCLE_METHOD;
            case 1 -> {
                action = QuickMenuAction.CYCLE_TECHNIQUE;
                category = TechniqueCategory.CULTIVATION;
            }
            case 2 -> {
                action = QuickMenuAction.CYCLE_TECHNIQUE;
                category = TechniqueCategory.WEAPON;
            }
            case 3 -> {
                action = QuickMenuAction.CYCLE_TECHNIQUE;
                category = TechniqueCategory.FOOTWORK;
            }
            case 5 -> {
                action = QuickMenuAction.CYCLE_TECHNIQUE;
                category = TechniqueCategory.EYESIGHT;
            }
            case 6 -> action = QuickMenuAction.TOGGLE_RESOURCE_SCANNING;
            case 7 -> action = QuickMenuAction.TOGGLE_CULTIVATION_GAUGE;
            case 8 -> action = QuickMenuAction.TOGGLE_LOOSE_WEIGHTS;
            default -> {
                return;
            }
        }

        PacketDistributor.sendToServer(new QuickMenuActionPayload(action, category, direction));
    }

    private int cellAt(double mouseX, double mouseY) {
        int left = panelLeft();
        int top = panelTop();
        int width = cellWidth();

        for (int index = 0; index < COLUMNS * ROWS; index++) {
            int x = cellX(index);
            int y = cellY(index);
            if (mouseX >= x
                    && mouseX < x + width
                    && mouseY >= y
                    && mouseY < y + CELL_HEIGHT) {
                return index;
            }
        }

        return -1;
    }

    private void drawFittedCentered(
            GuiGraphics graphics,
            String text,
            int centerX,
            int y,
            int maxWidth,
            int color) {

        String safe = text == null ? "" : text;
        if (font.width(safe) <= maxWidth) {
            graphics.drawCenteredString(font, Component.literal(safe), centerX, y, color);
            return;
        }

        String trimmed = safe;
        while (!trimmed.isEmpty() && font.width(trimmed + "...") > maxWidth) {
            trimmed = trimmed.substring(0, trimmed.length() - 1);
        }
        graphics.drawCenteredString(font, Component.literal(trimmed + "..."), centerX, y, color);
    }

    private String prettyId(String id) {
        if (id == null || id.isBlank()) {
            return "None";
        }

        int slash = id.lastIndexOf('/');
        int colon = id.lastIndexOf(':');
        int start = Math.max(slash, colon) + 1;
        String raw = id.substring(Math.max(0, start)).replace('_', ' ').toLowerCase(Locale.ROOT);

        StringBuilder result = new StringBuilder();
        for (String part : raw.split(" ")) {
            if (part.isBlank()) {
                continue;
            }
            if (!result.isEmpty()) {
                result.append(' ');
            }
            result.append(Character.toUpperCase(part.charAt(0))).append(part.substring(1));
        }
        return result.toString();
    }

    private int panelWidth() {
        return Math.min(PANEL_MAX_WIDTH, Math.max(360, width - 80));
    }

    private int cellWidth() {
        return (panelWidth() - GAP * (COLUMNS - 1)) / COLUMNS;
    }

    private int panelLeft() {
        return (width - panelWidth()) / 2;
    }

    private int panelTop() {
        int totalHeight = ROWS * CELL_HEIGHT + GAP * (ROWS - 1);
        return Math.max(54, (height - totalHeight) / 2);
    }

    private int cellX(int index) {
        int column = index % COLUMNS;
        return panelLeft() + column * (cellWidth() + GAP);
    }

    private int cellY(int index) {
        int row = index / COLUMNS;
        return panelTop() + row * (CELL_HEIGHT + GAP);
    }
}
