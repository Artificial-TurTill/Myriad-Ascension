package io.github.artificialturtill.myriadascension.clan;

import io.github.artificialturtill.myriadascension.MyriadAscension;
import io.github.artificialturtill.myriadascension.quest.ServiceProviderType;
import io.github.artificialturtill.myriadascension.quest.ServiceQuestDefinition;
import io.github.artificialturtill.myriadascension.quest.ServiceQuestType;
import java.util.List;
import net.minecraft.resources.ResourceLocation;

public final class PrimordialisTestudoServiceQuests {
    public static final ServiceQuestDefinition REPAIR_CLAN_BUILDING =
            quest(
                    "repair_clan_building",
                    "Repair the Clan Compound",
                    ServiceQuestType.REPAIR_BUILDING,
                    ServiceProviderType.CLAN,
                    ServiceProviderType.CLAN,
                    2,
                    List.of("minecraft:planks", "minecraft:logs", "minecraft:stone_tool_materials"));

    public static final ServiceQuestDefinition FETCH_WEAPONS =
            quest(
                    "fetch_weapons",
                    "Collect the Clan's Weapons",
                    ServiceQuestType.FETCH_WEAPONS,
                    ServiceProviderType.BLACKSMITH,
                    ServiceProviderType.CLAN,
                    1,
                    List.of());

    public static final ServiceQuestDefinition FETCH_MEDICINE =
            quest(
                    "fetch_medicine",
                    "Collect Pills and Medicine",
                    ServiceQuestType.FETCH_MEDICINE,
                    ServiceProviderType.ALCHEMIST,
                    ServiceProviderType.CLAN,
                    1,
                    List.of());

    public static final ServiceQuestDefinition FETCH_BAKED_GOODS =
            quest(
                    "fetch_baked_goods",
                    "Bring Food from the Bakery",
                    ServiceQuestType.FETCH_BAKED_GOODS,
                    ServiceProviderType.BAKERY,
                    ServiceProviderType.CLAN,
                    1,
                    List.of());

    public static final ServiceQuestDefinition FETCH_NOODLE_SOUP =
            quest(
                    "fetch_noodle_soup",
                    "Bring Noodle Soup from the Inn",
                    ServiceQuestType.FETCH_NOODLE_SOUP,
                    ServiceProviderType.INN,
                    ServiceProviderType.CLAN,
                    1,
                    List.of());

    public static final List<ServiceQuestDefinition> ADMISSION_POOL = List.of(
            REPAIR_CLAN_BUILDING,
            FETCH_WEAPONS,
            FETCH_MEDICINE,
            FETCH_BAKED_GOODS,
            FETCH_NOODLE_SOUP);

    private PrimordialisTestudoServiceQuests() {
    }

    private static ServiceQuestDefinition quest(
            String path,
            String displayName,
            ServiceQuestType type,
            ServiceProviderType source,
            ServiceProviderType destination,
            int serviceMerit,
            List<String> requiredItemTags) {
        return new ServiceQuestDefinition(
                ResourceLocation.fromNamespaceAndPath(
                        MyriadAscension.MOD_ID,
                        "primordialis_testudo/" + path),
                displayName,
                type,
                source,
                destination,
                serviceMerit,
                requiredItemTags);
    }
}
