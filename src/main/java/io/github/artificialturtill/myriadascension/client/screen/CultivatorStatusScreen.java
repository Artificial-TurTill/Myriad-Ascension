package io.github.artificialturtill.myriadascension.client.screen;

import io.github.artificialturtill.myriadascension.network.CultivatorSyncPayload;
import java.util.Locale;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class CultivatorStatusScreen extends Screen {
    private final CultivatorSyncPayload data;

    public CultivatorStatusScreen(CultivatorSyncPayload data) {
        super(Component.translatable("screen.myriad_ascension.status.title"));
        this.data = data;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);

        int centerX = width / 2;
        int left = Math.max(16, centerX - 205);
        int right = centerX + 15;
        int top = Math.max(20, height / 2 - 105);

        graphics.drawCenteredString(font, title, centerX, top, 0xFFFFD978);

        int yLeft = top + 24;
        yLeft = line(graphics, left, yLeft, "Realm", realmText());
        yLeft = line(graphics, left, yLeft, "Sex", pretty(data.sex().name()));
        yLeft = line(graphics, left, yLeft, "Affiliation", pretty(data.affiliation().name()));
        yLeft = line(graphics, left, yLeft, "Moral Alignment", signed(data.moralAlignment()));
        yLeft = line(graphics, left, yLeft, "Karma", decimal(data.karma()));
        yLeft += 5;

        yLeft = line(graphics, left, yLeft, "Qi", decimal(data.currentQi()) + " / " + decimal(data.maximumQi()));
        yLeft = line(graphics, left, yLeft, "Circulation", decimal(data.circulationPercent()) + "%");
        yLeft = line(graphics, left, yLeft, "Burst", data.burstMode() ? "ACTIVE" : "Inactive");
        yLeft = line(graphics, left, yLeft, "Cultivation Progress", decimal(data.cultivationProgress()));
        yLeft = line(graphics, left, yLeft, "Cultivation Comprehension", decimal(data.cultivationComprehension()));
        yLeft = line(graphics, left, yLeft, "Battle Comprehension", decimal(data.battleComprehension()));

        int yRight = top + 24;
        graphics.drawString(font, Component.literal("Affinities"), right, yRight, 0xFFD8C690);
        yRight += 14;
        yRight = line(graphics, right, yRight, "Wood", Integer.toString(data.wood()));
        yRight = line(graphics, right, yRight, "Fire", Integer.toString(data.fire()));
        yRight = line(graphics, right, yRight, "Earth", Integer.toString(data.earth()));
        yRight = line(graphics, right, yRight, "Metal", Integer.toString(data.metal()));
        yRight = line(graphics, right, yRight, "Water", Integer.toString(data.water()));
        yRight = line(graphics, right, yRight, "Yin", Integer.toString(data.yin()));
        yRight = line(graphics, right, yRight, "Yang", Integer.toString(data.yang()));
        yRight += 5;

        graphics.drawString(font, Component.literal("Condition"), right, yRight, 0xFFD8C690);
        yRight += 14;
        yRight = line(graphics, right, yRight, "Vessel Purity", decimal(data.vesselPurity()) + "%");
        yRight = line(graphics, right, yRight, "Impurities", decimal(data.impurityLoad()));
        yRight = line(graphics, right, yRight, "Demonic Qi", decimal(data.demonicQiContamination()));
        yRight = line(graphics, right, yRight, "Body Injury", decimal(data.bodyInjury()));
        yRight = line(graphics, right, yRight, "Meridian Injury", decimal(data.meridianInjury()));
        yRight = line(graphics, right, yRight, "Soul Injury", decimal(data.soulInjury()));
        yRight = line(graphics, right, yRight, "Recovery Debt", decimal(data.recoveryDebt()));
        yRight += 5;

        graphics.drawString(font, Component.literal("Skills"), right, yRight, 0xFFD8C690);
        yRight += 14;
        yRight = line(graphics, right, yRight, "Passive Qi Recharging", level(data.passiveQiRechargingLevel()));
        yRight = line(graphics, right, yRight, "Meditation", level(data.meditationLevel()));
        line(graphics, right, yRight, "Qi Concealment", level(data.qiConcealmentLevel()));
    }

    private int line(GuiGraphics graphics, int x, int y, String label, String value) {
        graphics.drawString(font, Component.literal(label + ": " + value), x, y, 0xFFFFFFFF);
        return y + 12;
    }

    private String realmText() {
        if (!data.realm().isCultivatorRealm()) {
            return data.realm().displayName();
        }
        return data.realm().displayName() + " — Rank " + data.minorStage();
    }

    private static String level(int value) {
        return value <= 0 ? "Unlearned" : "Lv. " + value;
    }

    private static String signed(double value) {
        return (value > 0 ? "+" : "") + decimal(value);
    }

    private static String decimal(double value) {
        return String.format(Locale.ROOT, "%.1f", value);
    }

    private static String pretty(String value) {
        String[] parts = value.toLowerCase(Locale.ROOT).split("_");
        StringBuilder result = new StringBuilder();
        for (String part : parts) {
            if (!result.isEmpty()) {
                result.append(' ');
            }
            result.append(Character.toUpperCase(part.charAt(0))).append(part.substring(1));
        }
        return result.toString();
    }
}
