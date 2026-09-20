package io.github.artificialturtill.myriadascension.client;

import com.mojang.blaze3d.platform.InputConstants;
import io.github.artificialturtill.myriadascension.MyriadAscension;
import net.minecraft.client.KeyMapping;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public final class ClientKeyMappings {
    public static final String CATEGORY = "key.categories." + MyriadAscension.MOD_ID + ".cultivation";

    public static final KeyMapping GATHER = new KeyMapping(
            "key." + MyriadAscension.MOD_ID + ".gather",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_G,
            CATEGORY);

    public static final KeyMapping SUPPRESS = new KeyMapping(
            "key." + MyriadAscension.MOD_ID + ".suppress",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_H,
            CATEGORY);

    public static final KeyMapping BURST = new KeyMapping(
            "key." + MyriadAscension.MOD_ID + ".burst",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_R,
            CATEGORY);

    public static final KeyMapping QUICK_MENU = new KeyMapping(
            "key." + MyriadAscension.MOD_ID + ".quick_menu",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_X,
            CATEGORY);

    public static final KeyMapping STATUS = new KeyMapping(
            "key." + MyriadAscension.MOD_ID + ".status",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_V,
            CATEGORY);

    private ClientKeyMappings() {
    }

    public static void register(RegisterKeyMappingsEvent event) {
        event.register(GATHER);
        event.register(SUPPRESS);
        event.register(BURST);
        event.register(QUICK_MENU);
        event.register(STATUS);
    }
}
