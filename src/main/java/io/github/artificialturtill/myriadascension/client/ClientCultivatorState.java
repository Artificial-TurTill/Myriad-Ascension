package io.github.artificialturtill.myriadascension.client;

import io.github.artificialturtill.myriadascension.network.CultivatorSyncPayload;
import org.jetbrains.annotations.Nullable;

public final class ClientCultivatorState {
    private static CultivatorSyncPayload snapshot;

    private ClientCultivatorState() {
    }

    public static void update(CultivatorSyncPayload payload) {
        snapshot = payload;
    }

    public static @Nullable CultivatorSyncPayload snapshot() {
        return snapshot;
    }

    public static void clear() {
        snapshot = null;
    }
}
