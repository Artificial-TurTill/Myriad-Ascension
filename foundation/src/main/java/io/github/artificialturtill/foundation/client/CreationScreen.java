package io.github.artificialturtill.foundation.client;

import io.github.artificialturtill.foundation.api.FoundationApi;
import io.github.artificialturtill.foundation.character.Appearance;
import io.github.artificialturtill.foundation.character.Fighter;
import io.github.artificialturtill.foundation.character.StatMath;
import io.github.artificialturtill.foundation.content.AppearanceOptions;
import io.github.artificialturtill.foundation.content.CharacterClass;
import io.github.artificialturtill.foundation.content.ContentSet;
import io.github.artificialturtill.foundation.content.PowerType;
import io.github.artificialturtill.foundation.content.Race;
import io.github.artificialturtill.foundation.network.CreateCharacterPayload;
import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.network.PacketDistributor;

/**
 * The character creation wizard (JRMCoreGuiScreen GUI 0 / 2 / 3): Appearance (race, gender, skin, hair, eyes, body
 * type, height), Power Type (power type, class, the starting attributes and statistics of that combination, computed
 * by the server's own maths) and Summary with Accept. Nothing is sent before Accept; the server validates everything.
 * The selections are kept while the game runs (like JRMCore's static *Slcted fields).
 */
public final class CreationScreen extends FoundationScreen {
    private static final int APPEARANCE = 0, POWER = 1, SUMMARY = 2;
    private static int page, raceIndex, powerIndex, classIndex;
    private static int gender, skin, hairStyle, hairColor, eye, bodyType, height = 100;
    private static String lastRace = "";

    private CreationScreen() { super(Component.translatable("screen.ascension_foundation.creation")); }
    public static CreationScreen open() { return new CreationScreen(); }
    @Override protected Tab tab() { return null; }

    private static ContentSet content() { return FoundationApi.content(); }
    private static List<Race> races() { return content().raceList(); }
    private static Race race() { List<Race> r = races(); return r.isEmpty() ? null : r.get(Math.floorMod(raceIndex, r.size())); }
    private static List<PowerType> powers() { Race race = race(); return content().powerTypeList().stream().filter(p -> race != null && race.allows(p.id())).toList(); }
    private static PowerType power() { List<PowerType> p = powers(); return p.isEmpty() ? null : p.get(Math.floorMod(powerIndex, p.size())); }
    private static List<CharacterClass> classes() { PowerType p = power(); return p == null ? List.of() : content().classesOf(p.id()); }
    private static CharacterClass characterClass() { List<CharacterClass> c = classes(); return c.isEmpty() ? null : c.get(Math.floorMod(classIndex, c.size())); }

    /** Clamp the look to what the current race offers (JRMCore setchangerace). */
    private static void fitRace() {
        Race race = race();
        if (race == null) return;
        AppearanceOptions o = race.appearance();
        if (!race.id().equals(lastRace)) {
            lastRace = race.id();
            Appearance d = Appearance.defaults(race);
            gender = d.gender(); skin = 0; hairStyle = d.hairStyle(); hairColor = 0; eye = 0; bodyType = 0; height = d.height();
            powerIndex = 0; classIndex = 0;
        }
        if (!race.genders().contains(gender == Appearance.MALE ? "male" : "female")) gender = 1 - gender;
        skin = Math.floorMod(skin, o.skinColors().size());
        eye = Math.floorMod(eye, o.eyeColors().size());
        bodyType = Math.floorMod(bodyType, o.bodyTypes());
        height = Math.clamp(height, o.minHeight(), o.maxHeight());
        if (o.canHaveHair()) { hairStyle = Math.clamp(hairStyle, -1, o.hairStyles() - 1); hairColor = Math.floorMod(hairColor, o.hairColors().size()); }
        else hairStyle = -1;
    }
    private static Appearance look() {
        AppearanceOptions o = race().appearance();
        return new Appearance(gender, o.skinColors().get(skin), hairStyle, o.canHaveHair() ? o.hairColors().get(hairColor) : 0,
            o.eyeColors().get(eye), bodyType, height);
    }
    /** A throw-away character of the current choice, so the preview uses the real rules. */
    private static Fighter preview() {
        try { return Fighter.create(content(), race().id(), power().id(), characterClass().id(), look()); }
        catch (RuntimeException e) { return null; }
    }

    private int rowY(int row) { return top + 24 + row * 15; }
    private void arrows(int row, Runnable back, Runnable next) {
        int y = rowY(row) - 3;
        addRenderableWidget(Button.builder(Component.literal("<"), b -> { back.run(); changed(); }).bounds(left + 178, y, 14, 14).build());
        addRenderableWidget(Button.builder(Component.literal(">"), b -> { next.run(); changed(); }).bounds(left + WIDTH - 22, y, 14, 14).build());
    }
    private void changed() { fitRace(); rebuildWidgets(); }

