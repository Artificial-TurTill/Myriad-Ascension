package io.github.artificialturtill.foundation.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.IntConsumer;
import java.util.function.Supplier;
import net.minecraft.network.chat.Component;

/**
 * The X menu's entries (client side). The Foundation registers its own; add-ons register theirs from their client
 * setup with {@link #register}. Entries fill the 3 x 3 grid in registration order around the centre cell ("More").
 */
public final class QuickMenu {
    /**
     * One cell: a title, the current value (and its colour), whether it applies right now, and what happens on
     * activation ({@code direction} 1 = release X / left click, -1 = right click).
     */
    public record Entry(String id, Supplier<Component> title, Supplier<Component> value, Supplier<Integer> color,
                        Supplier<Boolean> available, IntConsumer activate) {}

    public static final int ON = 0xFF1E9E30, OFF = 0xFFB02828, NEUTRAL = 0xFF242424, MUTED = 0xFF6A6A6A;
    private static final List<Entry> ENTRIES = new ArrayList<>();

    public static synchronized void register(Entry entry) {
        if (ENTRIES.stream().anyMatch(e -> e.id().equals(entry.id()))) throw new IllegalArgumentException("Duplicate X menu entry " + entry.id());
        ENTRIES.add(entry);
    }
    public static synchronized List<Entry> entries() { return Collections.unmodifiableList(new ArrayList<>(ENTRIES)); }
    private QuickMenu() {}
}
