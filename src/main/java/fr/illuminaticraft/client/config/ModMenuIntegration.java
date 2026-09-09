package fr.illuminaticraft.client.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import fr.illuminaticraft.config.IlluminatiCraftConfig;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.text.Text;

/**
 * Intégration Mod Menu : bouton de config dans la liste des mods,
 * écran généré avec Cloth Config.
 */
public class ModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            IlluminatiCraftConfig cfg = IlluminatiCraftConfig.get();
            ConfigBuilder builder = ConfigBuilder.create()
                    .setParentScreen(parent)
                    .setTitle(Text.translatable("config.illuminaticraft.title"))
                    .setSavingRunnable(IlluminatiCraftConfig::save);

            ConfigEntryBuilder entry = builder.entryBuilder();

            // === Ability ===
            ConfigCategory ability = builder.getOrCreateCategory(
                    Text.translatable("config.illuminaticraft.category.ability"));

            ability.addEntry(entry.startIntField(
                            Text.translatable("config.illuminaticraft.cooldownSeconds"), cfg.cooldownSeconds)
                    .setMin(0).setMax(3600).setDefaultValue(120)
                    .setTooltip(Text.translatable("config.illuminaticraft.cooldownSeconds.tooltip"))
                    .setSaveConsumer(v -> cfg.cooldownSeconds = v).build());

            ability.addEntry(entry.startBooleanToggle(
                            Text.translatable("config.illuminaticraft.craftingRecipesOnly"), cfg.craftingRecipesOnly)
                    .setDefaultValue(true)
                    .setTooltip(Text.translatable("config.illuminaticraft.craftingRecipesOnly.tooltip"))
                    .setSaveConsumer(v -> cfg.craftingRecipesOnly = v).build());

            ability.addEntry(entry.startBooleanToggle(
                            Text.translatable("config.illuminaticraft.giveFullRecipeOutput"), cfg.giveFullRecipeOutput)
                    .setDefaultValue(true)
                    .setSaveConsumer(v -> cfg.giveFullRecipeOutput = v).build());

            ability.addEntry(entry.startDoubleField(
                            Text.translatable("config.illuminaticraft.selfDrawBoostPercent"), cfg.selfDrawBoostPercent)
                    .setMin(0.0).setMax(100.0).setDefaultValue(1.0)
                    .setTooltip(Text.translatable("config.illuminaticraft.selfDrawBoostPercent.tooltip"))
                    .setSaveConsumer(v -> cfg.selfDrawBoostPercent = v).build());

            ability.addEntry(entry.startBooleanToggle(
                            Text.translatable("config.illuminaticraft.broadcastDraws"), cfg.broadcastDraws)
                    .setDefaultValue(false)
                    .setSaveConsumer(v -> cfg.broadcastDraws = v).build());

            // === Sons ===
            ConfigCategory sounds = builder.getOrCreateCategory(
                    Text.translatable("config.illuminaticraft.category.sounds"));

            sounds.addEntry(entry.startBooleanToggle(
                            Text.translatable("config.illuminaticraft.enableCustomSounds"), cfg.enableCustomSounds)
                    .setDefaultValue(true)
                    .setSaveConsumer(v -> cfg.enableCustomSounds = v).build());

            sounds.addEntry(entry.startBooleanToggle(
                            Text.translatable("config.illuminaticraft.themeSoundGlobal"), cfg.themeSoundGlobal)
                    .setDefaultValue(false)
                    .setTooltip(Text.translatable("config.illuminaticraft.themeSoundGlobal.tooltip"))
                    .setSaveConsumer(v -> cfg.themeSoundGlobal = v).build());

            sounds.addEntry(entry.startFloatField(
                            Text.translatable("config.illuminaticraft.soundVolume"), cfg.soundVolume)
                    .setMin(0.0f).setMax(2.0f).setDefaultValue(1.0f)
                    .setSaveConsumer(v -> cfg.soundVolume = v).build());

            // === Obtention ===
            ConfigCategory obtaining = builder.getOrCreateCategory(
                    Text.translatable("config.illuminaticraft.category.obtaining"));

            obtaining.addEntry(entry.startBooleanToggle(
                            Text.translatable("config.illuminaticraft.addToCreativeTab"), cfg.addToCreativeTab)
                    .setDefaultValue(true)
                    .setTooltip(Text.translatable("config.illuminaticraft.restart_required"))
                    .setSaveConsumer(v -> cfg.addToCreativeTab = v).build());

            obtaining.addEntry(entry.startBooleanToggle(
                            Text.translatable("config.illuminaticraft.enableChestLoot"), cfg.enableChestLoot)
                    .setDefaultValue(true)
                    .setSaveConsumer(v -> cfg.enableChestLoot = v).build());

            obtaining.addEntry(entry.startFloatField(
                            Text.translatable("config.illuminaticraft.chestLootChance"), cfg.chestLootChance)
                    .setMin(0.0f).setMax(1.0f).setDefaultValue(0.02f)
                    .setTooltip(Text.translatable("config.illuminaticraft.chestLootChance.tooltip"))
                    .setSaveConsumer(v -> cfg.chestLootChance = v).build());

            return builder.build();
        };
    }
}
