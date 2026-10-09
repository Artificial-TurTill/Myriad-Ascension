package io.github.artificialturtill.foundation.network;

import io.github.artificialturtill.foundation.character.CharacterService;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

/** Payload registration. The protocol comes from gradle.properties (foundation_protocol): client and server must match. */
public final class FoundationNetwork {
    public static final String PROTOCOL = readProtocol();

    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(PROTOCOL);
        registrar.playToServer(CharacterActionPayload.TYPE, CharacterActionPayload.CODEC,
            (payload, context) -> context.enqueueWork(() -> CharacterService.handleAction(context.player(), payload)));
        registrar.playToServer(CreateCharacterPayload.TYPE, CreateCharacterPayload.CODEC,
            (payload, context) -> context.enqueueWork(() -> CharacterService.handleCreate(context.player(), payload)));
        registrar.playToClient(CharacterSyncPayload.TYPE, CharacterSyncPayload.CODEC,
            (payload, context) -> context.enqueueWork(() -> { if (FMLEnvironment.dist.isClient()) ClientBridge.sync(payload); }));
        registrar.playToClient(ResultPayload.TYPE, ResultPayload.CODEC,
            (payload, context) -> context.enqueueWork(() -> { if (FMLEnvironment.dist.isClient()) ClientBridge.result(payload); }));
    }

    private static String readProtocol() {
        Properties properties = new Properties();
        try (InputStream in = FoundationNetwork.class.getResourceAsStream("/foundation.properties")) {
            if (in != null) properties.load(in);
        } catch (IOException ignored) { }
        String protocol = properties.getProperty("protocol", "1");
        return protocol.startsWith("${") ? "1" : protocol;
    }
    private FoundationNetwork() {}
}
