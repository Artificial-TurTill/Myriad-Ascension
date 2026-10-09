package io.github.artificialturtill.foundation.client.hair;

import io.github.artificialturtill.foundation.character.HairCode;
import io.github.artificialturtill.foundation.client.FoundationScreen;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

/**
 * The custom hairstyle editor (JRMCoreGuiScreen GUI 20-24, ported from Dragonblock C+ HairEditor.Editor): Front,
 * Right, Left, Back and Top pages with one cell per hair group: Tier 1 sliders (X / Y / Z rotation, length), Tier 2
 * sliders (bend, bend point, shape change), show / hide, tier switch, reset to the preset; "repeat" copies the first
 * group of a page to all of them. Yaw / pitch turn the head preview. Front page: presets (built-in and saved ones),
 * save / delete preset. Side buttons: Share (copy the code), Paste, Reset (to the preset), page Back / Next, Done.
 */
public final class HairEditorScreen extends FoundationScreen implements HairState.Preview {
    private static final String[] PAGES = {"front", "right", "left", "back", "top"};
    private static final float[] PAGE_YAW = {0F, -90F, 90F, 180F, 0F}, PAGE_PITCH = {0F, 0F, 0F, 0F, -90F};
    private static final int[][] POS_H = {{0, 1, 2, 3, 0, 1, 2, 0, 1, 0}, {0, 1, 2, 3, 1, 2, 3, 2, 3, 3},
        {3, 2, 1, 0, 3, 2, 1, 0, 3, 2, 1, 0, 3, 2, 1, 0}, {0, 1, 2, 3, 0, 1, 2, 3, 0, 1, 2, 3, 0, 1, 2, 3}};
    private static final int[][] POS_V = {{0, 0, 0, 0, 1, 1, 1, 2, 2, 3}, {0, 0, 0, 0, 1, 1, 1, 2, 2, 3},
        {0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3}, {3, 3, 3, 3, 2, 2, 2, 2, 1, 1, 1, 1, 0, 0, 0, 0}};
    /** hbt: tier 2 shown per group; dfa: repeat mode per page; the head view angles. */
    private static final int[] tier = new int[HairCode.GROUPS];
    private static final boolean[] repeat = new boolean[5];
    private static int yaw, pitch;
    private final int page;
    private record Tip(Component text, int x, int y, int w, int h) {}
    private final List<Tip> tips = new ArrayList<>();

    public HairEditorScreen(int page) {
        super(Component.translatable("hair.ascension_foundation.page." + PAGES[page]));
        this.page = page;
        if (HairState.code.isEmpty()) HairState.code = HairState.presetCode();
    }
    @Override protected Tab tab() { return null; }
    @Override public String previewHair() { return HairState.code; }
    @Override public int previewColor() { return HairState.color; }

    private void apply(int face, int field, int value) {
        HairState.code = HairCode.set(HairState.code, face, field, value);
        if (repeat[page] && face == HairCode.PAGE_START[page])
            for (int other = face + 1; other < HairCode.PAGE_START[page + 1]; other++) HairState.code = HairCode.set(HairState.code, other, field, value);
    }
    private static int fromSlider(double v) { return Math.clamp((int) (v * 100.0 / 1.56) + 18, 18, 82); }
    private static double toSlider(int v) { return (int) ((v - 18) * 1.56F) * 0.01; }
    private void tip(int x, int y, int w, int h, Component text) { tips.add(new Tip(text, x, y, w, h)); }
    private static Component tr(String key, Object... args) { return Component.translatable("hair.ascension_foundation." + key, args); }

