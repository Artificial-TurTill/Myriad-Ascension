package io.github.artificialturtill.foundation.character;

import io.github.artificialturtill.foundation.api.FoundationApi;
import io.github.artificialturtill.foundation.content.StatDef;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

/**
 * Where Foundation values meet vanilla attributes. Natural characters (hearts) get their training as vanilla
 * modifiers measured from the starting attributes, so a fresh Natural character is exactly vanilla: max health +
 * (Health - starting Health), attack damage + (Melee - starting Melee), armor + (Defense - starting Defense) / 10.
 * Every character gets its appearance height as the 1.21 scale attribute.
 */
public final class VanillaBridge {
    private static final ResourceLocation HEALTH = id("natural_health"), MELEE = id("natural_melee"), ARMOR = id("natural_armor"), SCALE = id("height");

    public static void apply(Player player) {
        if (player.level().isClientSide) return;
        Fighter f = CharacterService.fighter(player);
        boolean natural = f != null && !f.powerType().bodyLife();
        set(player, Attributes.MAX_HEALTH, HEALTH, natural ? gain(f, StatDef.HEALTH) : 0, AttributeModifier.Operation.ADD_VALUE);
        set(player, Attributes.ATTACK_DAMAGE, MELEE, natural ? gain(f, StatDef.MELEE) : 0, AttributeModifier.Operation.ADD_VALUE);
        set(player, Attributes.ARMOR, ARMOR, natural ? gain(f, StatDef.DEFENSE) / 10D : 0, AttributeModifier.Operation.ADD_VALUE);
        set(player, Attributes.SCALE, SCALE, f == null ? 0 : f.appearance().height() / 100D - 1D, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
        if (player.getHealth() > player.getMaxHealth()) player.setHealth(player.getMaxHealth());
    }
    /** Statistic now minus the same statistic at the starting attributes. */
    static double gain(Fighter f, String role) {
        int index = f.powerType().statIndex(role);
        if (index < 0) return 0;
        StatDef def = f.powerType().stats().get(index);
        int start = StatMath.stat(def, f.startingAttributes()[def.attribute()], f.statBonus(index), f.skillBonus(index));
        return f.output(index) - start;
    }
    private static void set(Player player, Holder<Attribute> attribute, ResourceLocation id, double amount, AttributeModifier.Operation op) {
        AttributeInstance instance = player.getAttribute(attribute);
        if (instance == null) return;
        AttributeModifier current = instance.getModifier(id);
        if (amount == 0) { if (current != null) instance.removeModifier(id); return; }
        if (current != null && current.amount() == amount && current.operation() == op) return;
        instance.addOrReplacePermanentModifier(new AttributeModifier(id, amount, op));
    }
    private static ResourceLocation id(String path) { return ResourceLocation.fromNamespaceAndPath(FoundationApi.MOD_ID, path); }
    private VanillaBridge() {}
}
