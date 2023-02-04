package com.kyllian.skyblockisles.islesextra.mixin.slots;

import com.kyllian.skyblockisles.islesextra.client.IslesExtraClient;
import com.kyllian.skyblockisles.islesextra.client.LockSlots;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Arm;
import net.minecraft.world.GameMode;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(InGameHud.class)
abstract class InGameHudMixin {

    @Shadow @Final private ItemRenderer itemRenderer;
    @Shadow @Final private MinecraftClient client;

    @Inject(
            method = "renderHotbar",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderHotbarItem(IIFLnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/item/ItemStack;I)V", shift = At.Shift.AFTER),
            locals = LocalCapture.CAPTURE_FAILSOFT
    )
    void drawHotbarLock(float tickDelta, MatrixStack matrices, CallbackInfo ci, PlayerEntity playerEntity, ItemStack itemStack, Arm arm, int i, int j, int k, int l, int m, int n, int o, int p) {
        MinecraftClient client = this.client;
        if (!IslesExtraClient.isOnIsles()) return;
        if (client.player == null || client.getNetworkHandler() == null) return;
        PlayerListEntry playerListEntry = client.getNetworkHandler().getPlayerListEntry(client.player.getGameProfile().getId());
        if (playerListEntry == null || (playerListEntry.getGameMode() != GameMode.SURVIVAL && playerListEntry.getGameMode() != GameMode.ADVENTURE)) return;
        if (!LockSlots.isLocked(n)) return;
        client.getItemRenderer().zOffset += 50;
        client.getItemRenderer().renderInGui(new ItemStack(Items.BARRIER), o, p);
        client.getItemRenderer().zOffset -= 50;
    }


}
