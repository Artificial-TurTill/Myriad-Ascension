package io.github.artificialturtill.foundation.character;

import io.github.artificialturtill.foundation.api.FoundationApi;
import io.github.artificialturtill.foundation.network.CharacterActionPayload;
import io.github.artificialturtill.foundation.network.CharacterSyncPayload;
import io.github.artificialturtill.foundation.network.CreateCharacterPayload;
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
            apply(profile, request);
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

    private static void reply(ServerPlayer player, String message) { PacketDistributor.sendToPlayer(player, new ResultPayload(message)); }

    /** Sends the owner snapshot (only when it changed unless forced) and refreshes the public look. */
    public static void sync(Player player, boolean force) {
        if (!(player instanceof ServerPlayer serverPlayer)) return;
        CharacterProfile profile = profile(player);
        CharacterRuntime runtime = player.getData(FoundationAttachments.RUNTIME);
        Fighter f = profile.fighterOrNull();
        PublicLook look = f == null ? PublicLook.NONE : PublicLook.of(f, runtime.charging);
        if (!look.equals(player.getData(FoundationAttachments.LOOK))) player.setData(FoundationAttachments.LOOK, look);
        CompoundTag tag = f == null ? new CompoundTag() : f.save();
        if (!force && tag.equals(runtime.lastSent)) return;
        runtime.lastSent = tag;
        PacketDistributor.sendToPlayer(serverPlayer, new CharacterSyncPayload(tag, profile.dormant()));
    }

    /** Erase (operator command): the player starts over at the creation wizard. */
    public static void erase(ServerPlayer player) {
        profile(player).erase();
        player.getData(FoundationAttachments.RUNTIME).charging = false;
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
        VanillaBridge.apply(player);
        sync(player, true);
    }
    public static void changeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        event.getEntity().getData(FoundationAttachments.RUNTIME).charging = false;
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
        if (player.tickCount % 20 == 0) {
            if (runtime.charging) f.chargeSecond(runtime.lowering);
            f.tickFormSecond();
        }
        if (player.tickCount % 100 == 0) f.recover(player.getFoodData().getFoodLevel() > 0);
        if (player.tickCount % 20 == 0) {
            VanillaBridge.apply(player);
            sync(player, false);
        }
    }
    private CharacterService() {}
}