    @Override protected void initContent() {
        tips.clear();
        String code = HairState.code;
        int ph = 0, pv = 0;
        for (int face = HairCode.PAGE_START[page], i = 0; face < HairCode.PAGE_START[page + 1]; face++, i++) {
            final int f = face;
            int l = HairCode.get(code, f, HairCode.L);
            int h = page == 0 ? ph : POS_H[page - 1][i], v = page == 0 ? pv : POS_V[page - 1][i];
            int x = left + 80 + h * 45, y = top + 18 + v * 43;
            boolean grey = repeat[page] && i == 0, tier1 = tier[f] == 0;
            if (l != 0 && tier1) {
                int[] fields = {HairCode.X, HairCode.Y, HairCode.Z};
                String[] axes = {"x_axis", "y_axis", "z_axis"};
                for (int k = 0; k < 3; k++) {
                    int field = fields[k], value = HairCode.clamped(code, f, field);
                    addRenderableWidget(new HairWidgets.VSlider(x + k * 7, y, toSlider(value), grey, s -> apply(f, field, fromSlider(s))));
                    tip(x + k * 7, y, 5, 35, Component.empty().append(tr("tier", 1)).append("\n").append(tr("rotations")).append("\n")
                        .append(tr(axes[k])).append(": " + (value - 50)));
                }
                addRenderableWidget(new HairWidgets.VSlider(x + 21, y, l * 0.01, grey, s -> apply(f, HairCode.L, Math.clamp((int) (s * 100), 1, 99))));
                tip(x + 21, y, 5, 35, Component.empty().append(tr("tier", 1)).append("\n").append(tr("length")).append(": " + ((int) (l / 2.8F) + 1)));
            }
            if (l != 0 && !tier1) {
                int[] fields = {HairCode.B, HairCode.P, HairCode.T};
                String[] names = {"bend", "bendpoint", "transf"};
                for (int k = 0; k < 3; k++) {
                    int field = fields[k], value = HairCode.clamped(code, f, field);
                    addRenderableWidget(new HairWidgets.VSlider(x + k * 7, y, toSlider(value), grey, s -> apply(f, field, fromSlider(s))));
                    tip(x + k * 7, y, 5, 35, Component.empty().append(tr("tier", 2)).append("\n").append(tr(names[k])).append(": " + (value - 50)));
                }
            }
            addRenderableWidget(new HairWidgets.SmallButton(x + 28, y, l != 0 ? "o" : "+", () -> { apply(f, HairCode.L, l != 0 ? 0 : 1); rebuildWidgets(); }));
            tip(x + 28, y, 10, 10, Component.empty().append(tr(l != 0 ? "hide" : "show")).append(" #" + f));
            if (l != 0) {
                addRenderableWidget(new HairWidgets.SmallButton(x + 28, y + 12, tier1 ? "1" : "2", () -> {
                    int next = tier[f] < 1 ? 1 : 0;
                    tier[f] = next;
                    if (repeat[page] && f == HairCode.PAGE_START[page])
                        for (int other = f + 1; other < HairCode.PAGE_START[page + 1]; other++) tier[other] = next;
                    rebuildWidgets();
                }));
                tip(x + 28, y + 12, 10, 10, Component.empty().append(tr("tier", tier1 ? 1 : 2)).append("\n").append(tr("tier_desc")));
            }
            addRenderableWidget(new HairWidgets.SmallButton(x + 28, y + 24, "x", () -> {
                String def = HairState.presetCode();
                for (int field = 0; field <= HairCode.T; field += 2) HairState.code = HairCode.set(HairState.code, f, field, HairCode.get(def, f, field));
                rebuildWidgets();
            }));
            tip(x + 28, y + 24, 10, 10, tr("reset_group"));
            if (++ph > 3) { pv++; ph = 0; }
        }
        addRenderableWidget(new HairWidgets.SmallButton(left + 66, top + 42, repeat[page] ? "o" : "r", () -> { repeat[page] = !repeat[page]; rebuildWidgets(); }));
        tip(left + 66, top + 42, 10, 10, Component.empty().append(tr(repeat[page] ? "repeat_on" : "repeat_off")).append("\n").append(tr("repeat_desc")));

        // Head view: yaw / pitch arrows and reset.
        addRenderableWidget(new HairWidgets.SmallButton(left + 6, top + 18, "<", () -> yaw += 10));
        addRenderableWidget(new HairWidgets.SmallButton(left + 50, top + 18, ">", () -> yaw -= 10));
        addRenderableWidget(new HairWidgets.SmallButton(left + 6, top + 30, "<", () -> pitch += 10));
        addRenderableWidget(new HairWidgets.SmallButton(left + 50, top + 30, ">", () -> pitch -= 10));
        addRenderableWidget(new HairWidgets.SmallButton(left + 6, top + 42, "x", () -> { yaw = 0; pitch = 0; }));

        if (page == 0) {
            int py = top + 70;
            addRenderableWidget(new HairWidgets.SmallButton(left + 80, py, "<", () -> selectPreset(-1)));
            addRenderableWidget(new HairWidgets.SmallButton(left + 160, py, ">", () -> selectPreset(1)));
            if (!HairState.presets.contains(HairState.code)) {
                addRenderableWidget(Button.builder(tr("save_preset"), b -> {
                    HairState.presets.add(HairState.code); HairState.preset = HairState.presets.size() - 1; HairState.savePresets(); rebuildWidgets();
                }).bounds(left + 80, py + 14, 90, 14).build());
            }
            if (HairState.preset >= HairCode.PRESETS.length && HairState.presets.contains(HairState.code)) {
                addRenderableWidget(Button.builder(tr("delete_preset"), b -> {
                    HairState.presets.remove(HairState.preset); HairState.preset--; HairState.code = HairState.presetCode();
                    HairState.savePresets(); rebuildWidgets();
                }).bounds(left + 80, py + 30, 90, 14).build());
            }
        }
        // Side column.
        int sx = left + WIDTH - 44;
        String[] side = {"share", "paste", "reset"};
        for (int i = 0; i < side.length; i++) {
            final int action = i;
            addRenderableWidget(Button.builder(tr(side[i]), b -> {
                switch (action) {
                    case 0 -> minecraft.keyboardHandler.setClipboard(HairState.code);
                    case 1 -> {
                        String pasted = HairCode.upgrade(minecraft.keyboardHandler.getClipboard());
                        if (HairCode.valid(pasted)) HairState.code = pasted;
                    }
                    default -> HairState.code = HairState.presetCode();
                }
                rebuildWidgets();
            }).bounds(sx, top + 18 + i * 18, 40, 16).build());
            tip(sx, top + 18 + i * 18, 40, 16, tr(side[i] + "_desc"));
        }
        if (page > 0) addRenderableWidget(Button.builder(tr("back"), b -> minecraft.setScreen(new HairEditorScreen(page - 1))).bounds(sx, top + 120, 40, 16).build());
        if (page < 4) addRenderableWidget(Button.builder(tr("next"), b -> minecraft.setScreen(new HairEditorScreen(page + 1))).bounds(sx, top + 138, 40, 16).build());
        addRenderableWidget(Button.builder(Component.translatable("gui.done"), b -> minecraft.setScreen(new BarberScreen())).bounds(sx, top + HEIGHT - 24, 40, 18).build());
    }
    private void selectPreset(int direction) {
        HairState.loadPresets();
        HairState.preset = Math.floorMod(HairState.preset + direction, HairState.presets.size());
        HairState.code = HairState.presetCode();
        rebuildWidgets();
    }

    @Override protected void drawContent(GuiGraphics g, int mouseX, int mouseY) {
        header(g, title, left + 8, top + 4);
        text(g, String.valueOf(page + 1) + "/5", left + WIDTH - 60, top + 4, DIM);
        text(g, tr("yaw"), left + 18, top + 19, DIM);
        text(g, tr("pitch"), left + 18, top + 31, DIM);
        if (page == 0) {
            int p = HairState.preset;
            text(g, Component.empty().append(tr("preset_n", p)).append(" G" + (p < HairCode.PRESETS.length ? HairCode.presetVersion(p) : HairCode.version(HairState.presets.get(p)))),
                left + 94, top + 71, TEXT);
        }
        g.enableScissor(left + 3, top + 56, left + 78, top + HEIGHT - 4);
        if (minecraft.player != null)
            HairState.drawHead(g, left + 40, page == 4 ? top + 120 : top + 132, 85F, PAGE_YAW[page] + yaw, PAGE_PITCH[page] + pitch, minecraft.player);
        g.disableScissor();
        for (Tip t : tips) hover(t.text(), t.x(), t.y(), t.w(), t.h(), mouseX, mouseY);
    }
}
