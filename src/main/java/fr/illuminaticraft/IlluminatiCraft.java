package fr.illuminaticraft;

import fr.illuminaticraft.commands.IlluminatiCommand;
import fr.illuminaticraft.config.IlluminatiCraftConfig;
import fr.illuminaticraft.items.ModItems;
import fr.illuminaticraft.loot.ModLootTables;
import fr.illuminaticraft.sounds.ModSounds;
import fr.illuminaticraft.util.RandomItemPicker;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Point d'entrée principal d'IlluminatiCraft.
 *
 * Le mod ajoute un seul item — l'Illuminati Pet — inspiré du pet du même nom
 * du mod Inventory Pets :
 *   - clic droit  : donne un item aléatoire pioché parmi TOUTES les recettes
 *                   chargées (vanilla + mods), cooldown de 2 minutes
 *   - Illuminati depuis un Illuminati : succès spécial + musique "confirmed"
 *   - drop de l'item : joue le thème custom
 */
public class IlluminatiCraft implements ModInitializer {

    public static final String MOD_ID = "illuminaticraft";
    public static final Logger LOGGER = LoggerFactory.getLogger("IlluminatiCraft");

    // === Build type detection (set by Gradle's processResources) ===
    private static final boolean IS_DEBUG_BUILD;
    static {
        boolean debug = false;
        try (InputStream is = IlluminatiCraft.class.getResourceAsStream("/illuminaticraft.build.properties")) {
            if (is != null) {
                Properties p = new Properties();
                p.load(is);
                debug = "debug".equalsIgnoreCase(p.getProperty("build.type", "public").trim());
            }
        } catch (IOException ignored) { }
        IS_DEBUG_BUILD = debug;
    }

    /** Returns true if this JAR was built as the debug variant. */
    public static boolean isDebugBuild() {
        return IS_DEBUG_BUILD;
    }

    @Override
    public void onInitialize() {
        LOGGER.info("==============================================");
        LOGGER.info("    IlluminatiCraft - Starting up ({} build)",
                IS_DEBUG_BUILD ? "DEBUG" : "PUBLIC");
        LOGGER.info("==============================================");

        // 1. Configuration
        IlluminatiCraftConfig.load();
        LOGGER.info("[IlluminatiCraft] Configuration chargée");

        // 2. Sons custom (thème au drop + musique "Illuminati confirmed")
        ModSounds.register();

        // 3. Item Illuminati Pet
        ModItems.register();

        // 4. Cache des recettes (rechargé à chaque reload de datapack)
        RandomItemPicker.register();

        // 5. Injection dans les loot tables de coffres
        ModLootTables.register();

        // 6. Commandes /illuminati
        CommandRegistrationCallback.EVENT.register(IlluminatiCommand::register);
        LOGGER.info("[IlluminatiCraft] Commandes enregistrées");

        LOGGER.info("[IlluminatiCraft] Mod chargé avec succès !");
    }

    /** Crée un Identifier dans le namespace du mod. */
    public static Identifier id(String path) {
        return Identifier.of(MOD_ID, path);
    }
}
