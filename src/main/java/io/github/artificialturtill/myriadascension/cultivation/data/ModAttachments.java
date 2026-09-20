package io.github.artificialturtill.myriadascension.cultivation.data;

import io.github.artificialturtill.myriadascension.MyriadAscension;
import java.util.function.Supplier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public final class ModAttachments {
    private static final DeferredRegister<AttachmentType<?>> ATTACHMENTS =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, MyriadAscension.MOD_ID);

    public static final Supplier<AttachmentType<CultivatorData>> CULTIVATOR_DATA =
            ATTACHMENTS.register("cultivator_data",
                    () -> AttachmentType.serializable(CultivatorData::new).build());

    private ModAttachments() {
    }

    public static void register(IEventBus modEventBus) {
        ATTACHMENTS.register(modEventBus);
    }
}
