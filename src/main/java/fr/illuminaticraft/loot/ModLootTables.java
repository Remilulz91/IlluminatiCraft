package fr.illuminaticraft.loot;

import fr.illuminaticraft.IlluminatiCraft;
import fr.illuminaticraft.config.IlluminatiCraftConfig;
import fr.illuminaticraft.items.ModItems;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.registry.RegistryKey;

import java.util.Set;

/**
 * Injecte l'Illuminati Pet dans les loot tables de coffres de structures rares.
 * Ne touche jamais aux fichiers vanilla : tout passe par l'event Fabric.
 */
public class ModLootTables {

    /** Coffres concernés (structures rares / à butin). */
    private static final Set<RegistryKey<LootTable>> TARGETS = Set.of(
            LootTables.SIMPLE_DUNGEON_CHEST,
            LootTables.ABANDONED_MINESHAFT_CHEST,
            LootTables.STRONGHOLD_LIBRARY_CHEST,
            LootTables.DESERT_PYRAMID_CHEST,
            LootTables.JUNGLE_TEMPLE_CHEST,
            LootTables.WOODLAND_MANSION_CHEST,
            LootTables.PILLAGER_OUTPOST_CHEST,
            LootTables.NETHER_BRIDGE_CHEST,
            LootTables.BASTION_TREASURE_CHEST,
            LootTables.ANCIENT_CITY_CHEST,
            LootTables.BURIED_TREASURE_CHEST,
            LootTables.SHIPWRECK_TREASURE_CHEST,
            LootTables.END_CITY_TREASURE_CHEST
    );

    public static void register() {
        // NOTE : signature de l'API loot v3 sur Fabric API 0.102.1+1.21.1.
        // Si la compilation échoue ici avec "incompatible parameter types in lambda",
        // c'est que la version de Fabric API utilise l'ancienne signature à 3
        // paramètres — remplacer alors par :
        //     LootTableEvents.MODIFY.register((key, tableBuilder, source) -> {
        LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
            IlluminatiCraftConfig cfg = IlluminatiCraftConfig.get();
            if (!cfg.enableChestLoot) return;
            if (!source.isBuiltin()) return;
            if (!TARGETS.contains(key)) return;

            tableBuilder.pool(LootPool.builder()
                    .rolls(ConstantLootNumberProvider.create(1.0f))
                    .conditionally(RandomChanceLootCondition.builder(cfg.chestLootChance))
                    .with(ItemEntry.builder(ModItems.ILLUMINATI)));
        });

        IlluminatiCraft.LOGGER.info("[ModLootTables] Injection loot enregistrée ({} tables ciblées)", TARGETS.size());
    }
}
