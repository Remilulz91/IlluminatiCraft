package fr.illuminaticraft.util;

import fr.illuminaticraft.config.IlluminatiCraftConfig;
import net.minecraft.registry.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

/**
 * Helpers de lecture des sons custom du mod.
 */
public class SoundUtil {

    /** Joue un son à une position, entendu par les joueurs à portée. */
    public static void playAt(ServerWorld world, double x, double y, double z, SoundEvent sound) {
        IlluminatiCraftConfig cfg = IlluminatiCraftConfig.get();
        if (!cfg.enableCustomSounds) return;

        world.playSound(null, x, y, z, sound, SoundCategory.RECORDS, cfg.soundVolume, 1.0f);
    }

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

    /**
     * Joue un son vanilla à un joueur, résolu par son id ("block.beacon.activate").
     * Passer par le registre évite de dépendre du type déclaré dans SoundEvents
     * (SoundEvent vs RegistryEntry.Reference&lt;SoundEvent&gt; selon les versions).
     */
    public static void playVanillaTo(ServerPlayerEntity player, String soundId, float volume, float pitch) {
        SoundEvent sound = Registries.SOUND_EVENT.get(Identifier.ofVanilla(soundId));
        if (sound == null) return;
        player.playSoundToPlayer(sound, SoundCategory.PLAYERS, volume, pitch);
    }
}
