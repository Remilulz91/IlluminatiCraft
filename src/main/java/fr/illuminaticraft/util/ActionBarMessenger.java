package fr.illuminaticraft.util;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Prolonge l'affichage d'un message dans la barre d'action.
 *
 * Minecraft affiche un message de barre d'action pendant 60 ticks (3 s) puis le
 * fait disparaître, et cette durée n'est pas paramétrable depuis le serveur. Pour
 * tenir plus longtemps, on renvoie simplement le même message avant qu'il ne
 * s'efface.
 */
public class ActionBarMessenger {

    /** Durée par défaut d'un message, en ticks (5 secondes). */
    public static final int DEFAULT_DURATION_TICKS = 100;

    /** Intervalle de renvoi : sous les 60 ticks de vanilla, pour éviter le fondu. */
    private static final int RESEND_INTERVAL_TICKS = 30;

    private static final Map<UUID, Entry> PENDING = new ConcurrentHashMap<>();

    private static final class Entry {
        final Text text;
        int remaining;
        int sinceLastSend;

        Entry(Text text, int remaining) {
            this.text = text;
            this.remaining = remaining;
        }
    }

    /** Envoie un message affiché pendant {@code durationTicks}. */
    public static void send(ServerPlayerEntity player, Text text, int durationTicks) {
        player.sendMessage(text, true);

        if (durationTicks > RESEND_INTERVAL_TICKS) {
            PENDING.put(player.getUuid(), new Entry(text, durationTicks));
        } else {
            PENDING.remove(player.getUuid());
        }
    }

    /** Envoie un message avec la durée par défaut de 5 secondes. */
    public static void send(ServerPlayerEntity player, Text text) {
        send(player, text, DEFAULT_DURATION_TICKS);
    }

    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            if (PENDING.isEmpty()) {
                return;
            }
            Iterator<Map.Entry<UUID, Entry>> it = PENDING.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<UUID, Entry> pending = it.next();
                Entry entry = pending.getValue();

                entry.remaining--;
                entry.sinceLastSend++;

                if (entry.remaining <= 0) {
                    it.remove();
                    continue;
                }

                ServerPlayerEntity player = server.getPlayerManager().getPlayer(pending.getKey());
                if (player == null) {
                    it.remove();
                    continue;
                }

                if (entry.sinceLastSend >= RESEND_INTERVAL_TICKS) {
                    entry.sinceLastSend = 0;
                    player.sendMessage(entry.text, true);
                }
            }
        });
    }
}
