package io.github.artificialturtill.myriadascension.alpha;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.artificialturtill.myriadascension.bloodline.BloodlineAcquisitionType;
import io.github.artificialturtill.myriadascension.bloodline.BloodlineRules;
import io.github.artificialturtill.myriadascension.bloodline.BloodlineSource;
import io.github.artificialturtill.myriadascension.bloodline.PrimordialTortoiseBloodline;
import io.github.artificialturtill.myriadascension.clan.PrimordialisTestudoAdmissionRules;
import io.github.artificialturtill.myriadascension.clan.PrimordialisTestudoClan;
import io.github.artificialturtill.myriadascension.clan.PrimordialisTestudoLibrary;
import io.github.artificialturtill.myriadascension.clan.PrimordialisTestudoLibraryService;
import io.github.artificialturtill.myriadascension.clan.PrimordialisTestudoQuestRewards;
import io.github.artificialturtill.myriadascension.clan.PrimordialisTestudoRank;
import io.github.artificialturtill.myriadascension.cultivation.data.CultivatorData;
import io.github.artificialturtill.myriadascension.cultivation.data.ModAttachments;
import io.github.artificialturtill.myriadascension.cultivation.qi.QiRules;
import io.github.artificialturtill.myriadascension.cultivation.realm.CultivationRealm;
import io.github.artificialturtill.myriadascension.inheritance.CultivationEntryRules;
import io.github.artificialturtill.myriadascension.inheritance.InheritanceAcquisitionRules;
import io.github.artificialturtill.myriadascension.network.ModNetworking;
import io.github.artificialturtill.myriadascension.organization.OrganizationStanding;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

public final class AlphaCommands {
    private AlphaCommands() {
    }

    public static void register(RegisterCommandsEvent event) {
        event.getDispatcher().register(
                Commands.literal("myriadalpha")
                        .then(Commands.literal("status")
                                .executes(context -> status(context.getSource())))
                        .then(Commands.literal("join_testudo")
                                .executes(context -> joinTestudo(context.getSource())))
                        .then(Commands.literal("tempered")
                                .then(Commands.literal("stage1")
                                        .executes(context -> setTemperedStage(context.getSource(), 1)))
                                .then(Commands.literal("stage4")
                                        .executes(context -> setTemperedStage(context.getSource(), 4)))
                                .then(Commands.literal("stage7")
                                        .executes(context -> setTemperedStage(context.getSource(), 7)))
                                .then(Commands.literal("stage9")
                                        .executes(context -> setTemperedStage(context.getSource(), 9))))
                        .then(Commands.literal("initial_element")
                                .executes(context -> enterInitialElement(context.getSource())))
                        .then(Commands.literal("grant_testudo_bloodline")
                                .executes(context -> grantTestudoBloodline(context.getSource())))
                        .then(Commands.literal("authorize_fundamentals")
                                .executes(context -> authorizeFundamentals(context.getSource()))));
    }

    private static int status(CommandSourceStack source) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        CultivatorData data = player.getData(ModAttachments.CULTIVATOR_DATA);

        String sealed = data.realm() == CultivationRealm.TEMPERED_BODY
                        && data.minorStage() >= 7
                ? " (sealed/passive)"
                : "";

        source.sendSuccess(() -> Component.literal(
                "[Myriad Ascension Alpha] "
                        + data.realm().displayName()
                        + (data.realm().hasSubdivisions()
                                ? " " + data.realm().subdivisionType().displayName()
                                        + " " + data.minorStage()
                                : "")
                        + " | Qi " + oneDecimal(data.currentQi())
                        + "/" + oneDecimal(data.maximumQi()) + sealed
                        + " | Method "
                        + (data.cultivationMethods().activeMethodId().isBlank()
                                ? "None"
                                : data.cultivationMethods().activeMethodId())),
                false);

