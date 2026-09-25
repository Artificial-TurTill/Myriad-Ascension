package io.github.artificialturtill.myriadascension.network;

import java.util.function.Consumer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public final class ClientPayloadBridge {
    private static Runnable openGenesisHandler = () -> {};
    private static Consumer<GenesisResultPayload> genesisResultHandler = payload -> {};
    private static Consumer<CultivatorSyncPayload> cultivatorSyncHandler = payload -> {};
    private static Runnable openMartialGuideHandler = () -> {};
    private static Consumer<QuickMenuSnapshotPayload> quickMenuSnapshotHandler = payload -> {};

    private ClientPayloadBridge() {
    }

    public static void installGenesisHandlers(
            Runnable openHandler,
            Consumer<GenesisResultPayload> resultHandler) {
        openGenesisHandler = openHandler == null ? () -> {} : openHandler;
        genesisResultHandler = resultHandler == null ? payload -> {} : resultHandler;
    }

    public static void installCultivatorSyncHandler(Consumer<CultivatorSyncPayload> syncHandler) {
        cultivatorSyncHandler = syncHandler == null ? payload -> {} : syncHandler;
    }

    public static void installMartialGuideHandler(Runnable handler) {
        openMartialGuideHandler = handler == null ? () -> {} : handler;
    }

    public static void installQuickMenuHandler(Consumer<QuickMenuSnapshotPayload> handler) {
        quickMenuSnapshotHandler = handler == null ? payload -> {} : handler;
    }

    public static void handleOpenGenesis(OpenGenesisPayload payload, IPayloadContext context) {
        openGenesisHandler.run();
    }

    public static void handleGenesisResult(GenesisResultPayload payload, IPayloadContext context) {
        genesisResultHandler.accept(payload);
    }

    public static void handleCultivatorSync(CultivatorSyncPayload payload, IPayloadContext context) {
        cultivatorSyncHandler.accept(payload);
    }

    public static void handleOpenMartialGuide(OpenMartialGuidePayload payload, IPayloadContext context) {
        openMartialGuideHandler.run();
    }

    public static void handleQuickMenuSnapshot(
            QuickMenuSnapshotPayload payload,
            IPayloadContext context) {
        quickMenuSnapshotHandler.accept(payload);
    }
}
