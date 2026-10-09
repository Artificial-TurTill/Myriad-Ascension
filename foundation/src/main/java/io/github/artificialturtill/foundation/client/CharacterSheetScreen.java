package io.github.artificialturtill.foundation.client;

import io.github.artificialturtill.foundation.character.Fighter;
import io.github.artificialturtill.foundation.character.StatMath;
import io.github.artificialturtill.foundation.content.PowerType;
import io.github.artificialturtill.foundation.content.StatDef;
import io.github.artificialturtill.foundation.network.CharacterActionPayload;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.network.PacketDistributor;

/**
 * The character sheet (JRMCoreGuiScreen GUI 10): level, TP, identity, alignment, attributes with "+" buttons and the
 * UC multiplier (x1 / x10 / x100 / x1000) on the left; power release, statistics and resources on the right.
 */
public final class CharacterSheetScreen extends FoundationScreen {
    /** JRMCoreGuiScreen.mcu: the UC multiplier, kept while the game runs. */
    private static int multiplier;

    public CharacterSheetScreen() { super(Component.translatable("screen.ascension_foundation.tab.character")); }
    @Override protected Tab tab() { return Tab.CHARACTER; }

    @Override protected void initContent() {
        Fighter f = ClientCharacter.fighter();
        if (f == null) return;
        int cost = f.upgradeCost(multiplier);
        int amount = StatMath.upgradeMultiplier(multiplier);
        for (int i = 0; i < f.powerType().attributeCount(); i++) {
            final int attribute = i;
            Button plus = addRenderableWidget(Button.builder(Component.literal("+"), b ->
                PacketDistributor.sendToServer(new CharacterActionPayload(CharacterActionPayload.UPGRADE, "", attribute, multiplier)))
                .bounds(left + 8, attrY(i) - 2, 12, 12).build());
            plus.active = cost > 0 && (long) f.attribute(i) + amount <= f.powerType().attributeLimit();
        }
        addRenderableWidget(Button.builder(Component.literal("x" + amount), b -> { multiplier = (multiplier + 1) % 4; rebuildWidgets(); })
            .bounds(left + 8, attrY(f.powerType().attributeCount()) + 2, 36, 12).build());
        if (f.race().appearance().hair())
            addRenderableWidget(Button.builder(Component.translatable("hair.ascension_foundation.barber"), b -> openBarber())
                .bounds(left + WIDTH - 78, top + HEIGHT - 18, 72, 14).build());
    }
    private int attrY(int i) { return top + 102 + i * 11; }

    /** The Barber Menu for the own character: Done sends the hairstyle to the server, then returns to the sheet. */
    public static void openBarber() {
        Fighter f = ClientCharacter.fighter();
        if (f == null) return;
        var look = f.appearance();
        io.github.artificialturtill.foundation.client.hair.HairState.begin(look.hair(), look.hairColor(), f.race().appearance().hairColors(),
            CharacterSheetScreen::new, r -> PacketDistributor.sendToServer(new io.github.artificialturtill.foundation.network.HairUpdatePayload(r.code(), r.color())));
        net.minecraft.client.Minecraft.getInstance().setScreen(new io.github.artificialturtill.foundation.client.hair.BarberScreen());
    }

