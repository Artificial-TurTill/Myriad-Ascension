package io.github.artificialturtill.foundation.client;

import io.github.artificialturtill.foundation.api.FoundationApi;
import io.github.artificialturtill.foundation.character.Fighter;
import io.github.artificialturtill.foundation.network.CharacterSyncPayload;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

/**
 * The owner's character on the client, rebuilt from the server snapshot with the client's own copy of the content
 * (so the sheet uses exactly the server's maths). Null = no character (Natural, vanilla).
 */
public final class ClientCharacter {
    private static Fighter fighter;
    private static boolean dormant;
    private static String message = "";
    private static long messageAt;

    public static Fighter fighter() { return fighter; }
    public static boolean dormant() { return dormant; }
    /** The last refusal from the server while it is fresh (5 seconds), otherwise "". */
    public static String message() { return System.currentTimeMillis() - messageAt < 5000 ? message : ""; }

    public static void receive(CharacterSyncPayload payload) {
        dormant = payload.dormant();
        fighter = payload.fighter().isEmpty() ? null : Fighter.load(FoundationApi.content(), payload.fighter());
        if (Minecraft.getInstance().screen instanceof FoundationScreen screen) screen.characterChanged();
    }
    public static void result(String text) {
        message = text;
        messageAt = System.currentTimeMillis();
        if (!text.isEmpty() && !(Minecraft.getInstance().screen instanceof FoundationScreen) && Minecraft.getInstance().player != null)
            Minecraft.getInstance().player.displayClientMessage(Component.literal(text), true);
    }
    public static void clear() { fighter = null; dormant = false; message = ""; }
    private ClientCharacter() {}
}
