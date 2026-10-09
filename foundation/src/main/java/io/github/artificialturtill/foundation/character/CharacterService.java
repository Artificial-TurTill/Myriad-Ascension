package io.github.artificialturtill.foundation.character;

import io.github.artificialturtill.foundation.api.FoundationApi;
import io.github.artificialturtill.foundation.network.CharacterActionPayload;
import io.github.artificialturtill.foundation.network.CharacterSyncPayload;
import io.github.artificialturtill.foundation.network.CreateCharacterPayload;
import io.github.artificialturtill.foundation.network.HairUpdatePayload;
import io.github.artificialturtill.foundation.network.PublicLook;
import io.github.artificialturtill.foundation.network.ResultPayload;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

/**
 * Server side of the character (Dragonblock C+ CharacterService): menu actions, creation, the per-tick life cycle
 * (charging, transformations, regeneration every 100 ticks) and syncing to the owner and trackers.
 */
public final class CharacterService {
    public static CharacterProfile profile(Player player) { return player.getData(FoundationAttachments.CHARACTER); }
    /** The active character, or null (no character, dormant, or client side). */
    public static Fighter fighter(Player player) {
        if (player.level().isClientSide) return null;
        return profile(player).fighterOrNull();
    }

    public static void handleCreate(Player player, CreateCharacterPayload request) {
        if (!(player instanceof ServerPlayer serverPlayer)) return;
        CharacterProfile profile = profile(player);
        if (!profile.acceptClick(player.tickCount)) return;
        try {
            if (!player.isAlive() || player.isSpectator()) throw new IllegalArgumentException("Character creation requires a living player.");
            profile.create(FoundationApi.content(), request.race(), request.powerType(), request.characterClass(), request.appearance());
            VanillaBridge.apply(player);
            reply(serverPlayer, "");
        } catch (IllegalArgumentException e) {
            reply(serverPlayer, e.getMessage());
        }
        sync(player, true);
    }

    /**
     * The hairstyle manager's Done (DBC+ HairService). Adaptation: DBC needed Barber Scissors after the free creation
     * edit; the Foundation has no items, so the hairstyle can be changed at any time.
     */
    public static void handleHair(Player player, HairUpdatePayload request) {
        if (!(player instanceof ServerPlayer serverPlayer)) return;
        CharacterProfile profile = profile(player);
        if (!profile.acceptClick(player.tickCount)) return;
        try {
            Fighter f = profile.fighter();
            f.setAppearance(f.appearance().withHair(HairCode.upgrade(request.code()), request.color()));
            reply(serverPlayer, "");
        } catch (IllegalArgumentException e) {
            reply(serverPlayer, e.getMessage());
        }
        sync(player, true);
    }

    public static void handleAction(Player player, CharacterActionPayload request) {
        if (!(player instanceof ServerPlayer serverPlayer)) return;
        CharacterProfile profile = profile(player);
        // Held input: no result message and no click limit.
        if (request.action() == CharacterActionPayload.CHARGE) {
            CharacterRuntime runtime = player.getData(FoundationAttachments.RUNTIME);
            Fighter f = profile.fighterOrNull();
            runtime.charging = request.a() == 1 && f != null && f.powerType().release() && player.isAlive();
            runtime.lowering = runtime.charging && request.b() == 1;
            return;
        }
        if (!profile.acceptClick(player.tickCount)) return;
        try {
            if (!player.isAlive() || player.isSpectator()) throw new IllegalArgumentException("Character changes require a living player.");
            if (request.action() == CharacterActionPayload.TURBO) toggleTurbo(player, profile.fighter());
            else apply(profile, request);
            VanillaBridge.apply(player);
            if (request.action() != CharacterActionPayload.REFRESH) reply(serverPlayer, "");
        } catch (IllegalArgumentException e) {
            reply(serverPlayer, e.getMessage());
        }
        sync(player, true);
    }

    static void apply(CharacterProfile profile, CharacterActionPayload request) {
        switch (request.action()) {
            case CharacterActionPayload.REFRESH -> { }
            case CharacterActionPayload.UPGRADE -> profile.fighter().upgrade(request.a(), request.b());
            case CharacterActionPayload.LEARN_SKILL -> profile.fighter().learnSkill(request.id(), false);
            case CharacterActionPayload.FORGET_SKILL -> profile.fighter().forgetSkill(request.id());
            case CharacterActionPayload.SELECT_FORM -> profile.fighter().selectForm(request.id());
            case CharacterActionPayload.ASCEND -> profile.fighter().ascend();
            case CharacterActionPayload.DESCEND -> { if (!profile.fighter().descend()) throw new IllegalArgumentException("Already in the base form."); }
            default -> throw new IllegalArgumentException("Unknown character action.");
        }
    }

