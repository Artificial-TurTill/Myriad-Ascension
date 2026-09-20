package io.github.artificialturtill.myriadascension.network;

import io.github.artificialturtill.myriadascension.cultivation.data.CultivatorData;
import io.github.artificialturtill.myriadascension.cultivation.data.ModAttachments;
import io.github.artificialturtill.myriadascension.cultivation.qi.QiRules;
import io.github.artificialturtill.myriadascension.cultivation.realm.CultivationRealm;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public final class ModNetworking {
    public static final String NETWORK_VERSION = "3";

    private ModNetworking() {
    }

    public static void registerPayloads(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(NETWORK_VERSION);
        registrar.playToServer(
                QiControlPayload.TYPE,
                QiControlPayload.STREAM_CODEC,
                ModNetworking::handleQiControl);

        registrar.playToServer(
                SubmitGenesisPayload.TYPE,
                SubmitGenesisPayload.STREAM_CODEC,
                ModNetworking::handleSubmitGenesis);

        registrar.playToClient(
                OpenGenesisPayload.TYPE,
                OpenGenesisPayload.STREAM_CODEC,
                ClientPayloadBridge::handleOpenGenesis);

        registrar.playToClient(
                GenesisResultPayload.TYPE,
                GenesisResultPayload.STREAM_CODEC,
                ClientPayloadBridge::handleGenesisResult);

        registrar.playToClient(
                CultivatorSyncPayload.TYPE,
                CultivatorSyncPayload.STREAM_CODEC,
                ClientPayloadBridge::handleCultivatorSync);
    }

    private static void handleSubmitGenesis(SubmitGenesisPayload payload, IPayloadContext context) {
        if (!(context.player() instanceof ServerPlayer player)) {
            return;
        }

        CultivatorData data = player.getData(ModAttachments.CULTIVATOR_DATA);
        if (data.hasCompletedInitialSetup()) {
            context.reply(GenesisResultPayload.from(data));
            context.reply(CultivatorSyncPayload.from(data));
            return;
        }

        if (payload.sex() == null || payload.sex() == io.github.artificialturtill.myriadascension.character.CharacterSex.UNSET) {
            return;
        }

        double startingAlignment = payload.benevolent() ? 1.0D : -1.0D;
        data.completeInitialSetup(payload.sex(), startingAlignment, player.getRandom());
        context.reply(GenesisResultPayload.from(data));
        context.reply(CultivatorSyncPayload.from(data));
    }

    private static void handleQiControl(QiControlPayload payload, IPayloadContext context) {
        if (!(context.player() instanceof ServerPlayer player)) {
            return;
        }

        CultivatorData data = player.getData(ModAttachments.CULTIVATOR_DATA);

        switch (payload.action()) {
            case GATHER_AND_CIRCULATE -> handleGatherAndCirculate(player, data);
            case SUPPRESS_CIRCULATION -> handleSuppress(player, data);
            case TOGGLE_BURST -> handleBurstToggle(player, data);
        }
    }

    private static void handleGatherAndCirculate(ServerPlayer player, CultivatorData data) {
        if (!data.tryAcceptCirculationControl(player.level().getGameTime())) {
            return;
        }

        // A Mortal with no cultivation capacity cannot manufacture usable Qi through input alone.
        if (data.realm() == CultivationRealm.MORTAL || data.maximumQi() <= 0.0D || data.currentQi() <= 0.0D) {
            return;
        }

        data.increaseCirculation(QiRules.CIRCULATION_PERCENT_PER_CONTROL_PULSE);
        syncPlayer(player, data);
    }

    private static void handleSuppress(ServerPlayer player, CultivatorData data) {
        if (!data.tryAcceptCirculationControl(player.level().getGameTime())) {
            return;
        }

        data.decreaseCirculation(QiRules.CIRCULATION_PERCENT_PER_CONTROL_PULSE);

        if (data.circulationPercent() <= 0.0D) {
            data.setBurstMode(false);
        }

        syncPlayer(player, data);
    }

    private static void handleBurstToggle(ServerPlayer player, CultivatorData data) {
        if (!data.tryAcceptBurstToggle(player.level().getGameTime())) {
            return;
        }

        if (data.burstMode()) {
            data.setBurstMode(false);
            syncPlayer(player, data);
            return;
        }

        boolean canBurst = data.realm() != CultivationRealm.MORTAL
                && data.maximumQi() > 0.0D
                && data.currentQi() > 0.0D
                && data.circulationPercent() > 0.0D;

        if (canBurst) {
            data.setBurstMode(true);
            syncPlayer(player, data);
        }
    }

    public static void syncPlayer(ServerPlayer player, CultivatorData data) {
        net.neoforged.neoforge.network.PacketDistributor.sendToPlayer(
                player,
                CultivatorSyncPayload.from(data));
    }
}
