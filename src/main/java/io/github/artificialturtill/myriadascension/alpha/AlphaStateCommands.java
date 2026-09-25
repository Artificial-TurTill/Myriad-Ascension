package io.github.artificialturtill.myriadascension.alpha;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.artificialturtill.myriadascension.affinity.AffinityType;
import io.github.artificialturtill.myriadascension.alignment.CultivationAffiliation;
import io.github.artificialturtill.myriadascension.character.CharacterSex;
import io.github.artificialturtill.myriadascension.cultivation.data.CultivatorData;
import io.github.artificialturtill.myriadascension.cultivation.data.ModAttachments;
import io.github.artificialturtill.myriadascension.cultivation.realm.CultivationRealm;
import io.github.artificialturtill.myriadascension.network.ModNetworking;
import io.github.artificialturtill.myriadascension.organization.OrganizationStanding;
import io.github.artificialturtill.myriadascension.stats.CultivatorStat;
import io.github.artificialturtill.myriadascension.technique.TechniqueCategory;
import io.github.artificialturtill.myriadascension.training.BodyTemperingVector;
import io.github.artificialturtill.myriadascension.training.TrainingActivity;
import java.util.Locale;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public final class AlphaStateCommands {
    private AlphaStateCommands() {
    }

    public static LiteralArgumentBuilder<CommandSourceStack> build() {
        return Commands.literal("edit")
                .then(Commands.literal("realm")
                        .then(Commands.argument("realm", StringArgumentType.word())
                                .executes(context -> setRealm(context, null))
                                .then(Commands.argument("stage", IntegerArgumentType.integer(0))
                                        .executes(context -> setRealm(
                                                context,
                                                IntegerArgumentType.getInteger(context, "stage"))))))
                .then(Commands.literal("sex")
                        .then(Commands.argument("value", StringArgumentType.word())
                                .executes(AlphaStateCommands::setSex)))
                .then(Commands.literal("affiliation")
                        .then(Commands.argument("value", StringArgumentType.word())
                                .executes(AlphaStateCommands::setAffiliation)))
                .then(Commands.literal("moral_alignment")
                        .then(Commands.argument("value", DoubleArgumentType.doubleArg(-100.0D, 100.0D))
                                .executes(context -> setScalar(
                                        context,
                                        "moral_alignment",
                                        DoubleArgumentType.getDouble(context, "value")))))
                .then(Commands.literal("karma")
                        .then(Commands.argument("value", DoubleArgumentType.doubleArg())
                                .executes(context -> setScalar(
                                        context,
                                        "karma",
                                        DoubleArgumentType.getDouble(context, "value")))))
                .then(Commands.literal("stat")
                        .then(Commands.argument("stat", StringArgumentType.word())
                                .then(Commands.argument("value", DoubleArgumentType.doubleArg(1.0D))
                                        .executes(AlphaStateCommands::setStat))))
                .then(Commands.literal("affinity")
                        .then(Commands.argument("affinity", StringArgumentType.word())
                                .then(Commands.argument("value", IntegerArgumentType.integer(0))
                                        .executes(context -> setAffinity(context, false)))))
                .then(Commands.literal("innate_affinity")
                        .then(Commands.argument("affinity", StringArgumentType.word())
                                .then(Commands.argument("value", IntegerArgumentType.integer(0))
                                        .executes(context -> setAffinity(context, true)))))
                .then(Commands.literal("skill")
                        .then(Commands.argument("skill", StringArgumentType.word())
                                .then(Commands.argument("level", IntegerArgumentType.integer(0, 10))
                                        .executes(AlphaStateCommands::setSkill))))
                .then(Commands.literal("qi")
                        .then(Commands.literal("current")
                                .then(Commands.argument("value", DoubleArgumentType.doubleArg(0.0D))
                                        .executes(context -> setScalar(
                                                context,
                                                "current_qi",
                                                DoubleArgumentType.getDouble(context, "value")))))
                        .then(Commands.literal("maximum")
                                .then(Commands.argument("value", DoubleArgumentType.doubleArg(0.0D))
                                        .executes(context -> setScalar(
                                                context,
                                                "maximum_qi",
                                                DoubleArgumentType.getDouble(context, "value")))))
                        .then(Commands.literal("circulation")
                                .then(Commands.argument("value", DoubleArgumentType.doubleArg(0.0D, 100.0D))
                                        .executes(context -> setScalar(
                                                context,
                                                "circulation",
                                                DoubleArgumentType.getDouble(context, "value")))))
                        .then(Commands.literal("burst")
                                .then(Commands.argument("value", BoolArgumentType.bool())
                                        .executes(AlphaStateCommands::setBurst))))
                .then(Commands.literal("progress")
                        .then(Commands.literal("cultivation")
                                .then(Commands.argument("value", DoubleArgumentType.doubleArg(0.0D))
                                        .executes(context -> setScalar(
                                                context,
                                                "cultivation_progress",
                                                DoubleArgumentType.getDouble(context, "value")))))
                        .then(Commands.literal("comprehension")
                                .then(Commands.argument("value", DoubleArgumentType.doubleArg(0.0D))
                                        .executes(context -> setScalar(
                                                context,
                                                "cultivation_comprehension",
                                                DoubleArgumentType.getDouble(context, "value")))))
                        .then(Commands.literal("battle")
                                .then(Commands.argument("value", DoubleArgumentType.doubleArg(0.0D))
                                        .executes(context -> setScalar(
                                                context,
                                                "battle_comprehension",
                                                DoubleArgumentType.getDouble(context, "value"))))))
                .then(Commands.literal("condition")
                        .then(Commands.argument("field", StringArgumentType.word())
                                .then(Commands.argument("value", DoubleArgumentType.doubleArg(0.0D))
                                        .executes(AlphaStateCommands::setCondition))))
                .then(Commands.literal("bloodline")
                        .then(Commands.literal("purity")
                                .then(Commands.argument("value", DoubleArgumentType.doubleArg(0.0D, 100.0D))
                                        .executes(AlphaStateCommands::setBloodlinePurity)))
                        .then(Commands.literal("conflict")
                                .then(Commands.argument("value", DoubleArgumentType.doubleArg(0.0D))
                                        .executes(AlphaStateCommands::setBloodlineConflict))))
                .then(Commands.literal("training")
                        .then(Commands.literal("vector")
                                .then(Commands.argument("vector", StringArgumentType.word())
                                        .then(Commands.argument("value", DoubleArgumentType.doubleArg(0.0D))
                                                .executes(AlphaStateCommands::setTrainingVector))))
                        .then(Commands.literal("adaptation")
                                .then(Commands.argument("activity", StringArgumentType.word())
                                        .then(Commands.argument("value", DoubleArgumentType.doubleArg(0.0D))
                                                .executes(AlphaStateCommands::setTrainingAdaptation))))
                        .then(Commands.literal("status")
                                .executes(AlphaStateCommands::trainingStatus)))
                .then(Commands.literal("technique")
                        .then(Commands.literal("learn")
                                .then(Commands.argument("id", StringArgumentType.greedyString())
                                        .executes(context -> techniqueKnowledge(context, true))))
                        .then(Commands.literal("forget")
                                .then(Commands.argument("id", StringArgumentType.greedyString())
                                        .executes(context -> techniqueKnowledge(context, false))))
                        .then(Commands.literal("equip")
                                .then(Commands.argument("category", StringArgumentType.word())
                                        .then(Commands.argument("id", StringArgumentType.greedyString())
                                                .executes(AlphaStateCommands::equipTechnique))))
                        .then(Commands.literal("clear")
                                .then(Commands.argument("category", StringArgumentType.word())
                                        .executes(AlphaStateCommands::clearTechnique))))
                .then(Commands.literal("method")
                        .then(Commands.literal("learn")
                                .then(Commands.argument("id", StringArgumentType.greedyString())
                                        .executes(context -> methodKnowledge(context, "learn"))))
                        .then(Commands.literal("forget")
                                .then(Commands.argument("id", StringArgumentType.greedyString())
                                        .executes(context -> methodKnowledge(context, "forget"))))
                        .then(Commands.literal("activate")
                                .then(Commands.argument("id", StringArgumentType.greedyString())
                                        .executes(context -> methodKnowledge(context, "activate"))))
                        .then(Commands.literal("clear")
                                .executes(context -> methodKnowledge(context, "clear"))))
                .then(Commands.literal("organization")
                        .then(Commands.argument("id", StringArgumentType.greedyString())
                                .then(Commands.literal("member")
                                        .then(Commands.argument("value", BoolArgumentType.bool())
                                                .executes(AlphaStateCommands::setOrganizationMember)))
                                .then(Commands.literal("rank")
                                        .then(Commands.argument("value", StringArgumentType.word())
                                                .executes(AlphaStateCommands::setOrganizationRank)))
                                .then(Commands.literal("merit")
                                        .then(Commands.argument("value", IntegerArgumentType.integer(0))
                                                .executes(AlphaStateCommands::setOrganizationMerit)))));
    }

    private static int setRealm(CommandContext<CommandSourceStack> context, Integer explicitStage)
            throws CommandSyntaxException {
        CultivatorData data = data(context);
        String value = StringArgumentType.getString(context, "realm");
        CultivationRealm realm = enumValue(CultivationRealm.class, value);
        if (realm == null) {
            return fail(context, "Unknown realm: " + value);
        }

        data.setRealm(realm);
        if (explicitStage != null && realm.hasSubdivisions()) {
            data.setMinorStage(explicitStage);
        }
        if (realm == CultivationRealm.MORTAL) {
            data.setMaximumQi(0.0D);
            data.setCurrentQi(0.0D);
            data.setCirculationPercent(0.0D);
            data.setBurstMode(false);
        }
        return changed(context, "Realm = " + realm.displayName()
                + (realm.hasSubdivisions() ? " " + data.minorStage() : ""));
    }

    private static int setSex(CommandContext<CommandSourceStack> context)
            throws CommandSyntaxException {
        String value = StringArgumentType.getString(context, "value");
        CharacterSex sex = enumValue(CharacterSex.class, value);
        if (sex == null) {
            return fail(context, "Unknown sex: " + value);
        }
        data(context).setCharacterSex(sex);
        return changed(context, "Sex = " + sex.name());
    }

    private static int setAffiliation(CommandContext<CommandSourceStack> context)
            throws CommandSyntaxException {
        String value = StringArgumentType.getString(context, "value");
        CultivationAffiliation affiliation = enumValue(CultivationAffiliation.class, value);
        if (affiliation == null) {
            return fail(context, "Unknown affiliation: " + value);
        }
        data(context).setAffiliation(affiliation);
        return changed(context, "Affiliation = " + affiliation.name());
    }

    private static int setScalar(
            CommandContext<CommandSourceStack> context,
            String field,
            double value) throws CommandSyntaxException {

        CultivatorData data = data(context);
        switch (field) {
            case "moral_alignment" -> data.setMoralAlignment(value);
            case "karma" -> data.setKarma(value);
            case "current_qi" -> data.setCurrentQi(value);
            case "maximum_qi" -> data.setMaximumQi(value);
            case "circulation" -> data.setCirculationPercent(value);
            case "cultivation_progress" -> data.setCultivationProgress(value);
            case "cultivation_comprehension" -> data.setCultivationComprehension(value);
            case "battle_comprehension" -> data.setBattleComprehension(value);
            default -> {
                return fail(context, "Unknown scalar field: " + field);
            }
        }
        return changed(context, field + " = " + value);
    }

    private static int setBurst(CommandContext<CommandSourceStack> context)
            throws CommandSyntaxException {
        boolean value = BoolArgumentType.getBool(context, "value");
        data(context).setBurstMode(value);
        return changed(context, "Burst = " + value);
    }

    private static int setStat(CommandContext<CommandSourceStack> context)
            throws CommandSyntaxException {
        String name = StringArgumentType.getString(context, "stat");
        CultivatorStat stat = enumValue(CultivatorStat.class, name);
        if (stat == null) {
            return fail(context, "Unknown stat: " + name);
        }

        double value = DoubleArgumentType.getDouble(context, "value");
        data(context).stats().set(stat, value);
        return changed(context, stat.displayName() + " = " + value);
    }

    private static int setAffinity(
            CommandContext<CommandSourceStack> context,
            boolean innate) throws CommandSyntaxException {

        String name = StringArgumentType.getString(context, "affinity");
        AffinityType type = enumValue(AffinityType.class, name);
        if (type == null) {
            return fail(context, "Unknown affinity: " + name);
        }

        int value = IntegerArgumentType.getInteger(context, "value");
        CultivatorData data = data(context);
        if (innate) {
            data.innateAffinities().set(type, value);
        } else {
            data.setAffinityForTesting(type, value);
        }
        return changed(context, (innate ? "Innate " : "Attunement ")
                + type.name() + " = " + value);
    }

    private static int setSkill(CommandContext<CommandSourceStack> context)
            throws CommandSyntaxException {
        String skill = normalize(StringArgumentType.getString(context, "skill"));
        int level = IntegerArgumentType.getInteger(context, "level");
        CultivatorData data = data(context);

        switch (skill) {
            case "passive_qi_recharging", "passive_recharge" ->
                    data.setPassiveQiRechargingLevel(level);
            case "meditation" -> data.setMeditationLevel(level);
            case "qi_concealment", "concealment" -> data.setQiConcealmentLevel(level);
            default -> {
                return fail(context, "Unknown skill: " + skill);
            }
        }

        return changed(context, skill + " = " + level);
    }

    private static int setCondition(CommandContext<CommandSourceStack> context)
            throws CommandSyntaxException {

        String field = normalize(StringArgumentType.getString(context, "field"));
        double value = DoubleArgumentType.getDouble(context, "value");
        CultivatorData data = data(context);

        switch (field) {
            case "meridian_load" -> data.setMeridianLoad(value);
            case "body_injury" -> data.setBodyInjury(value);
            case "meridian_injury" -> data.setMeridianInjury(value);
            case "soul_injury" -> data.setSoulInjury(value);
            case "recovery_debt" -> data.setRecoveryDebt(value);
            case "vessel_purity" -> data.setVesselPurity(value);
            case "impurities", "impurity_load" -> data.setImpurityLoad(value);
            case "demonic_qi", "demonic_qi_contamination" ->
                    data.setDemonicQiContamination(value);
            case "training_fatigue" -> data.setTrainingFatigue(value);
            default -> {
                return fail(context, "Unknown condition field: " + field);
            }
        }

        return changed(context, field + " = " + value);
    }

    private static int setBloodlinePurity(CommandContext<CommandSourceStack> context)
            throws CommandSyntaxException {
        double value = DoubleArgumentType.getDouble(context, "value");
        data(context).bloodline().setPurityForTesting(value);
        return changed(context, "Bloodline purity = " + value);
    }

    private static int setBloodlineConflict(CommandContext<CommandSourceStack> context)
            throws CommandSyntaxException {
        double value = DoubleArgumentType.getDouble(context, "value");
        data(context).bloodline().setConflictDamageForTesting(value);
        return changed(context, "Bloodline conflict = " + value);
    }

    private static int setTrainingVector(CommandContext<CommandSourceStack> context)
            throws CommandSyntaxException {
        String name = StringArgumentType.getString(context, "vector");
        BodyTemperingVector vector = enumValue(BodyTemperingVector.class, name);
        if (vector == null) {
            return fail(context, "Unknown Body Tempering vector: " + name);
        }
        double value = DoubleArgumentType.getDouble(context, "value");
        data(context).bodyTempering().setDevelopmentForTesting(vector, value);
        return changed(context, vector.displayName() + " development = " + value);
    }

    private static int setTrainingAdaptation(CommandContext<CommandSourceStack> context)
            throws CommandSyntaxException {
        String name = StringArgumentType.getString(context, "activity");
        TrainingActivity activity = enumValue(TrainingActivity.class, name);
        if (activity == null) {
            return fail(context, "Unknown training activity: " + name);
        }
        double value = DoubleArgumentType.getDouble(context, "value");
        data(context).bodyTempering().setAdaptationForTesting(activity, value);
        return changed(context, activity.displayName() + " adaptation = " + value);
    }

    private static int trainingStatus(CommandContext<CommandSourceStack> context)
            throws CommandSyntaxException {
        CultivatorData data = data(context);
        StringBuilder text = new StringBuilder("[Alpha training]");
        for (BodyTemperingVector vector : BodyTemperingVector.values()) {
            text.append(" ")
                    .append(vector.name().toLowerCase(Locale.ROOT))
                    .append("=")
                    .append(String.format(Locale.ROOT, "%.2f", data.bodyTempering().development(vector)));
        }
        context.getSource().sendSuccess(() -> Component.literal(text.toString()), false);
        return Command.SINGLE_SUCCESS;
    }

    private static int techniqueKnowledge(
            CommandContext<CommandSourceStack> context,
            boolean learn) throws CommandSyntaxException {

        ResourceLocation id = resourceId(context, "id");
        if (id == null) {
            return fail(context, "Invalid technique id.");
        }

        CultivatorData data = data(context);
        boolean changed = learn
                ? data.techniqueKnowledge().learn(id)
                : data.techniqueKnowledge().forget(id);
        sync(context);
        context.getSource().sendSuccess(
                () -> Component.literal("[Alpha edit] Technique " + id
                        + (changed ? (learn ? " learned." : " forgotten.") : " unchanged.")),
                false);
        return Command.SINGLE_SUCCESS;
    }

    private static int equipTechnique(CommandContext<CommandSourceStack> context)
            throws CommandSyntaxException {
        String categoryName = StringArgumentType.getString(context, "category");
        TechniqueCategory category = enumValue(TechniqueCategory.class, categoryName);
        if (category == null) {
            return fail(context, "Unknown technique category: " + categoryName);
        }

        ResourceLocation id = resourceId(context, "id");
        if (id == null) {
            return fail(context, "Invalid technique id.");
        }

        CultivatorData data = data(context);
        data.techniqueKnowledge().learn(id);
        data.techniqueLoadout().equip(category, id);
        return changed(context, category.displayName() + " technique = " + id);
    }

    private static int clearTechnique(CommandContext<CommandSourceStack> context)
            throws CommandSyntaxException {
        String categoryName = StringArgumentType.getString(context, "category");
        TechniqueCategory category = enumValue(TechniqueCategory.class, categoryName);
        if (category == null) {
            return fail(context, "Unknown technique category: " + categoryName);
        }
        data(context).techniqueLoadout().clear(category);
        return changed(context, category.displayName() + " technique cleared");
    }

    private static int methodKnowledge(
            CommandContext<CommandSourceStack> context,
            String operation) throws CommandSyntaxException {

        CultivatorData data = data(context);
        if ("clear".equals(operation)) {
            data.cultivationMethods().clearActive();
            return changed(context, "Active cultivation method cleared");
        }

        ResourceLocation id = resourceId(context, "id");
        if (id == null) {
            return fail(context, "Invalid cultivation method id.");
        }

        boolean result;
        switch (operation) {
            case "learn" -> result = data.cultivationMethods().learn(id);
            case "forget" -> result = data.cultivationMethods().forget(id);
            case "activate" -> {
                data.cultivationMethods().learn(id);
                result = data.cultivationMethods().setActive(id);
            }
            default -> {
                return fail(context, "Unknown method operation.");
            }
        }

        sync(context);
        context.getSource().sendSuccess(
                () -> Component.literal("[Alpha edit] Method " + id
                        + " operation " + operation + " -> " + result),
                false);
        return result ? Command.SINGLE_SUCCESS : 0;
    }

    private static int setOrganizationMember(CommandContext<CommandSourceStack> context)
            throws CommandSyntaxException {
        OrganizationStanding standing = organization(context);
        boolean value = BoolArgumentType.getBool(context, "value");
        standing.setMember(value);
        return changed(context, "Organization membership = " + value);
    }

    private static int setOrganizationRank(CommandContext<CommandSourceStack> context)
            throws CommandSyntaxException {
        OrganizationStanding standing = organization(context);
        String value = StringArgumentType.getString(context, "value");
        standing.setRank(value);
        return changed(context, "Organization rank = " + value);
    }

    private static int setOrganizationMerit(CommandContext<CommandSourceStack> context)
            throws CommandSyntaxException {
        OrganizationStanding standing = organization(context);
        int value = IntegerArgumentType.getInteger(context, "value");
        standing.setServiceMerit(value);
        return changed(context, "Organization merit = " + value);
    }

    private static OrganizationStanding organization(CommandContext<CommandSourceStack> context)
            throws CommandSyntaxException {
        String id = StringArgumentType.getString(context, "id");
        return data(context).organizationStandings().getOrCreate(id);
    }

    private static CultivatorData data(CommandContext<CommandSourceStack> context)
            throws CommandSyntaxException {
        return player(context).getData(ModAttachments.CULTIVATOR_DATA);
    }

    private static ServerPlayer player(CommandContext<CommandSourceStack> context)
            throws CommandSyntaxException {
        return context.getSource().getPlayerOrException();
    }

    private static int changed(
            CommandContext<CommandSourceStack> context,
            String description) throws CommandSyntaxException {
        sync(context);
        context.getSource().sendSuccess(
                () -> Component.literal("[Alpha edit] " + description),
                false);
        return Command.SINGLE_SUCCESS;
    }

    private static void sync(CommandContext<CommandSourceStack> context)
            throws CommandSyntaxException {
        ServerPlayer player = player(context);
        ModNetworking.syncPlayer(player, player.getData(ModAttachments.CULTIVATOR_DATA));
    }

    private static int fail(
            CommandContext<CommandSourceStack> context,
            String message) {
        context.getSource().sendFailure(Component.literal("[Alpha edit] " + message));
        return 0;
    }

    private static ResourceLocation resourceId(
            CommandContext<CommandSourceStack> context,
            String argument) {
        return ResourceLocation.tryParse(StringArgumentType.getString(context, argument));
    }

    private static <E extends Enum<E>> E enumValue(
            Class<E> type,
            String raw) {
        if (raw == null) {
            return null;
        }
        String normalized = normalize(raw).toUpperCase(Locale.ROOT);
        try {
            return Enum.valueOf(type, normalized);
        } catch (IllegalArgumentException ignored) {
            return null;
        }
    }

    private static String normalize(String value) {
        return value == null ? "" : value.trim().replace('-', '_');
    }
}