    @Override protected void initContent() {
        fitRace();
        if (race() == null) return;
        int bottom = top + HEIGHT - 24;
        switch (page) {
            case APPEARANCE -> {
                AppearanceOptions o = race().appearance();
                arrows(0, () -> raceIndex--, () -> raceIndex++);
                if (race().genders().size() > 1) arrows(1, () -> gender = 1 - gender, () -> gender = 1 - gender);
                arrows(2, () -> skin--, () -> skin++);
                if (o.canHaveHair()) {
                    arrows(3, () -> hairStyle = hairStyle <= -1 ? o.hairStyles() - 1 : hairStyle - 1, () -> hairStyle = hairStyle >= o.hairStyles() - 1 ? -1 : hairStyle + 1);
                    arrows(4, () -> hairColor--, () -> hairColor++);
                }
                arrows(5, () -> eye--, () -> eye++);
                if (o.bodyTypes() > 1) arrows(6, () -> bodyType--, () -> bodyType++);
                arrows(7, () -> height = Math.max(o.minHeight(), height - 1), () -> height = Math.min(o.maxHeight(), height + 1));
                addRenderableWidget(Button.builder(Component.literal("X"), b -> onClose()).bounds(left + 6, bottom, 20, 18).build());
                addRenderableWidget(Button.builder(Component.translatable("screen.ascension_foundation.next"), b -> { page = POWER; rebuildWidgets(); })
                    .bounds(left + WIDTH - 66, bottom, 60, 18).build());
            }
            case POWER -> {
                arrows(0, () -> { powerIndex--; classIndex = 0; }, () -> { powerIndex++; classIndex = 0; });
                arrows(1, () -> classIndex--, () -> classIndex++);
                addRenderableWidget(Button.builder(Component.translatable("screen.ascension_foundation.back"), b -> { page = APPEARANCE; rebuildWidgets(); })
                    .bounds(left + 6, bottom, 60, 18).build());
                addRenderableWidget(Button.builder(Component.translatable("screen.ascension_foundation.next"), b -> { page = SUMMARY; rebuildWidgets(); })
                    .bounds(left + WIDTH - 66, bottom, 60, 18).build()).active = characterClass() != null;
            }
            default -> {
                addRenderableWidget(Button.builder(Component.translatable("screen.ascension_foundation.back"), b -> { page = POWER; rebuildWidgets(); })
                    .bounds(left + 6, bottom, 60, 18).build());
                addRenderableWidget(Button.builder(Component.translatable("screen.ascension_foundation.accept"), b -> accept())
                    .bounds(left + WIDTH - 66, bottom, 60, 18).build()).active = preview() != null;
            }
        }
    }

    private void accept() {
        PacketDistributor.sendToServer(new CreateCharacterPayload(race().id(), power().id(), characterClass().id(), look()));
        page = APPEARANCE;
        onClose();
    }

