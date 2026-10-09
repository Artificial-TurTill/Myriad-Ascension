package io.github.artificialturtill.foundation.client;

import com.mojang.blaze3d.platform.InputConstants;
import io.github.artificialturtill.foundation.api.FoundationApi;
import io.github.artificialturtill.foundation.character.Fighter;
import io.github.artificialturtill.foundation.network.CharacterActionPayload;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
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
 * Client entry point: keys (V character menu, G charge / Shift+G lower release, R transform, Z descend), the HUD and
 * hiding the vanilla hearts for characters whose life is Body (JRMCoreGuiScreenE cancels the HEALTH overlay).
 */
@Mod(value = FoundationApi.MOD_ID, dist = Dist.CLIENT)
public final class FoundationClient {
    private static final String CATEGORY = "key.categories.ascension_foundation";
    public static final KeyMapping MENU = new KeyMapping("key.ascension_foundation.menu", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_V, CATEGORY);
    public static final KeyMapping CHARGE = new KeyMapping("key.ascension_foundation.charge", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_G, CATEGORY);
    public static final KeyMapping ASCEND = new KeyMapping("key.ascension_foundation.ascend", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_R, CATEGORY);
    public static final KeyMapping DESCEND = new KeyMapping("key.ascension_foundation.descend", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_Z, CATEGORY);
    private static boolean sentCharging, sentLowering;

    public FoundationClient(IEventBus bus, ModContainer container) {
        bus.addListener((RegisterKeyMappingsEvent e) -> { e.register(MENU); e.register(CHARGE); e.register(ASCEND); e.register(DESCEND); });
        bus.addListener((RegisterGuiLayersEvent e) ->
            e.registerAboveAll(ResourceLocation.fromNamespaceAndPath(FoundationApi.MOD_ID, "character_hud"), HudLayer::render));
        NeoForge.EVENT_BUS.addListener(FoundationClient::tick);
        NeoForge.EVENT_BUS.addListener(FoundationClient::hideHearts);
        NeoForge.EVENT_BUS.addListener((ClientPlayerNetworkEvent.LoggingOut e) -> { ClientCharacter.clear(); sentCharging = sentLowering = false; });
    }

    private static void tick(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.getConnection() == null) return;
        while (MENU.consumeClick()) if (mc.screen == null) mc.setScreen(FoundationScreen.open());
        while (ASCEND.consumeClick()) PacketDistributor.sendToServer(CharacterActionPayload.of(CharacterActionPayload.ASCEND));
        while (DESCEND.consumeClick()) PacketDistributor.sendToServer(CharacterActionPayload.of(CharacterActionPayload.DESCEND));
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
}