    /** DBCKiTech.TurboMode: R toggles; switching on needs release above 0 % and energy. */
    public static void toggleTurbo(Player player, Fighter f) {
        CharacterRuntime runtime = player.getData(FoundationAttachments.RUNTIME);
        if (runtime.turbo) { runtime.turbo = false; return; }
        String reason = f.turboBlocker();
        if (reason != null) throw new IllegalArgumentException(reason);
        runtime.turbo = true;
        runtime.turboTicks = 0;
    }
    /** Turbo pays per second only while the run can happen (DashKi: not flying, swimming, riding or gliding). */
    static boolean turboRuns(Player player) {
        return !player.getAbilities().flying && !player.isFallFlying() && !player.isPassenger() && !player.isInWaterOrBubble()
            && !player.isInLava() && !player.isSpectator();
    }

    private static void reply(ServerPlayer player, String message) { PacketDistributor.sendToPlayer(player, new ResultPayload(message)); }

    /** Sends the owner snapshot (only when it changed unless forced) and refreshes the public look. */
    public static void sync(Player player, boolean force) {
        if (!(player instanceof ServerPlayer serverPlayer)) return;
        CharacterProfile profile = profile(player);
        CharacterRuntime runtime = player.getData(FoundationAttachments.RUNTIME);
        Fighter f = profile.fighterOrNull();
        PublicLook look = f == null ? PublicLook.NONE : PublicLook.of(f, runtime.charging, runtime.turbo);
        if (!look.equals(player.getData(FoundationAttachments.LOOK))) player.setData(FoundationAttachments.LOOK, look);
        CompoundTag tag = f == null ? new CompoundTag() : f.save();
        if (!force && tag.equals(runtime.lastSent)) return;
        runtime.lastSent = tag;
        PacketDistributor.sendToPlayer(serverPlayer, new CharacterSyncPayload(tag, profile.dormant()));
    }

    /** Erase (operator command): the player starts over at the creation wizard. */
    public static void erase(ServerPlayer player) {
        profile(player).erase();
        player.getData(FoundationAttachments.RUNTIME).stop();
        VanillaBridge.apply(player);
        sync(player, true);
    }

    public static void login(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        Fighter f = fighter(player);
        if (f != null && f.powerType().bodyLife() && f.body() <= 0) f.restoreResources();
        if (profile(player).dormant() && player instanceof ServerPlayer sp)
            sp.sendSystemMessage(net.minecraft.network.chat.Component.translatable("message.ascension_foundation.dormant"));
        VanillaBridge.apply(player);
        sync(player, true);
    }
    public static void respawn(PlayerEvent.PlayerRespawnEvent event) {
        Player player = event.getEntity();
        Fighter f = fighter(player);
        if (f != null && !event.isEndConquered()) f.resetAfterDeath();
        player.getData(FoundationAttachments.RUNTIME).stop();
        VanillaBridge.apply(player);
        sync(player, true);
    }
    public static void changeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        event.getEntity().getData(FoundationAttachments.RUNTIME).stop();
        sync(event.getEntity(), true);
    }

    public static void tick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (player.level().isClientSide || !player.isAlive() || player.isSpectator()) return;
        Fighter f = fighter(player);
        if (f == null) return;
        CharacterCombat.tickLife(player, f);
        if (!player.isAlive()) return;
        f.tickRecoveryDelay();
        CharacterRuntime runtime = player.getData(FoundationAttachments.RUNTIME);
        // Turbo ends at 0 % release or without energy; while it runs it costs (int)(1 + run factor) energy per second.
        if (runtime.turbo && f.turboMustStop()) runtime.turbo = false;
        if (runtime.turbo && turboRuns(player) && ++runtime.turboTicks >= 20) {
            runtime.turboTicks = 0;
            if (!player.getAbilities().instabuild && !f.spendTurboSecond()) runtime.turbo = false;
        }
        // ChargeKi: one 5 % step per 20 ticks, twice as fast with Turbo.
        if (runtime.charging && ++runtime.chargeTicks >= (runtime.turbo ? 10 : 20)) {
            runtime.chargeTicks = 0;
            f.chargeSecond(runtime.lowering);
        }
        if (player.tickCount % 20 == 0) f.tickFormSecond();
        if (player.tickCount % 100 == 0) f.recover(player.getFoodData().getFoodLevel() > 0, runtime.turbo);
        if (player.tickCount % 20 == 0) {
            VanillaBridge.apply(player);
            sync(player, false);
        }
    }
    private CharacterService() {}
}
