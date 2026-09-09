package fr.illuminaticraft.events;

import fr.illuminaticraft.IlluminatiCraft;
import fr.illuminaticraft.config.IlluminatiCraftConfig;
import fr.illuminaticraft.items.ModItems;
import fr.illuminaticraft.util.SoundUtil;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Iterator;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Prévient le joueur quand son Illuminati Pet est de nouveau prêt.
 *
 * Les joueurs sont surveillés uniquement après une utilisation : la liste est
 * vide la plupart du temps, et l'inspection n'a lieu qu'une fois toutes les
 * 10 ticks (2 fois par seconde).
 */
public class CooldownWatcher {

    private static final Set<UUID> WATCHED = ConcurrentHashMap.newKeySet();
    private static final int CHECK_INTERVAL_TICKS = 10;

    private static int tickCounter = 0;

    /** Place un joueur sous surveillance après qu'il a déclenché un cooldown. */
    public static void watch(ServerPlayerEntity player) {
        WATCHED.add(player.getUuid());
    }

    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            if (WATCHED.isEmpty()) {
                return;
            }
            if (++tickCounter < CHECK_INTERVAL_TICKS) {
                return;
            }
            tickCounter = 0;

            Iterator<UUID> it = WATCHED.iterator();
            while (it.hasNext()) {
                UUID uuid = it.next();
                ServerPlayerEntity player = server.getPlayerManager().getPlayer(uuid);

                // Joueur déconnecté : on arrête de le suivre
                if (player == null) {
                    it.remove();
                    continue;
                }

                if (!player.getItemCooldownManager().isCoolingDown(ModItems.ILLUMINATI_PET)) {
                    it.remove();
                    notifyReady(player);
                }
            }
        });

        IlluminatiCraft.LOGGER.info("[CooldownWatcher] Surveillance des cooldowns enregistrée");
    }

    private static void notifyReady(ServerPlayerEntity player) {
        if (!IlluminatiCraftConfig.get().notifyWhenReady) {
            return;
        }
        player.sendMessage(
                Text.translatable("illuminaticraft.pet.ready").formatted(Formatting.GOLD),
                true);
        SoundUtil.playVanillaTo(player, "block.note_block.bell", 0.5f, 1.4f);
    }
}
