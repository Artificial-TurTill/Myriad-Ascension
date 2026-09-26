package io.github.artificialturtill.myriadascension.network;

import io.github.artificialturtill.myriadascension.cultivation.data.CultivatorData;
import io.github.artificialturtill.myriadascension.cultivation.data.ModAttachments;
import io.github.artificialturtill.myriadascension.cultivation.qi.QiRules;
import io.github.artificialturtill.myriadascension.cultivation.realm.CultivationRealm;
import io.github.artificialturtill.myriadascension.cultivation.realm.RealmMilestoneRules;
import io.github.artificialturtill.myriadascension.registry.ModItems;
import io.github.artificialturtill.myriadascension.training.BodyTemperingRules;
import io.github.artificialturtill.myriadascension.training.TestudoTrainingRules;
import io.github.artificialturtill.myriadascension.technique.TechniqueCategory;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public final class ModNetworking {
    public static final String NETWORK_VERSION = "8";

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

        registrar.playToServer(
                TrainingControlPayload.TYPE,
                TrainingControlPayload.STREAM_CODEC,
                ModNetworking::handleTrainingControl);

        registrar.playToServer(
                QuickMenuRequestPayload.TYPE,
                QuickMenuRequestPayload.STREAM_CODEC,
                ModNetworking::handleQuickMenuRequest);

        registrar.playToServer(
                QuickMenuActionPayload.TYPE,
                QuickMenuActionPayload.STREAM_CODEC,
                ModNetworking::handleQuickMenuAction);

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

        registrar.playToClient(
                OpenMartialGuidePayload.TYPE,
                OpenMartialGuidePayload.STREAM_CODEC,
                ClientPayloadBridge::handleOpenMartialGuide);

        registrar.playToClient(
                QuickMenuSnapshotPayload.TYPE,
                QuickMenuSnapshotPayload.STREAM_CODEC,
                ClientPayloadBridge::handleQuickMenuSnapshot);
    }

    private static void handleQuickMenuRequest(
            QuickMenuRequestPayload payload,
            IPayloadContext context) {

        if (!(context.player() instanceof ServerPlayer player)) {
            return;
        }

        CultivatorData data = player.getData(ModAttachments.CULTIVATOR_DATA);
        context.reply(QuickMenuSnapshotPayload.from(data));
    }

    private static void handleQuickMenuAction(
            QuickMenuActionPayload payload,
            IPayloadContext context) {

        if (!(context.player() instanceof ServerPlayer player)) {
            return;
        }

        CultivatorData data = player.getData(ModAttachments.CULTIVATOR_DATA);
        int direction = payload.direction() < 0 ? -1 : 1;

        switch (payload.action()) {
            case CYCLE_METHOD -> cycleMethod(data, direction);
            case CYCLE_TECHNIQUE -> {
                if (payload.category() != null) {
                    cycleTechnique(data, payload.category(), direction);
                }
            }
            case TOGGLE_RESOURCE_SCANNING ->
                    data.setResourceScanningEnabled(!data.resourceScanningEnabled());
            case TOGGLE_CULTIVATION_GAUGE ->
                    data.setCultivationGaugeEnabled(!data.cultivationGaugeEnabled());
            case TOGGLE_LOOSE_WEIGHTS ->
                    data.setLooseTrainingWeightsEnabled(!data.looseTrainingWeightsEnabled());
        }

        syncPlayer(player, data);
        context.reply(QuickMenuSnapshotPayload.from(data));
    }

    private static void cycleMethod(CultivatorData data, int direction) {
        List<String> choices = new ArrayList<>();
        choices.add("");
        choices.addAll(data.cultivationMethods().knownMethods());

        String next = cycleValue(
                choices,
                data.cultivationMethods().activeMethodId(),
                direction);

        if (next.isBlank()) {
            data.cultivationMethods().clearActive();
            return;
        }

        ResourceLocation id = ResourceLocation.tryParse(next);
        if (id != null) {
            data.cultivationMethods().setActive(id);
        }
    }

    private static void cycleTechnique(
            CultivatorData data,
            TechniqueCategory category,
            int direction) {

        List<String> choices = new ArrayList<>();
        choices.add("");

        for (String raw : data.techniqueKnowledge().view()) {
            ResourceLocation id = ResourceLocation.tryParse(raw);
            if (id == null) {
                continue;
            }

            var manual = ModItems.manualFor(id);
            if (manual != null && manual.get().category() == category) {
                choices.add(raw);
            }
        }

        String current = data.techniqueLoadout().equippedId(category);
        if (!current.isBlank() && !choices.contains(current)) {
            choices.add(current);
        }

        String next = cycleValue(choices, current, direction);
        if (next.isBlank()) {
            data.techniqueLoadout().clear(category);
            return;
        }

        ResourceLocation id = ResourceLocation.tryParse(next);
        if (id != null) {
            data.techniqueLoadout().equip(category, id);
        }
    }

    private static String cycleValue(
            List<String> values,
            String current,
            int direction) {

        if (values.isEmpty()) {
            return current == null ? "" : current;
        }

        String safeCurrent = current == null ? "" : current;
        int index = values.indexOf(safeCurrent);
        if (index < 0) {
            index = 0;
        }

        return values.get(Math.floorMod(index + direction, values.size()));
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

        ItemStack guide = new ItemStack(ModItems.INSTRUCTION_TO_THE_MARTIAL_WORLD.get());
        if (!player.getInventory().add(guide)) {
            player.drop(guide, false);
        }

        context.reply(GenesisResultPayload.from(data));
        context.reply(CultivatorSyncPayload.from(data));
    }

    private static void handleTrainingControl(TrainingControlPayload payload, IPayloadContext context) {
        if (!(context.player() instanceof ServerPlayer player)) {
            return;
        }

        CultivatorData data = player.getData(ModAttachments.CULTIVATOR_DATA);
        data.setTrainingRequested(payload.training());

        if (!payload.training()) {
            syncPlayer(player, data);
        }
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

        // Tempered Body 4-6 uses G only as a short focus/sense pulse while the
        // Testudo stance is held. It is not an absorption or Qi-control action.
        if (data.trainingRequested()
                && data.realm() == CultivationRealm.TEMPERED_BODY
                && data.minorStage() >= 4
                && data.minorStage() <= 6) {
            data.markTrainingBreathPulse(player.level().getGameTime());
            return;
        }

        // Tempered Body 7-9: G consciously gathers Qi into the vessel.
        // It is storage only; circulation and active use remain locked.
        if (data.realm() == CultivationRealm.TEMPERED_BODY
                && data.minorStage() >= 7
                && data.minorStage() <= 9) {

            double capacity = TestudoTrainingRules.temperedYuanQiCapacity(data.minorStage());
            if (data.maximumQi() < capacity) {
                data.setMaximumQi(capacity);
            }

            double environment = BodyTemperingRules.testudoEnvironmentMultiplier(player, data);
            double gathered = TestudoTrainingRules.consciousYuanQiPerPulse(data.minorStage())
                    * environment
                    * TestudoTrainingRules.fatigueEfficiency(data);

            double before = data.currentQi();
            data.setCurrentQi(before + gathered);
            data.setCirculationPercent(0.0D);
            data.setBurstMode(false);

            double actuallyStored = Math.max(0.0D, data.currentQi() - before);
            if (actuallyStored > 0.0D) {
                BodyTemperingRules.trainNaturalAbsorption(data, actuallyStored, environment);
            }

            syncPlayer(player, data);
            return;
        }

        // Initial Element+ is true internal Qi control. G both actively
        // replenishes the reserve and raises internal circulation.
        if (!RealmMilestoneRules.canActivelyUseQi(data.realm())
                || data.maximumQi() <= 0.0D) {
            return;
        }

        data.setCurrentQi(
                data.currentQi()
                        + QiRules.activeGatherPerControlPulse(
                                data.maximumQi(),
                                data.meditationLevel()));
        data.increaseCirculation(QiRules.CIRCULATION_PERCENT_PER_CONTROL_PULSE);
        syncPlayer(player, data);
    }

    private static void handleSuppress(ServerPlayer player, CultivatorData data) {
        if (!data.tryAcceptCirculationControl(player.level().getGameTime())) {
            return;
        }

        if (!RealmMilestoneRules.canActivelyUseQi(data.realm())) {
            data.setCirculationPercent(0.0D);
            data.setBurstMode(false);
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

        boolean canBurst = RealmMilestoneRules.canActivelyUseQi(data.realm())
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
