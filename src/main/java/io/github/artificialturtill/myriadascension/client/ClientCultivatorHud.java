package io.github.artificialturtill.myriadascension.client;

import io.github.artificialturtill.myriadascension.MyriadAscension;
import io.github.artificialturtill.myriadascension.cultivation.realm.CultivationRealm;
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

    private static final ResourceLocation HUD_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(
                    MyriadAscension.MOD_ID,
                    "textures/gui/cultivator_hud.png");

    private static final int X = 8;
    private static final int Y = 8;
    private static final int WIDTH = 142;
    private static final int BAR_HEIGHT = 11;
    private static final int GAP = 3;

    private static final int BAR_BACKGROUND = 0x00000000;
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

        int visibleBars = visibleBars(data);
        int textureHeight = switch (visibleBars) {
            case 1 -> 15;
            case 2 -> 30;
            default -> 45;
        };

        graphics.blit(
                HUD_TEXTURE,
                X - 3,
                Y - 3,
                148,
                textureHeight,
                0.0F,
                0.0F,
                148,
                textureHeight,
                148,
                45);

        int y = Y;
        drawBar(
                graphics,
                y,
                "Health",
                health,
                maxHealth,
                HEALTH_FILL,
                decimal(health) + " / " + decimal(maxHealth));

        if (visibleBars >= 2) {
            y += BAR_HEIGHT + GAP;
            boolean sealed = data != null
                    && data.realm() == CultivationRealm.TEMPERED_BODY;
            drawBar(
                    graphics,
                    y,
                    sealed ? "Yuan Qi [Sealed]" : energyName(data),
                    qi,
                    maxQi,
                    QI_FILL,
                    decimal(qi) + " / " + decimal(maxQi));
        }

        if (visibleBars >= 3) {
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
    }

    private static int visibleBars(CultivatorSyncPayload data) {
        if (data == null || data.realm() == CultivationRealm.MORTAL) {
            return 1;
        }

        if (data.realm() == CultivationRealm.TEMPERED_BODY) {
            return data.minorStage() >= 7 ? 2 : 1;
        }

        return 3;
    }

    private static String energyName(CultivatorSyncPayload data) {
        if (data == null) {
            return "Qi";
        }

        CultivationRealm realm = data.realm();
        if (realm.ordinal() <= CultivationRealm.SEPARATION_AND_REUNION.ordinal()) {
            return "Yuan Qi";
        }
        if (realm.ordinal() <= CultivationRealm.TRANSCENDENT.ordinal()) {
            return "True Qi";
        }
        if (realm.ordinal() <= CultivationRealm.ORIGIN_KING.ordinal()) {
            return "Saint Qi";
        }
        if (realm == CultivationRealm.DAO_SOURCE) {
            return "Source Qi";
        }
        if (realm.ordinal() <= CultivationRealm.HALF_STEP_OPEN_HEAVEN.ordinal()) {
            return "Emperor Qi";
        }
        if (realm == CultivationRealm.OPEN_HEAVEN) {
            return "World Force";
        }
        return "Creation Power";
    }

    private static void drawBar(
            GuiGraphics graphics,
            int y,
            String label,
            double value,
            double maximum,
            int fillColor,
            String valueText) {

        if ((BAR_BACKGROUND >>> 24) != 0) {
            graphics.fill(X, y, X + WIDTH, y + BAR_HEIGHT, BAR_BACKGROUND);
        }

        double fraction = maximum <= 0.0D
                ? 0.0D
                : Math.max(0.0D, Math.min(1.0D, value / maximum));

        int fillWidth = (int) Math.round(WIDTH * fraction);
        if (fillWidth > 0) {
            graphics.fill(
                    X + 1,
                    y + 1,
                    X + Math.max(1, fillWidth - 1),
                    y + BAR_HEIGHT - 1,
                    fillColor);
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
