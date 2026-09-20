package io.github.artificialturtill.myriadascension.clan;

import io.github.artificialturtill.myriadascension.cultivation.data.CultivatorData;
import io.github.artificialturtill.myriadascension.inheritance.InheritanceAcquisitionRules;
import io.github.artificialturtill.myriadascension.organization.OrganizationStanding;
import io.github.artificialturtill.myriadascension.quest.ServiceQuestDefinition;

public final class PrimordialisTestudoQuestRewards {
    public static final String ORGANIZATION_ID = "myriad_ascension:primordialis_testudo_clan";

    private PrimordialisTestudoQuestRewards() {
    }

    public static boolean acceptServiceQuest(
            CultivatorData data,
            ServiceQuestDefinition quest) {
        return PrimordialisTestudoServiceQuests.ADMISSION_POOL.contains(quest)
                && data.questJournal().accept(quest.id());
    }

    public static boolean completeServiceQuest(
            CultivatorData data,
            ServiceQuestDefinition quest) {
        if (!PrimordialisTestudoServiceQuests.ADMISSION_POOL.contains(quest)
                || !data.questJournal().complete(quest.id())) {
            return false;
        }

        OrganizationStanding standing = data.organizationStandings().getOrCreate(ORGANIZATION_ID);
        standing.addServiceMerit(quest.serviceMerit());

        if (PrimordialisTestudoAdmissionRules.acceptAsJuniorDisciple(standing)) {
            InheritanceAcquisitionRules.grantMethod(
                    data,
                    PrimordialisTestudoClan.PRIMORDIAL_TESTUDO_LONGEVITY_ART,
                    true);
        }

        return true;
    }
}
