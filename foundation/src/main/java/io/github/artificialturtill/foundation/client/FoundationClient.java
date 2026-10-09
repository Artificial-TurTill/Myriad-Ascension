package io.github.artificialturtill.foundation.client;

import com.mojang.blaze3d.platform.InputConstants;
import io.github.artificialturtill.foundation.api.FoundationApi;
import io.github.artificialturtill.foundation.character.Fighter;
import io.github.artificialturtill.foundation.character.FoundationAttachments;
import io.github.artificialturtill.foundation.content.Transformation;
import io.github.artificialturtill.foundation.network.CharacterActionPayload;
import java.util.List;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.PacketDistributor;
import org.lwjgl.glfw.GLFW;

/**
 * Client entry point: keys (V character menu, C charge / Shift+C lower release, G transform, H descend, R Turbo
 * (Qi Surge for Qi), X quick menu), the HUD, the Turbo run, the X menu's built-in entries and hiding the vanilla
 * hearts for characters whose life is Body (JRMCoreGuiScreenE cancels the HEALTH overlay).
 */
@Mod(value = FoundationApi.MOD_ID, dist = Dist.CLIENT)
public final class FoundationClient {
    private static final String CATEGORY = "key.categories.ascension_foundation";
    public static final KeyMapping MENU = key("menu", GLFW.GLFW_KEY_V);
    public static final KeyMapping CHARGE = key("charge", GLFW.GLFW_KEY_C);
    public static final KeyMapping ASCEND = key("ascend", GLFW.GLFW_KEY_G);
    public static final KeyMapping DESCEND = key("descend", GLFW.GLFW_KEY_H);
    public static final KeyMapping TURBO = key("turbo", GLFW.GLFW_KEY_R);
    public static final KeyMapping QUICK_MENU = key("quick_menu", GLFW.GLFW_KEY_X);
    /** Resource HUD shown (X menu switch, kept while the game runs). */
    public static boolean hudVisible = true;
    private static boolean sentCharging, sentLowering;

    private static KeyMapping key(String name, int code) {
        return new KeyMapping("key.ascension_foundation." + name, InputConstants.Type.KEYSYM, code, CATEGORY);
    }

    public FoundationClient(IEventBus bus, ModContainer container) {
        bus.addListener((RegisterKeyMappingsEvent e) -> {
            for (KeyMapping k : List.of(MENU, CHARGE, ASCEND, DESCEND, TURBO, QUICK_MENU)) e.register(k);
        });
        bus.addListener((RegisterGuiLayersEvent e) ->
            e.registerAboveAll(ResourceLocation.fromNamespaceAndPath(FoundationApi.MOD_ID, "character_hud"), HudLayer::render));
        NeoForge.EVENT_BUS.addListener(FoundationClient::tick);
        NeoForge.EVENT_BUS.addListener(FoundationClient::hideHearts);
        NeoForge.EVENT_BUS.addListener(TurboClient::preTick);
        NeoForge.EVENT_BUS.addListener(TurboClient::postTick);
        NeoForge.EVENT_BUS.addListener(TurboClient::fov);
        NeoForge.EVENT_BUS.addListener((ClientPlayerNetworkEvent.LoggingOut e) -> { ClientCharacter.clear(); sentCharging = sentLowering = false; });
        registerQuickMenu();
    }

    private static void send(int action) { PacketDistributor.sendToServer(CharacterActionPayload.of(action)); }

