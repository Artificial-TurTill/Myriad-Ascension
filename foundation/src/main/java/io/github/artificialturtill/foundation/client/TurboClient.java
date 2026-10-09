package io.github.artificialturtill.foundation.client;

import io.github.artificialturtill.foundation.character.Fighter;
import io.github.artificialturtill.foundation.character.FoundationAttachments;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.neoforged.neoforge.client.event.ComputeFovModifierEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

/**
 * DBC 1.4.85 DBCKiTech.DashKi for Turbo (Myriad Realm: Qi Surge), as in Dragonblock C+ DashClient: while Turbo is on
 * the local player's horizontal motion is replaced by the run factor x 0.25 blocks per tick in the input direction.
 * The view does not react (no FOV change, head bobbing capped at the vanilla sprint pace). Sprinting stays vanilla
 * (adaptation: DBC also ran with sprint).
 */
public final class TurboClient {
    private static final float SPRINT_WALK_DIST = 0.6F * 0.2806F;
    private static boolean running;

    public static void preTick(PlayerTickEvent.Pre event) {
        if (!(event.getEntity() instanceof LocalPlayer player) || !player.isControlledByLocalInstance()) return;
        running = false;
        Fighter f = ClientCharacter.fighter();
        if (f == null || !player.getData(FoundationAttachments.LOOK).turbo() || f.release() <= 0) return;
        if (player.getAbilities().flying || player.isFallFlying() || player.isPassenger() || player.isInWaterOrBubble()
            || player.isInLava() || player.isCreative() || player.isSpectator()) return;
        if (f.energy() < f.turboCost()) return; // DashKi: curEnergy < cost -> no run
        running = true;
        double add = f.runFactor(true);
        float strafe = player.input.leftImpulse * 0.98F, forward = player.input.forwardImpulse * 0.98F;
        var motion = player.getDeltaMovement();
        float length = strafe * strafe + forward * forward;
        if (length < 1.0E-4F) { player.setDeltaMovement(0, motion.y, 0); return; }
        length = Mth.sqrt(length);
        if (length < 1F) length = 1F;
        float scale = (float) add / length;
        float sin = Mth.sin(player.getYRot() * Mth.DEG_TO_RAD), cos = Mth.cos(player.getYRot() * Mth.DEG_TO_RAD);
        strafe *= scale; forward *= scale;
        player.setDeltaMovement((strafe * cos - forward * sin) * 0.25F, motion.y, (forward * cos + strafe * sin) * 0.25F);
    }
    public static void postTick(PlayerTickEvent.Post event) {
        if (!running || !(event.getEntity() instanceof LocalPlayer player)) return;
        if (player.walkDist - player.walkDistO > SPRINT_WALK_DIST) player.walkDist = player.walkDistO + SPRINT_WALK_DIST;
    }
    public static void fov(ComputeFovModifierEvent event) {
        if (running && !event.getPlayer().isUsingItem()) event.setNewFovModifier(1F);
    }
    private TurboClient() {}
}
