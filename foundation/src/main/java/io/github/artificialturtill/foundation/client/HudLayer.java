package io.github.artificialturtill.foundation.client;

import io.github.artificialturtill.foundation.character.Fighter;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

/** The resource HUD (JRMCoreGuiBars): Body, energy and Stamina bars, power release, Turbo and the active form, top left; switched off in the X menu. */
public final class HudLayer {
    public static void render(GuiGraphics g, DeltaTracker delta) {
        Minecraft mc = Minecraft.getInstance();
        Fighter f = ClientCharacter.fighter();
        if (f == null || !FoundationClient.hudVisible || mc.screen instanceof QuickMenuScreen || mc.options.hideGui || mc.player == null || mc.player.isSpectator()) return;
        Fighter.Caps caps = f.caps();
        int x = 4, y = 4, w = 90;
        boolean any = false;
        if (f.powerType().bodyLife()) { line(g, mc, x, y, w, f.body(), caps.body(), 0xFFD84040, Component.translatable("screen.ascension_foundation.body")); y += 11; any = true; }
        if (f.powerType().hasEnergy()) { line(g, mc, x, y, w, f.energy(), caps.energy(), 0xFF40C8FF, Names.energy(f.powerType())); y += 11; any = true; }
        if (caps.stamina() > 0) { line(g, mc, x, y, w, f.stamina(), caps.stamina(), 0xFFE8C040, Component.translatable("screen.ascension_foundation.stamina")); y += 11; any = true; }
        if (!any) return;
        String status = f.powerType().release() ? f.release() + " %" : "";
        if (mc.player.getData(io.github.artificialturtill.foundation.character.FoundationAttachments.LOOK).turbo())
            status += (status.isEmpty() ? "" : "  ") + Names.turbo(f.powerType()).getString();
        if (!f.inBaseForm()) status += (status.isEmpty() ? "" : "  ") + Names.of("form", f.form()).getString();
        if (!status.isEmpty()) g.drawString(mc.font, status, x, y + 1, 0xFFFFD27F, true);
    }
    private static void line(GuiGraphics g, Minecraft mc, int x, int y, int w, int value, int max, int color, Component label) {
        g.fill(x - 1, y - 1, x + w + 1, y + 5, 0xA0000000);
        int filled = max <= 0 ? 0 : (int) ((long) w * Math.clamp(value, 0, max) / max);
        g.fill(x, y, x + filled, y + 4, color);
        g.drawString(mc.font, label.getString() + " " + Names.num(value) + "/" + Names.num(max), x + w + 4, y - 2, 0xFFEDE6D6, true);
    }
    private HudLayer() {}
}
