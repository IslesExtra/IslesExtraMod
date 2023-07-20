package com.kyllian.skyblockisles.islesextra.mixin.slots;

import com.kyllian.skyblockisles.islesextra.client.IslesExtraClient;
import com.kyllian.skyblockisles.islesextra.client.LockSlots;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Identifier;
import net.minecraft.world.GameMode;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HandledScreen.class)
abstract class HandledScreenMixin {

    //@Shadow protected abstract void drawItem(ItemStack stack, int x, int y, String amountText);

    void drawItem(ItemStack stack, int x, int y, MatrixStack matrixStack) {
        matrixStack.translate(0, 0, 300);
        drawImage(matrixStack, x, y, 0, 0, 16, 16, 512, 512, 512, 512, new Identifier("islesextra:textures/gui/lock.png"));
        matrixStack.translate(0, 0, -300);
    }

    private static void drawImage(MatrixStack matrixStack, int screenX, int screenY, int imgX, int imgY, int width, int height, int regionWidth, int regionHeight, int imgWidth, int imgHeight, Identifier texture) {
        RenderSystem.setShaderTexture(0, texture);
        // TODO: fix
        //Screen.drawTexture(matrixStack, screenX, screenY, width, height, imgX, imgY, regionWidth, regionHeight, imgWidth, imgHeight);
    }

    @Shadow @Nullable protected Slot focusedSlot;

    @Inject(method = "drawSlot", at = @At("TAIL"))
    void drawLock(DrawContext context, Slot slot, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (!IslesExtraClient.isOnIsles()) return;
        if (client.player == null || client.getNetworkHandler() == null) return;
        PlayerListEntry playerListEntry = client.getNetworkHandler().getPlayerListEntry(client.player.getGameProfile().getId());
        if (playerListEntry == null || (playerListEntry.getGameMode() != GameMode.SURVIVAL && playerListEntry.getGameMode() != GameMode.ADVENTURE)) return;
        if (!(slot.inventory instanceof PlayerInventory)) return;
        if (LockSlots.isLocked(((SlotAccessor) slot).getIndex())) drawItem(new ItemStack(Items.BARRIER), slot.x, slot.y, context.getMatrices());
    }

    @Inject(method = "onMouseClick(Lnet/minecraft/screen/slot/Slot;IILnet/minecraft/screen/slot/SlotActionType;)V", at = @At("HEAD"), cancellable = true)
    void cancelSlotClick(Slot slot, int slotId, int button, SlotActionType actionType, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (slot == null) return;
        if (!IslesExtraClient.isOnIsles()) return;
        if (client.player == null || client.getNetworkHandler() == null) return;
        PlayerListEntry playerListEntry = client.getNetworkHandler().getPlayerListEntry(client.player.getGameProfile().getId());
        if (playerListEntry == null || (playerListEntry.getGameMode() != GameMode.SURVIVAL && playerListEntry.getGameMode() != GameMode.ADVENTURE)) return;
        if (!(slot.inventory instanceof PlayerInventory)) return;
        if (LockSlots.isLocked(((SlotAccessor) slot).getIndex())) ci.cancel();
    }

    @Inject(method = "handleHotbarKeyPressed", at = @At("HEAD"), cancellable = true)
    void cancelSlotSwap(int keyCode, int scanCode, CallbackInfoReturnable<Boolean> cir) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (!IslesExtraClient.isOnIsles()) return;
        if (client.player == null || client.getNetworkHandler() == null) return;
        PlayerListEntry playerListEntry = client.getNetworkHandler().getPlayerListEntry(client.player.getGameProfile().getId());
        if (playerListEntry == null || (playerListEntry.getGameMode() != GameMode.SURVIVAL && playerListEntry.getGameMode() != GameMode.ADVENTURE)) return;
        for(int i = 0; i < 9; ++i) {
            if (client.options.hotbarKeys[i].matchesKey(keyCode, scanCode)) {
                /*if (Arrays.stream(LockSlots.getLockedSlots().toArray(new Integer[0])).anyMatch(value -> value == slotID)) {
                    cir.setReturnValue(true);
                }*/
                if (LockSlots.isLocked(i)) cir.setReturnValue(true);
            }
        }
    }

    @Inject(method = "keyPressed", at = @At("HEAD"))
    void onKeyPress(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        if (!LockSlots.lockKey.matchesKey(keyCode, scanCode)) return;
        if (focusedSlot==null) return;
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.getNetworkHandler() == null) return;
        PlayerListEntry playerListEntry = client.getNetworkHandler().getPlayerListEntry(client.player.getGameProfile().getId());
        if (playerListEntry == null || (playerListEntry.getGameMode() != GameMode.SURVIVAL && playerListEntry.getGameMode() != GameMode.ADVENTURE)) return;
        LockSlots.lockSlot(focusedSlot);
    }

}
