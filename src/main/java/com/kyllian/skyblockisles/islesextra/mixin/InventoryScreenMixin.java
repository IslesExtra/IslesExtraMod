package com.kyllian.skyblockisles.islesextra.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.slot.CraftingResultSlot;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin {

    @Inject(method = "init", at = @At("HEAD"))
    void init(CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        PlayerEntity player = client.player;
        if (player == null) return;
        //player.playerScreenHandler.slots.set(0, new Slot(player.playerScreenHandler.getCraftingInput(), 0, 50, 50));
        //player.playerScreenHandler.slots.set(1, new Slot(player.playerScreenHandler.getCraftingInput(), 1, 20, 50));
        int c = 1;
        player.playerScreenHandler.slots.set(0, new Slot(player.playerScreenHandler.getCraftingInput(), 0, 120, 28));
        for (int i = 0; i < 2; ++i) {
            for (int j = 0; j < 2; ++j) {
                player.playerScreenHandler.slots.set(c, new Slot(player.playerScreenHandler.getCraftingInput(), j + i * 2, 50 + j * 18, 18 + i * 18));
                c++;
            }
        }
    }

}
