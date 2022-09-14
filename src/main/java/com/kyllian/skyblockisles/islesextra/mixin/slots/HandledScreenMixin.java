package com.kyllian.skyblockisles.islesextra.mixin.slots;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;

@Mixin(HandledScreen.class)
abstract class HandledScreenMixin {

    @Shadow protected abstract void drawItem(ItemStack stack, int x, int y, String amountText);

    private static final int[] SLOTS = new int[] {1, 10, 35};

    @Inject(method = "drawSlot", at = @At("TAIL"))
    void drawLock(MatrixStack matrices, Slot slot, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        assert client.player != null;
        if (!slot.inventory.equals(client.player.getInventory())) return;
        if (Arrays.stream(SLOTS).anyMatch(value -> value == ((SlotAccessor) slot).getIndex())) {
            drawItem(new ItemStack(Items.DIAMOND), slot.x, slot.y, "");
        }
    }

}
