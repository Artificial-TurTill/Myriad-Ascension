package io.github.artificialturtill.myriadascension.client.screen;

import io.github.artificialturtill.myriadascension.MyriadAscension;
import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;

public final class MartialWorldGuideScreen extends Screen {
    private static final int GOLD = 0xFFFFD978;
    private static final int TEXT = 0xFFF2EEE2;
    private static final int MUTED = 0xFFAAA79F;
    private static final int PANEL_SIZE = 256;

    private static final ResourceLocation PANEL_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(
                    MyriadAscension.MOD_ID,
                    "textures/gui/martial_world_guide.png");

    private static final List<GuidePage> PAGES = List.of(
            new GuidePage(
                    "Controls",
                    List.of(
                            "V — Cultivator Status",
                            "B — Hold the current Testudo training stance",
                            "G — Stage-dependent focus/circulation control",
                            "H — Lower conscious circulation",
                            "R — Toggle Burst when conscious Qi use is unlocked",
                            "",
                            "Tempered Body does not use G as generic active gathering.")),
            new GuidePage(
                    "The Cultivation Path",
                    List.of(
                            "You begin as a Mortal with no usable Qi.",
                            "Tempered Body strengthens and prepares the vessel.",
                            "Initial Element unlocks conscious Yuan Qi usage.",
                            "Qi Transformation unlocks external Qi projection.",
                            "True Element evolves Yuan Qi into True Qi.",
                            "Immortal Ascension unlocks Knowledge Sea and Divine Sense.")),
            new GuidePage(
                    "Tempered Body 1-3",
                    List.of(
                            "These stages are physical tempering.",
                            "No World Energy sensing or Yuan Qi gathering is available yet.",
                            "Testudo training can include stance work, load carrying,",
                            "striking exercises, controlled impacts, repetition and recovery.",
                            "",
                            "G is not required for this physical foundation training.")),
            new GuidePage(
                    "Tempered Body 4-6",
                    List.of(
                            "World Energy perception gradually develops.",
                            "Physical conditioning continues.",
                            "During Testudo training, G acts as focus/sense input only.",
                            "Earth-rich surroundings are compatible with the Testudo inheritance.",
                            "",
                            "These stages still do not consciously absorb or circulate Yuan Qi.")),
            new GuidePage(
                    "Tempered Body 7-9",
                    List.of(
                            "Natural World Energy absorption begins.",
                            "A sealed Yuan Qi reserve forms in the dantian and meridians.",
                            "The reserve accumulates passively; G does not accelerate it.",
                            "Power % and active Qi techniques remain locked.",
                            "",
                            "Initial Element is the threshold for conscious Yuan Qi use.")),
            new GuidePage(
                    "Technique Training",
                    List.of(
                            "Training depends on the technique, not one universal action.",
                            "Methods may advance through repeated use or combat.",
                            "Others require the correct elemental environment,",
                            "relevant elemental damage, or hostile-environment survival.",
                            "Rare Wood/Water methods may even use healing-based training.")),
            new GuidePage(
                    "Calm Cultivation",
                    List.of(
                            "Calm cultivation is the seated/meditative training path.",
                            "It is designed for Qi recovery, circulation, comprehension,",
                            "Meditation training and interaction with veins, arrays and chambers.",
                            "",
                            "The full seated-cultivation runtime is not yet implemented in this alpha.")),
            new GuidePage(
                    "Affinity & Attunement",
                    List.of(
                            "Innate Affinity is what the cultivator was born with.",
                            "Current Attunement begins equal to Innate Affinity but may improve.",
                            "Sources include aligned training, elemental resources, techniques,",
                            "environment, spirit veins, bloodlines, treasures and safe exposure.",
                            "",
                            "Low affinity is not a permanent class lock.",
                            "Numeric attunement-training sources are not fully wired in this alpha.")),
            new GuidePage(
                    "Power",
                    List.of(
                            "Realm Potential is the largest part of the combat scale.",
                            "Personal expression also depends on physical, energy, soul,",
                            "foundation, condition and battle factors.",
                            "Minor-stage gaps are meaningful but can be overcome.",
                            "Major-realm breakthroughs are qualitative jumps.",
                            "",
                            "Displayed realm potential is not literal Minecraft damage.")),
            new GuidePage(
                    "Techniques",
                    List.of(
                            "Manuals permanently record techniques as learned.",
                            "One technique per category may be equipped at a time.",
                            "Current categories: Cultivation, Footwork, Weapon and Eyesight.",
                            "Another technique in the same category replaces the active slot.",
                            "Some manuals may require clan permission, rank or affinity.")),
            new GuidePage(
                    "Medicines & Artifacts",
                    List.of(
                            "Medicines can repair health, injuries, meridians or fatigue.",
                            "Low-grade pills may leave impurities.",
                            "Artifacts include weapons, shields, armor, storage and utility tools.",
                            "Artifact grade is separate from the user's realm.",
                            "A strong artifact does not grant its wielder a higher-realm body.")),
            new GuidePage(
                    "The Martial World",
                    List.of(
                            "Villages may host clans, craftsmen and cultivation opportunities.",
                            "Clans and sects control techniques, inheritances and resources.",
                            "Alignment, affiliation, morality, Karma and politics are separate.",
                            "Spirit veins, formations, treasures and elemental regions matter.",
                            "Higher worlds eventually require ascension or interstellar travel."))
    );

