package io.github.artificialturtill.myriadascension.client.screen;

import io.github.artificialturtill.myriadascension.character.CharacterSex;
import io.github.artificialturtill.myriadascension.network.GenesisResultPayload;
import io.github.artificialturtill.myriadascension.network.SubmitGenesisPayload;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.network.PacketDistributor;

public final class CharacterGenesisScreen extends Screen {
    private CharacterSex selectedSex = CharacterSex.UNSET;
    private Boolean benevolent;
    private boolean awaitingServer;
    private GenesisResultPayload result;

    private Button maleButton;
    private Button femaleButton;
    private Button benevolentButton;
    private Button maliciousButton;
    private Button submitButton;
    private Button finishButton;

    public CharacterGenesisScreen() {
        super(Component.translatable("screen.myriad_ascension.genesis.title"));
    }

    @Override
    protected void init() {
        super.init();

        int centerX = this.width / 2;
        int top = Math.max(35, this.height / 2 - 95);

        maleButton = addRenderableWidget(Button.builder(
                Component.translatable("screen.myriad_ascension.genesis.male"),
                button -> {
                    selectedSex = CharacterSex.MALE;
                    updateSelectionState();
                }).bounds(centerX - 155, top + 35, 150, 20).build());

        femaleButton = addRenderableWidget(Button.builder(
                Component.translatable("screen.myriad_ascension.genesis.female"),
                button -> {
                    selectedSex = CharacterSex.FEMALE;
                    updateSelectionState();
                }).bounds(centerX + 5, top + 35, 150, 20).build());

        benevolentButton = addRenderableWidget(Button.builder(
                Component.translatable("screen.myriad_ascension.genesis.benevolent"),
                button -> {
                    benevolent = true;
                    updateSelectionState();
                }).bounds(centerX - 155, top + 80, 150, 20).build());

        maliciousButton = addRenderableWidget(Button.builder(
                Component.translatable("screen.myriad_ascension.genesis.malicious"),
                button -> {
                    benevolent = false;
                    updateSelectionState();
                }).bounds(centerX + 5, top + 80, 150, 20).build());

        submitButton = addRenderableWidget(Button.builder(
                Component.translatable("screen.myriad_ascension.genesis.submit"),
                button -> submit()).bounds(centerX - 75, top + 125, 150, 20).build());

        finishButton = addRenderableWidget(Button.builder(
                Component.translatable("screen.myriad_ascension.genesis.enter_world"),
                button -> {
                    if (result != null) {
                        this.minecraft.setScreen(null);
                    }
                }).bounds(centerX - 75, top + 150, 150, 20).build());

        updateSelectionState();
    }

    private void updateSelectionState() {
        if (maleButton == null) {
            return;
        }

        maleButton.setMessage(Component.translatable(
                selectedSex == CharacterSex.MALE
                        ? "screen.myriad_ascension.genesis.male_selected"
                        : "screen.myriad_ascension.genesis.male"));

        femaleButton.setMessage(Component.translatable(
                selectedSex == CharacterSex.FEMALE
                        ? "screen.myriad_ascension.genesis.female_selected"
                        : "screen.myriad_ascension.genesis.female"));

        benevolentButton.setMessage(Component.translatable(
                Boolean.TRUE.equals(benevolent)
                        ? "screen.myriad_ascension.genesis.benevolent_selected"
                        : "screen.myriad_ascension.genesis.benevolent"));

        maliciousButton.setMessage(Component.translatable(
                Boolean.FALSE.equals(benevolent)
                        ? "screen.myriad_ascension.genesis.malicious_selected"
                        : "screen.myriad_ascension.genesis.malicious"));

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
        if (awaitingServer || result != null || selectedSex == CharacterSex.UNSET || benevolent == null) {
            return;
        }

        awaitingServer = true;
        updateSelectionState();
        PacketDistributor.sendToServer(new SubmitGenesisPayload(selectedSex, benevolent));
    }

    public void applyResult(GenesisResultPayload result) {
        this.result = result;
        this.awaitingServer = false;
        this.selectedSex = result.sex();
        this.benevolent = result.moralAlignment() > 0;

        if (maleButton != null) {
            updateSelectionState();
        }
    }

    @Override
    public void onClose() {
        if (result != null) {
            this.minecraft.setScreen(null);
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);

        int centerX = this.width / 2;
        int top = Math.max(35, this.height / 2 - 95);

        graphics.drawCenteredString(this.font, this.title, centerX, top, 0xFFFFFFFF);

        if (result == null) {
            graphics.drawCenteredString(
                    this.font,
                    Component.translatable("screen.myriad_ascension.genesis.sex_prompt"),
                    centerX,
                    top + 20,
                    0xFFD8C690);

            graphics.drawCenteredString(
                    this.font,
                    Component.translatable("screen.myriad_ascension.genesis.morality_prompt"),
                    centerX,
                    top + 65,
                    0xFFD8C690);

            Component status = awaitingServer
                    ? Component.translatable("screen.myriad_ascension.genesis.awaiting")
                    : Component.translatable("screen.myriad_ascension.genesis.fate_note");

            graphics.drawCenteredString(this.font, status, centerX, top + 110, 0xFFAAAAAA);
            return;
        }

        graphics.drawCenteredString(
                this.font,
                Component.translatable("screen.myriad_ascension.genesis.result_title"),
                centerX,
                top + 20,
                0xFFFFD978);

        graphics.drawCenteredString(
                this.font,
                Component.literal("Wood " + result.wood()
                        + "   Fire " + result.fire()
                        + "   Earth " + result.earth()),
                centerX,
                top + 52,
                0xFFFFFFFF);

        graphics.drawCenteredString(
                this.font,
                Component.literal("Metal " + result.metal()
                        + "   Water " + result.water()),
                centerX,
                top + 68,
                0xFFFFFFFF);

        graphics.drawCenteredString(
                this.font,
                Component.literal("Yin " + result.yin()
                        + "   Yang " + result.yang()),
                centerX,
                top + 84,
                0xFFFFFFFF);

        graphics.drawCenteredString(
                this.font,
                Component.translatable(
                        result.moralAlignment() > 0
                                ? "screen.myriad_ascension.genesis.result_benevolent"
                                : "screen.myriad_ascension.genesis.result_malicious"),
                centerX,
                top + 105,
                0xFFD8C690);
    }
}
