package io.github.artificialturtill.myriadascension.client.screen;

import io.github.artificialturtill.myriadascension.client.ClientCultivatorState;
import io.github.artificialturtill.myriadascension.network.CultivatorSyncPayload;
import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class CultivatorStatusScreen extends Screen {
    private static final int GOLD = 0xFFFFD978;
    private static final int PALE_GOLD = 0xFFD8C690;
    private static final int TEXT = 0xFFF2EEE2;
    private static final int MUTED = 0xFFAAA79F;
    private static final int PANEL = 0xE014120F;
    private static final int PANEL_INNER = 0xD0201C16;
    private static final int BORDER = 0xFF8D6D2F;

    private final CultivatorSyncPayload initialData;
    private final Map<CultivatorStatusTab, Button> tabButtons =
            new EnumMap<>(CultivatorStatusTab.class);

    private CultivatorStatusTab selectedTab = CultivatorStatusTab.OVERVIEW;

    public CultivatorStatusScreen(CultivatorSyncPayload data) {
        super(Component.translatable("screen.myriad_ascension.status.title"));
        this.initialData = data;
    }

    @Override
    protected void init() {
        super.init();
        tabButtons.clear();

        int left = panelLeft();
        int top = panelTop();
        int navWidth = navigationWidth();

        int y = top + 36;
        for (CultivatorStatusTab tab : CultivatorStatusTab.values()) {
            Button button = addRenderableWidget(Button.builder(
                    Component.literal(tab.displayName()),
                    pressed -> selectTab(tab))
                    .bounds(left + 8, y, navWidth - 16, 20)
                    .build());
            tabButtons.put(tab, button);
            y += 25;
        }

        updateTabButtons();
    }

    private void selectTab(CultivatorStatusTab tab) {
        selectedTab = tab;
        updateTabButtons();
    }

    private void updateTabButtons() {
        for (Map.Entry<CultivatorStatusTab, Button> entry : tabButtons.entrySet()) {
            boolean selected = entry.getKey() == selectedTab;
            entry.getValue().active = !selected;
            entry.getValue().setMessage(Component.literal(
                    (selected ? "> " : "") + entry.getKey().displayName()));
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        drawFrame(graphics);

        CultivatorSyncPayload data = data();
        switch (selectedTab) {
            case OVERVIEW -> renderOverview(graphics, data);
            case STATS_AFFINITIES -> renderStatsAndAffinities(graphics, data);
            case SKILLS -> renderSkills(graphics, data);
            case TECHNIQUES -> renderTechniques(graphics, data);
        }

        super.render(graphics, mouseX, mouseY, partialTick);
    }

    private void drawFrame(GuiGraphics graphics) {
        int left = panelLeft();
        int top = panelTop();
        int width = panelWidth();
        int height = panelHeight();
        int navWidth = navigationWidth();

        graphics.fill(left, top, left + width, top + height, PANEL);
        graphics.fill(left + 2, top + 2, left + width - 2, top + height - 2, PANEL_INNER);

        graphics.fill(left, top, left + width, top + 2, BORDER);
        graphics.fill(left, top + height - 2, left + width, top + height, BORDER);
        graphics.fill(left, top, left + 2, top + height, BORDER);
        graphics.fill(left + width - 2, top, left + width, top + height, BORDER);

        graphics.fill(left + navWidth, top + 2, left + navWidth + 1, top + height - 2, BORDER);

        graphics.drawCenteredString(
                font,
                title,
                left + width / 2,
                top + 10,
                GOLD);

        graphics.drawCenteredString(
                font,
                Component.literal(selectedTab.displayName()),
                contentLeft() + contentWidth() / 2,
                top + 24,
                PALE_GOLD);
    }

    private void renderOverview(GuiGraphics graphics, CultivatorSyncPayload data) {
        int x = contentLeft() + 10;
        int right = contentLeft() + contentWidth() / 2 + 8;
        int y = contentTop();

        section(graphics, x, y, "Cultivator");
        y += 16;
        y = line(graphics, x, y, "Realm", realmText(data));
        y = line(graphics, x, y, "Method", prettyId(data.activeCultivationMethodId()));
        y = line(graphics, x, y, "Sex", pretty(data.sex().name()));
        y = line(graphics, x, y, "Affiliation", pretty(data.affiliation().name()));
        y = line(graphics, x, y, "Moral Alignment", signed(data.moralAlignment()));
        y = line(graphics, x, y, "Karma", decimal(data.karma()));

        int ry = contentTop();
        section(graphics, right, ry, "Qi & Condition");
        ry += 16;
        ry = line(graphics, right, ry, "Qi",
                decimal(data.currentQi()) + " / " + decimal(data.maximumQi()));
        ry = line(graphics, right, ry, "Circulation", decimal(data.circulationPercent()) + "%");
        ry = line(graphics, right, ry, "Burst", data.burstMode() ? "ACTIVE" : "Inactive");
        ry = line(graphics, right, ry, "Vessel Purity", decimal(data.vesselPurity()) + "%");
        ry = line(graphics, right, ry, "Impurities", decimal(data.impurityLoad()));
        ry = line(graphics, right, ry, "Demonic Qi", decimal(data.demonicQiContamination()));

        ry += 6;
        section(graphics, right, ry, "Bloodline");
        ry += 16;
        ry = line(graphics, right, ry, "Lineage", bloodlineText(data));
        ry = line(graphics, right, ry, "Purity", decimal(data.bloodlinePurity()) + "%");
        if (data.bloodlineConflictDamage() > 0.0D) {
            line(graphics, right, ry, "Conflict", decimal(data.bloodlineConflictDamage()));
        }

        int bottomY = panelTop() + panelHeight() - 48;
        graphics.drawString(
                font,
                Component.literal("Cultivation " + decimal(data.cultivationProgress())
                        + "   |   Comprehension " + decimal(data.cultivationComprehension())
                        + "   |   Battle " + decimal(data.battleComprehension())),
                x,
                bottomY,
                MUTED);

        graphics.drawString(
                font,
                Component.literal("Injuries — Body " + decimal(data.bodyInjury())
                        + " / Meridian " + decimal(data.meridianInjury())
                        + " / Soul " + decimal(data.soulInjury())
                        + " / Recovery " + decimal(data.recoveryDebt())),
                x,
                bottomY + 13,
                MUTED);
    }

    private void renderStatsAndAffinities(GuiGraphics graphics, CultivatorSyncPayload data) {
        int x = contentLeft() + 10;
        int right = contentLeft() + contentWidth() / 2 + 18;
        int y = contentTop();

        section(graphics, x, y, "Stats");
        y += 17;
        y = statLine(graphics, x, y, "Strength", data.strength());
        y = statLine(graphics, x, y, "Vitality", data.vitality());
        y = statLine(graphics, x, y, "Agility", data.agility());
        y = statLine(graphics, x, y, "Spiritual Sense", data.spiritualSense());
        y = statLine(graphics, x, y, "Meridian Quality", data.meridianQuality());
        y = statLine(graphics, x, y, "Dantian Quality", data.dantianQuality());
        statLine(graphics, x, y, "Soul Strength", data.soulStrength());

        int ry = contentTop();
        section(graphics, right, ry, "Affinities");
        ry += 17;
        ry = line(graphics, right, ry, "Wood", Integer.toString(data.wood()));
        ry = line(graphics, right, ry, "Fire", Integer.toString(data.fire()));
        ry = line(graphics, right, ry, "Earth", Integer.toString(data.earth()));
        ry = line(graphics, right, ry, "Metal", Integer.toString(data.metal()));
        ry = line(graphics, right, ry, "Water", Integer.toString(data.water()));
        ry = line(graphics, right, ry, "Yin", Integer.toString(data.yin()));
        line(graphics, right, ry, "Yang", Integer.toString(data.yang()));

        graphics.drawString(
                font,
                Component.literal("Core stats have a hard minimum value of 1."),
                x,
                panelTop() + panelHeight() - 28,
                MUTED);
    }

    private void renderSkills(GuiGraphics graphics, CultivatorSyncPayload data) {
        int x = contentLeft() + 14;
        int y = contentTop();

        section(graphics, x, y, "Cultivation Skills");
        y += 19;
        y = skillLine(graphics, x, y, "Passive Qi Recharging", data.passiveQiRechargingLevel());
        y = skillLine(graphics, x, y, "Meditation", data.meditationLevel());
        y = skillLine(graphics, x, y, "Qi Concealment", data.qiConcealmentLevel());

        y += 14;
        graphics.drawString(
                font,
                Component.literal("More skills will appear here as they are learned."),
                x,
                y,
                MUTED);
    }

    private void renderTechniques(GuiGraphics graphics, CultivatorSyncPayload data) {
        int x = contentLeft() + 14;
        int y = contentTop();

        section(graphics, x, y, "Equipped Technique Slots");
        y += 19;

        y = techniqueLine(graphics, x, y, "Cultivation",
                firstNonBlank(data.cultivationTechniqueId(), data.activeCultivationMethodId()));
        y = techniqueLine(graphics, x, y, "Footwork", data.footworkTechniqueId());
        y = techniqueLine(graphics, x, y, "Weapon", data.weaponTechniqueId());
        y = techniqueLine(graphics, x, y, "Eyesight", data.eyesightTechniqueId());

        y += 16;
        graphics.drawString(
                font,
                Component.literal("Only one technique can be equipped in each category."),
                x,
                y,
                PALE_GOLD);
        graphics.drawString(
                font,
                Component.literal("Equipping another technique in the same category replaces the current one."),
                x,
                y + 14,
                MUTED);
    }

    private CultivatorSyncPayload data() {
        CultivatorSyncPayload live = ClientCultivatorState.snapshot();
        return live == null ? initialData : live;
    }

    private int line(GuiGraphics graphics, int x, int y, String label, String value) {
        graphics.drawString(font, Component.literal(label + ": "), x, y, MUTED);
        graphics.drawString(
                font,
                Component.literal(value),
                x + font.width(label + ": "),
                y,
                TEXT);
        return y + 13;
    }

    private int statLine(GuiGraphics graphics, int x, int y, String label, double value) {
        return line(graphics, x, y, label, decimal(Math.max(1.0D, value)));
    }

    private int skillLine(GuiGraphics graphics, int x, int y, String label, int value) {
        return line(graphics, x, y, label, level(value));
    }

    private int techniqueLine(GuiGraphics graphics, int x, int y, String category, String techniqueId) {
        return line(graphics, x, y, category, prettyId(techniqueId));
    }

    private void section(GuiGraphics graphics, int x, int y, String name) {
        graphics.drawString(font, Component.literal(name), x, y, GOLD);
    }

    private String realmText(CultivatorSyncPayload data) {
        if (!data.realm().isCultivatorRealm() || !data.realm().hasSubdivisions()) {
            return data.realm().displayName();
        }

        return data.realm().displayName()
                + " — "
                + data.realm().subdivisionType().displayName()
                + " "
                + data.minorStage();
    }

    private String bloodlineText(CultivatorSyncPayload data) {
        if (data.bloodlineName() == null || data.bloodlineName().isBlank()) {
            return "None";
        }

        if (data.bloodlineGrade() == null || data.bloodlineGrade().isBlank()) {
            return data.bloodlineName();
        }

        return data.bloodlineName() + " — " + data.bloodlineGrade();
    }

    private static String level(int value) {
        return value <= 0 ? "Unlearned" : "Lv. " + value;
    }

    private static String firstNonBlank(String first, String second) {
        if (first != null && !first.isBlank()) {
            return first;
        }
        return second;
    }

    private static String prettyId(String id) {
        if (id == null || id.isBlank()) {
            return "None";
        }

        int slash = id.lastIndexOf('/');
        int colon = id.lastIndexOf(':');
        int start = Math.max(slash, colon) + 1;
        return pretty(id.substring(Math.max(0, start)).toUpperCase(Locale.ROOT));
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
        return Math.min(470, Math.max(320, width - 24));
    }

    private int panelHeight() {
        return Math.min(280, Math.max(220, height - 24));
    }

    private int panelLeft() {
        return (width - panelWidth()) / 2;
    }

    private int panelTop() {
        return (height - panelHeight()) / 2;
    }

    private int navigationWidth() {
        return Math.min(112, Math.max(96, panelWidth() / 4));
    }

    private int contentLeft() {
        return panelLeft() + navigationWidth() + 1;
    }

    private int contentWidth() {
        return panelWidth() - navigationWidth() - 1;
    }

    private int contentTop() {
        return panelTop() + 45;
    }
}
