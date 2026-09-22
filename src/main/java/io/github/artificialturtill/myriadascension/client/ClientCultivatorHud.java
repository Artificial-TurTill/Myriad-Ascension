package io.github.artificialturtill.myriadascension.client;

import io.github.artificialturtill.myriadascension.MyriadAscension;
import io.github.artificialturtill.myriadascension.network.CultivatorSyncPayload;
import java.util.Locale;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;

public final class ClientCultivatorHud {
    private static final ResourceLocation LAYER_ID =
            ResourceLocation.fromNamespaceAndPath(
                    MyriadAscension.MOD_ID,
                    "cultivator_hud");

    private static final int X = 8;
    private static final int Y = 8;
    private static final int WIDTH = 142;
    private static final int BAR_HEIGHT = 11;
    private static final int GAP = 3;

    private static final int PANEL = 0xB0100E0B;
    private static final int BORDER = 0xCC7B5A24;
    private static final int BAR_BACKGROUND = 0xCC25211C;
    private static final int HEALTH_FILL = 0xD8B12D2D;
    private static final int QI_FILL = 0xD8387CC6;
    private static final int POWER_FILL = 0xD8D49A38;
    private static final int TEXT = 0xFFF6F0E2;

    private ClientCultivatorHud() {
    }

    public static void register(RegisterGuiLayersEvent event) {
        event.registerAboveAll(LAYER_ID, ClientCultivatorHud::render);
    }

    private static void render(GuiGraphics graphics, DeltaTracker deltaTracker) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null) {
            return;
        }

        CultivatorSyncPayload data = ClientCultivatorState.snapshot();

        double health = Math.max(0.0D, minecraft.player.getHealth());
        double maxHealth = Math.max(1.0D, minecraft.player.getMaxHealth());

        double qi = data == null ? 0.0D : Math.max(0.0D, data.currentQi());
        double maxQi = data == null ? 0.0D : Math.max(0.0D, data.maximumQi());
        double power = data == null
                ? 0.0D
                : Math.max(0.0D, Math.min(100.0D, data.circulationPercent()));

        int totalHeight = 6 + (BAR_HEIGHT * 3) + (GAP * 2);
        graphics.fill(X - 3, Y - 3, X + WIDTH + 3, Y + totalHeight + 3, PANEL);
        graphics.fill(X - 3, Y - 3, X + WIDTH + 3, Y - 2, BORDER);
        graphics.fill(X - 3, Y + totalHeight + 2, X + WIDTH + 3, Y + totalHeight + 3, BORDER);

        int y = Y;
        drawBar(
                graphics,
                y,
                "Health",
                health,
                maxHealth,
                HEALTH_FILL,
                decimal(health) + " / " + decimal(maxHealth));

        y += BAR_HEIGHT + GAP;
        drawBar(
                graphics,
                y,
                "Qi",
                qi,
                maxQi,
                QI_FILL,
                decimal(qi) + " / " + decimal(maxQi));

        y += BAR_HEIGHT + GAP;
        String burstSuffix = data != null && data.burstMode() ? "  BURST" : "";
        drawBar(
                graphics,
                y,
                "Power",
                power,
                100.0D,
                POWER_FILL,
                Integer.toString((int) Math.round(power)) + "%" + burstSuffix);
    }

    private static void drawBar(
            GuiGraphics graphics,
            int y,
            String label,
            double value,
            double maximum,
            int fillColor,
            String valueText) {

        graphics.fill(X, y, X + WIDTH, y + BAR_HEIGHT, BAR_BACKGROUND);

        double fraction = maximum <= 0.0D
                ? 0.0D
                : Math.max(0.0D, Math.min(1.0D, value / maximum));

        int fillWidth = (int) Math.round(WIDTH * fraction);
        if (fillWidth > 0) {
            graphics.fill(X, y, X + fillWidth, y + BAR_HEIGHT, fillColor);
        }

        String text = label + "  " + valueText;
        graphics.drawString(
                Minecraft.getInstance().font,
                text,
                X + 3,
                y + 2,
                TEXT,
                true);
    }

    private static String decimal(double value) {
        return String.format(Locale.ROOT, "%.1f", value);
    }
}
