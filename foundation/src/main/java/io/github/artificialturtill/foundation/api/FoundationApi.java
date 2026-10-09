package io.github.artificialturtill.foundation.api;

import io.github.artificialturtill.foundation.content.ContentLoader;
import io.github.artificialturtill.foundation.content.ContentSet;
import java.util.ArrayList;
import java.util.List;

/**
 * The add-on entry point. An add-on mod (e.g. Myriad Realm) calls {@link #addContent} from its mod constructor for
 * each of its ARSN content files; the Foundation loads all of them together during common setup, checks every
 * cross reference, and from then on {@link #content()} holds every race, power type, class, skill and transformation.
 *
 * <pre>{@code
 * public MyAddon(IEventBus bus) {
 *     FoundationApi.addContent(MyAddon.class, "/data/my_addon/arsn/forms.arsn");
 * }
 * }</pre>
 */
public final class FoundationApi {
    public static final String MOD_ID = "ascension_foundation";
    /** Built-in ids. */
    public static final String NATURAL = MOD_ID + ":natural", HUMAN = MOD_ID + ":human", UNCLASSED = MOD_ID + ":unclassed";

    private static final List<ContentLoader.Source> SOURCES = new ArrayList<>();
    private static volatile ContentSet content = ContentSet.EMPTY;
    private static boolean loaded;

    /** Registers one content file shipped in the jar of {@code anchor}'s mod. Must be called before common setup. */
    public static synchronized void addContent(Class<?> anchor, String path) {
        if (loaded) throw new IllegalStateException("Content is already loaded; register content files in the mod constructor.");
        SOURCES.add(new ContentLoader.Source(anchor, path));
    }

    /** Everything that was loaded (empty before common setup). */
    public static ContentSet content() { return content; }

    /** Called once by the Foundation during common setup. A broken file stops the game with its error. */
    public static synchronized void loadAll() {
        List<ContentLoader.Source> all = new ArrayList<>();
        all.add(new ContentLoader.Source(FoundationApi.class, "/data/ascension_foundation/arsn/foundation.arsn"));
        all.addAll(SOURCES);
        content = ContentLoader.load(all);
        loaded = true;
    }
    /** Test hook: use a prepared content set. */
    public static void useContent(ContentSet set) { content = set; }
    private FoundationApi() {}
}
