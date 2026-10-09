package io.github.artificialturtill.foundation.client;

import io.github.artificialturtill.foundation.character.Fighter;
import io.github.artificialturtill.foundation.content.Transformation;
import io.github.artificialturtill.foundation.network.CharacterActionPayload;
import java.util.List;
import java.util.Locale;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.network.PacketDistributor;

/**
 * Transformations of the character's power type and race (DBC forms): requirements, multipliers, drain, mastery,
 * which one the transform key aims for, and Ascend / Descend buttons (the same as the keys).
 */
public final class FormsScreen extends FoundationScreen {
    private static final int ROW = 30, PER_PAGE = 4;
    private static int scroll;

    public FormsScreen() { super(Component.translatable("screen.ascension_foundation.tab.forms")); }
    @Override protected Tab tab() { return Tab.FORMS; }
    private int rowY(int i) { return top + 40 + i * ROW; }

    @Override protected void initContent() {
        Fighter f = ClientCharacter.fighter();
        if (f == null) return;
        addRenderableWidget(Button.builder(Component.translatable("screen.ascension_foundation.ascend"),
            b -> PacketDistributor.sendToServer(CharacterActionPayload.of(CharacterActionPayload.ASCEND))).bounds(left + WIDTH - 130, top + 4, 60, 16).build());
        addRenderableWidget(Button.builder(Component.translatable("screen.ascension_foundation.descend"),
            b -> PacketDistributor.sendToServer(CharacterActionPayload.of(CharacterActionPayload.DESCEND))).bounds(left + WIDTH - 66, top + 4, 60, 16).build())
            .active = !f.inBaseForm();
        List<Transformation> forms = f.availableForms();
        scroll = Math.clamp(scroll, 0, Math.max(0, forms.size() - PER_PAGE));
        for (int i = 0; i < Math.min(PER_PAGE, forms.size() - scroll); i++) {
            Transformation t = forms.get(scroll + i);
            boolean selected = t.id().equals(f.selectedForm());
            addRenderableWidget(Button.builder(Component.translatable(selected ? "screen.ascension_foundation.selected" : "screen.ascension_foundation.select"),
                b -> PacketDistributor.sendToServer(new CharacterActionPayload(CharacterActionPayload.SELECT_FORM, selected ? "" : t.id(), 0, 0)))
                .bounds(left + WIDTH - 66, rowY(i) + 6, 56, 16).build());
        }
    }
    @Override public boolean mouseScrolled(double mouseX, double mouseY, double dx, double dy) {
        scroll -= (int) Math.signum(dy);
        rebuildWidgets();
        return true;
    }

    @Override protected void drawContent(GuiGraphics g, int mx, int my) {
        Fighter f = ClientCharacter.fighter();
        if (f == null) return;
        header(g, title, left + 8, top + 6);
        pair(g, Component.translatable("screen.ascension_foundation.form"),
            f.inBaseForm() ? Component.translatable("screen.ascension_foundation.base_form").getString() : Names.of("form", f.form()).getString(), left + 8, top + 22, ACCENT);
        List<Transformation> forms = f.availableForms();
        if (forms.isEmpty()) { g.drawWordWrap(font, Component.translatable("screen.ascension_foundation.no_forms"), left + 8, top + 42, WIDTH - 16, DIM); return; }
        for (int i = 0; i < Math.min(PER_PAGE, forms.size() - scroll); i++) {
            Transformation t = forms.get(scroll + i);
            int y = rowY(i);
            boolean active = t.id().equals(f.form());
            g.fill(left + 4, y, left + WIDTH - 4, y + ROW - 2, active ? 0x40FFD27F : i % 2 == 0 ? 0x30FFFFFF : 0x18FFFFFF);
            g.fill(left + 4, y, left + 6, y + ROW - 2, 0xFF000000 | t.color());
            text(g, Names.of("form", t.id()), left + 10, y + 2, TEXT);
            String reason = active ? null : f.blocker(t);
            text(g, reason == null ? Component.translatable(active ? "screen.ascension_foundation.active" : "screen.ascension_foundation.ready").getString() : reason,
                left + 110, y + 2, reason == null ? GOOD : DIM);
            StringBuilder mult = new StringBuilder();
            for (int a = 0; a < t.multipliers().length; a++) if (t.multipliers()[a] != 1F)
                mult.append(f.powerType().attributes().get(a).shortName()).append(" x").append(String.format(Locale.ROOT, "%.2f", t.multipliers()[a])).append("  ");
            text(g, mult.toString().trim(), left + 10, y + 11, BLUE);
            double mastery = f.mastery(t.id());
            text(g, Component.translatable("screen.ascension_foundation.mastery", String.format(Locale.ROOT, "%.1f", mastery),
                String.format(Locale.ROOT, "%.1f", t.masteryMax())).getString(), left + 10, y + 20, DIM);
            bar(g, left + 120, y + 22, 100, (int) (mastery * 10), (int) (t.masteryMax() * 10), 0xFF000000 | t.color());
            Component tip = Component.translatable("screen.ascension_foundation.form_tip", String.format(Locale.ROOT, "%.1f", t.energyDrain()),
                String.format(Locale.ROOT, "%.1f", t.staminaDrain()), t.minRelease(),
                t.previous().isEmpty() ? Component.translatable("screen.ascension_foundation.base_form").getString() : Names.of("form", t.previous()).getString());
            hover(Component.empty().append(Names.desc("form", t.id())).append("\n").append(tip), left + 4, y, WIDTH - 72, ROW - 2, mx, my);
        }
    }
}
