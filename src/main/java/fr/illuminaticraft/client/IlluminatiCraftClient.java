package fr.illuminaticraft.client;

import fr.illuminaticraft.IlluminatiCraft;
import fr.illuminaticraft.network.CooldownSyncPayload;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

/**
 * Point d'entrée du mod côté client.
 * Le mod est essentiellement server-side ; le client affiche l'item, joue les sons,
 * propose l'écran de config et gère le refus pendant le cooldown (que le serveur ne
 * voit jamais — voir ClientCooldownFeedback).
 */
public class IlluminatiCraftClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientCooldownFeedback.register();

        // Le serveur annonce la durée réelle du cooldown qu'il vient d'appliquer
        ClientPlayNetworking.registerGlobalReceiver(CooldownSyncPayload.ID,
                (payload, context) -> context.client().execute(
                        () -> ClientCooldownFeedback.setCooldownTotalTicks(payload.totalTicks())));

        IlluminatiCraft.LOGGER.info("[IlluminatiCraft] Client initialisé");
    }
}
