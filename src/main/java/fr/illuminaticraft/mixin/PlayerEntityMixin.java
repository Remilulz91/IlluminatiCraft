package fr.illuminaticraft.mixin;

import fr.illuminaticraft.config.IlluminatiCraftConfig;
import fr.illuminaticraft.items.ModItems;
import fr.illuminaticraft.sounds.ModSounds;
import fr.illuminaticraft.util.SoundUtil;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Détecte le drop d'un Illuminati Pet par un joueur (touche Q, ou expulsion
 * depuis un inventaire plein) pour lancer le thème du mod.
 */
@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

    @Inject(
            method = "dropItem(Lnet/minecraft/item/ItemStack;ZZ)Lnet/minecraft/entity/ItemEntity;",
            at = @At("HEAD")
    )
    private void illuminaticraft$onDropItem(ItemStack stack, boolean throwRandomly, boolean retainOwnership,
                                           CallbackInfoReturnable<ItemEntity> cir) {
        if (stack == null || stack.isEmpty() || !stack.isOf(ModItems.ILLUMINATI_PET)) {
            return;
        }

        PlayerEntity self = (PlayerEntity) (Object) this;
        if (self.getWorld().isClient) {
            return;
        }
        if (!(self instanceof ServerPlayerEntity player)) {
            return;
        }

        IlluminatiCraftConfig cfg = IlluminatiCraftConfig.get();
        MinecraftServer server = player.getServer();

        if (cfg.dropSoundGlobal && server != null) {
            SoundUtil.playToAll(server, ModSounds.ILLUMINATI_THEME);
        } else {
            SoundUtil.playAt(player.getServerWorld(),
                    player.getX(), player.getY(), player.getZ(),
                    ModSounds.ILLUMINATI_THEME);
        }
    }
}
