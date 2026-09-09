package fr.illuminaticraft.items;

import fr.illuminaticraft.IlluminatiCraft;
import fr.illuminaticraft.config.IlluminatiCraftConfig;
import fr.illuminaticraft.sounds.ModSounds;
import fr.illuminaticraft.util.AdvancementUtil;
import fr.illuminaticraft.util.RandomItemPicker;
import fr.illuminaticraft.util.SoundUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.List;

/**
 * L'Illuminati Pet.
 *
 * Clic droit : donne au joueur un item aléatoire pioché parmi toutes les recettes
 * chargées (vanilla + mods), puis applique un cooldown de 2 minutes.
 *
 * Si le tirage retombe sur un Illuminati Pet, le joueur débloque le succès
 * "Illuminati Confirmed" et la musique associée est jouée.
 */
public class IlluminatiPetItem extends Item {

    public static final Identifier CONFIRMED_ADVANCEMENT = IlluminatiCraft.id("illuminati_confirmed");

    public IlluminatiPetItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);

        // Côté client : on laisse jouer l'animation de main, la logique est serveur
        if (world.isClient) {
            return TypedActionResult.success(stack, true);
        }
        if (!(user instanceof ServerPlayerEntity player)) {
            return TypedActionResult.pass(stack);
        }

        IlluminatiCraftConfig cfg = IlluminatiCraftConfig.get();
        MinecraftServer server = player.getServer();
        if (server == null) {
            return TypedActionResult.pass(stack);
        }

        // === Cooldown ===
        if (!cfg.debugNoCooldown && player.getItemCooldownManager().isCoolingDown(this)) {
            float progress = player.getItemCooldownManager().getCooldownProgress(this, 0.0f);
            int remaining = Math.max(1, Math.round(progress * cfg.cooldownTicks() / 20.0f));
            player.sendMessage(
                    Text.translatable("illuminaticraft.pet.cooldown", remaining).formatted(Formatting.GRAY),
                    true);
            return TypedActionResult.fail(stack);
        }

        // === Tirage ===
        ItemStack reward;
        double boost = cfg.selfDrawBoostPercent;
        if (boost > 0.0 && RandomItemPicker.rollPercent() < boost) {
            reward = new ItemStack(ModItems.ILLUMINATI_PET);
        } else {
            reward = RandomItemPicker.draw(server);
        }

        if (reward.isEmpty()) {
            player.sendMessage(
                    Text.translatable("illuminaticraft.pet.no_recipes").formatted(Formatting.RED),
                    true);
            return TypedActionResult.fail(stack);
        }

        boolean selfDraw = reward.isOf(ModItems.ILLUMINATI_PET);
        Text rewardName = reward.getName();
        int rewardCount = reward.getCount();

        // === Remise de l'item ===
        if (!player.getInventory().insertStack(reward)) {
            player.dropItem(reward, false);
        }

        // === Cooldown appliqué après un tirage réussi ===
        if (!cfg.debugNoCooldown) {
            player.getItemCooldownManager().set(this, cfg.cooldownTicks());
        }

        ServerWorld serverWorld = player.getServerWorld();

        if (selfDraw) {
            // ★ ILLUMINATI CONFIRMED ★
            AdvancementUtil.grant(player, CONFIRMED_ADVANCEMENT);
            SoundUtil.playTo(player, ModSounds.ILLUMINATI_CONFIRMED);

            player.sendMessage(
                    Text.translatable("illuminaticraft.pet.confirmed").formatted(Formatting.GOLD, Formatting.BOLD),
                    false);

            serverWorld.spawnParticles(ParticleTypes.TOTEM_OF_UNDYING,
                    player.getX(), player.getY() + 1.0, player.getZ(),
                    120, 0.6, 1.0, 0.6, 0.35);
            serverWorld.spawnParticles(ParticleTypes.END_ROD,
                    player.getX(), player.getY() + 1.2, player.getZ(),
                    60, 0.4, 0.8, 0.4, 0.08);

            // Événement suffisamment rare pour être annoncé à tout le serveur
            server.getPlayerManager().broadcast(
                    Text.translatable("illuminaticraft.pet.confirmed.broadcast", player.getDisplayName())
                            .formatted(Formatting.GOLD),
                    false);
        } else {
            player.sendMessage(
                    Text.translatable("illuminaticraft.pet.drawn", rewardName, rewardCount)
                            .formatted(Formatting.LIGHT_PURPLE),
                    true);

            // Le thème du mod accompagne chaque tirage, comme dans Inventory Pets.
            if (cfg.themeSoundGlobal) {
                SoundUtil.playToAll(server, ModSounds.ILLUMINATI_THEME);
            } else {
                SoundUtil.playTo(player, ModSounds.ILLUMINATI_THEME);
            }

            serverWorld.spawnParticles(ParticleTypes.ENCHANT,
                    player.getX(), player.getY() + 1.2, player.getZ(),
                    40, 0.4, 0.6, 0.4, 0.6);

            if (cfg.broadcastDraws) {
                server.getPlayerManager().broadcast(
                        Text.translatable("illuminaticraft.pet.drawn.broadcast",
                                player.getDisplayName(), rewardName, rewardCount)
                                .formatted(Formatting.DARK_PURPLE),
                        false);
            }
        }

        return TypedActionResult.success(stack, false);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.translatable("illuminaticraft.pet.tooltip.line1").formatted(Formatting.GRAY));
        tooltip.add(Text.translatable("illuminaticraft.pet.tooltip.line2",
                IlluminatiCraftConfig.get().cooldownSeconds).formatted(Formatting.DARK_GRAY));
        super.appendTooltip(stack, context, tooltip, type);
    }
}
