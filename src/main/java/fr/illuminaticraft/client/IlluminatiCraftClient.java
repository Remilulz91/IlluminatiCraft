package fr.illuminaticraft.client;

import fr.illuminaticraft.IlluminatiCraft;
import net.fabricmc.api.ClientModInitializer;

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
        IlluminatiCraft.LOGGER.info("[IlluminatiCraft] Client initialisé");
    }
}
