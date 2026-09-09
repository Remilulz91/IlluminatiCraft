package fr.illuminaticraft.util;

import fr.illuminaticraft.config.IlluminatiCraftConfig;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;

/**
 * Helpers de lecture des sons custom du mod.
 */
public class SoundUtil {

    /** Joue un son directement dans les oreilles d'un joueur (pas d'atténuation). */
    public static void playTo(ServerPlayerEntity player, SoundEvent sound) {
        IlluminatiCraftConfig cfg = IlluminatiCraftConfig.get();
        if (!cfg.enableCustomSounds) return;

        player.playSoundToPlayer(sound, SoundCategory.RECORDS, cfg.soundVolume, 1.0f);
    }

    /** Joue un son pour tous les joueurs connectés. */
    public static void playToAll(MinecraftServer server, SoundEvent sound) {
        IlluminatiCraftConfig cfg = IlluminatiCraftConfig.get();
        if (!cfg.enableCustomSounds) return;

        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            player.playSoundToPlayer(sound, SoundCategory.RECORDS, cfg.soundVolume, 1.0f);
        }
    }
}
