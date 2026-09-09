package fr.illuminaticraft.network;

import fr.illuminaticraft.IlluminatiCraft;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;

/**
 * Enregistrement des paquets du mod. Appelé côté commun : le type de paquet doit
 * être connu du serveur comme du client.
 */
public class ModNetworking {

    public static void register() {
        PayloadTypeRegistry.playS2C().register(CooldownSyncPayload.ID, CooldownSyncPayload.CODEC);
        IlluminatiCraft.LOGGER.info("[ModNetworking] Paquets enregistrés");
    }

    /**
     * Envoie au joueur la durée du cooldown qui vient de lui être appliqué.
     * Silencieux si le client n'est pas en mesure de le recevoir.
     */
    public static void sendCooldown(ServerPlayerEntity player, int totalTicks) {
        if (ServerPlayNetworking.canSend(player, CooldownSyncPayload.ID)) {
            ServerPlayNetworking.send(player, new CooldownSyncPayload(totalTicks));
        }
    }
}