    private int page;

    public MartialWorldGuideScreen() {
        super(Component.literal("Instruction to the Martial World"));
    }

    @Override
    protected void init() {
        int bottom = panelTop() + PANEL_SIZE - 27;

        addRenderableWidget(new MartialButton(
                panelLeft() + 12,
                bottom,
                28,
                20,
                Component.literal("<"),
                button -> changePage(-1)));

        addRenderableWidget(new MartialButton(
                panelLeft() + PANEL_SIZE - 40,
                bottom,
                28,
                20,
                Component.literal(">"),
                button -> changePage(1)));
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

        graphics.blit(
                PANEL_TEXTURE,
                left,
                top,
                PANEL_SIZE,
                PANEL_SIZE,
                0.0F,
                0.0F,
                PANEL_SIZE,
                PANEL_SIZE,
                PANEL_SIZE,
                PANEL_SIZE);

        graphics.drawCenteredString(
                font,
                title,
                left + PANEL_SIZE / 2,
                top + 10,
                GOLD);

        GuidePage current = PAGES.get(page);
        graphics.drawCenteredString(
                font,
                Component.literal(current.title()),
                left + PANEL_SIZE / 2,
                top + 33,
                GOLD);

        int y = top + 52;
        int textWidth = PANEL_SIZE - 36;
        graphics.enableScissor(left + 14, top + 46, left + PANEL_SIZE - 14, top + 221);
        for (String line : current.lines()) {
            if (line.isBlank()) {
                y += 8;
                continue;
            }

            List<FormattedCharSequence> wrapped =
                    font.split(Component.literal(line), textWidth);
            for (FormattedCharSequence part : wrapped) {
                if (y > top + 207) {
                    break;
                }
                graphics.drawString(font, part, left + 18, y, TEXT);
                y += 12;
            }
        }
        graphics.disableScissor();

        graphics.drawCenteredString(
                font,
                Component.literal((page + 1) + " / " + PAGES.size()),
                left + PANEL_SIZE / 2,
                top + PANEL_SIZE - 20,
                MUTED);

        super.render(graphics, mouseX, mouseY, partialTick);
    }

    private int panelLeft() {
        return (width - PANEL_SIZE) / 2;
    }

    private int panelTop() {
        return (height - PANEL_SIZE) / 2;
    }

    private record GuidePage(String title, List<String> lines) {
    }
}
