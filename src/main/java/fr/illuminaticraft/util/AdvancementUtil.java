package fr.illuminaticraft.util;

import fr.illuminaticraft.IlluminatiCraft;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.advancement.AdvancementProgress;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

/**
 * Attribution manuelle d'un succès depuis le code.
 *
 * Le succès "illuminaticraft:illuminati_confirmed" utilise le trigger
 * "minecraft:impossible" : il ne peut donc être débloqué que par ici.
 */
public class AdvancementUtil {

    public static void grant(ServerPlayerEntity player, Identifier advancementId) {
        MinecraftServer server = player.getServer();
        if (server == null) return;

        AdvancementEntry entry = server.getAdvancementLoader().get(advancementId);
        if (entry == null) {
            IlluminatiCraft.LOGGER.warn("[Advancement] Succès introuvable : {}", advancementId);
            return;
        }

        AdvancementProgress progress = player.getAdvancementTracker().getProgress(entry);
        if (progress.isDone()) return;

        for (String criterion : progress.getUnobtainedCriteria()) {
            player.getAdvancementTracker().grantCriterion(entry, criterion);
        }
    }
}
