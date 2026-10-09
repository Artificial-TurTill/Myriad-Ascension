package io.github.artificialturtill.foundation.character;

import io.github.artificialturtill.foundation.content.SkillDef;
import io.github.artificialturtill.foundation.content.StatDef;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.neoforged.neoforge.common.damagesource.DamageContainer;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingUseTotemEvent;

/**
 * The JRMCore damage pipeline (Dragonblock C+ CharacterCombat, JRMCoreEH + JRMCoreH.jrmcDam) for power types whose
 * life is Body: vanilla health never moves, every hit is applied to Body after the JRMCore armor rule, the released
 * Defense (passive share) and Endurance; mobs pierce 10 % of the defense. Attackers with a character add their
 * released Melee to powered punches (Stamina cost) and earn TP per hit. Natural characters keep vanilla combat; their
 * statistics act through vanilla attributes ({@link VanillaBridge}).
 */
public final class CharacterCombat {
    /** LivingAttackEvent part: vanilla armor / enchantment / effect / absorption reductions are off for Body characters. */
    public static void incoming(LivingIncomingDamageEvent event) {
        DamageSource source = event.getSource();
        if (source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) return;
        LivingEntity target = event.getEntity();
        Fighter victim = target instanceof Player p ? CharacterService.fighter(p) : null;
        Fighter attacker = source.getEntity() instanceof Player p ? CharacterService.fighter(p) : null;
        if (victim != null && victim.powerType().bodyLife() || attacker != null && attacker.powerType().bodyLife()) {
            for (var reduction : new DamageContainer.Reduction[]{DamageContainer.Reduction.ARMOR, DamageContainer.Reduction.ENCHANTMENTS,
                    DamageContainer.Reduction.MOB_EFFECTS, DamageContainer.Reduction.ABSORPTION})
                event.getContainer().addModifier(reduction, (container, value) -> 0F);
        }
    }

    /** LivingHurtEvent: attacker bonus, then Body damage on Body characters or direct damage on everything else. */
    public static void beforeDamage(LivingDamageEvent.Pre event) {
        DamageSource source = event.getSource();
        if (source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) return; // /kill and the void stay vanilla
        LivingEntity target = event.getEntity();
        if (!(event.getNewDamage() > 0) || target.level().isClientSide) return;
        Player victimPlayer = target instanceof Player p ? p : null;
        if (victimPlayer != null && victimPlayer.getAbilities().invulnerable) return;
        Fighter victim = victimPlayer == null ? null : CharacterService.fighter(victimPlayer);
        float hit = event.getNewDamage();

        if (source.getEntity() instanceof Player attackerPlayer && attackerPlayer != target) {
            Fighter attacker = CharacterService.fighter(attackerPlayer);
            boolean melee = source.getDirectEntity() == attackerPlayer && source.is(DamageTypes.PLAYER_ATTACK);
            if (attacker != null && melee) {
                if (attacker.powerType().bodyLife() && attacker.canPowerPunch()) {
                    // curAtr = released Melee, added in full to every powered punch, which costs Stamina.
                    hit += attacker.outputOf(StatDef.MELEE);
                    if (!attackerPlayer.getAbilities().instabuild) attacker.spendPunch();
                }
                if (!(target instanceof ArmorStand) && !attackerPlayer.getAbilities().instabuild) attacker.rewardHit(new java.util.Random(attackerPlayer.getRandom().nextLong()));
                CharacterService.sync(attackerPlayer, false);
            }
        }
        if (victim == null || !victim.powerType().bodyLife()) { event.setNewDamage(hit); return; }
        int penetration = source.getEntity() instanceof LivingEntity && !(source.getEntity() instanceof Player) ? StatMath.MOB_PENETRATION : 0;
        hurtBody(victimPlayer, victim, (int) Math.ceil(hit), source, penetration, event);
        CharacterService.sync(victimPlayer, false);
    }

    /** jrmcDam for a Body character: armor, passive Defense, penetration, Endurance; vanilla damage 0 or lethal. */
    static void hurtBody(Player player, Fighter f, int hit, DamageSource source, int penetration, LivingDamageEvent.Pre event) {
        boolean fall = source.is(DamageTypes.FALL);
        hit = applyArmor(player, hit, source);
        int defense = StatMath.passive(f.outputOf(StatDef.DEFENSE), f.powerType().passiveDefense());
        int damage = StatMath.bodyDamage(hit, fall ? 0 : defense, penetration, f.skillEffect(SkillDef.ENDURANCE));
        int body = Math.max(0, f.body() - damage);
        f.setResources(body, f.energy(), f.stamina());
        f.markHurt();
        event.setNewDamage(body == 0 ? Float.MAX_VALUE : 0F);
    }
    /** JRMCoreEH.ApplyArmor on the raw hit; armor-bypassing sources are not reduced. */
    static int applyArmor(Player player, int hit, DamageSource source) {
        if (source.is(DamageTypeTags.BYPASSES_ARMOR)) return hit;
        int protection = 0;
        for (EquipmentSlot slot : new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET})
            if (player.getItemBySlot(slot).getItem() instanceof ArmorItem armor) protection += StatMath.armorProtection(hit, armor.getDefense());
        return Math.max(0, hit - protection);
    }

    /** A totem only saves the vanilla player; give a Body character a sliver of Body. */
    public static void totem(LivingUseTotemEvent event) {
        if (event.getEntity() instanceof Player player) {
            Fighter f = CharacterService.fighter(player);
            if (f != null && f.powerType().bodyLife()) { f.setResources(Math.max(1, f.body()), f.energy(), f.stamina()); CharacterService.sync(player, false); }
        }
    }

    /** JRMCoreComTickH: Body 0 kills; vanilla health of Body characters is kept full. */
    static void tickLife(Player player, Fighter f) {
        if (!f.powerType().bodyLife() || player.getAbilities().invulnerable) return;
        if (f.body() <= 0) {
            player.hurt(player.getFoodData().getFoodLevel() <= 0 ? player.damageSources().starve() : player.damageSources().genericKill(), Float.MAX_VALUE);
            return;
        }
        if (player.getHealth() < player.getMaxHealth()) player.setHealth(player.getMaxHealth());
    }
    private CharacterCombat() {}
}
