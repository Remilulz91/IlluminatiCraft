package fr.illuminaticraft.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.illuminaticraft.IlluminatiCraft;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Configuration centrale d'IlluminatiCraft.
 * Sauvegardée dans config/illuminaticraft.json.
 *
 * Modifiable via :
 *  - l'écran de config in-game (Mod Menu + Cloth Config)
 *  - la commande /illuminati config set &lt;option&gt; &lt;valeur&gt;
 *  - directement dans le fichier config/illuminaticraft.json
 */
public class IlluminatiCraftConfig {

    private static final Path CONFIG_PATH = FabricLoader.getInstance()
            .getConfigDir()
            .resolve("illuminaticraft.json");

    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    private static IlluminatiCraftConfig INSTANCE = new IlluminatiCraftConfig();

    // === Ability principale ===

    /** Cooldown entre deux utilisations, en secondes (Inventory Pets : 120 s). */
    public int cooldownSeconds = 120;

    /** Ne piocher que dans les recettes de type "crafting" (établi / inventaire). */
    public boolean craftingRecipesOnly = true;

    /** Donner la quantité complète de la recette (ex. 4 planches) au lieu d'un seul item. */
    public boolean giveFullRecipeOutput = true;

    /**
     * Chance forcée (en %) de piocher l'Illuminati Pet lui-même, avant le tirage
     * aléatoire normal. 0.0 = 100 % aléatoire (fidèle à Inventory Pets, mais le
     * succès devient quasi inatteignable : ~1 chance sur 1500 en vanilla pur).
     */
    public double selfDrawBoostPercent = 1.0;

    /** L'item pioché est-il aussi annoncé aux autres joueurs du serveur ? */
    public boolean broadcastDraws = false;

    /** Prévenir le joueur, dans la barre d'action, quand le cooldown est écoulé. */
    public boolean notifyWhenReady = true;

    /** Namespaces d'items interdits au tirage (ex. "minecraft" pour du 100 % modé). */
    public List<String> blacklistedNamespaces = new ArrayList<>();

    /** Ids d'items interdits au tirage (ex. "minecraft:command_block"). */
    public List<String> blacklistedItems = new ArrayList<>(List.of(
            "minecraft:command_block",
            "minecraft:chain_command_block",
            "minecraft:repeating_command_block",
            "minecraft:structure_block",
            "minecraft:jigsaw",
            "minecraft:barrier",
            "minecraft:light",
            "minecraft:debug_stick"
    ));

    // === Sons ===

    /** Jouer les sons custom du mod (thème au drop, musique "confirmed"). */
    public boolean enableCustomSounds = true;

    /** Le thème joué au tirage est-il entendu par tout le serveur (true) ou seulement par le joueur (false) ? */
    public boolean themeSoundGlobal = false;

    /** Volume des sons custom. */
    public float soundVolume = 1.0f;

    // === Obtention ===

    /** Ajouter l'Illuminati Pet au menu créatif. */
    public boolean addToCreativeTab = true;

    /** Injecter l'Illuminati Pet dans les loot tables de coffres. */
    public boolean enableChestLoot = true;

    /** Chance d'apparition dans un coffre concerné (0.0 – 1.0). */
    public float chestLootChance = 0.02f;

    // === DEBUG (off dans les builds public, on dans les builds debug) ===

    /** [DEBUG] Ignore complètement le cooldown. */
    public boolean debugNoCooldown = IlluminatiCraft.isDebugBuild();

    /** [DEBUG] Active les commandes /illuminati debug ... */
    public boolean enableDebugCommands = IlluminatiCraft.isDebugBuild();

    // === Méthodes ===

    public static IlluminatiCraftConfig get() {
        return INSTANCE;
    }

    /** Cooldown converti en ticks. */
    public int cooldownTicks() {
        return Math.max(0, cooldownSeconds) * 20;
    }

    public static void load() {
        try {
            if (Files.exists(CONFIG_PATH)) {
                String json = Files.readString(CONFIG_PATH);
                IlluminatiCraftConfig loaded = GSON.fromJson(json, IlluminatiCraftConfig.class);
                if (loaded != null) {
                    INSTANCE = loaded;
                }
                IlluminatiCraft.LOGGER.info("[Config] Configuration loaded from {}", CONFIG_PATH);
            } else {
                save();
                IlluminatiCraft.LOGGER.info("[Config] Default configuration created");
            }
        } catch (IOException e) {
            IlluminatiCraft.LOGGER.error("[Config] Loading error: {}", e.getMessage());
        }

        // Champs potentiellement absents d'un ancien fichier de config
        if (INSTANCE.blacklistedItems == null) INSTANCE.blacklistedItems = new ArrayList<>();
        if (INSTANCE.blacklistedNamespaces == null) INSTANCE.blacklistedNamespaces = new ArrayList<>();

        // SECURITY: dans un build PUBLIC, les flags debug sont forcés à false,
        // peu importe le contenu du fichier de config.
        if (!IlluminatiCraft.isDebugBuild()) {
            boolean wasModified = false;
            if (INSTANCE.enableDebugCommands) {
                INSTANCE.enableDebugCommands = false;
                wasModified = true;
            }
            if (INSTANCE.debugNoCooldown) {
                INSTANCE.debugNoCooldown = false;
                wasModified = true;
            }
            if (wasModified) {
                IlluminatiCraft.LOGGER.warn("[Config] Debug flags found in config file but this is a PUBLIC build —");
                IlluminatiCraft.LOGGER.warn("[Config] they are IGNORED. To use debug features, install the DEBUG build.");
            }
        }
    }

    public static void save() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            Files.writeString(CONFIG_PATH, GSON.toJson(INSTANCE));
        } catch (IOException e) {
            IlluminatiCraft.LOGGER.error("[Config] Erreur lors de la sauvegarde : {}", e.getMessage());
        }
    }
}
