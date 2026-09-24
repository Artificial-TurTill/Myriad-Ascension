package io.github.artificialturtill.myriadascension.client.screen;

import io.github.artificialturtill.myriadascension.MyriadAscension;
import io.github.artificialturtill.myriadascension.character.CharacterSex;
import io.github.artificialturtill.myriadascension.network.GenesisResultPayload;
import io.github.artificialturtill.myriadascension.network.SubmitGenesisPayload;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.PacketDistributor;

public final class CharacterGenesisScreen extends Screen {
    private static final ResourceLocation PANEL_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(
                    MyriadAscension.MOD_ID,
                    "textures/gui/character_genesis.png");

    private CharacterSex selectedSex = CharacterSex.UNSET;
    private Boolean benevolent;
    private boolean awaitingServer;
    private GenesisResultPayload result;

    private MartialButton maleButton;
    private MartialButton femaleButton;
    private MartialButton benevolentButton;
    private MartialButton maliciousButton;
    private MartialButton submitButton;
    private MartialButton finishButton;

    public CharacterGenesisScreen() {
        super(Component.translatable("screen.myriad_ascension.genesis.title"));
    }

    @Override
    protected void init() {
        super.init();

        int centerX = width / 2;
        int top = panelTop();

        maleButton = addRenderableWidget(new MartialButton(
                centerX - 155,
                top + 66,
                150,
                20,
                Component.translatable("screen.myriad_ascension.genesis.male"),
                button -> {
                    selectedSex = CharacterSex.MALE;
                    updateSelectionState();
                }));

        femaleButton = addRenderableWidget(new MartialButton(
                centerX + 5,
                top + 66,
                150,
                20,
                Component.translatable("screen.myriad_ascension.genesis.female"),
                button -> {
                    selectedSex = CharacterSex.FEMALE;
                    updateSelectionState();
                }));

        benevolentButton = addRenderableWidget(new MartialButton(
                centerX - 155,
                top + 116,
                150,
                20,
                Component.translatable("screen.myriad_ascension.genesis.benevolent"),
                button -> {
                    benevolent = true;
                    updateSelectionState();
                }));

        maliciousButton = addRenderableWidget(new MartialButton(
                centerX + 5,
                top + 116,
                150,
                20,
                Component.translatable("screen.myriad_ascension.genesis.malicious"),
                button -> {
                    benevolent = false;
                    updateSelectionState();
                }));

        submitButton = addRenderableWidget(new MartialButton(
                centerX - 75,
                top + 164,
                150,
                20,
                Component.translatable("screen.myriad_ascension.genesis.submit"),
                button -> submit()));

        finishButton = addRenderableWidget(new MartialButton(
                centerX - 75,
                top + 190,
                150,
                20,
                Component.translatable("screen.myriad_ascension.genesis.enter_world"),
                button -> {
                    if (result != null) {
                        minecraft.setScreen(null);
                    }
                }));

        updateSelectionState();
    }

    private void updateSelectionState() {
        if (maleButton == null) {
            return;
        }

        maleButton.setSelected(selectedSex == CharacterSex.MALE);
        femaleButton.setSelected(selectedSex == CharacterSex.FEMALE);
        benevolentButton.setSelected(Boolean.TRUE.equals(benevolent));
        maliciousButton.setSelected(Boolean.FALSE.equals(benevolent));

        boolean completeChoice = selectedSex != CharacterSex.UNSET && benevolent != null;
        submitButton.active = completeChoice && !awaitingServer && result == null;

        maleButton.active = !awaitingServer && result == null;
        femaleButton.active = !awaitingServer && result == null;
        benevolentButton.active = !awaitingServer && result == null;
        maliciousButton.active = !awaitingServer && result == null;

        finishButton.active = result != null;
        finishButton.visible = result != null;
    }

    private void submit() {
        if (awaitingServer
                || result != null
                || selectedSex == CharacterSex.UNSET
                || benevolent == null) {
            return;
        }

        awaitingServer = true;
        updateSelectionState();
        PacketDistributor.sendToServer(new SubmitGenesisPayload(selectedSex, benevolent));
    }

    public void applyResult(GenesisResultPayload result) {
        this.result = result;
        awaitingServer = false;
        selectedSex = result.sex();
        benevolent = result.moralAlignment() > 0;

        if (maleButton != null) {
            updateSelectionState();
        }
    }

    @Override
    public void onClose() {
        if (result != null) {
            minecraft.setScreen(null);
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
        // In-world/profile overlay; the custom PNG provides the visual shell.
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
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
                512,
                256,
                512,
                256);

        graphics.drawCenteredString(
                font,
                title,
                width / 2,
                top + 14,
                0xFFFFD978);

        if (result == null) {
            graphics.drawCenteredString(
                    font,
                    Component.translatable("screen.myriad_ascension.genesis.sex_prompt"),
                    width / 2,
                    top + 49,
                    0xFFD8C690);

            graphics.drawCenteredString(
                    font,
                    Component.translatable("screen.myriad_ascension.genesis.morality_prompt"),
                    width / 2,
                    top + 99,
                    0xFFD8C690);

            Component status = awaitingServer
                    ? Component.translatable("screen.myriad_ascension.genesis.awaiting")
                    : Component.translatable("screen.myriad_ascension.genesis.fate_note");

            graphics.drawCenteredString(
                    font,
                    status,
                    width / 2,
                    top + 148,
                    0xFFAAA79F);
        } else {
            graphics.drawCenteredString(
                    font,
                    Component.translatable("screen.myriad_ascension.genesis.result_title"),
                    width / 2,
                    top + 48,
                    0xFFFFD978);

            graphics.drawCenteredString(
                    font,
                    Component.literal("Wood " + result.wood()
                            + "   Fire " + result.fire()
                            + "   Earth " + result.earth()),
                    width / 2,
                    top + 78,
                    0xFFF2EEE2);

            graphics.drawCenteredString(
                    font,
                    Component.literal("Metal " + result.metal()
                            + "   Water " + result.water()),
                    width / 2,
                    top + 94,
                    0xFFF2EEE2);

            graphics.drawCenteredString(
                    font,
                    Component.literal("Yin " + result.yin()
                            + "   Yang " + result.yang()),
                    width / 2,
                    top + 110,
                    0xFFF2EEE2);

            graphics.drawCenteredString(
                    font,
                    Component.translatable(
                            result.moralAlignment() > 0
                                    ? "screen.myriad_ascension.genesis.result_benevolent"
                                    : "screen.myriad_ascension.genesis.result_malicious"),
                    width / 2,
                    top + 134,
                    0xFFD8C690);
        }

        super.render(graphics, mouseX, mouseY, partialTick);
    }

    private int panelWidth() {
        return Math.min(420, Math.max(350, width - 30));
    }

    private int panelHeight() {
        return Math.min(230, Math.max(215, height - 30));
    }

    private int panelLeft() {
        return (width - panelWidth()) / 2;
    }

    private int panelTop() {
        return Math.max(12, (height - panelHeight()) / 2);
    }
}
