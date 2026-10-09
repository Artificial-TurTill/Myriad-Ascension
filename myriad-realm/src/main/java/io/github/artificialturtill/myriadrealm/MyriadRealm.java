package io.github.artificialturtill.myriadrealm;

import com.mojang.logging.LogUtils;
import io.github.artificialturtill.foundation.api.FoundationApi;
import java.util.List;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

/**
 * Myriad Realm: the cultivation add-on of the Ascension Foundation. Everything Myriad Realm adds to the character
 * system is content for the Foundation (ARSN files in data/myriad_realm/arsn): the Qi power type and its classes,
 * Qi skills and transformations. Myriad Realm-only systems (cultivation realms, techniques, sects, world Qi, ...) will
 * live in this mod and talk to the Foundation through its API.
 */
@Mod(MyriadRealm.MOD_ID)
public final class MyriadRealm {
    public static final String MOD_ID = "myriad_realm";
    public static final String QI = MOD_ID + ":qi";
    /** Content files, loaded by the Foundation in this order. New idea files are added here. */
    public static final List<String> CONTENT = List.of("qi", "skills", "transformations");
    public static final Logger LOGGER = LogUtils.getLogger();

    public MyriadRealm(IEventBus bus, ModContainer container) {
        for (String file : CONTENT) FoundationApi.addContent(MyriadRealm.class, "/data/myriad_realm/arsn/" + file + ".arsn");
        LOGGER.info("Myriad Realm registered {} content files with the Ascension Foundation.", CONTENT.size());
    }
}
