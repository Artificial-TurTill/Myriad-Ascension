package io.github.artificialturtill.myriadascension.client.screen;

import io.github.artificialturtill.myriadascension.MyriadAscension;
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
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;

public final class CultivatorStatusScreen extends Screen {
    private static final ResourceLocation PANEL_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(
                    MyriadAscension.MOD_ID,
                    "textures/gui/cultivator_status_panel.png");

    private static final int GOLD = 0xFFFFD978;
    private static final int PALE_GOLD = 0xFFD8C690;
    private static final int TEXT = 0xFFF2EEE2;
    private static final int MUTED = 0xFFAAA79F;
    private static final int LOCKED = 0xFF8B7770;

    private static final int INNER_MARGIN = 12;
    private static final int COLUMN_GAP = 14;
    private static final int LINE_HEIGHT = 13;
    private static final int ABILITY_ROW_HEIGHT = 43;

    private final CultivatorSyncPayload initialData;
    private final Map<CultivatorStatusTab, CultivatorTabButton> tabButtons =
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

        int y = top + 38;
        for (CultivatorStatusTab tab : CultivatorStatusTab.values()) {
            CultivatorTabButton button = addRenderableWidget(
                    new CultivatorTabButton(
                            left + 9,
                            y,
                            navWidth - 18,
                            20,
                            Component.literal(tab.displayName()),
                            pressed -> selectTab(tab)));
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
        for (Map.Entry<CultivatorStatusTab, CultivatorTabButton> entry : tabButtons.entrySet()) {
            entry.getValue().setSelected(entry.getKey() == selectedTab);
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
        // Deliberately keep the world visible and sharp behind the in-world sheet.
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

        graphics.enableScissor(
                contentLeft() + 5,
                contentTop() - 4,
                panelLeft() + panelWidth() - 9,
                panelTop() + panelHeight() - 9);

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

        graphics.disableScissor();
        super.render(graphics, mouseX, mouseY, partialTick);
    }

    private void drawFrame(GuiGraphics graphics) {
        int left = panelLeft();
        int top = panelTop();

        graphics.blit(
                PANEL_TEXTURE,
                left,
                top,
                panelWidth(),
                panelHeight(),
                0.0F,
                0.0F,
                256,
                256,
                256,
                256);

        graphics.drawCenteredString(
                font,
                title,
                left + panelWidth() / 2,
                top + 10,
                GOLD);

        graphics.drawCenteredString(
                font,
                Component.literal(selectedTab.displayName()),
                contentLeft() + contentWidth() / 2,
                top + 25,
                PALE_GOLD);
    }

    private void renderOverview(GuiGraphics graphics, CultivatorSyncPayload data) {
        int x = contentLeft() + INNER_MARGIN;
        int innerWidth = contentWidth() - INNER_MARGIN * 2;
        int columnWidth = (innerWidth - COLUMN_GAP) / 2;
        int right = x + columnWidth + COLUMN_GAP;

        int y = contentTop();
        section(graphics, x, y, "Cultivator");
        y += 17;
        y = wrappedLine(graphics, x, y, "Realm", realmText(data), columnWidth);
        y = wrappedLine(graphics, x, y, "Method", prettyId(data.activeCultivationMethodId()), columnWidth);
        y = wrappedLine(graphics, x, y, "Sex", pretty(data.sex().name()), columnWidth);
        y = wrappedLine(graphics, x, y, "Affiliation", pretty(data.affiliation().name()), columnWidth);
        y = wrappedLine(graphics, x, y, "Moral Alignment", signed(data.moralAlignment()), columnWidth);
        wrappedLine(graphics, x, y, "Karma", decimal(data.karma()), columnWidth);

        int ry = contentTop();
        section(graphics, right, ry, "Energy");
        ry += 17;
        ry = wrappedLine(
                graphics,
                right,
                ry,
                energyName(data.realm(), data.minorStage()),
                decimal(data.currentQi()) + " / " + decimal(data.maximumQi()),
                columnWidth);
        boolean qiControlLocked = data.realm().ordinal() < CultivationRealm.INITIAL_ELEMENT.ordinal();
        ry = wrappedLine(
                graphics,
                right,
                ry,
                "Power",
                qiControlLocked ? "Locked until Initial Element" : decimal(data.circulationPercent()) + "%",
                columnWidth);
        ry = wrappedLine(
                graphics,
                right,
                ry,
                "Burst",
                qiControlLocked ? "Locked" : (data.burstMode() ? "ACTIVE" : "Inactive"),
                columnWidth);
        ry = wrappedLine(graphics, right, ry, "Combat Index", formatPower(data.currentCombatIndex()), columnWidth);

        ry += 7;
        section(graphics, right, ry, "Bloodline");
        ry += 17;
        ry = wrappedLine(graphics, right, ry, "Lineage", bloodlineText(data), columnWidth);
        wrappedLine(graphics, right, ry, "Purity", decimal(data.bloodlinePurity()) + "%", columnWidth);

        drawWrapped(
                graphics,
                "Cultivation " + decimal(data.cultivationProgress())
                        + "   |   Comprehension " + decimal(data.cultivationComprehension())
                        + "   |   Battle " + decimal(data.battleComprehension()),
                x,
                panelTop() + panelHeight() - 42,
                innerWidth,
                MUTED,
                1);
    }

    private void renderStats(GuiGraphics graphics, CultivatorSyncPayload data) {
        int x = contentLeft() + INNER_MARGIN;
        int innerWidth = contentWidth() - INNER_MARGIN * 2;
        int columnWidth = (innerWidth - COLUMN_GAP) / 2;
        int right = x + columnWidth + COLUMN_GAP;

        int y = contentTop();
        section(graphics, x, y, "Core Stats");
        y += 17;
        y = statLine(graphics, x, y, "Strength", data.strength(), columnWidth);
        y = statLine(graphics, x, y, "Vitality", data.vitality(), columnWidth);
        y = statLine(graphics, x, y, "Agility", data.agility(), columnWidth);
        y = statLine(graphics, x, y, "Spiritual Sense", data.spiritualSense(), columnWidth);
        y = statLine(graphics, x, y, "Meridian Quality", data.meridianQuality(), columnWidth);
        y = statLine(graphics, x, y, "Dantian Quality", data.dantianQuality(), columnWidth);
        statLine(graphics, x, y, "Soul Strength", data.soulStrength(), columnWidth);

        int ry = contentTop();
        section(graphics, right, ry, "Power Breakdown");
        ry += 17;
        ry = wrappedLine(graphics, right, ry, "Realm Potential", formatPower(data.realmPotential()), columnWidth);
        ry = wrappedLine(graphics, right, ry, "Current Combat", formatPower(data.currentCombatIndex()), columnWidth);
        ry = wrappedLine(graphics, right, ry, "Physical", factor(data.physicalFactor()), columnWidth);
        ry = wrappedLine(graphics, right, ry, "Energy", factor(data.energyFactor()), columnWidth);
        ry = wrappedLine(graphics, right, ry, "Soul", factor(data.soulFactor()), columnWidth);
        ry = wrappedLine(graphics, right, ry, "Foundation", factor(data.foundationFactor()), columnWidth);
        ry = wrappedLine(graphics, right, ry, "Condition", factor(data.conditionFactor()), columnWidth);
        wrappedLine(graphics, right, ry, "Battle Skill", factor(data.battleFactor()), columnWidth);

        drawWrapped(
                graphics,
                "Physical = STR/VIT/AGI. Foundation = Meridians/Dantian/Purity. Core stats never fall below 1.",
                x,
                panelTop() + panelHeight() - 46,
                innerWidth,
                MUTED,
                2);
    }

    private void renderAffinities(GuiGraphics graphics, CultivatorSyncPayload data) {
        int x = contentLeft() + 20;
        int available = contentWidth() - 40;
        int y = contentTop();

        section(graphics, x, y, "Current Attunement");
        y += 19;
        y = wrappedLine(graphics, x, y, "Wood", Integer.toString(data.wood()), available);
        y = wrappedLine(graphics, x, y, "Fire", Integer.toString(data.fire()), available);
        y = wrappedLine(graphics, x, y, "Earth", Integer.toString(data.earth()), available);
        y = wrappedLine(graphics, x, y, "Metal", Integer.toString(data.metal()), available);
        y = wrappedLine(graphics, x, y, "Water", Integer.toString(data.water()), available);
        y = wrappedLine(graphics, x, y, "Yin", Integer.toString(data.yin()), available);
        wrappedLine(graphics, x, y, "Yang", Integer.toString(data.yang()), available);

        drawWrapped(
                graphics,
                "Affinity changes efficiency, learning, refinement and Dao compatibility. "
                        + "Low affinity makes a path harder; it does not permanently forbid it.",
                x,
                panelTop() + panelHeight() - 55,
                available,
                MUTED,
                2);
    }

    private void renderSkills(GuiGraphics graphics, CultivatorSyncPayload data) {
        int x = contentLeft() + 18;
        int available = contentWidth() - 36;
        int y = contentTop();

        section(graphics, x, y, "Learned Skills");
        y += 20;
        y = skillLine(graphics, x, y, "Passive Qi Recharging", data.passiveQiRechargingLevel(), available);
        y = skillLine(graphics, x, y, "Meditation", data.meditationLevel(), available);
        y = skillLine(graphics, x, y, "Qi Concealment", data.qiConcealmentLevel(), available);

        y += 16;
        drawWrapped(
                graphics,
                "Skills are trainable competencies, separate from realm and equipped techniques.",
                x,
                y,
                available,
                MUTED,
                2);
    }

    private void renderTechniques(GuiGraphics graphics, CultivatorSyncPayload data) {
        int x = contentLeft() + 18;
        int available = contentWidth() - 36;
        int y = contentTop();

        section(graphics, x, y, "Equipped Technique Slots");
        y += 20;

        y = techniqueLine(
                graphics,
                x,
                y,
                "Cultivation",
                firstNonBlank(data.cultivationTechniqueId(), data.activeCultivationMethodId()),
                available);
        y = techniqueLine(graphics, x, y, "Footwork", data.footworkTechniqueId(), available);
        y = techniqueLine(graphics, x, y, "Weapon", data.weaponTechniqueId(), available);
        y = techniqueLine(graphics, x, y, "Eyesight", data.eyesightTechniqueId(), available);

        y += 14;
        graphics.drawString(font, Component.literal("One active technique per category."), x, y, PALE_GOLD);
        drawWrapped(
                graphics,
                "Manuals teach techniques. Equipping another technique in the same category replaces the current slot.",
                x,
                y + 14,
                available,
                MUTED,
                2);
    }

    private void renderAbilities(GuiGraphics graphics, CultivatorSyncPayload data) {
        List<CultivationAbilityState> abilities =
                CultivationAbilityRules.relevantForDisplay(data.realm(), data.minorStage());

        int visible = visibleAbilityCount();
        int start = Math.min(scrollOffset, Math.max(0, abilities.size() - visible));
        int end = Math.min(abilities.size(), start + visible);

        int x = contentLeft() + 14;
        int available = contentWidth() - 28;
        int y = contentTop();

        for (int i = start; i < end; i++) {
            CultivationAbilityState ability = abilities.get(i);
            int color = ability.unlocked() ? GOLD : LOCKED;
            String prefix = ability.unlocked() ? "[Unlocked] " : "[Locked] ";

            drawWrapped(
                    graphics,
                    prefix + ability.name(),
                    x,
                    y,
                    available,
                    color,
                    1);

            drawWrapped(
                    graphics,
                    ability.description(),
                    x + 8,
                    y + 13,
                    available - 8,
                    ability.unlocked() ? TEXT : MUTED,
                    2);

            y += ABILITY_ROW_HEIGHT;
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
        int available = contentWidth() - 36;
        int y = contentTop();

        section(graphics, x, y, "Foundation & Condition");
        y += 20;
        y = wrappedLine(graphics, x, y, "Vessel Purity", decimal(data.vesselPurity()) + "%", available);
        y = wrappedLine(graphics, x, y, "Impurities", decimal(data.impurityLoad()), available);
        y = wrappedLine(graphics, x, y, "Demonic Qi", decimal(data.demonicQiContamination()), available);
        y = wrappedLine(graphics, x, y, "Body Injury", decimal(data.bodyInjury()), available);
        y = wrappedLine(graphics, x, y, "Meridian Injury", decimal(data.meridianInjury()), available);
        y = wrappedLine(graphics, x, y, "Soul Injury", decimal(data.soulInjury()), available);
        y = wrappedLine(graphics, x, y, "Recovery Debt", decimal(data.recoveryDebt()), available);
        y = wrappedLine(graphics, x, y, "Bloodline Conflict", decimal(data.bloodlineConflictDamage()), available);
        wrappedLine(graphics, x, y, "Condition Power", factor(data.conditionFactor()), available);

        drawWrapped(
                graphics,
                "Medicines may repair damage, but low-grade pills can add impurities.",
                x,
                panelTop() + panelHeight() - 30,
                available,
                MUTED,
                2);
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
        return Math.max(3, (panelHeight() - 92) / ABILITY_ROW_HEIGHT);
    }

    private int wrappedLine(
            GuiGraphics graphics,
            int x,
            int y,
            String label,
            String value,
            int maxWidth) {

        String prefix = label + ": ";
        int prefixWidth = font.width(prefix);

        graphics.drawString(font, Component.literal(prefix), x, y, MUTED);

        int valueWidth = Math.max(48, maxWidth - prefixWidth);
        List<FormattedCharSequence> lines =
                font.split(Component.literal(value == null ? "" : value), valueWidth);

        if (lines.isEmpty()) {
            return y + LINE_HEIGHT;
        }

        int drawY = y;
        int maxLines = Math.min(3, lines.size());
        for (int i = 0; i < maxLines; i++) {
            graphics.drawString(
                    font,
                    lines.get(i),
                    x + prefixWidth,
                    drawY,
                    TEXT);
            drawY += LINE_HEIGHT;
        }

        return drawY;
    }

    private int statLine(
            GuiGraphics graphics,
            int x,
            int y,
            String label,
            double value,
            int maxWidth) {
        return wrappedLine(graphics, x, y, label, decimal(Math.max(1.0D, value)), maxWidth);
    }

    private int skillLine(
            GuiGraphics graphics,
            int x,
            int y,
            String label,
            int value,
            int maxWidth) {
        return wrappedLine(graphics, x, y, label, level(value), maxWidth);
    }

    private int techniqueLine(
            GuiGraphics graphics,
            int x,
            int y,
            String category,
            String techniqueId,
            int maxWidth) {
        return wrappedLine(graphics, x, y, category, prettyId(techniqueId), maxWidth);
    }

    private void drawWrapped(
            GuiGraphics graphics,
            String text,
            int x,
            int y,
            int maxWidth,
            int color,
            int maxLines) {

        List<FormattedCharSequence> lines =
                font.split(Component.literal(text == null ? "" : text), Math.max(20, maxWidth));

        int count = Math.min(maxLines, lines.size());
        for (int i = 0; i < count; i++) {
            graphics.drawString(font, lines.get(i), x, y + i * LINE_HEIGHT, color);
        }
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

    private static String energyName(CultivationRealm realm, int stage) {
        if (realm == CultivationRealm.TEMPERED_BODY && stage >= 7) {
            return "Yuan Qi (Sealed)";
        }
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
        return Math.min(620, Math.max(420, width - 30));
    }

    private int panelHeight() {
        return Math.min(340, Math.max(250, height - 30));
    }

    private int panelLeft() {
        return (width - panelWidth()) / 2;
    }

    private int panelTop() {
        return (height - panelHeight()) / 2;
    }

    private int navigationWidth() {
        return panelWidth() / 4;
    }

    private int contentLeft() {
        return panelLeft() + navigationWidth() + 1;
    }

    private int contentWidth() {
        return panelWidth() - navigationWidth() - 1;
    }

    private int contentTop() {
        return panelTop() + 48;
    }
}
