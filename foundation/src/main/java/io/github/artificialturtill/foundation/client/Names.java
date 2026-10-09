package io.github.artificialturtill.foundation.client;

import io.github.artificialturtill.foundation.content.PowerType;
import java.util.Locale;
import net.minecraft.network.chat.Component;

/**
 * Display names of content ids: "race.<namespace>.<path>", "power_type.…", "class.…", "skill.…", "form.…" (+ ".desc");
 * attributes and statistics use the power type's namespace: "attribute.<ns>.<key>", "stat.<ns>.<key>",
 * "resource.<ns>.<energy>". Missing translations fall back to the id path in title case.
 */
public final class Names {
    public static Component of(String prefix, String id) {
        return Component.translatableWithFallback(prefix + "." + id.replace(':', '.'), pretty(id.substring(id.indexOf(':') + 1)));
    }
    public static Component desc(String prefix, String id) {
        return Component.translatableWithFallback(prefix + "." + id.replace(':', '.') + ".desc", "");
    }
    public static String namespace(PowerType power) { return power.id().substring(0, power.id().indexOf(':')); }
    public static Component attribute(PowerType power, int index) {
        String key = power.attributes().get(index).key();
        return Component.translatableWithFallback("attribute." + namespace(power) + "." + key, pretty(key));
    }
    public static Component attributeDesc(PowerType power, int index) {
        return Component.translatableWithFallback("attribute." + namespace(power) + "." + power.attributes().get(index).key() + ".desc", "");
    }
    public static Component stat(PowerType power, int index) {
        String key = power.stats().get(index).key();
        return Component.translatableWithFallback("stat." + namespace(power) + "." + key, pretty(key));
    }
    public static Component energy(PowerType power) {
        return power.hasEnergy() ? Component.translatableWithFallback("resource." + namespace(power) + "." + power.energy(), pretty(power.energy()))
            : Component.literal("-");
    }
    public static String pretty(String path) {
        StringBuilder out = new StringBuilder();
        for (String word : path.split("[_/]")) {
            if (word.isEmpty()) continue;
            if (!out.isEmpty()) out.append(' ');
            out.append(word.substring(0, 1).toUpperCase(Locale.ROOT)).append(word.substring(1));
        }
        return out.toString();
    }
    /** 1234567 -> "1,234,567". */
    public static String num(long value) { return String.format(Locale.ROOT, "%,d", value); }
    private Names() {}
}
