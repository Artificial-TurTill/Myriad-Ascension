package io.github.artificialturtill.foundation.client;

import io.github.artificialturtill.foundation.api.FoundationApi;
import io.github.artificialturtill.foundation.character.Fighter;
import io.github.artificialturtill.foundation.content.SkillDef;
import io.github.artificialturtill.foundation.network.CharacterActionPayload;
import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.network.PacketDistributor;

/** V > Skills (JRMCore GUI Skills): every skill of the power type, its level, next TP / capacity cost, learn and forget. */
public final class SkillsScreen extends FoundationScreen {
    private static final int ROW = 24, PER_PAGE = 6;
    private static int scroll;

    public SkillsScreen() { super(Component.translatable("screen.ascension_foundation.tab.skills")); }
    @Override protected Tab tab() { return Tab.SKILLS; }
    private static List<SkillDef> skills(Fighter f) { return FoundationApi.content().skillsOf(f.powerType().id()); }
    private int rowY(int i) { return top + 24 + i * ROW; }

    @Override protected void initContent() {
        Fighter f = ClientCharacter.fighter();
        if (f == null) return;
        List<SkillDef> all = skills(f);
        scroll = Math.clamp(scroll, 0, Math.max(0, all.size() - PER_PAGE));
        for (int i = 0; i < Math.min(PER_PAGE, all.size() - scroll); i++) {
            SkillDef s = all.get(scroll + i);
            int level = f.skill(s.id());
            boolean teacher = s.learn().equals(SkillDef.LEARN_TEACHER) && level == 0;
            Button learn = addRenderableWidget(Button.builder(Component.translatable(level == 0 ? "screen.ascension_foundation.learn" : "screen.ascension_foundation.upgrade"),
                b -> PacketDistributor.sendToServer(new CharacterActionPayload(CharacterActionPayload.LEARN_SKILL, s.id(), 0, 0)))
                .bounds(left + WIDTH - 92, rowY(i) + 2, 56, 16).build());
            learn.active = level < s.maxLevel() && !teacher;
            if (level > 0) addRenderableWidget(Button.builder(Component.literal("x"),
                b -> PacketDistributor.sendToServer(new CharacterActionPayload(CharacterActionPayload.FORGET_SKILL, s.id(), 0, 0)))
                .bounds(left + WIDTH - 32, rowY(i) + 2, 16, 16).tooltip(net.minecraft.client.gui.components.Tooltip.create(
                    Component.translatable("screen.ascension_foundation.forget"))).build());
        }
        if (all.size() > PER_PAGE) {
            addRenderableWidget(Button.builder(Component.literal("^"), b -> { scroll--; rebuildWidgets(); }).bounds(left + WIDTH - 14, top + 24, 12, 12).build()).active = scroll > 0;
            addRenderableWidget(Button.builder(Component.literal("v"), b -> { scroll++; rebuildWidgets(); }).bounds(left + WIDTH - 14, top + HEIGHT - 16, 12, 12).build())
                .active = scroll + PER_PAGE < all.size();
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
        String cap = f.powerType().capacityAttribute() >= 0 ? f.powerType().attributes().get(f.powerType().capacityAttribute()).shortName() : "";
        text(g, "TP " + Names.num(f.tp()) + (cap.isEmpty() ? "" : "   " + cap + " " + Names.num(f.reservedCapacity()) + "/" + Names.num(f.capacity())),
            left + 120, top + 6, ACCENT);
        List<SkillDef> all = skills(f);
        if (all.isEmpty()) { g.drawWordWrap(font, Component.translatable("screen.ascension_foundation.no_skills"), left + 8, top + 26, WIDTH - 16, DIM); return; }
        for (int i = 0; i < Math.min(PER_PAGE, all.size() - scroll); i++) {
            SkillDef s = all.get(scroll + i);
            int y = rowY(i), level = f.skill(s.id());
            g.fill(left + 4, y, left + WIDTH - 18, y + ROW - 2, i % 2 == 0 ? 0x30FFFFFF : 0x18FFFFFF);
            text(g, Names.of("skill", s.id()), left + 8, y + 2, TEXT);
            text(g, level + "/" + s.maxLevel(), left + 150, y + 2, level > 0 ? GOOD : DIM);
            String next = level >= s.maxLevel() ? Component.translatable("screen.ascension_foundation.max_level").getString()
                : Component.translatable("screen.ascension_foundation.next_cost", Names.num(s.tpCost(level)), s.capacityCost(level), cap).getString();
            text(g, next, left + 8, y + 12, DIM);
            Component tip = Names.desc("skill", s.id());
            if (s.learn().equals(SkillDef.LEARN_TEACHER)) tip = Component.empty().append(tip).append("\n").append(Component.translatable("screen.ascension_foundation.teacher_only"));
            if (s.effect().isEmpty()) tip = Component.empty().append(tip).append("\n").append(Component.translatable("screen.ascension_foundation.concept_only"));
            hover(tip, left + 4, y, WIDTH - 100, ROW - 2, mx, my);
        }
    }
}
