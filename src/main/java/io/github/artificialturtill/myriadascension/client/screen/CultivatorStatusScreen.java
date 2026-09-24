package io.github.artificialturtill.myriadascension.client.screen;

import io.github.artificialturtill.myriadascension.ability.CultivationAbilityRules;
import io.github.artificialturtill.myriadascension.ability.CultivationAbilityState;
import io.github.artificialturtill.myriadascension.client.ClientCultivatorState;
import io.github.artificialturtill.myriadascension.cultivation.realm.CultivationRealm;
import io.github.artificialturtill.myriadascension.network.CultivatorSyncPayload;
import java.util.EnumMap;
import java.util.List;
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
    private static final int LOCKED = 0xFF8B7770;
    private static final int PANEL = 0xE014120F;
    private static final int PANEL_INNER = 0xD0201C16;
    private static final int BORDER = 0xFF8D6D2F;

    private final CultivatorSyncPayload initialData;
    private final Map<CultivatorStatusTab, Button> tabButtons =
            new EnumMap<>(CultivatorStatusTab.class);

    private CultivatorStatusTab selectedTab = CultivatorStatusTab.OVERVIEW;
    private int scrollOffset;

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
        scrollOffset = 0;
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
    public void renderBackground(
            GuiGraphics graphics,
            int mouseX,
            int mouseY,
            float partialTick) {
        // In-world character sheet: no vanilla menu blur.
    }

    @Override
    public boolean mouseScrolled(
            double mouseX,
            double mouseY,
            double scrollX,
            double scrollY) {

        if (selectedTab == CultivatorStatusTab.ABILITIES) {
            int max = maxAbilityScroll(data());
            if (scrollY > 0.0D) {
                scrollOffset = Math.max(0, scrollOffset - 1);
            } else if (scrollY < 0.0D) {
                scrollOffset = Math.min(max, scrollOffset + 1);
            }
            return true;
        }

        return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        drawFrame(graphics);

        CultivatorSyncPayload data = data();
        switch (selectedTab) {
            case OVERVIEW -> renderOverview(graphics, data);
            case STATS -> renderStats(graphics, data);
            case AFFINITIES -> renderAffinities(graphics, data);
            case SKILLS -> renderSkills(graphics, data);
            case TECHNIQUES -> renderTechniques(graphics, data);
            case ABILITIES -> renderAbilities(graphics, data);
            case CONDITIONS -> renderConditions(graphics, data);
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
        line(graphics, x, y, "Karma", decimal(data.karma()));

        int ry = contentTop();
        section(graphics, right, ry, "Energy");
        ry += 16;
        ry = line(graphics, right, ry, energyName(data.realm()),
                decimal(data.currentQi()) + " / " + decimal(data.maximumQi()));
        ry = line(graphics, right, ry, "Power", decimal(data.circulationPercent()) + "%");
        ry = line(graphics, right, ry, "Burst", data.burstMode() ? "ACTIVE" : "Inactive");
        ry = line(graphics, right, ry, "Combat Index", formatPower(data.currentCombatIndex()));

        ry += 7;
        section(graphics, right, ry, "Bloodline");
        ry += 16;
        ry = line(graphics, right, ry, "Lineage", bloodlineText(data));
        line(graphics, right, ry, "Purity", decimal(data.bloodlinePurity()) + "%");

        int bottomY = panelTop() + panelHeight() - 42;
        graphics.drawString(
                font,
                Component.literal("Cultivation " + decimal(data.cultivationProgress())
                        + "   |   Comprehension " + decimal(data.cultivationComprehension())
                        + "   |   Battle " + decimal(data.battleComprehension())),
                x,
                bottomY,
                MUTED);
    }

    private void renderStats(GuiGraphics graphics, CultivatorSyncPayload data) {
        int x = contentLeft() + 10;
        int right = contentLeft() + contentWidth() / 2 + 12;
        int y = contentTop();

        section(graphics, x, y, "Core Stats");
        y += 17;
        y = statLine(graphics, x, y, "Strength", data.strength());
        y = statLine(graphics, x, y, "Vitality", data.vitality());
        y = statLine(graphics, x, y, "Agility", data.agility());
        y = statLine(graphics, x, y, "Spiritual Sense", data.spiritualSense());
        y = statLine(graphics, x, y, "Meridian Quality", data.meridianQuality());
        y = statLine(graphics, x, y, "Dantian Quality", data.dantianQuality());
        statLine(graphics, x, y, "Soul Strength", data.soulStrength());

        int ry = contentTop();
        section(graphics, right, ry, "Power Breakdown");
        ry += 17;
        ry = line(graphics, right, ry, "Realm Potential", formatPower(data.realmPotential()));
        ry = line(graphics, right, ry, "Current Combat", formatPower(data.currentCombatIndex()));
        ry = line(graphics, right, ry, "Physical", factor(data.physicalFactor()));
        ry = line(graphics, right, ry, "Energy", factor(data.energyFactor()));
        ry = line(graphics, right, ry, "Soul", factor(data.soulFactor()));
        ry = line(graphics, right, ry, "Foundation", factor(data.foundationFactor()));
        ry = line(graphics, right, ry, "Condition", factor(data.conditionFactor()));
        line(graphics, right, ry, "Battle Skill", factor(data.battleFactor()));

        graphics.drawString(
                font,
                Component.literal("Physical: STR/VIT/AGI   Foundation: Meridians/Dantian/Purity"),
                x,
                panelTop() + panelHeight() - 42,
                MUTED);
        graphics.drawString(
                font,
                Component.literal("Core stats have a hard minimum value of 1."),
                x,
                panelTop() + panelHeight() - 28,
                MUTED);
    }

    private void renderAffinities(GuiGraphics graphics, CultivatorSyncPayload data) {
        int x = contentLeft() + 20;
        int y = contentTop();

        section(graphics, x, y, "Current Attunement");
        y += 19;
        y = line(graphics, x, y, "Wood", Integer.toString(data.wood()));
        y = line(graphics, x, y, "Fire", Integer.toString(data.fire()));
        y = line(graphics, x, y, "Earth", Integer.toString(data.earth()));
        y = line(graphics, x, y, "Metal", Integer.toString(data.metal()));
        y = line(graphics, x, y, "Water", Integer.toString(data.water()));
        y = line(graphics, x, y, "Yin", Integer.toString(data.yin()));
        line(graphics, x, y, "Yang", Integer.toString(data.yang()));

        int noteY = panelTop() + panelHeight() - 55;
        graphics.drawString(
                font,
                Component.literal("Affinity changes efficiency, learning, refinement and Dao compatibility."),
                x,
                noteY,
                MUTED);
        graphics.drawString(
                font,
                Component.literal("Low affinity makes a path harder; it does not permanently forbid it."),
                x,
                noteY + 14,
                MUTED);
    }

    private void renderSkills(GuiGraphics graphics, CultivatorSyncPayload data) {
        int x = contentLeft() + 18;
        int y = contentTop();

        section(graphics, x, y, "Learned Skills");
        y += 20;
        y = skillLine(graphics, x, y, "Passive Qi Recharging", data.passiveQiRechargingLevel());
        y = skillLine(graphics, x, y, "Meditation", data.meditationLevel());
        y = skillLine(graphics, x, y, "Qi Concealment", data.qiConcealmentLevel());

        y += 16;
        graphics.drawString(
                font,
                Component.literal("Skills are trainable competencies, separate from realm and techniques."),
                x,
                y,
                MUTED);
    }

    private void renderTechniques(GuiGraphics graphics, CultivatorSyncPayload data) {
        int x = contentLeft() + 18;
        int y = contentTop();

        section(graphics, x, y, "Equipped Technique Slots");
        y += 20;

        y = techniqueLine(graphics, x, y, "Cultivation",
                firstNonBlank(data.cultivationTechniqueId(), data.activeCultivationMethodId()));
        y = techniqueLine(graphics, x, y, "Footwork", data.footworkTechniqueId());
        y = techniqueLine(graphics, x, y, "Weapon", data.weaponTechniqueId());
        y = techniqueLine(graphics, x, y, "Eyesight", data.eyesightTechniqueId());

        y += 17;
        graphics.drawString(
                font,
                Component.literal("One active technique per category."),
                x,
                y,
                PALE_GOLD);
        graphics.drawString(
                font,
                Component.literal("Manuals teach techniques; equipping another replaces the current slot."),
                x,
                y + 14,
                MUTED);
    }

    private void renderAbilities(GuiGraphics graphics, CultivatorSyncPayload data) {
        List<CultivationAbilityState> abilities =
                CultivationAbilityRules.relevantForDisplay(data.realm(), data.minorStage());

        int visible = visibleAbilityCount();
        int start = Math.min(scrollOffset, Math.max(0, abilities.size() - visible));
        int end = Math.min(abilities.size(), start + visible);

        int x = contentLeft() + 14;
        int y = contentTop();

        for (int i = start; i < end; i++) {
            CultivationAbilityState ability = abilities.get(i);
            int color = ability.unlocked() ? GOLD : LOCKED;
            String prefix = ability.unlocked() ? "[Unlocked] " : "[Locked] ";

            graphics.drawString(
                    font,
                    Component.literal(prefix + ability.name()),
                    x,
                    y,
                    color);
            graphics.drawString(
                    font,
                    Component.literal(ability.description()),
                    x + 8,
                    y + 12,
                    ability.unlocked() ? TEXT : MUTED);
            y += 31;
        }

        if (abilities.size() > visible) {
            graphics.drawString(
                    font,
                    Component.literal("Mouse wheel to scroll  "
                            + (start + 1) + "-" + end + " / " + abilities.size()),
                    x,
                    panelTop() + panelHeight() - 26,
                    MUTED);
        }
    }

    private void renderConditions(GuiGraphics graphics, CultivatorSyncPayload data) {
        int x = contentLeft() + 18;
        int y = contentTop();

        section(graphics, x, y, "Foundation & Condition");
        y += 20;
        y = line(graphics, x, y, "Vessel Purity", decimal(data.vesselPurity()) + "%");
        y = line(graphics, x, y, "Impurities", decimal(data.impurityLoad()));
        y = line(graphics, x, y, "Demonic Qi", decimal(data.demonicQiContamination()));
        y = line(graphics, x, y, "Body Injury", decimal(data.bodyInjury()));
        y = line(graphics, x, y, "Meridian Injury", decimal(data.meridianInjury()));
        y = line(graphics, x, y, "Soul Injury", decimal(data.soulInjury()));
        y = line(graphics, x, y, "Recovery Debt", decimal(data.recoveryDebt()));
        y = line(graphics, x, y, "Bloodline Conflict", decimal(data.bloodlineConflictDamage()));
        line(graphics, x, y, "Condition Power", factor(data.conditionFactor()));

        graphics.drawString(
                font,
                Component.literal("Medicines may repair damage, but low-grade pills can add impurities."),
                x,
                panelTop() + panelHeight() - 28,
                MUTED);
    }

    private CultivatorSyncPayload data() {
        CultivatorSyncPayload live = ClientCultivatorState.snapshot();
        return live == null ? initialData : live;
    }

    private int maxAbilityScroll(CultivatorSyncPayload data) {
        int size = CultivationAbilityRules.relevantForDisplay(
                data.realm(),
                data.minorStage()).size();
        return Math.max(0, size - visibleAbilityCount());
    }

    private int visibleAbilityCount() {
        return Math.max(3, (panelHeight() - 86) / 31);
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

    private static String energyName(CultivationRealm realm) {
        if (realm.ordinal() <= CultivationRealm.SEPARATION_AND_REUNION.ordinal()) {
            return realm == CultivationRealm.MORTAL ? "Qi" : "Yuan Qi";
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

    private static String factor(double value) {
        return String.format(Locale.ROOT, "x%.2f", value);
    }

    private static String formatPower(double value) {
        double abs = Math.abs(value);
        if (abs < 1_000.0D) {
            return String.format(Locale.ROOT, "%.1f", value);
        }
        if (abs < 1_000_000.0D) {
            return String.format(Locale.ROOT, "%.2fK", value / 1_000.0D);
        }
        if (abs < 1_000_000_000.0D) {
            return String.format(Locale.ROOT, "%.2fM", value / 1_000_000.0D);
        }
        if (abs < 1_000_000_000_000.0D) {
            return String.format(Locale.ROOT, "%.2fB", value / 1_000_000_000.0D);
        }
        if (abs < 1.0E15D) {
            return String.format(Locale.ROOT, "%.2fT", value / 1.0E12D);
        }
        return String.format(Locale.ROOT, "%.2e", value);
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
        return Math.min(540, Math.max(360, width - 30));
    }

    private int panelHeight() {
        return Math.min(320, Math.max(240, height - 30));
    }

    private int panelLeft() {
        return (width - panelWidth()) / 2;
    }

    private int panelTop() {
        return (height - panelHeight()) / 2;
    }

    private int navigationWidth() {
        return Math.min(122, Math.max(104, panelWidth() / 4));
    }

    private int contentLeft() {
        return panelLeft() + navigationWidth() + 1;
    }

    private int contentWidth() {
        return panelWidth() - navigationWidth() - 1;
    }

    private int contentTop() {
        return panelTop() + 46;
    }
}
