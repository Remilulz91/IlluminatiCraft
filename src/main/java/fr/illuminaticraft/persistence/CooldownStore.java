package fr.illuminaticraft.persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import fr.illuminaticraft.IlluminatiCraft;
import fr.illuminaticraft.events.CooldownWatcher;
import fr.illuminaticraft.items.ModItems;
import fr.illuminaticraft.network.ModNetworking;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.WorldSavePath;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Rend le cooldown persistant.
 *
 * L'ItemCooldownManager de Minecraft vit en mémoire dans l'entité joueur : il est
 * perdu à la déconnexion. Sans ce store, il suffisait de quitter la partie et de
 * revenir pour réutiliser l'Illuminati immédiatement.
 *
 * On enregistre donc, par joueur, le tick de monde auquel le cooldown expire, dans
 * un fichier du dossier de sauvegarde. Le temps de monde n'avance que pendant que
 * la partie tourne, ce qui est le comportement attendu en solo : mettre le jeu en
 * pause ne fait pas fondre le cooldown.
 */
public class CooldownStore {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String FILE_NAME = "illuminaticraft-cooldowns.json";

    /** UUID du joueur -> tick de monde auquel le cooldown se termine. */
    private static final Map<String, Long> EXPIRY = new ConcurrentHashMap<>();

    private static Path storePath;

    public static void register() {
        ServerLifecycleEvents.SERVER_STARTED.register(CooldownStore::load);
        ServerPlayConnectionEvents.JOIN.register(
                (handler, sender, server) -> restore(handler.getPlayer(), server));
    }

    /** Enregistre un cooldown de {@code ticks} pour ce joueur. */
    public static void set(ServerPlayerEntity player, int ticks) {
        MinecraftServer server = player.getServer();
        if (server == null) return;

        EXPIRY.put(player.getUuidAsString(), currentTick(server) + ticks);
        save();
    }

    /** Oublie le cooldown d'un joueur (expiré ou remis à zéro). */
    public static void clear(String uuid) {
        if (EXPIRY.remove(uuid) != null) {
            save();
        }
    }

    private static long currentTick(MinecraftServer server) {
        return server.getOverworld() != null ? server.getOverworld().getTime() : 0L;
    }

    /** À la connexion : réapplique le cooldown restant, s'il en reste. */
    private static void restore(ServerPlayerEntity player, MinecraftServer server) {
        String uuid = player.getUuidAsString();
        Long expiry = EXPIRY.get(uuid);
        if (expiry == null) return;

        long remaining = expiry - currentTick(server);
        if (remaining <= 0) {
            clear(uuid);
            return;
        }

        int ticks = (int) Math.min(remaining, Integer.MAX_VALUE);
        player.getItemCooldownManager().set(ModItems.ILLUMINATI, ticks);
        CooldownWatcher.watch(player);

        // Le cooldown restauré est un reliquat : sans cette annonce, le client
        // afficherait la durée pleine au lieu du temps réellement restant.
        ModNetworking.sendCooldown(player, ticks);

        IlluminatiCraft.LOGGER.info("[CooldownStore] Cooldown restauré pour {} ({} s)",
                player.getName().getString(), ticks / 20);
    }

    private static void load(MinecraftServer server) {
        storePath = server.getSavePath(WorldSavePath.ROOT).resolve(FILE_NAME);
        EXPIRY.clear();

        if (!Files.exists(storePath)) {
            return;
        }
        try {
            String json = Files.readString(storePath);
            Map<String, Long> loaded = GSON.fromJson(
                    json, new TypeToken<HashMap<String, Long>>() { }.getType());
            if (loaded != null) {
                EXPIRY.putAll(loaded);
            }
            IlluminatiCraft.LOGGER.info("[CooldownStore] {} cooldown(s) chargé(s)", EXPIRY.size());
        } catch (IOException | RuntimeException e) {
            IlluminatiCraft.LOGGER.error("[CooldownStore] Lecture impossible : {}", e.getMessage());
        }
    }

    private static void save() {
        if (storePath == null) return;
        try {
            Files.writeString(storePath, GSON.toJson(EXPIRY));
        } catch (IOException e) {
            IlluminatiCraft.LOGGER.error("[CooldownStore] Écriture impossible : {}", e.getMessage());
        }
    }
}
