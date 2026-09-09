package fr.illuminaticraft.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import fr.illuminaticraft.config.IlluminatiCraftConfig;
import fr.illuminaticraft.items.ModItems;
import fr.illuminaticraft.util.RandomItemPicker;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Collection;
import java.util.List;

/**
 * Commandes d'administration : /illuminati ...
 */
public class IlluminatiCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher,
                                CommandRegistryAccess registryAccess,
                                CommandManager.RegistrationEnvironment environment) {

        dispatcher.register(CommandManager.literal("illuminati")
                .requires(source -> source.hasPermissionLevel(2))

                // /illuminati give [joueurs] [nombre]
                .then(CommandManager.literal("give")
                        .executes(ctx -> give(ctx, List.of(ctx.getSource().getPlayerOrThrow()), 1))
                        .then(CommandManager.argument("targets", EntityArgumentType.players())
                                .executes(ctx -> give(ctx, EntityArgumentType.getPlayers(ctx, "targets"), 1))
                                .then(CommandManager.argument("count", IntegerArgumentType.integer(1, 64))
                                        .executes(ctx -> give(ctx,
                                                EntityArgumentType.getPlayers(ctx, "targets"),
                                                IntegerArgumentType.getInteger(ctx, "count"))))))

                // /illuminati info
                .then(CommandManager.literal("info")
                        .executes(IlluminatiCommand::info))

                // /illuminati reload
                .then(CommandManager.literal("reload")
                        .executes(IlluminatiCommand::reload))

                // /illuminati cooldown reset [joueurs]
                .then(CommandManager.literal("cooldown")
                        .then(CommandManager.literal("reset")
                                .executes(ctx -> resetCooldown(ctx, List.of(ctx.getSource().getPlayerOrThrow())))
                                .then(CommandManager.argument("targets", EntityArgumentType.players())
                                        .executes(ctx -> resetCooldown(ctx,
                                                EntityArgumentType.getPlayers(ctx, "targets")))))));
    }

    private static int give(CommandContext<ServerCommandSource> ctx,
                            Collection<ServerPlayerEntity> targets, int count) {
        for (ServerPlayerEntity player : targets) {
            for (int i = 0; i < count; i++) {
                ItemStack stack = new ItemStack(ModItems.ILLUMINATI_PET);
                if (!player.getInventory().insertStack(stack)) {
                    player.dropItem(stack, false);
                }
            }
        }
        final int total = count * targets.size();
        ctx.getSource().sendFeedback(
                () -> Text.literal("✦ " + total + " Illuminati Pet distribué(s)").formatted(Formatting.GOLD),
                true);
        return total;
    }

    private static int info(CommandContext<ServerCommandSource> ctx) {
        IlluminatiCraftConfig cfg = IlluminatiCraftConfig.get();
        ServerCommandSource source = ctx.getSource();

        source.sendFeedback(() -> Text.literal("=== IlluminatiCraft ===").formatted(Formatting.GOLD), false);
        source.sendFeedback(() -> Text.literal("Items piochables en cache : " + RandomItemPicker.size())
                .formatted(Formatting.GRAY), false);
        source.sendFeedback(() -> Text.literal("Cooldown : " + cfg.cooldownSeconds + " s")
                .formatted(Formatting.GRAY), false);
        source.sendFeedback(() -> Text.literal("Recettes de craft uniquement : " + cfg.craftingRecipesOnly)
                .formatted(Formatting.GRAY), false);
        source.sendFeedback(() -> Text.literal("Chance d'auto-invocation forcée : " + cfg.selfDrawBoostPercent + " %")
                .formatted(Formatting.GRAY), false);
        source.sendFeedback(() -> Text.literal("Loot en coffre : " + cfg.enableChestLoot
                + " (" + (cfg.chestLootChance * 100f) + " %)").formatted(Formatting.GRAY), false);
        return 1;
    }

    private static int reload(CommandContext<ServerCommandSource> ctx) {
        IlluminatiCraftConfig.load();
        RandomItemPicker.rebuild(ctx.getSource().getServer());
        ctx.getSource().sendFeedback(
                () -> Text.literal("✔ Config rechargée — " + RandomItemPicker.size() + " items piochables")
                        .formatted(Formatting.GREEN),
                true);
        return 1;
    }

    private static int resetCooldown(CommandContext<ServerCommandSource> ctx,
                                     Collection<ServerPlayerEntity> targets) {
        for (ServerPlayerEntity player : targets) {
            player.getItemCooldownManager().remove(ModItems.ILLUMINATI_PET);
        }
        ctx.getSource().sendFeedback(
                () -> Text.literal("✔ Cooldown réinitialisé").formatted(Formatting.GREEN), true);
        return targets.size();
    }
}
