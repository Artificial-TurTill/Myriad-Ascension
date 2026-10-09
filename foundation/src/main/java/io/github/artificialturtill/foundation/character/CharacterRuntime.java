package io.github.artificialturtill.foundation.character;

/** Per-player input state that is not saved (DBC+ AbilityRuntime). */
public final class CharacterRuntime {
    /** The charge key is held; {@code lowering}: with sneak, the release goes down instead. */
    public boolean charging, lowering;
    /** DBC Turbo (Myriad Realm: Qi Surge), status effect 3: on until toggled off, 0 % release, no energy, death. */
    public boolean turbo;
    /** Ticks since the last charge step (20, or 10 with Turbo) and since the last Turbo payment. */
    public int chargeTicks, turboTicks;
    /** Last snapshot sent to the owner (only changes are sent). */
    public net.minecraft.nbt.CompoundTag lastSent;

    public void stop() { charging = false; lowering = false; turbo = false; chargeTicks = 0; turboTicks = 0; }
}
