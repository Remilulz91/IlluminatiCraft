package fr.illuminaticraft.util;

import fr.illuminaticraft.IlluminatiCraft;
import fr.illuminaticraft.config.IlluminatiCraftConfig;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Construit et met en cache la liste de tous les items obtenables via une recette
 * — vanilla ET modés — puis en tire un au hasard.
 *
 * Le cache est reconstruit au démarrage du serveur et à chaque /reload, ce qui
 * couvre automatiquement l'ajout d'un mod ou d'un datapack.
 */
public class RandomItemPicker {

    private static final Random RANDOM = new Random();

    /** Résultats de recettes valides, prêts à être copiés. */
    private static List<ItemStack> CACHE = new ArrayList<>();

    /** Nombre de recettes chargées lors de la dernière construction du cache. */
    private static int LAST_RECIPE_COUNT = -1;

    public static void register() {
        ServerLifecycleEvents.SERVER_STARTED.register(RandomItemPicker::rebuild);
    }

    /** Reconstruit le cache des résultats de recettes. */
    public static void rebuild(MinecraftServer server) {
        IlluminatiCraftConfig cfg = IlluminatiCraftConfig.get();
        List<ItemStack> results = new ArrayList<>();

        int skipped = 0;
        for (RecipeEntry<?> entry : server.getRecipeManager().values()) {
            try {
                if (cfg.craftingRecipesOnly && entry.value().getType() != RecipeType.CRAFTING) {
                    continue;
                }

                ItemStack result = entry.value().getResult(server.getRegistryManager());
                if (result == null || result.isEmpty()) {
                    continue;
                }

                Identifier itemId = Registries.ITEM.getId(result.getItem());
                if (isBlacklisted(cfg, itemId)) {
                    continue;
                }

                results.add(result.copy());
            } catch (Throwable t) {
                // Certaines recettes modées calculent leur résultat dynamiquement
                // et peuvent lever une exception hors contexte de craft : on ignore.
                skipped++;
            }
        }

        CACHE = results;
        LAST_RECIPE_COUNT = server.getRecipeManager().values().size();
        IlluminatiCraft.LOGGER.info("[RandomItemPicker] {} résultats de recettes en cache ({} recettes ignorées)",
                CACHE.size(), skipped);
    }

    private static boolean isBlacklisted(IlluminatiCraftConfig cfg, Identifier itemId) {
        String full = itemId.toString();
        if (cfg.blacklistedItems != null && cfg.blacklistedItems.contains(full)) {
            return true;
        }
        return cfg.blacklistedNamespaces != null && cfg.blacklistedNamespaces.contains(itemId.getNamespace());
    }

    /** Nombre d'items actuellement piochables. */
    public static int size() {
        return CACHE.size();
    }

    /**
     * Tire un item au hasard parmi toutes les recettes chargées.
     * Renvoie une pile vide si aucune recette n'est disponible.
     */
    public static ItemStack draw(MinecraftServer server) {
        // Reconstruction paresseuse : couvre le premier usage et tout /reload
        // ayant modifié le nombre de recettes chargées (ajout d'un datapack…).
        if (CACHE.isEmpty() || server.getRecipeManager().values().size() != LAST_RECIPE_COUNT) {
            rebuild(server);
        }
        if (CACHE.isEmpty()) {
            return ItemStack.EMPTY;
        }

        ItemStack picked = CACHE.get(RANDOM.nextInt(CACHE.size())).copy();

        if (!IlluminatiCraftConfig.get().giveFullRecipeOutput) {
            picked.setCount(1);
        }
        return picked;
    }

    /** Tirage 0–100 utilisé pour le "boost" de l'auto-invocation. */
    public static double rollPercent() {
        return RANDOM.nextDouble() * 100.0;
    }
}
