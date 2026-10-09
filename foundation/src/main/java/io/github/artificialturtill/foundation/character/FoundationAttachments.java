package io.github.artificialturtill.foundation.character;

import io.github.artificialturtill.foundation.api.FoundationApi;
import io.github.artificialturtill.foundation.network.PublicLook;
import java.util.function.Supplier;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.attachment.IAttachmentSerializer;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public final class FoundationAttachments {
    public static final DeferredRegister<AttachmentType<?>> TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, FoundationApi.MOD_ID);

    /** The saved character, kept through death. */
    public static final Supplier<AttachmentType<CharacterProfile>> CHARACTER = TYPES.register("character", () ->
        AttachmentType.builder(CharacterProfile::new).serialize(new IAttachmentSerializer<CompoundTag, CharacterProfile>() {
            @Override public CharacterProfile read(IAttachmentHolder holder, CompoundTag tag, HolderLookup.Provider registries) {
                return CharacterProfile.load(tag);
            }
            @Override public CompoundTag write(CharacterProfile value, HolderLookup.Provider registries) { return value.save(); }
        }).copyOnDeath().build());
    /** Runtime input state (charge key held, lowering), never saved. */
    public static final Supplier<AttachmentType<CharacterRuntime>> RUNTIME = TYPES.register("runtime", () ->
        AttachmentType.builder(CharacterRuntime::new).build());
    /** What other clients need to draw the player (appearance, power type, form, aura); synced to every tracker. */
    public static final Supplier<AttachmentType<PublicLook>> LOOK = TYPES.register("look", () ->
        AttachmentType.builder(() -> PublicLook.NONE).sync(PublicLook.CODEC).build());
    private FoundationAttachments() {}
}
