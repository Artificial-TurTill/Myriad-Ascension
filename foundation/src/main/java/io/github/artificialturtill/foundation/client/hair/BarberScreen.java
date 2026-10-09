package io.github.artificialturtill.foundation.client.hair;

import io.github.artificialturtill.foundation.client.FoundationScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

/**
 * The Barber Menu (JRMCoreGuiScreen GUI 8): custom hairstyle (opens the five-page editor) or bald, the hair colour
 * (quick presets with arrows, any colour with the picker), Revert and Done. Done hands the result to whoever opened
 * the menu (the server from the character sheet, the creation wizard during creation).
 */
public final class BarberScreen extends FoundationScreen implements HairState.Preview {
    public BarberScreen() { super(Component.translatable("hair.ascension_foundation.barber")); }
    @Override protected Tab tab() { return null; }
    @Override public String previewHair() { return HairState.code; }
    @Override public int previewColor() { return HairState.color; }

    @Override protected void initContent() {
        int x = left + 150, y = top + 24;
        boolean bald = HairState.code.isEmpty();
        addRenderableWidget(Button.builder(Component.translatable(bald ? "hair.ascension_foundation.bald" : "hair.ascension_foundation.custom"),
            b -> { HairState.code = bald ? HairState.presetCode() : ""; rebuildWidgets(); }).bounds(x, y, 66, 16)
            .tooltip(net.minecraft.client.gui.components.Tooltip.create(Component.translatable("hair.ascension_foundation.bald_toggle"))).build());
        Button edit = addRenderableWidget(Button.builder(Component.translatable("hair.ascension_foundation.edit"),
            b -> minecraft.setScreen(new HairEditorScreen(0))).bounds(x + 70, y, 66, 16).build());
        edit.active = !bald;
        y += 30;
        if (!HairState.colorPresets.isEmpty()) {
            addRenderableWidget(new HairWidgets.SmallButton(x, y, "<", () -> cyclePreset(-1)));
            addRenderableWidget(new HairWidgets.SmallButton(x + 126, y, ">", () -> cyclePreset(1)));
        }
        addRenderableWidget(new HairWidgets.Swatch(x + 16, y, 104, () -> HairState.color,
            () -> minecraft.setScreen(new ColorPickerScreen(HairState.color, c -> HairState.color = c, BarberScreen::new))));
        int bottom = top + HEIGHT - 24;
        addRenderableWidget(Button.builder(Component.translatable("hair.ascension_foundation.revert"), b -> {
            HairState.code = HairState.original; HairState.color = HairState.originalColor; rebuildWidgets();
        }).bounds(left + 6, bottom, 60, 18).build());
        addRenderableWidget(Button.builder(Component.translatable("gui.cancel"), b -> minecraft.setScreen(HairState.home.get()))
            .bounds(left + WIDTH - 132, bottom, 60, 18).build());
        addRenderableWidget(Button.builder(Component.translatable("gui.done"), b -> {
            HairState.done.accept(new HairState.Result(HairState.code, HairState.color));
            minecraft.setScreen(HairState.home.get());
        }).bounds(left + WIDTH - 66, bottom, 60, 18).build());
    }
    private void cyclePreset(int direction) {
        var list = HairState.colorPresets;
        int at = list.indexOf(HairState.color);
        HairState.color = list.get(Math.floorMod(at < 0 ? (direction > 0 ? -1 : 0) + direction : at + direction, list.size()));
    }

    @Override protected void drawContent(GuiGraphics g, int mouseX, int mouseY) {
        header(g, title, left + 8, top + 6);
        text(g, Component.translatable("hair.ascension_foundation.style"), left + 150, top + 14, DIM);
        text(g, Component.translatable("hair.ascension_foundation.color"), left + 150, top + 44, DIM);
        if (!HairState.code.isEmpty() && HairState.presets.contains(HairState.code)) {
            text(g, Component.translatable("hair.ascension_foundation.preset_n", HairState.presets.indexOf(HairState.code)), left + 150, top + 74, DIM);
        }
        g.drawWordWrap(font, Component.translatable("hair.ascension_foundation.barber_note"), left + 150, top + 92, WIDTH - 156, DIM);
        g.enableScissor(left + 3, top + 18, left + 140, top + HEIGHT - 28);
        if (minecraft.player != null)
            HairState.drawHead(g, left + 70, top + 100, 70F, 25F - (mouseX - left - 70) * 0.2F, -(mouseY - top - 100) * 0.1F, minecraft.player);
        g.disableScissor();
    }
}
