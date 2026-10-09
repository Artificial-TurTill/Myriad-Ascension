package io.github.artificialturtill.foundation.character;

/** Per-player input state that is not saved (DBC+ AbilityRuntime). */
public final class CharacterRuntime {
    /** The charge key is held; {@code lowering}: with sneak, the release goes down instead. */
    public boolean charging, lowering;
    /** Last snapshot sent to the owner (only changes are sent). */
    public net.minecraft.nbt.CompoundTag lastSent;
}
