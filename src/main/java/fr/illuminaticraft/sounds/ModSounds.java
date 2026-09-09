package fr.illuminaticraft.sounds;

import fr.illuminaticraft.IlluminatiCraft;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

/**
 * Sons custom du mod.
 *
 * Les fichiers audio correspondants doivent être placés dans :
 *   src/main/resources/assets/illuminaticraft/sounds/&lt;nom&gt;.ogg
 * et déclarés dans assets/illuminaticraft/sounds.json.
 *
 * Les .ogg livrés avec le dépôt sont des PLACEHOLDERS SILENCIEUX :
 * remplace-les par tes propres fichiers (voir README, section "Sons").
 */
public class ModSounds {

    /** Joué quand un joueur drop un Illuminati Pet. */
    public static final SoundEvent ILLUMINATI_THEME = of("illuminati_theme");

    /** Joué quand un Illuminati Pet se donne lui-même (succès "Illuminati Confirmed"). */
    public static final SoundEvent ILLUMINATI_CONFIRMED = of("illuminati_confirmed");

    private static SoundEvent of(String name) {
        Identifier id = IlluminatiCraft.id(name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static void register() {
        IlluminatiCraft.LOGGER.info("[ModSounds] 2 sons enregistrés");
    }
}
