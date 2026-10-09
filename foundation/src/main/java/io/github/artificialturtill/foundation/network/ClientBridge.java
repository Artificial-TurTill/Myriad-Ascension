package io.github.artificialturtill.foundation.network;

/** Indirection so the common payload registration never loads client classes on a dedicated server. */
final class ClientBridge {
    static void sync(CharacterSyncPayload payload) { io.github.artificialturtill.foundation.client.ClientCharacter.receive(payload); }
    static void result(ResultPayload payload) { io.github.artificialturtill.foundation.client.ClientCharacter.result(payload.message()); }
    private ClientBridge() {}
}
