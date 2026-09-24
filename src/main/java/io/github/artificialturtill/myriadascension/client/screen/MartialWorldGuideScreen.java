package io.github.artificialturtill.myriadascension.client.screen;

import io.github.artificialturtill.myriadascension.MyriadAscension;
import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;

public final class MartialWorldGuideScreen extends Screen {
    private static final int GOLD = 0xFFFFD978;
    private static final int TEXT = 0xFFF2EEE2;
    private static final int MUTED = 0xFFAAA79F;
    private static final ResourceLocation PANEL_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(
                    MyriadAscension.MOD_ID,
                    "textures/gui/martial_world_guide.png");

    private static final List<GuidePage> PAGES = List.of(
            new GuidePage(
                    "Controls",
                    List.of(
                            "V — Cultivator Status",
                            "X — Cultivation Quick Menu (reserved)",
                            "G — Circulate/gather when the realm and method permit",
                            "H — Lower active circulation",
                            "R — Toggle Burst",
                            "B — Hold Foundation Training Stance",
                            "",
                            "Before Initial Element, G does not grant conscious Qi use.",
                            "During early Testudo training it is used as a breathing rhythm.")),
            new GuidePage(
                    "The Cultivation Path",
                    List.of(
                            "You begin as a Mortal with no usable Qi.",
                            "Cultivation requires a valid method or inheritance.",
                            "Tempered Body first strengthens the vessel.",
                            "Initial Element unlocks conscious Yuan Qi usage.",
                            "Qi Transformation unlocks external Qi projection.",
                            "True Element evolves Yuan Qi into True Qi.",
                            "Immortal Ascension unlocks Knowledge Sea and Divine Sense.",
                            "Higher realms eventually unlock Saint Qi, Shi, Domain,",
                            "Principles, Dao Seal, Small Universe and World Force.")),
            new GuidePage(
                    "Training",
                    List.of(
                            "Training depends on both realm and cultivation method.",
                            "The Primordialis Testudo path begins with physical foundation work.",
                            "Tempered Body 1-3: physical conditioning.",
                            "Tempered Body 4-6: World Energy perception.",
                            "Tempered Body 7-9: natural Yuan Qi accumulation.",
                            "",
                            "Fatigue, injuries, purity, environment and method quality matter.",
                            "A full progress bar alone does not guarantee a breakthrough.")),
            new GuidePage(
                    "Power",
                    List.of(
                            "Realm is the largest source of combat power, not the only one.",
                            "Strength, Vitality and Agility determine physical performance.",
                            "Meridian and Dantian quality influence energy throughput.",
                            "Spiritual Sense and Soul Strength govern spiritual power.",
                            "Purity, injuries, Qi reserve, Power %, mastery and artifacts modify",
                            "the effective combat index.",
                            "",
                            "A superior artifact does not grant the body speed or control of its rank.")),
            new GuidePage(
                    "Techniques",
                    List.of(
                            "Every technique exists as its own Manual or Scroll item.",
                            "Using a manual permanently records the technique as learned.",
                            "Only one technique per category may be equipped at a time.",
                            "Current categories: Cultivation, Footwork, Weapon and Eyesight.",
                            "Equipping another technique in the same category replaces the old one.",
                            "",
                            "Some manuals may require clan permission, rank, affinity or prerequisites.")),
            new GuidePage(
                    "Medicines & Poisons",
                    List.of(
                            "Medicines can repair health, body injuries, meridians or fatigue.",
                            "Low-grade pills may leave impurities in the vessel.",
                            "Poisons can affect both ordinary health and cultivation condition.",
                            "Antidotes remove ordinary poison but may not cure every exotic toxin.",
                            "",
                            "Alchemists will eventually refine grades far beyond the starting items.")),
            new GuidePage(
                    "Artifacts",
                    List.of(
                            "Artifacts include weapons, shields, armor, storage and utility tools.",
                            "Artifact grade is separate from the user's realm.",
                            "A weak cultivator can carry a strong artifact but may not fully exploit it.",
                            "",
                            "Minor Storage Bag: 9 internal inventory slots.",
                            "Storage contents remain attached to the bag itself.")),
            new GuidePage(
                    "The Martial World",
                    List.of(
                            "Villages may host clans, craftsmen and cultivation opportunities.",
                            "Clans and sects control techniques, inheritances and resources.",
                            "Alignment, affiliation, morality, Karma and politics are separate systems.",
                            "Spirit veins, formations, treasures and elemental regions will matter.",
                            "Higher worlds eventually require ascension or interstellar travel.",
                            "",
                            "The completed world is intended to grow from Mortal survival",
                            "to World Creation and mastery of entire universes."))
    );

    private int page;

    public MartialWorldGuideScreen() {
        super(Component.literal("Instruction to the Martial World"));
    }

    @Override
    protected void init() {
        int bottom = panelTop() + panelHeight() - 28;

        addRenderableWidget(Button.builder(
                Component.literal("<"),
                button -> changePage(-1))
                .bounds(panelLeft() + 12, bottom, 28, 20)
                .build());

        addRenderableWidget(Button.builder(
                Component.literal(">"),
                button -> changePage(1))
                .bounds(panelLeft() + panelWidth() - 40, bottom, 28, 20)
                .build());
    }

    private void changePage(int direction) {
        page = Math.floorMod(page + direction, PAGES.size());
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
        // In-world manual: keep the world sharp.
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        int left = panelLeft();
        int top = panelTop();
        int width = panelWidth();
        int height = panelHeight();

        graphics.blit(
                PANEL_TEXTURE,
                left,
                top,
                width,
                height,
                0.0F,
                0.0F,
                256,
                256,
                256,
                256);

        graphics.drawCenteredString(
                font,
                title,
                left + width / 2,
                top + 10,
                GOLD);

        GuidePage current = PAGES.get(page);
        graphics.drawCenteredString(
                font,
                Component.literal(current.title()),
                left + width / 2,
                top + 30,
                GOLD);

        int y = top + 52;
        int textWidth = width - 36;
        graphics.enableScissor(left + 14, top + 46, left + width - 14, top + height - 36);
        for (String line : current.lines()) {
            if (line.isBlank()) {
                y += 10;
                continue;
            }

            List<FormattedCharSequence> wrapped =
                    font.split(Component.literal(line), textWidth);
            for (FormattedCharSequence part : wrapped) {
                graphics.drawString(font, part, left + 18, y, TEXT);
                y += 13;
            }
        }
        graphics.disableScissor();

        graphics.drawCenteredString(
                font,
                Component.literal((page + 1) + " / " + PAGES.size()),
                left + width / 2,
                top + height - 22,
                MUTED);

        super.render(graphics, mouseX, mouseY, partialTick);
    }

    private int panelWidth() {
        return Math.min(520, Math.max(360, width - 40));
    }

    private int panelHeight() {
        return Math.min(300, Math.max(230, height - 40));
    }

    private int panelLeft() {
        return (width - panelWidth()) / 2;
    }

    private int panelTop() {
        return (height - panelHeight()) / 2;
    }

    private record GuidePage(String title, List<String> lines) {
    }
}