    private static void tick(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.getConnection() == null) return;
        while (MENU.consumeClick()) if (mc.screen == null) mc.setScreen(FoundationScreen.open());
        while (QUICK_MENU.consumeClick()) if (mc.screen == null) mc.setScreen(new QuickMenuScreen());
        while (ASCEND.consumeClick()) send(CharacterActionPayload.ASCEND);
        while (DESCEND.consumeClick()) send(CharacterActionPayload.DESCEND);
        while (TURBO.consumeClick()) send(CharacterActionPayload.TURBO);
        Fighter f = ClientCharacter.fighter();
        boolean charging = mc.screen == null && CHARGE.isDown() && f != null && f.powerType().release();
        boolean lowering = charging && mc.player.isShiftKeyDown();
        if (charging != sentCharging || lowering != sentLowering) {
            sentCharging = charging; sentLowering = lowering;
            PacketDistributor.sendToServer(new CharacterActionPayload(CharacterActionPayload.CHARGE, "", charging ? 1 : 0, lowering ? 1 : 0));
        }
    }

    private static void hideHearts(RenderGuiLayerEvent.Pre event) {
        Fighter f = ClientCharacter.fighter();
        if (f != null && f.powerType().bodyLife() && event.getName().equals(VanillaGuiLayers.PLAYER_HEALTH)) event.setCanceled(true);
    }

    private static boolean turboOn() {
        var player = Minecraft.getInstance().player;
        return player != null && player.getData(FoundationAttachments.LOOK).turbo();
    }
    private static Component onOff(boolean on) { return Component.translatable(on ? "options.on" : "options.off"); }

    /** The Foundation's X menu cells; add-ons add theirs with {@link QuickMenu#register}. */
    private static void registerQuickMenu() {
        QuickMenu.register(new QuickMenu.Entry("ascension_foundation:turbo",
            () -> ClientCharacter.fighter() == null ? Component.literal("Turbo") : Names.turbo(ClientCharacter.fighter().powerType()),
            () -> onOff(turboOn()), () -> turboOn() ? QuickMenu.ON : QuickMenu.OFF,
            () -> ClientCharacter.fighter() != null && ClientCharacter.fighter().powerType().turbo(),
            direction -> send(CharacterActionPayload.TURBO)));
        QuickMenu.register(new QuickMenu.Entry("ascension_foundation:form_target",
            () -> Component.translatable("screen.ascension_foundation.quick_menu.form_target"),
            () -> {
                Fighter f = ClientCharacter.fighter();
                if (f == null || f.availableForms().isEmpty()) return Component.translatable("screen.ascension_foundation.quick_menu.none_yet");
                return f.selectedForm().isEmpty() ? Component.translatable("screen.ascension_foundation.quick_menu.first_available")
                    : Names.of("form", f.selectedForm());
            },
            () -> QuickMenu.NEUTRAL,
            () -> ClientCharacter.fighter() != null && !ClientCharacter.fighter().availableForms().isEmpty(),
            direction -> {
                Fighter f = ClientCharacter.fighter();
                List<Transformation> forms = f.availableForms();
                int current = -1;
                for (int i = 0; i < forms.size(); i++) if (forms.get(i).id().equals(f.selectedForm())) current = i;
                // -1 = "first available", then every form, cycling both ways.
                int next = Math.floorMod(current + 1 + direction, forms.size() + 1) - 1;
                PacketDistributor.sendToServer(new CharacterActionPayload(CharacterActionPayload.SELECT_FORM, next < 0 ? "" : forms.get(next).id(), 0, 0));
            }));
        QuickMenu.register(new QuickMenu.Entry("ascension_foundation:character",
            () -> Component.translatable("screen.ascension_foundation.tab.character"),
            () -> Component.translatable(ClientCharacter.fighter() == null ? "screen.ascension_foundation.quick_menu.create" : "screen.ascension_foundation.quick_menu.open"),
            () -> QuickMenu.NEUTRAL, () -> true,
            direction -> Minecraft.getInstance().setScreen(FoundationScreen.open())));
        QuickMenu.register(new QuickMenu.Entry("ascension_foundation:skills",
            () -> Component.translatable("screen.ascension_foundation.tab.skills"),
            () -> Component.translatable("screen.ascension_foundation.quick_menu.open"),
            () -> QuickMenu.NEUTRAL, () -> ClientCharacter.fighter() != null,
            direction -> Minecraft.getInstance().setScreen(new SkillsScreen())));
        QuickMenu.register(new QuickMenu.Entry("ascension_foundation:transformations",
            () -> Component.translatable("screen.ascension_foundation.tab.forms"),
            () -> Component.translatable("screen.ascension_foundation.quick_menu.open"),
            () -> QuickMenu.NEUTRAL, () -> ClientCharacter.fighter() != null,
            direction -> Minecraft.getInstance().setScreen(new FormsScreen())));
        QuickMenu.register(new QuickMenu.Entry("ascension_foundation:hud",
            () -> Component.translatable("screen.ascension_foundation.quick_menu.hud"),
            () -> onOff(hudVisible), () -> hudVisible ? QuickMenu.ON : QuickMenu.OFF, () -> true,
            direction -> hudVisible = !hudVisible));
    }
}