    @Override protected void drawContent(GuiGraphics g, int mx, int my) {
        Fighter f = ClientCharacter.fighter();
        if (f == null) return;
        PowerType p = f.powerType();
        int x = left + 8, y = top + 8;
        int[] start = f.startingAttributes();
        pair(g, Component.translatable("screen.ascension_foundation.level"), Names.num(f.level()), x, y, TEXT);
        hover(Component.translatable("screen.ascension_foundation.level_next", StatMath.attributesUntilNextLevel(f.attributes(), start)), x, y, 120, 10, mx, my);
        pair(g, Component.translatable("screen.ascension_foundation.tp"), Names.num(f.tp()), x, y += 11, ACCENT);
        hover(Component.translatable("screen.ascension_foundation.tp_desc"), x, y, 120, 10, mx, my);
        pair(g, Component.translatable("screen.ascension_foundation.race"), Names.of("race", f.race().id()).getString(), x, y += 11, TEXT);
        pair(g, Component.translatable("screen.ascension_foundation.power_type"), Names.of("power_type", p.id()).getString(), x, y += 11, TEXT);
        hover(Names.desc("power_type", p.id()), x, y, 140, 10, mx, my);
        pair(g, Component.translatable("screen.ascension_foundation.class"), Names.of("class", f.characterClass().id()).getString(), x, y += 11, TEXT);
        hover(Names.desc("class", f.characterClass().id()), x, y, 140, 10, mx, my);
        pair(g, Component.translatable("screen.ascension_foundation.form"),
            f.inBaseForm() ? Component.translatable("screen.ascension_foundation.base_form").getString() : Names.of("form", f.form()).getString(), x, y += 11, TEXT);
        int group = StatMath.alignmentGroup(f.alignment());
        pair(g, Component.translatable("screen.ascension_foundation.alignment"),
            Component.translatable("screen.ascension_foundation.alignment." + group).getString() + " (" + f.alignment() + " %)", x, y += 11,
            group == 0 ? BLUE : group == 1 ? 0xFFC88CFF : BAD);

        header(g, Component.translatable("screen.ascension_foundation.attributes"), x, attrY(0) - 14);
        for (int i = 0; i < p.attributeCount(); i++) {
            int yy = attrY(i);
            pair(g, Component.literal(p.attributes().get(i).shortName()), Names.num(f.attribute(i)), x + 16, yy, BAD);
            if (f.formAttribute(i) != f.attribute(i)) text(g, "(" + Names.num(f.formAttribute(i)) + ")", x + 90, yy, ACCENT);
            hover(Component.empty().append(Names.attribute(p, i)).append("\n").append(Names.attributeDesc(p, i)), x + 16, yy, 100, 10, mx, my);
        }
        int cost = f.upgradeCost(multiplier);
        String uc = cost <= 0 ? Component.translatable("screen.ascension_foundation.limit_reached").getString() : Names.num(cost) + " TP";
        text(g, uc, x + 50, attrY(p.attributeCount()) + 4, f.tp() >= cost && cost > 0 ? GOOD : DIM);
        hover(Component.translatable("screen.ascension_foundation.uc_desc", StatMath.upgradeMultiplier(multiplier)), x + 50, attrY(p.attributeCount()) + 2, 90, 12, mx, my);

        // Right column: release, statistics, resources.
        int rx = left + 158;
        y = top + 8;
        if (p.release()) pair(g, Component.translatable("screen.ascension_foundation.release"), f.release() + " %", rx, y, ACCENT);
        else text(g, Component.translatable("screen.ascension_foundation.no_release"), rx, y, DIM);
        header(g, Component.translatable("screen.ascension_foundation.stats"), rx, y += 14);
        y += 3;
        for (int s = 0; s < p.stats().size(); s++) {
            StatDef def = p.stats().get(s);
            y += 11;
            if (def.role().equals(StatDef.SPEED)) {
                // JRMCore sheet "Running speed": spdFrm at 100 % release with Turbo.
                pair(g, Names.stat(p, s), f.turboSpeedPercent() + " %", rx, y, BLUE);
                hover(Component.translatable("screen.ascension_foundation.speed_desc", Names.turbo(p), p.attributes().get(def.attribute()).shortName()),
                    rx, y, WIDTH - 166, 10, mx, my);
                continue;
            }
            String value = def.released() ? Names.num(f.output(s)) + " / " + Names.num(f.fullStat(s)) : Names.num(f.output(s));
            pair(g, Names.stat(p, s), value, rx, y, BLUE);
            float inc = StatMath.statInc(def, f.statBonus(s), f.skillBonus(s));
            Component tip = Component.translatable("screen.ascension_foundation.stat_inc", p.attributes().get(def.attribute()).shortName(), inc);
            if (def.released()) tip = Component.empty().append(tip).append("\n").append(Component.translatable("screen.ascension_foundation.stat_released"));
            if (def.role().equals(StatDef.DEFENSE) && p.bodyLife())
                tip = Component.empty().append(tip).append("\n").append(Component.translatable("screen.ascension_foundation.stat_passive", p.passiveDefense(),
                    Names.num(StatMath.passive(f.output(s), p.passiveDefense()))));
            hover(tip, rx, y, WIDTH - 166, 10, mx, my);
        }
        header(g, Component.translatable("screen.ascension_foundation.resources"), rx, y += 16);
        y += 3;
        Fighter.Caps caps = f.caps();
        if (p.bodyLife()) { pair(g, Component.translatable("screen.ascension_foundation.body"), Names.num(f.body()) + " / " + Names.num(caps.body()), rx, y += 11, BAD); }
        else pair(g, Component.translatable("screen.ascension_foundation.health"), Component.translatable("screen.ascension_foundation.hearts").getString(), rx, y += 11, BAD);
        if (p.hasEnergy()) pair(g, Names.energy(p), Names.num(f.energy()) + " / " + Names.num(caps.energy()), rx, y += 11, BLUE);
        if (caps.stamina() > 0) pair(g, Component.translatable("screen.ascension_foundation.stamina"), Names.num(f.stamina()) + " / " + Names.num(caps.stamina()), rx, y += 11, ACCENT);
        if (p.capacityAttribute() >= 0) {
            pair(g, Component.translatable("screen.ascension_foundation.capacity", p.attributes().get(p.capacityAttribute()).shortName()),
                Names.num(f.reservedCapacity()) + " / " + Names.num(f.capacity()), rx, y += 11, DIM);
            hover(Component.translatable("screen.ascension_foundation.capacity_desc"), rx, y, WIDTH - 166, 10, mx, my);
        }
    }
}
