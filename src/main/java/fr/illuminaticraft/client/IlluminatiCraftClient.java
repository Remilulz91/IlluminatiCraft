package fr.illuminaticraft.client;

import fr.illuminaticraft.IlluminatiCraft;
import net.fabricmc.api.ClientModInitializer;

/**
 * Point d'entrée du mod côté client.
 * Rien de lourd ici : le mod est essentiellement server-side, le client ne sert
 * qu'à afficher l'item, jouer les sons et proposer l'écran de config.
 */
public class IlluminatiCraftClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        IlluminatiCraft.LOGGER.info("[IlluminatiCraft] Client initialisé");
    }
}
