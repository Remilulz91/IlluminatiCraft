package fr.illuminaticraft.network;

import fr.illuminaticraft.IlluminatiCraft;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

/**
 * Informe le client de la durée réelle du cooldown qui vient d'être appliqué.
 *
 * Sans ça, le client ne dispose que d'une progression de 0 à 1 : il ne peut pas
 * en déduire un temps absolu. Il devait donc supposer que le cooldown avait
 * démarré à la durée configurée dans SA propre config — faux sur un serveur
 * réglé différemment, et faux aussi après une restauration de cooldown partiel,
 * où la durée appliquée est le reliquat et non la durée pleine.
 */
public record CooldownSyncPayload(int totalTicks) implements CustomPayload {

    public static final CustomPayload.Id<CooldownSyncPayload> ID =
            new CustomPayload.Id<>(IlluminatiCraft.id("cooldown_sync"));

    public static final PacketCodec<RegistryByteBuf, CooldownSyncPayload> CODEC =
            PacketCodec.tuple(
                    PacketCodecs.VAR_INT, CooldownSyncPayload::totalTicks,
                    CooldownSyncPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