    @Override protected void drawContent(GuiGraphics g, int mouseX, int mouseY) {
        if (race() == null) { text(g, Component.translatable("screen.ascension_foundation.no_content"), left + 10, top + 10, BAD); return; }
        Component[] titles = {Component.translatable("screen.ascension_foundation.appearance"), Component.translatable("screen.ascension_foundation.power_type"),
            Component.translatable("screen.ascension_foundation.summary")};
        header(g, titles[page], left + 8, top + 6);
        text(g, (page + 1) + "/3", left + WIDTH - 24, top + 6, DIM);
        // The player model on the left (the body renderer for custom looks is an open point; see the vault).
        g.enableScissor(left + 4, top + 18, left + 104, top + HEIGHT - 28);
        if (minecraft.player != null)
            InventoryScreen.renderEntityInInventoryFollowsMouse(g, left + 4, top + 18, left + 104, top + HEIGHT - 28, 55, 0.0625F, mouseX, mouseY, minecraft.player);
        g.disableScissor();
        switch (page) {
            case APPEARANCE -> drawAppearance(g, mouseX, mouseY);
            case POWER -> drawPower(g, mouseX, mouseY);
            default -> drawSummary(g);
        }
    }
    private void row(GuiGraphics g, int row, String label, Component value) {
        text(g, Component.translatable("screen.ascension_foundation." + label), left + 110, rowY(row), DIM);
        int center = (left + 192 + left + WIDTH - 22) / 2;
        g.drawCenteredString(font, value, center, rowY(row), TEXT);
    }
    private void swatchRow(GuiGraphics g, int row, String label, int color) {
        text(g, Component.translatable("screen.ascension_foundation." + label), left + 110, rowY(row), DIM);
        swatch(g, left + 200, rowY(row), WIDTH - 22 - 200 - 6, color);
    }
    private void drawAppearance(GuiGraphics g, int mouseX, int mouseY) {
        Appearance look = look();
        AppearanceOptions o = race().appearance();
        row(g, 0, "race", Names.of("race", race().id()));
        row(g, 1, "gender", Component.translatable("screen.ascension_foundation.gender." + (gender == Appearance.MALE ? "male" : "female")));
        swatchRow(g, 2, "skin", look.skinColor());
        if (o.canHaveHair()) {
            row(g, 3, "hair", hairStyle < 0 ? Component.translatable("screen.ascension_foundation.bald") : Component.literal("#" + (hairStyle + 1)));
            swatchRow(g, 4, "hair_color", look.hairColor());
        } else row(g, 3, "hair", Component.translatable("screen.ascension_foundation.no_hair"));
        swatchRow(g, 5, "eyes", look.eyeColor());
        row(g, 6, "body_type", Component.literal("#" + (bodyType + 1)));
        row(g, 7, "height", Component.literal(height + " %"));
        g.drawWordWrap(font, Names.desc("race", race().id()), left + 110, rowY(8) + 4, WIDTH - 118, DIM);
    }
    private void drawPower(GuiGraphics g, int mouseX, int mouseY) {
        PowerType p = power();
        CharacterClass c = characterClass();
        if (p == null) { text(g, Component.translatable("screen.ascension_foundation.no_power"), left + 110, rowY(0), BAD); return; }
        row(g, 0, "power_type", Names.of("power_type", p.id()));
        hover(Names.desc("power_type", p.id()), left + 110, rowY(0) - 2, WIDTH - 118, 12, mouseX, mouseY);
        row(g, 1, "class", c == null ? Component.literal("-") : Names.of("class", c.id()));
        if (c != null) hover(Names.desc("class", c.id()), left + 110, rowY(1) - 2, WIDTH - 118, 12, mouseX, mouseY);
        Fighter f = preview();
        if (f == null) return;
        int y = rowY(2) + 4;
        header(g, Component.translatable("screen.ascension_foundation.start_attributes"), left + 110, y);
        header(g, Component.translatable("screen.ascension_foundation.stats"), left + 190, y);
        y += 13;
        for (int i = 0; i < p.attributeCount(); i++) {
            int yy = y + i * 10;
            pair(g, Component.literal(p.attributes().get(i).shortName()), Names.num(f.attribute(i)), left + 110, yy, BAD);
            hover(Component.empty().append(Names.attribute(p, i)).append("\n").append(Names.attributeDesc(p, i)), left + 110, yy, 75, 10, mouseX, mouseY);
        }
        for (int s = 0; s < p.stats().size(); s++) {
            int yy = y + s * 10;
            pair(g, Names.stat(p, s), Names.num(f.fullStat(s)), left + 190, yy, BLUE);
            float inc = StatMath.statInc(p.stats().get(s), f.statBonus(s), f.skillBonus(s));
            hover(Component.translatable("screen.ascension_foundation.stat_inc", p.attributes().get(p.stats().get(s).attribute()).shortName(), inc),
                left + 190, yy, WIDTH - 196, 10, mouseX, mouseY);
        }
        int bottom = y + Math.max(p.attributeCount(), p.stats().size()) * 10 + 4;
        String life = p.bodyLife() ? "screen.ascension_foundation.life_body" : "screen.ascension_foundation.life_vanilla";
        g.drawWordWrap(font, Component.translatable(life), left + 110, bottom, WIDTH - 118, DIM);
    }
    private void drawSummary(GuiGraphics g) {
        Appearance look = look();
        int x = left + 110, y = top + 24;
        pair(g, Component.translatable("screen.ascension_foundation.race"), Names.of("race", race().id()).getString(), x, y, TEXT);
        pair(g, Component.translatable("screen.ascension_foundation.gender"),
            Component.translatable("screen.ascension_foundation.gender." + (gender == Appearance.MALE ? "male" : "female")).getString(), x, y += 12, TEXT);
        text(g, Component.translatable("screen.ascension_foundation.skin"), x, y += 12, DIM); swatch(g, x + 80, y, 40, look.skinColor());
        text(g, Component.translatable("screen.ascension_foundation.hair"), x, y += 12, DIM);
        if (look.hairStyle() >= 0) { text(g, "#" + (look.hairStyle() + 1), x + 80, y, TEXT); swatch(g, x + 110, y, 30, look.hairColor()); }
        else text(g, Component.translatable("screen.ascension_foundation.bald"), x + 80, y, TEXT);
        text(g, Component.translatable("screen.ascension_foundation.eyes"), x, y += 12, DIM); swatch(g, x + 80, y, 40, look.eyeColor());
        pair(g, Component.translatable("screen.ascension_foundation.body_type"), "#" + (look.bodyType() + 1) + "  " + look.height() + " %", x, y += 12, TEXT);
        pair(g, Component.translatable("screen.ascension_foundation.power_type"), Names.of("power_type", power().id()).getString(), x, y += 16, TEXT);
        pair(g, Component.translatable("screen.ascension_foundation.class"), characterClass() == null ? "-" : Names.of("class", characterClass().id()).getString(), x, y += 12, TEXT);
        g.drawWordWrap(font, Component.translatable("screen.ascension_foundation.accept_note"), x, y + 18, WIDTH - 118, DIM);
    }
}
