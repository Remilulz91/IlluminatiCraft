package fr.illuminaticraft.client;

import fr.illuminaticraft.config.IlluminatiCraftConfig;
import fr.illuminaticraft.items.ModItems;
import fr.illuminaticraft.util.TimeFormat;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.registry.Registries;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

/**
 * Retour visuel et sonore quand le joueur essaie d'utiliser l'Illuminati pendant
 * sa recharge.
 *
 * Pourquoi côté client : Minecraft filtre l'utilisation d'un item en cooldown dans
 * ClientPlayerInteractionManager, AVANT d'envoyer quoi que ce soit au serveur. Le
 * serveur n'apprend donc jamais que le joueur a cliqué, et ne peut pas répondre.
 * Le refus doit être traité ici.
 */
@Environment(EnvType.CLIENT)
public class ClientCooldownFeedback {

    private static final int DISPLAY_TICKS = 100; // 5 secondes

    private static boolean wasUseKeyPressed = false;
    private static int displayTicksLeft = 0;

    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(ClientCooldownFeedback::tick);
    }

    private static void tick(MinecraftClient client) {
        ClientPlayerEntity player = client.player;
        if (player == null || client.world == null) {
            displayTicksLeft = 0;
            wasUseKeyPressed = false;
            return;
        }

        boolean pressed = client.options.useKey.isPressed();
        boolean holdingPet = player.getMainHandStack().isOf(ModItems.ILLUMINATI_PET)
                || player.getOffHandStack().isOf(ModItems.ILLUMINATI_PET);
        boolean cooling = player.getItemCooldownManager().isCoolingDown(ModItems.ILLUMINATI_PET);

        // Front montant uniquement : un clic maintenu ne rejoue pas le son en boucle
        if (pressed && !wasUseKeyPressed && holdingPet && cooling) {
            displayTicksLeft = DISPLAY_TICKS;
            playRefusalSound(player);
        }
        wasUseKeyPressed = pressed;

        if (displayTicksLeft <= 0) {
            return;
        }
        if (!cooling) {
            displayTicksLeft = 0;
            return;
        }

        displayTicksLeft--;
        // Réémis à chaque tick : le message reste net au lieu de s'estomper
        client.inGameHud.setOverlayMessage(
                Text.translatable("illuminaticraft.pet.cooldown", remainingLabel(player))
                        .formatted(Formatting.GRAY),
                false);
    }

    /**
     * Le client ne connaît pas la durée configurée sur le serveur : il n'a que la
     * progression 0–1 du cooldown. On la convertit avec la valeur de SA config, ce
     * qui est exact en solo et sur un serveur laissé au réglage par défaut.
     */
    private static String remainingLabel(ClientPlayerEntity player) {
        float progress = player.getItemCooldownManager()
                .getCooldownProgress(ModItems.ILLUMINATI_PET, 0.0f);
        int totalTicks = IlluminatiCraftConfig.get().cooldownTicks();
        int seconds = Math.max(1, Math.round(progress * totalTicks / 20.0f));
        return TimeFormat.minutesSeconds(seconds);
    }

    private static void playRefusalSound(ClientPlayerEntity player) {
        SoundEvent sound = Registries.SOUND_EVENT.get(Identifier.ofVanilla("block.dispenser.fail"));
        if (sound != null) {
            player.playSound(sound, 0.5f, 1.2f);
        }
    }
}
