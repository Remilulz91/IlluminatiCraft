package fr.illuminaticraft.items;

import fr.illuminaticraft.IlluminatiCraft;
import fr.illuminaticraft.config.IlluminatiCraftConfig;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Rarity;

/**
 * Enregistre les items du mod.
 */
public class ModItems {

    /** L'Illuminati Pet — clic droit pour un item aléatoire craftable. */
    public static final Item ILLUMINATI_PET = register(
            "illuminati_pet",
            new IlluminatiPetItem(new Item.Settings()
                    .maxCount(1)
                    .rarity(Rarity.EPIC)
                    .fireproof())
    );

    private static Item register(String id, Item item) {
        return Registry.register(Registries.ITEM, IlluminatiCraft.id(id), item);
    }

    public static void register() {
        IlluminatiCraft.LOGGER.info("[ModItems] 1 item enregistré");

        if (IlluminatiCraftConfig.get().addToCreativeTab) {
            ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS)
                    .register(entries -> entries.add(ILLUMINATI_PET));
        }
    }
}
