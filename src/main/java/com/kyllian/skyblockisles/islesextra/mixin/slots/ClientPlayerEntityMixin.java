package com.kyllian.skyblockisles.islesextra.mixin.slots;

import com.kyllian.skyblockisles.islesextra.client.IslesExtraClient;
import com.kyllian.skyblockisles.islesextra.client.LockSlots;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.world.GameMode;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {

    @Shadow @Final protected MinecraftClient client;

    @Inject(at = @At("HEAD"), method = "dropSelectedItem", cancellable = true)
    public void dropSelectedItem(boolean dropEntireStack, CallbackInfoReturnable<Boolean> cir){
        MinecraftClient client = this.client;
        if (!IslesExtraClient.isOnIsles()) return;
        if (client.player == null || client.getNetworkHandler() == null) return;
        PlayerListEntry playerListEntry = client.getNetworkHandler().getPlayerListEntry(client.player.getGameProfile().getId());
        if (playerListEntry == null || (playerListEntry.getGameMode() != GameMode.SURVIVAL && playerListEntry.getGameMode() != GameMode.ADVENTURE)) return;
        int slotID = client.player.getInventory().selectedSlot;
        /*if (Arrays.stream(LockSlots.getLockedSlots().toArray(new Integer[0])).anyMatch(value -> value == slotID)) {
            cir.setReturnValue(false);
        }*/
        if (LockSlots.isLocked(slotID)) cir.setReturnValue(true);
    }

}
