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
    private static final ResourceLocation PANEL_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(
                    MyriadAscension.MOD_ID,
                    "textures/gui/martial_world_guide.png");

    private static final List<GuidePage> PAGES = List.of(
            new GuidePage(
                    "Controls",
                    List.of(
                            "V — Cultivator Status",
                            "X — Hold for the translucent Cultivation Quick Menu",
                            "B — Hold the Primordialis Testudo Foundation Stance",
                            "G — Stage 4-6: focus World Energy perception while physical tempering continues",
                            "G — Stage 7-9: consciously gather/store Yuan Qi; Initial Element+: recharge and circulate",
                            "H — Lower active circulation after Initial Element",
                            "R — Toggle Burst after active Qi is available",
                            "",
                            "Tempered Body 1-3 has no energy-control input.",
                            "Tempered Body 7-9 stores Yuan Qi only through deliberate G gathering; it cannot circulate it yet.")),
            new GuidePage(
                    "The Cultivation Path",
                    List.of(
                            "You begin as a Mortal with no usable Qi.",
                            "Cultivation requires a valid method or inheritance.",
                            "Tempered Body 1-3 strengthens and prepares the physical vessel.",
                            "Tempered Body 4-6 keeps active physical tempering while developing World Energy perception.",
                            "Tempered Body 7-9 consciously gathers Yuan Qi into the prepared vessel.",
                            "Initial Element unlocks true internal circulation, Qi use, and much easier active recharging.",
                            "Qi Transformation unlocks external Qi projection.",
                            "True Element evolves Yuan Qi into True Qi.",
                            "Immortal Ascension unlocks Knowledge Sea and Divine Sense.")),
            new GuidePage(
                    "Testudo Training",
                    List.of(
                            "The Primordialis Testudo path changes training by stage.",
                            "Stages 1-3: physical foundation. Hold B; G is not required.",
                            "Stages 4-6: keep tempering the body; hold B and use G to add perception work.",
                            "Stages 7-9: physical consolidation continues; hold G to consciously draw Yuan Qi into the vessel.",
                            "",
                            "At Stages 7-9 storing Qi is itself a feat; the reserve remains sealed from true circulation/use.",
                            "Location, future arrays, spirit veins and resources may improve gathering.",
                            "Initial Element still requires a separate breakthrough.")),
            new GuidePage(
                    "Training Methods",
                    List.of(
                            "Different techniques may demand different forms of practice.",
                            "Established routes include repeated technique use and combat.",
                            "Some methods improve in the correct elemental environment.",
                            "Some train by receiving matching elemental damage.",
                            "Some require surviving hostile environments.",
                            "Rare Wood/Water methods may use healing-based training.",
                            "",
                            "Technique training is not the same thing as raw realm progression.",
                            "Mastery, comprehension, fatigue, injury and environment all matter.")),
            new GuidePage(
                    "Affinity & Attunement",
                    List.of(
                            "Innate Affinity is the potential you were born with.",
                            "Current Attunement begins equal to Innate Affinity but can change.",
                            "Attunement may improve through aligned training, elemental resources,",
                            "techniques, environment and spirit veins.",
                            "Rare higher-ranked resources may permanently influence affinity.",
                            "",
                            "Affinity changes efficiency and compatibility; it is not a hard class lock.",
                            "Tempered Body can begin environmental attunement before active elemental Qi arts.")),
            new GuidePage(
                    "Power",
                    List.of(
                            "Realm Potential is the largest baseline source of combat power.",
                            "Strength, Vitality and Agility determine physical expression.",
                            "Meridian and Dantian quality influence foundation and throughput.",
                            "Spiritual Sense and Soul Strength govern spiritual capability.",
                            "Purity, injuries, reserve, Power %, mastery and artifacts modify",
                            "the effective Combat Index.",
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

        addRenderableWidget(new MartialButton(
                panelLeft() + 12,
                bottom,
                28,
                20,
                Component.literal("<"),
                button -> changePage(-1)));

        addRenderableWidget(new MartialButton(
                panelLeft() + panelWidth() - 40,
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
