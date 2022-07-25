package com.kyllian.islesextra.islesextra.mixin;

import com.kyllian.islesextra.islesextra.client.ClientData;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerInventory.class)
public class ItemAddedEvent {

    @Inject(method = "addStack(ILnet/minecraft/item/ItemStack;)I", at = @At("TAIL"))
    private void addStack(int slot, ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        System.out.println("Item added");
        ClientData.addPickedUpItem(stack);
    }

}