        return Command.SINGLE_SUCCESS;
    }

    private static int joinTestudo(CommandSourceStack source) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        CultivatorData data = player.getData(ModAttachments.CULTIVATOR_DATA);

        if (!data.hasCompletedInitialSetup()) {
            source.sendFailure(Component.literal(
                    "Complete Mortal Genesis before using the alpha Testudo shortcut."));
            return 0;
        }

        OrganizationStanding standing = data.organizationStandings()
                .getOrCreate(PrimordialisTestudoQuestRewards.ORGANIZATION_ID);
        standing.setMember(true);
        standing.setRank(PrimordialisTestudoAdmissionRules.INITIAL_MEMBER_RANK);

        InheritanceAcquisitionRules.grantMethod(
                data,
                PrimordialisTestudoClan.PRIMORDIAL_TESTUDO_LONGEVITY_ART,
                true);

        if (data.realm() == CultivationRealm.MORTAL) {
            CultivationEntryRules.beginTemperedBody(
                    data,
                    PrimordialisTestudoClan.PRIMORDIAL_TESTUDO_LONGEVITY_ART);
        }

        data.setMaximumQi(QiRules.temperedBodyPassiveCapacity(data.minorStage()));
        data.setCurrentQi(Math.min(data.currentQi(), data.maximumQi()));
        data.setCirculationPercent(0.0D);
        data.setBurstMode(false);

        ModNetworking.syncPlayer(player, data);
        source.sendSuccess(() -> Component.literal(
                "[Alpha] Joined the Primordialis Testudo Clan as a Junior Disciple "
                        + "and activated the Primordialis Testudo Longevity Art."),
                false);
        return Command.SINGLE_SUCCESS;
    }

    private static int setTemperedStage(CommandSourceStack source, int stage)
            throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        CultivatorData data = player.getData(ModAttachments.CULTIVATOR_DATA);

        if (!data.cultivationMethods().isActive(
                PrimordialisTestudoClan.PRIMORDIAL_TESTUDO_LONGEVITY_ART.id())) {
            source.sendFailure(Component.literal(
                    "Activate the Testudo inheritance first with /myriadalpha join_testudo."));
            return 0;
        }

        data.setRealm(CultivationRealm.TEMPERED_BODY);
        data.setMinorStage(stage);
        data.setMaximumQi(QiRules.temperedBodyPassiveCapacity(stage));
        data.setCurrentQi(0.0D);
        data.setCirculationPercent(0.0D);
        data.setBurstMode(false);

        ModNetworking.syncPlayer(player, data);

        String note = stage >= 7
                ? " A sealed Yuan Qi reserve is now visible and will refill naturally."
                : stage >= 4
                        ? " G acts only as World Energy focus while training."
                        : " This is physical tempering; G is not required.";

        source.sendSuccess(() -> Component.literal(
                "[Alpha] Tempered Body Stage " + stage + " selected for testing." + note),
                false);
        return Command.SINGLE_SUCCESS;
    }

    private static int enterInitialElement(CommandSourceStack source)
            throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        CultivatorData data = player.getData(ModAttachments.CULTIVATOR_DATA);

        if (!data.cultivationMethods().isActive(
                PrimordialisTestudoClan.PRIMORDIAL_TESTUDO_LONGEVITY_ART.id())) {
            source.sendFailure(Component.literal(
                    "Activate the Testudo inheritance first with /myriadalpha join_testudo."));
            return 0;
        }

        data.setRealm(CultivationRealm.INITIAL_ELEMENT);
        data.setMinorStage(1);
        data.setMaximumQi(100.0D);
        data.setCurrentQi(100.0D);
        data.setCirculationPercent(10.0D);
        data.setBurstMode(false);

        ModNetworking.syncPlayer(player, data);
        source.sendSuccess(() -> Component.literal(
                "[Alpha] Entered Initial Element Stage 1 with a temporary 100 Qi test reserve. "
                        + "Conscious G/H/R circulation controls are now available."),
                false);
        return Command.SINGLE_SUCCESS;
    }

    private static int grantTestudoBloodline(CommandSourceStack source)
            throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        CultivatorData data = player.getData(ModAttachments.CULTIVATOR_DATA);

        var result = BloodlineRules.apply(
                data.bloodline(),
                PrimordialTortoiseBloodline.LINEAGE,
                PrimordialTortoiseBloodline.EARTH_RANK,
                new BloodlineSource(
                        BloodlineAcquisitionType.QUEST,
                        CultivationRealm.SAINT,
                        3,
                        100.0D));

        ModNetworking.syncPlayer(player, data);
        source.sendSuccess(() -> Component.literal(
                "[Alpha] Primordial Tortoise bloodline test result: " + result.name()),
                false);
        return Command.SINGLE_SUCCESS;
    }

    private static int authorizeFundamentals(CommandSourceStack source)
            throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        CultivatorData data = player.getData(ModAttachments.CULTIVATOR_DATA);

        var result = PrimordialisTestudoLibraryService.authorizeTakeout(
                data,
                PrimordialisTestudoRank.CUSTODIAN,
                PrimordialisTestudoLibrary.CULTIVATION_FUNDAMENTALS);

        ModNetworking.syncPlayer(player, data);
        source.sendSuccess(() -> Component.literal(
                "[Alpha] Library authorization result: " + result.name()),
                false);
        return result.name().equals("AUTHORIZED") ? Command.SINGLE_SUCCESS : 0;
    }

    private static String oneDecimal(double value) {
        return String.format(java.util.Locale.ROOT, "%.1f", value);
    }
}
