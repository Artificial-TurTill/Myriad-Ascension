package io.github.artificialturtill.foundation.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.artificialturtill.foundation.api.FoundationApi;
import io.github.artificialturtill.foundation.character.CharacterService;
import io.github.artificialturtill.foundation.character.Fighter;
import io.github.artificialturtill.foundation.character.VanillaBridge;
import io.github.artificialturtill.foundation.content.ContentSet;
import java.util.function.BiConsumer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

/**
 * /foundation: operator tools for testing (Dragonblock C+ /dbcplus): content overview, character info, TP,
 * attributes, skills, mastery, release, alignment and erase.
 */
public final class FoundationCommands {
    public static void register(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> d = event.getDispatcher();
        d.register(Commands.literal("foundation").requires(s -> s.hasPermission(2))
            .then(Commands.literal("content").executes(FoundationCommands::content))
            .then(Commands.literal("info").then(Commands.argument("player", EntityArgument.player()).executes(FoundationCommands::info)))
            .then(Commands.literal("erase").then(Commands.argument("player", EntityArgument.player()).executes(c -> {
                ServerPlayer p = EntityArgument.getPlayer(c, "player");
                CharacterService.erase(p);
                c.getSource().sendSuccess(() -> Component.literal("Erased the character of " + p.getScoreboardName() + "."), true);
                return 1;
            })))
            .then(Commands.literal("tp").then(Commands.argument("player", EntityArgument.player())
                .then(Commands.argument("amount", IntegerArgumentType.integer(0)).executes(c ->
                    edit(c, (f, s) -> f.addTp(IntegerArgumentType.getInteger(c, "amount")), "TP added.")))))
            .then(Commands.literal("attribute").then(Commands.argument("player", EntityArgument.player())
                .then(Commands.argument("index", IntegerArgumentType.integer(0)).then(Commands.argument("value", IntegerArgumentType.integer(1)).executes(c ->
                    edit(c, (f, s) -> f.setAttribute(IntegerArgumentType.getInteger(c, "index"), IntegerArgumentType.getInteger(c, "value")), "Attribute set."))))))
            .then(Commands.literal("skill").then(Commands.argument("player", EntityArgument.player())
                .then(Commands.argument("skill", ResourceLocationArgument.id()).suggests((c, b) -> SharedSuggestionProvider.suggest(FoundationApi.content().skills().keySet(), b))
                    .then(Commands.argument("level", IntegerArgumentType.integer(0)).executes(c ->
                        edit(c, (f, s) -> f.setSkill(ResourceLocationArgument.getId(c, "skill").toString(), IntegerArgumentType.getInteger(c, "level")), "Skill set."))))))
            .then(Commands.literal("mastery").then(Commands.argument("player", EntityArgument.player())
                .then(Commands.argument("form", ResourceLocationArgument.id()).suggests((c, b) -> SharedSuggestionProvider.suggest(FoundationApi.content().transformations().keySet(), b))
                    .then(Commands.argument("value", DoubleArgumentType.doubleArg(0)).executes(c ->
                        edit(c, (f, s) -> f.setMastery(ResourceLocationArgument.getId(c, "form").toString(), DoubleArgumentType.getDouble(c, "value")), "Mastery set."))))))
            .then(Commands.literal("release").then(Commands.argument("player", EntityArgument.player())
                .then(Commands.argument("value", IntegerArgumentType.integer(0, 100)).executes(c ->
                    edit(c, (f, s) -> f.setRelease(IntegerArgumentType.getInteger(c, "value") / 5 * 5), "Release set.")))))
            .then(Commands.literal("alignment").then(Commands.argument("player", EntityArgument.player())
                .then(Commands.argument("value", IntegerArgumentType.integer(0, 100)).executes(c ->
                    edit(c, (f, s) -> f.setAlignment(IntegerArgumentType.getInteger(c, "value")), "Alignment set.")))))
            .then(Commands.literal("restore").then(Commands.argument("player", EntityArgument.player()).executes(c ->
                edit(c, (f, s) -> f.restoreResources(), "Resources restored.")))));
    }

    private static int edit(CommandContext<CommandSourceStack> c, BiConsumer<Fighter, CommandSourceStack> change, String done) throws CommandSyntaxException {
        ServerPlayer player = EntityArgument.getPlayer(c, "player");
        Fighter f = CharacterService.fighter(player);
        if (f == null) { c.getSource().sendFailure(Component.literal(player.getScoreboardName() + " has no character.")); return 0; }
        try {
            change.accept(f, c.getSource());
        } catch (IllegalArgumentException e) {
            c.getSource().sendFailure(Component.literal(e.getMessage()));
            return 0;
        }
        VanillaBridge.apply(player);
        CharacterService.sync(player, true);
        c.getSource().sendSuccess(() -> Component.literal(done), true);
        return 1;
    }

    private static int content(CommandContext<CommandSourceStack> c) {
        ContentSet set = FoundationApi.content();
        c.getSource().sendSuccess(() -> Component.literal("Foundation content: " + set.races().size() + " races " + set.races().keySet() + ", "
            + set.powerTypes().size() + " power types " + set.powerTypes().keySet() + ", " + set.classes().size() + " classes, "
            + set.skills().size() + " skills, " + set.transformations().size() + " transformations."), false);
        return 1;
    }

    private static int info(CommandContext<CommandSourceStack> c) throws CommandSyntaxException {
        ServerPlayer player = EntityArgument.getPlayer(c, "player");
        Fighter f = CharacterService.fighter(player);
        if (f == null) {
            boolean dormant = CharacterService.profile(player).dormant();
            c.getSource().sendSuccess(() -> Component.literal(player.getScoreboardName() + ": " + (dormant ? "dormant character (add-on missing)" : "no character (Natural, vanilla)")), false);
            return 1;
        }
        var caps = f.caps();
        c.getSource().sendSuccess(() -> Component.literal(player.getScoreboardName() + ": " + f.race().id() + " / " + f.powerType().id() + " / "
            + f.characterClass().id() + ", level " + f.level() + ", TP " + f.tp() + ", attributes " + java.util.Arrays.toString(f.attributes())
            + ", body " + f.body() + "/" + caps.body() + ", energy " + f.energy() + "/" + caps.energy() + ", stamina " + f.stamina() + "/" + caps.stamina()
            + ", release " + f.release() + "%, form " + (f.form().isEmpty() ? "base" : f.form()) + ", skills " + f.skills()), false);
        return 1;
    }
    private FoundationCommands() {}
}
