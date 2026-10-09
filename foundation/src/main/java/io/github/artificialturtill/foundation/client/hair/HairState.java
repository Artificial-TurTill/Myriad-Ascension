package io.github.artificialturtill.foundation.client.hair;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.math.Axis;
import io.github.artificialturtill.foundation.character.HairCode;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.LivingEntity;
import org.joml.Quaternionf;

/**
 * Shared state of the hairstyle manager (Dragonblock C+ HairEditor / JRMCoreGuiScreen dnsH, ColorSlcted): the code and
 * colour being edited, the original ones (Revert), the preset list (the six JRMCore presets plus the player's saved
 * ones in config/ascension_foundation-hairpresets.txt), where Done returns to and what it does.
 */
public final class HairState {
    /** A screen that shows a hair preview on the local player. */
    public interface Preview { String previewHair(); int previewColor(); }

    static String code = HairCode.DEFAULT, original = HairCode.DEFAULT;
    static int color, originalColor;
    /** Quick colour presets of the race (the colour picker allows any colour). */
    static List<Integer> colorPresets = List.of();
    static int preset;
    static final List<String> presets = new ArrayList<>();
    /** Where the editor's and the picker's Done return to, and what the Barber Menu's Done does with the result. */
    static Supplier<Screen> home = () -> null;
    static Consumer<HairState.Result> done = r -> { };
    private static final Path PRESET_FILE = Path.of("config", "ascension_foundation-hairpresets.txt");

    public record Result(String code, int color) {}

    /** Starts an edit session (Barber Menu from the sheet, or the creation wizard). */
    public static void begin(String startCode, int startColor, List<Integer> colors, Supplier<Screen> returnTo, Consumer<Result> onDone) {
        code = original = HairCode.validOrBald(startCode) ? startCode : HairCode.DEFAULT;
        color = originalColor = startColor & 0xFFFFFF;
        colorPresets = List.copyOf(colors);
        home = returnTo;
        done = onDone;
        loadPresets();
        preset = Math.max(0, presets.indexOf(code));
    }
    public static String code() { return code; }
    public static int color() { return color; }

    /**
     * The preset list is loaded before every use: Dragonblock C+ BUG-049 (crash on Reset in the hair editor) came from
     * an empty preset list when the editor was opened without the Barber Menu.
     */
    static void loadPresets() {
        presets.clear();
        presets.addAll(List.of(HairCode.PRESETS));
        try {
            if (Files.exists(PRESET_FILE))
                for (String saved : Files.readString(PRESET_FILE, StandardCharsets.UTF_8).split(","))
                    if (HairCode.valid(saved.trim()) && !presets.contains(saved.trim())) presets.add(saved.trim());
        } catch (Exception ignored) { }
        preset = Math.clamp(preset, 0, presets.size() - 1);
    }
    static void savePresets() {
        try {
            Files.createDirectories(PRESET_FILE.getParent());
            Files.writeString(PRESET_FILE, String.join(",", presets.subList(HairCode.PRESETS.length, presets.size())), StandardCharsets.UTF_8);
        } catch (Exception ignored) { }
    }
    static String presetCode() {
        if (presets.isEmpty()) loadPresets();
        return presets.get(Math.clamp(preset, 0, presets.size() - 1));
    }

    /** Draws an entity with the head at (x, y), turned by yaw / pitch degrees (JRMCoreGuiScreen.hairCheck). */
    public static void drawHead(GuiGraphics g, int x, int y, float scale, float yaw, float pitch, LivingEntity entity) {
        float body = entity.yBodyRot, yRot = entity.getYRot(), xRot = entity.getXRot(), headO = entity.yHeadRotO, head = entity.yHeadRot;
        entity.yBodyRot = 180F; entity.setYRot(180F); entity.setXRot(0F); entity.yHeadRot = 180F; entity.yHeadRotO = 180F;
        g.pose().pushPose();
        g.pose().translate(x, y, 150);
        g.pose().scale(scale, scale, -scale);
        g.pose().mulPose(new Quaternionf(Axis.ZP.rotationDegrees(180F)).mul(Axis.XP.rotationDegrees(pitch)).mul(Axis.YP.rotationDegrees(yaw)));
        g.pose().translate(0F, -entity.getEyeHeight(), 0F);
        Lighting.setupForEntityInInventory();
        EntityRenderDispatcher dispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        dispatcher.setRenderShadow(false);
        RenderSystem.runAsFancy(() -> dispatcher.render(entity, 0, 0, 0, 0F, 1F, g.pose(), g.bufferSource(), 15728880));
        g.flush();
        dispatcher.setRenderShadow(true);
        g.pose().popPose();
        Lighting.setupFor3DItems();
        entity.yBodyRot = body; entity.setYRot(yRot); entity.setXRot(xRot); entity.yHeadRotO = headO; entity.yHeadRot = head;
    }
    private HairState() {}
}
